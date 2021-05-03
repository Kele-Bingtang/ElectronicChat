package Swing.Frame;


import ChatClient.Chat.Send;
import ChatClient.load.GetDataFromDao;
import Swing.Dialog.ChangeNickName;
import Swing.Dialog.ChangePassword;
import Swing.Dialog.ChangeSignature;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

/**
 * 主界面
 */
public class MainFrame extends JFrame {
    //昵称
    public static JLabel nickNameLabel;
    //个性签名
    public static JTextField signatureField;
    //滚动面板
    public JScrollPane CenterPanel,listPanel;
    //列表
    public static JList<String> list;
    //好友列表数组
    private String[] fd;
    //列表s
    public static DefaultListModel<String> model;

    //通信
    Socket socket;
    //打开窗口的个数
    int sum = 0;

    GetDataFromDao getDataFromDao;

    String userid;
    String nickName;
    String signature;

    public MainFrame(Socket socket){
        this.socket = socket;
        getDataFromDao = new GetDataFromDao();
        //获取用户名
        userid = LoginFrame.userField.getText();
        //获取昵称
        nickName = getDataFromDao.getNickName(userid);
        //获取个性签名
        signature = getDataFromDao.getSignNature(userid);
        System.out.println("------" + nickName + "客户端------");
        new Send(socket).sendMsg(nickName);
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
        nickNameLabel = new JLabel(nickName);
        nickNameLabel.setBounds(100,10,100,30);
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
        //背景透明
        list.setOpaque(false);
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
                            list.clearSelection();
                        }
                    }
                }
            }
        });

        //初始化panel_2
        //初始化面板二
        CenterPanel = new JScrollPane();
        listPanel = new JScrollPane(list);

        //listPanel存放list,根据list高度变化而变化，防止点击空白处，list自动获取最后一行
        //CenterPanel存放listPanel，高度确定
        listPanel.setBorder(BorderFactory.createTitledBorder("联系人"));
        CenterPanel.setLocation(0,147);
        //高度自定义，防止点击空白处自动获取list的最后一行
        listPanel.setSize(new Dimension(295, (model.size() + 1 )* list.getFixedCellHeight() + 3));
        listPanel.getViewport().setOpaque(false);
        listPanel.setOpaque(false);
        CenterPanel.setSize(new Dimension(295,470));
        CenterPanel.getViewport().setOpaque(false);
        CenterPanel.setOpaque(false);

        CenterPanel.add(listPanel);

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
        label_4.setBounds(100, 50, 100, 20);
        topPanel.add(label_4);

        //设置文本
        signatureField = new JTextField(signature);
        signatureField.setBounds(100, 80, 180, 30);
        signatureField.setOpaque(false);
        signatureField.setFont(new Font("宋体",Font.PLAIN,15));
        signatureField.setBorder(BorderFactory.createEmptyBorder());
        signatureField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signatureField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                signatureField.setOpaque(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signatureField.setBorder(BorderFactory.createEmptyBorder());
            }
        });
        signatureField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                signatureField.setOpaque(false);
            }
        });
        topPanel.add(signatureField);

        //搜索符号
        JLabel searchlabel = new JLabel("\uD83D\uDD0D");
        searchlabel.setForeground(Color.RED);
        searchlabel.setBounds(10, 122, 20, 20);
        topPanel.add(searchlabel);

        //设置搜索栏
        JTextField searchField = new JTextField();
        searchField.setBounds(30, 120, 250, 25);
        searchField.setBorder(BorderFactory.createEmptyBorder());
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
        String Item0 = "\uD83D\uDD12\uD83D\uDD28\uD83D\uDD13";
        String Item1 = "修改密码";
        String Item2 = "修改昵称";
        String Item3 = "修改签名";
        box_1.addItem(Item0);
        box_1.addItem(Item1);
        box_1.addItem(Item2);
        box_1.addItem(Item3);
        box_1.setBounds(8, 20, 100, 25);
        buttomPanel.add(box_1);

        box_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(box_1.getSelectedItem().equals(Item0)){
                    System.out.println("进行权限操作");
                }else if(box_1.getSelectedItem().equals(Item1)){
                    //修改密码
                    ChangePassword changePassword = new ChangePassword(MainFrame.this);
                    if(changePassword.isClick()){
                        String oldPassword = changePassword.getOldtValues();
                        String newPassword = changePassword.getNewValues();
                        boolean isRight = getDataFromDao.verifyPassword(userid,oldPassword);
                        if(isRight){
                            //数据库更新密码
                            getDataFromDao.modifyPassword(userid,newPassword);
                            new TipMessageFrame().modifypasswordSucc();
                        }else{
                            new TipMessageFrame().modifypasswordFail();
                        }
                    }
                }else if(box_1.getSelectedItem().equals(Item2)){
                    //修改昵称
                    ChangeNickName changeNickName = new ChangeNickName(MainFrame.this,nickName);
                    if(changeNickName.isClick()){
                        String newNickName = changeNickName.getValues();
                        nickNameLabel.setText(newNickName);
                        //数据库更新昵称
                        getDataFromDao.setNickName(userid,newNickName);
                    }
                }else if(box_1.getSelectedItem().equals(Item3)){
                    //修改签名
                    ChangeSignature changeSignature = new ChangeSignature(MainFrame.this,signature);
                    if(changeSignature.isClick()){
                        String newSignature = changeSignature.getValues();
                        signatureField.setText(newSignature);
                        //数据库更新签名
                        getDataFromDao.setSignature(userid,newSignature);
                    }
                }
            }
        });

        //添加好友、删除好友
        JComboBox<String> box_2 = new JComboBox<>();
        String ItemIcon = "\uD83D\uDC65";
        String addFriend = "添加好友";
        String deleteFriend = "删除还有";
        box_2.addItem(ItemIcon);
        box_2.addItem(addFriend);
        box_2.addItem(deleteFriend);
        box_2.setBounds(170, 20, 100, 25);

        box_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(box_2.getSelectedItem().equals(ItemIcon)){
                    System.out.println("进行好友操作");
                }else if(box_2.getSelectedItem().equals(addFriend)){

                }else if(box_2.getSelectedItem().equals(deleteFriend)){

                }
            }
        });

        buttomPanel.add(box_2);

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
        //无法调节窗口大小
        frame.setResizable(false);
        label.setBounds(0, 0, 295, 700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //点击面板给头像获得焦点，促使文本框失去焦点
        topPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_2.requestFocus();
            }
        });
        CenterPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_2.requestFocus();
            }
        });
        buttomPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_2.requestFocus();
            }
        });
        frame.add(topPanel);
        frame.add(CenterPanel);
        frame.add(buttomPanel);
    }
    public void addModelRow(){
        fd = getDataFromDao.getFriends(LoginFrame.userField.getText());
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
