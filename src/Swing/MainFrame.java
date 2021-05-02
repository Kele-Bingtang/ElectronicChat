package Swing;


import ChatClient.Chat.Send;
import ChatClient.load.LoadDatas;
import ChatServer.History;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

/**
 * @author 1
 */
public class MainFrame extends JFrame {
    //昵称
    public static JLabel nickNameLabel;
    //个性签名
    public static JTextField signField;
    //滚动面板
    public JScrollPane CenterPanel;
    //列表
    public static JList<String> list;
    //好友列表数组
    private String[] fd;
    //列表s
    public static DefaultListModel<String> model;

    History history;
    //通信
    Socket socket;
    //打开窗口的个数
    int sum = 0;

    LoadDatas loadDatas;


    public MainFrame(Socket socket){
        this.socket = socket;
        history = new History();
        loadDatas = new LoadDatas();
        System.out.println("------" + loadDatas.getNickName(LoginFrame.userField.getText()) + "客户端------");
        new Send(socket).sendMsg(loadDatas.getNickName(LoginFrame.userField.getText()));
    }

    public void init(){

        //初始化panel_1
        //面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setLocation(0,0); //初始位置
        topPanel.setBorder(BorderFactory.createTitledBorder("资料"));//标签
        topPanel.setSize(new Dimension(295, 148));  //大小
        topPanel.setOpaque(false);//背景

        //初始化panel_3
        JPanel buttomPanel = new JPanel();
        buttomPanel.setLayout(null);
        buttomPanel.setLocation(0,617);
        buttomPanel.setSize(new Dimension(295,55));
        buttomPanel.setOpaque(false);

        //设置label_1昵称
        nickNameLabel = new JLabel();
        nickNameLabel.setBounds(130,10,100,30);
        nickNameLabel.setFont(new Font("宋体",Font.PLAIN,18));
        topPanel.add(nickNameLabel);

        //设置label_2(头像)
        //标签
        JLabel label_2 = new JLabel(new ImageIcon("src/ChatClient/Image/tx1.png"));
        label_2.setBounds(15,15,80,80);
        topPanel.add(label_2);

        //初始化model
        model = new DefaultListModel<>();
        addModelRow();
        list = new JList<>(model);
        //设置每个列表的高
        list.setFixedCellHeight(20);
        list.setSelectionBackground(new Color(0xBBFFFF));
        //打开聊天窗口
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    for(int i = 0;i < model.size();i++){
                        if(model.get(i).equals(list.getSelectedValue())){
                            new ChatFrame(socket,list.getSelectedValue());
                            //清空
                            ChatFrame.messageToFrame.get(list.getSelectedValue()).delete(0,ChatFrame.messageToFrame.get(list.getSelectedValue()).length());
                            System.out.println("创建了第" + ++sum + "个聊天窗口");
                        }
                    }
                }
            }
        });

        //初始化panel_2
        //初始化面板二
        CenterPanel = new JScrollPane(list);

        CenterPanel.setBorder(BorderFactory.createTitledBorder("联系人"));
        CenterPanel.setLocation(0, 147);
        CenterPanel.setSize(new Dimension(295, 470));
        CenterPanel.getViewport().setOpaque(false);
        list.setOpaque(false);
        CenterPanel.setOpaque(false);

        //设置在线状态bBox();
        //状态框
        JComboBox<String> box = new JComboBox<>();
        box.addItem("✅在线");
        box.addItem("\uD83D\uDCBF隐身");
        box.addItem("\uD83D\uDCBB忙碌");
        box.addItem("❎离线");
        box.setBounds(200, 10, 70, 30);
        topPanel.add(box);

        //设置个性签名的标签
        JLabel label_4 = new JLabel("个性签名:");
        label_4.setFont(new Font("宋体", Font.PLAIN, 16));
        label_4.setForeground(Color.BLUE);
        label_4.setBounds(120, 50, 100, 20);
        topPanel.add(label_4);

        //设置文本
        signField = new JTextField("");
        signField.setBounds(120, 80, 160, 30);
        topPanel.add(signField);

        //搜索符号
        JLabel searchlabel = new JLabel("\uD83D\uDD0D");
        searchlabel.setForeground(Color.RED);
        searchlabel.setBounds(10, 122, 20, 20);
        topPanel.add(searchlabel);

        //设置搜索栏
        JTextField searchField = new JTextField();
        searchField.setBounds(30, 120, 250, 25);
        topPanel.add(searchField);
        //回车键即可查询
        searchField.addActionListener(e -> {
            String msg = searchField.getText().trim();
            if(msg.length() == 0){
                model.clear();
                addModelRow();
                return;
            }
            model.clear();
            onsearchID(msg);
        });
        //搜索符号也能点击查询
        searchlabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String msg = searchField.getText().trim();
                if(msg.length() == 0){
                    model.clear();
                    addModelRow();
                    return;
                }
                model.clear();
                onsearchID(msg);
            }
        });

        JComboBox<String> box_1 = new JComboBox<>();
        box_1.addItem("\uD83D\uDD12\uD83D\uDD28\uD83D\uDD13");
        box_1.addItem("修改密码");
        box_1.addItem("修改昵称");
        box_1.addItem("修改签名");
        box_1.setBounds(8, 20, 100, 25);
        buttomPanel.add(box_1);

        //添加好友、删除好友
        JComboBox<String> box_2 = new JComboBox<>();
        box_2.addItem("\uD83D\uDC65");
        box_2.addItem("添加好友");
        box_2.addItem("删除好友");
        box_2.setBounds(170, 20, 100, 25);

        buttomPanel.add(box_2);
        //加载数据库的昵称和个性签名
        loadDatas.loadData(LoginFrame.userField.getText());


        //容器
        JFrame frame = new JFrame();
        //设置窗体信息
        frame.setTitle("界面");
        //给窗体设置图片
        frame.setIconImage(new ImageIcon("src/ChatClient/Image/3.png").getImage());
        JLabel label = new JLabel(new ImageIcon("src/ChatClient/Image/5.png"));
        //获取窗口的第二层，将label放入
        frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE)); //MAX是覆盖，MIN是后置

        //获取frame的顶层容器,并设置为透明
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setOpaque(false);
        frame.setLayout(null);
        frame.setLocation(1200, 50);
        frame.setSize(287, 700);
        frame.setVisible(true);
        frame.setResizable(false);
        label.setBounds(0, 0, 295, 700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(topPanel);
        frame.add(CenterPanel);
        frame.add(buttomPanel);
    }
    public void addModelRow(){
        fd = loadDatas.getFriends(LoginFrame.userField.getText());
        for (String s : fd) {
            model.addElement(s);
        }
    }
    public void onsearchID(String msg){
        for (String s : fd) {
            if(msg.contains(s)){
                model.addElement(s);
            }
        }
    }
}
