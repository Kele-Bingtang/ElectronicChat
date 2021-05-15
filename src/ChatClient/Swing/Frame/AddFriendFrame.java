package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatServer.load.EnMsgType;
import ChatServer.load.GetDataFromDao;
import ChatServer.bean.Information;
import Utils.JDBCUtils;
import Utils.SxUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class AddFriendFrame extends JFrame {
    Container container;
    DefaultTableModel tableModel = new DefaultTableModel();
    JTable table;
    JButton searchButton,addButton;
    JTextField searchField = new JTextField(50);

    List<Information> dataList;

    GetDataFromDao getDataFromDao;

    String []friendid;

    String userid;
    Socket socket;

    int width = 660;
    int height = 530;

    public static void main(String[] args) {
        new AddFriendFrame(null,"kele");
    }

    public AddFriendFrame(Socket socket,String userid){
        this.socket = socket;
        this.userid = userid;
        getDataFromDao = new GetDataFromDao();
        setTitle("添加");
        container = getContentPane();
        container.setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());

        //初始化表格
        initTable();

        //初始化工具栏
        initToolBar();

        //获取数据
        dataList = getDataFromDao.getAllData();

        //加载数据
        loadData();

        //更改字体大小格式和列名大小格式
        table.setFont(new Font("宋体",Font.PLAIN,18));
        table.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,17));
        //选中背景色
        table.setSelectionBackground(Color.LIGHT_GRAY);

        setVisible(true);
        setBounds(700,250,width,height);
        //关闭该窗口，不会整个程序关闭
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    /**
     * 初始化表格
     */
    public void initTable(){
        table = new JTable(tableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(0xFCFCFC));//设置颜色
        table.setRowHeight(30);

        container.add(scrollPane,BorderLayout.CENTER);
        initTableModel();

    }

    /**
     * 初始化工具栏
     */
    public void initToolBar() {
        JToolBar toolBar = new JToolBar();
        container.add(toolBar,BorderLayout.PAGE_START);
        //去点前面的间隙
        toolBar.setFloatable(false);

        searchButton = new JButton("查找");
        searchButton.setFont(new Font("黑体",Font.PLAIN,16));
        toolBar.addSeparator(new Dimension(40,10));  //和前面远离一定距离
        toolBar.add(searchField);
        toolBar.add(searchButton);
        searchButton.setToolTipText("点击查找即可编辑，双击查找可禁止输入");
        searchField.setToolTipText("点击查找即可编辑，双击查找可禁止输入");
        searchField.setEditable(false);
        searchField.setMaximumSize(new Dimension(120,30));

        //双击查询按钮可禁止输入
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    searchField.setEditable(false);
                }
            }
        });
        //按下按钮查询(触发是查询按钮)
        searchButton.addActionListener((e -> {
            searchField.setEditable(true);
            //按回车键触发时间
            onRearch();
        }));
        //按回车键查询(触发是单行输入)
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRearch();
            }
        });

        addButton = new JButton("添加");
        addButton.setFont(new Font("黑体",Font.PLAIN,16));
        toolBar.addSeparator(new Dimension(240,30));
        toolBar.add(addButton);

        //如果是好友，则禁用添加按钮
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String relation = "";
                int[] rows = table.getSelectedRows();
                for(int row : rows){
                    //第五行为关系，如果是好友则禁用添加
                    relation = (String) table.getValueAt(row,4);

                    if(relation.equals("好友")){
                        addButton.setEnabled(false);
                    }else {
                        addButton.setEnabled(true);
                    }
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[] row = table.getSelectedRows();
                for (int i = 0; i < row.length; i++) {
                    String addUserid = (String) tableModel.getValueAt(row[i],0);
                    //添加完后 变成好友
                    table.clearSelection();
                    tableModel.setValueAt("好友",row[i],4);
                    //发送到服务端
                    new Send(socket).sendMsg(EnMsgType.EN_MSG_ADD_FRIEND.toString() + " " + userid + ":" + addUserid);
                }
            }
        });
    }

    /**
     * 查询功能
     * 根据userid和昵称查询
     */
    public void onRearch(){
        //获取用户输入的过滤条件
        String fieldInfo = searchField.getText().trim();
        if(fieldInfo.length() == 0){ // 过滤条件为空
            // 恢复原始数据
            tableModel.setRowCount(0);//清空
            loadData();
            return;
        }

        // 把符合条件的记录显示在表格里
        tableModel.setRowCount(0);//清空
        for (Information in: dataList) {
            //判断条件
            if(fieldInfo.equals(in.getUid())){
                addTableRow(in);
            }
        }
    }

    /**
     * 初始化表格列名
     */
    public void initTableModel(){
        tableModel.addColumn("用户id");
        tableModel.addColumn("昵称");
        tableModel.addColumn("个性签名");
        tableModel.addColumn("状态");
        tableModel.addColumn("关系");
    }

    /**
     * 获取查询的用户
     * @param in 用户类
     */
    public void addTableRow(Information in) {

        Vector<Object> rowData = new Vector<>();
        rowData.add(in.getUid());
        rowData.add(in.getNickName());
        rowData.add(in.getSignNature());
        rowData.add(in.getStatus());
        //判断是不是好友
        if(friendid.length != 0){
            for (String s : friendid) {
                if (in.getUid().equals(s)) {
                    rowData.remove("陌生人");
                    rowData.add("好友");
                    break;
                }else {
                    rowData.remove("陌生人");
                    rowData.add("陌生人");
                }
            }
        }else {
            rowData.add("陌生人");
        }
        //如果是自己的id，则不添加进去
        if(!userid.equals(in.getUid())){
            tableModel.addRow(rowData);
        }
    }

    /**
     * 初始加载全部用户信息
     */
    public void loadData() {
        friendid = getDataFromDao.getFriendid(userid);
        for (Information in : dataList) {
           addTableRow(in);
        }
    }

}
