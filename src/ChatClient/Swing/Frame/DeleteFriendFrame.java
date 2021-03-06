package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatServer.load.EnMsgType;
import ChatServer.load.GetDataFromDao;
import ChatServer.bean.Information;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

/**
 * 删除好友界面
 */
public class DeleteFriendFrame extends JFrame {
    //主界面
    Container container;
    //显示数据
    DefaultTableModel tableModel = new DefaultTableModel();
    //表
    JTable table;
    //搜索按钮，删除按钮
    JButton searchButton,deleteButton;
    //搜索文本框
    JTextField searchField = new JTextField(50);
    //存储好友信息
    List<Information> dataList;
    //是否删除
    String userid;
    //通信
    Socket socket;

    //大小
    int width = 660;
    int height = 530;

    /**
     * 初始化删除好友界面
     * @param socket 通信
     * @param userid 自己的userid
     */
    public DeleteFriendFrame(Socket socket, String userid){
        this.socket = socket;
        this.userid = userid;
        setTitle("删除");
        container = getContentPane();
        container.setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());

        //初始化表格
        initTable();

        //初始化工具栏
        initToolBar();

        //获取数据
        dataList = new GetDataFromDao().getFriends(userid);

        //加载数据
        loadData();

        //更改字体大小格式和列名大小格式
        table.setFont(new Font("宋体",Font.PLAIN,18));
        table.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,18));
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
        //滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        //设置颜色
        scrollPane.getViewport().setBackground(new Color(0xFCFCFC));
        //每一行之间的高度
        table.setRowHeight(30);

        container.add(scrollPane,BorderLayout.CENTER);
        //初始化tablemodel
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
        //和前面远离一定距离
        toolBar.addSeparator(new Dimension(40,10));
        toolBar.add(searchField);
        toolBar.add(searchButton);
        //鼠标悬停提示
        searchButton.setToolTipText("点击查找即可编辑，双击查找可禁止输入");
        searchField.setToolTipText("点击查找即可编辑，双击查找可禁止输入");
        //设为不可编辑
        searchField.setEditable(false);
        //最大尺寸
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

        deleteButton = new JButton("删除");
        deleteButton.setFont(new Font("黑体",Font.PLAIN,16));
        toolBar.addSeparator(new Dimension(240,30));
        toolBar.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //存储选择的单行或者多行
                int[] rows = table.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    //获取每一行的userid
                    String deleteUserid = (String) tableModel.getValueAt(rows[i],0);
                    //发送到服务端
                    new Send(socket).sendMsg(EnMsgType.EN_MSG_DEL_FRIEND.toString() + " " + userid + ":" + deleteUserid);
                }
                onDelete(rows);
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
            //名字或者学号有一个字符串出现，则都显示
            if(fieldInfo.equals(in.getUid())){
                addTableRow(in);
            }
        }
    }

    public void onDelete(int []rows){
        //在列表中删除
        //技巧，从后往前面删除，因为删除前面索引会变化，后面往前移动一位
        for(int i = rows.length - 1;i >= 0;i--){
            int row = rows[i];
            String id = (String) tableModel.getValueAt(row,0);
            for (int j = 0; j < dataList.size(); j++) {
                if(id .equals(dataList.get(j).getUid())){
                    dataList.remove(j);
                    break;
                }
            }
            //从tableModel中删除记录
            tableModel.removeRow(row);
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
        tableModel.addRow(rowData);
    }

    /**
     * 初始加载全部用户信息
     */
    public void loadData() {
        //一个rowData是一行
        for (int i = 0;i < dataList.size();i++){
            Vector<Object> rowData = new Vector<>();
            rowData.add(dataList.get(i).getUid());
            rowData.add(dataList.get(i).getNickName());
            rowData.add(dataList.get(i).getSignNature());
            rowData.add(dataList.get(i).getStatus());
            tableModel.addRow(rowData);
        }
    }

}
