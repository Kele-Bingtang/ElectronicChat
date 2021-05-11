package ChatClient.Swing.Frame;


import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.controller.Handle;
import ChatClient.Swing.Dialog.ChangeNickName;
import ChatClient.Swing.Dialog.ChangePassword;
import ChatClient.Swing.Dialog.ChangeSignature;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * 主界面
 */
public class MainFrame extends JFrame {
    JFrame frame = new JFrame();
    //昵称
    public static JLabel nickNameLabel;
    //个性签名
    public static JTextField signatureField;
    //滚动面板
    public JScrollPane CenterPanel,listPanel;
    //列表
    public static JList<String> friendJList,groupJList;
    //好友列表数组
    private String[] friendList,groupList;
    //列表s
    public static DefaultListModel<String> friendModel,groupModel;

    //通信
    Socket socket;
    //打开窗口的个数
    int sum = 0;


    String userid;
    String nickName;
    String signature;

    public MainFrame(Socket socket,String userid,String nickName,String signature,String[] friendList,String[] groupList){
        this.socket = socket;
        //获取用户名
        this.userid = userid;
        //获取昵称
        this.nickName = nickName;
        //获取个性签名
        this.signature = signature;
        //回去好友列表
        this.friendList = friendList;
        this.groupList = groupList;
        System.out.println("------" + nickName + "客户端------");
        //new Send(socket).sendMsg(nickName);
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
        nickNameLabel.setForeground(Color.WHITE);
        topPanel.add(nickNameLabel);

        //设置label_2(头像)
        //标签
        JLabel label_2 = new JLabel(new ImageIcon("src/ChatClient/Image/tx1.png"));
        label_2.setBounds(15,30,80,80);
        topPanel.add(label_2);

        //初始化好友列表
        initFriendList();
        initGroupList();

        //初始化panel_2
        //初始化面板二
        CenterPanel = new JScrollPane();
        JLabel frinedLabel = new JLabel("好友");
        frinedLabel.setBounds(5,0,60,30);
        frinedLabel.setFont(new Font("宋体",Font.PLAIN,20));

        JLabel groupLabel = new JLabel("群聊");
        groupLabel.setBounds(80,0,60,30);
        groupLabel.setFont(new Font("宋体",Font.PLAIN,20));
        groupLabel.setBounds(45,0,30,20);

        //工具栏，存储中间面板的标题
        JToolBar titleBar = new JToolBar();
        titleBar.setBounds(0,0,295,30);
        titleBar.setFloatable(false);
        titleBar.addSeparator(new Dimension(50,30));
        titleBar.add(frinedLabel);
        titleBar.addSeparator(new Dimension(50,30));
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder());
        titleBar.add(groupLabel);

        //线，放在标题下面
        JLabel lineOnTool = new JLabel();
        lineOnTool.setBounds(0,30,295,1);
        lineOnTool.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));

        JLabel line1 = new JLabel();
        line1.setBounds(45,30,50,1);
        line1.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));

        JLabel line2 = new JLabel();
        line2.setBounds(135,30,50,1);
        line2.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
        line2.setVisible(false);

        CenterPanel.add(line1);
        CenterPanel.add(line2);
        CenterPanel.add(lineOnTool);
        CenterPanel.add(titleBar);

        //初始化好友列表面板
        listPanel = new JScrollPane();

        listPanel.setViewportView(friendJList);
        listPanel.setBounds(0,40,80,20);
        //listPanel存放list,根据list高度变化而变化，防止点击空白处，list自动获取最后一行
        //CenterPanel存放listPanel，高度确定
        CenterPanel.setLocation(0,147);
        //高度自定义，防止点击空白处自动获取list的最后一行
        listPanel.setSize(new Dimension(295, (friendModel.size())* friendJList.getFixedCellHeight() + 3));
        listPanel.getViewport().setOpaque(false);
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder());

        CenterPanel.setSize(new Dimension(295,470));
        CenterPanel.getViewport().setOpaque(false);
        CenterPanel.setOpaque(false);
        CenterPanel.add(listPanel);



        frinedLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listPanel.setViewportView(friendJList);
                if(friendList.length != 0){
                    listPanel.setSize(new Dimension(295, (friendModel.size())* friendJList.getFixedCellHeight() + 3));
                }else {
                    listPanel.setSize(new Dimension(295,0));
                }
            }
        });

        groupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listPanel.setViewportView(groupJList);
                if(groupList.length != 0){
                    listPanel.setSize(new Dimension(295, (groupModel.size())* groupJList.getFixedCellHeight() + 3));
                }else {
                    listPanel.setSize(new Dimension(295,0));
                }
            }
        });

        //设置在线状态bBox();
        //状态框
        JComboBox<String> box = new JComboBox<>();
        box.addItem("✅在线");
        box.addItem("\uD83D\uDCBF隐身");
        box.addItem("\uD83D\uDCBB忙碌");
        box.addItem("❎离线");
        box.setBounds(200, 10, 70, 30);
        box.setFont(new Font(null,Font.PLAIN,14));
        box.setForeground(Color.BLACK);
        topPanel.add(box);

        //设置个性签名的标签
        JLabel label_4 = new JLabel("个性签名:");
        label_4.setFont(new Font("宋体", Font.PLAIN, 16));
        label_4.setForeground(Color.WHITE);
        label_4.setBounds(100, 50, 100, 20);
        topPanel.add(label_4);

        //设置文本
        signatureField = new JTextField(signature);
        signatureField.setBounds(100, 80, 180, 30);
        signatureField.setOpaque(false);
        signatureField.setFont(new Font("宋体",Font.PLAIN,15));
        signatureField.setForeground(Color.WHITE);
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
            onSearch(searchField);

        });
        //搜索符号也能点击查询
        searchlabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onSearch(searchField);
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
        box_1.setFont(new Font(null,Font.PLAIN,18));
        box_1.setForeground(Color.BLACK);
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

                        if(newPassword.equals("") || newPassword.contains(" ") || newPassword.contains(":")){
                            new TipMessageFrame().SuccOrFail("失败","您输入的新密码为空");
                        }else if(!oldPassword.equals(newPassword)){
                            //发送信息给服务端进行解析 修改密码
                            new Send(socket).sendMsg(EnMsgType.EN_MSG_MODIFY_PASSWORD.toString() + " " + userid + ":" + oldPassword + ":" + newPassword);
                        }else {
                            new TipMessageFrame().SuccOrFail("失败","您输入的新密码和旧密码相同");
                        }
                    }
                }else if(box_1.getSelectedItem().equals(Item2)){
                    //修改昵称
                    ChangeNickName changeNickName = new ChangeNickName(MainFrame.this,nickName);
                    if(changeNickName.isClick()){
                        String newNickName = changeNickName.getValues();
                        if(newNickName.equals("") || newNickName.contains(" ") || newNickName.contains(":")){
                            new TipMessageFrame().sendMessageTip("失败","您的昵称非法",true);
                        }else if(!nickName.equals(newNickName)){
                            nickNameLabel.setText(newNickName);
                            nickName = newNickName;
                            //发送信息给服务端进行解析 更新昵称
                            new Send(socket).sendMsg(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString() + " " + userid + ":" + newNickName);
                        }else {
                            new TipMessageFrame().sendMessageTip("失败","您的昵称和原来的一样",true);
                        }
                    }
                }else if(box_1.getSelectedItem().equals(Item3)){
                    //修改签名
                    ChangeSignature changeSignature = new ChangeSignature(MainFrame.this,signature);
                    if(changeSignature.isClick()){
                        String newSignature = changeSignature.getValues();
                        if(newSignature.equals("") || newSignature.contains(" ") || newSignature.contains(":")){
                            new TipMessageFrame().sendMessageTip("失败","您的签名非法",true);
                        }else if(!signature.equals(newSignature)){
                            signatureField.setText(newSignature);
                            signature = newSignature;
                            //数据库更新签名
                            new Send(socket).sendMsg(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString() + " " + userid + ":" + newSignature);
                        }else {
                            new TipMessageFrame().sendMessageTip("失败","您的签名和原来的一样",true);
                        }
                    }
                }
            }
        });

        //添加好友、删除好友
        JComboBox<String> box_2 = new JComboBox<>();
        String ItemIcon = "\uD83D\uDC65";
        String addFriend = "添加好友";
        String deleteFriend = "删除好友";
        box_2.addItem(ItemIcon);
        box_2.addItem(addFriend);
        box_2.addItem(deleteFriend);
        box_2.setFont(new Font(null,Font.PLAIN,18));
        box_2.setForeground(Color.BLACK);
        box_2.setBounds(170, 20, 100, 25);

        box_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(box_2.getSelectedItem().equals(ItemIcon)){
                    System.out.println("进行好友操作");
                }else if(box_2.getSelectedItem().equals(addFriend)){
                    //添加好友
                    new AddFriendFrame(socket,userid);
                }else if(box_2.getSelectedItem().equals(deleteFriend)){
                    //删除好友
                    new DeleteFriendFrame(socket,userid);
                }
            }
        });

        buttomPanel.add(box_2);

        //刷新JList列表
        JLabel fluashLabel = new JLabel("刷新");
        fluashLabel.setBounds(120,20,50,25);
        buttomPanel.add(fluashLabel);
        fluashLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              onFlush();
            }

        });

        //容器
        frame = new JFrame();
        //设置窗体信息
        frame.setTitle("界面");
        //给窗体设置图片
        frame.setIconImage(new ImageIcon("src/ChatClient/Image/3.png").getImage());

        JLabel label = new JLabel(new ImageIcon("src/ChatClient/Image/mainbj3.png"));
        //获取窗口的第二层，将label放入
        frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE)); //MAX是覆盖，MIN是后置

        //获取frame的顶层容器,并设置为透明
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setOpaque(false);
        frame.setLayout(null);
        frame.setLocation(1200, 50);
        frame.setSize(287, 700);
        //开局失去焦点
        frame.setFocusable(true);
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
        frame.add(buttomPanel);
        frame.add(CenterPanel);


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //告诉客户端，我已经离线
                new Send(socket).sendMsg(EnMsgType.EN_MSG_EXIT.toString() + " " + userid + ":" + nickName);
            }
        });



    }

    public void flushFriendList(){
        friendList = Handle.friends;
        if(Arrays.toString(friendList).trim().length() != 0){
            for (String s : friendList) {
                if(!s.equals("")){
                    friendModel.addElement(s);
                }
            }
        }
    }
    public void flushGroupList(){
        groupList = Handle.groups;
        if(Arrays.toString(groupList).trim().length() != 0){
            for (String s : groupList) {
                if(!s.equals("")) {
                    groupModel.addElement(s);
                }
            }
        }

    }

    public void onSearch(JTextField searchField){
        if(listPanel.getViewport().getView().equals(friendJList)){
            String msg = searchField.getText().trim();
            if(msg.length() == 0){
                friendModel.clear();
                flushFriendList();
                return;
            }
            friendModel.clear();
            onsearchID(msg,friendModel,friendList);
        }else if(listPanel.getViewport().getView().equals(groupJList)){
            String msg = searchField.getText().trim();
            if(msg.length() == 0){
                groupModel.clear();
                flushGroupList();
                return;
            }
            groupModel.clear();
            onsearchID(msg,groupModel,groupList);
        }
    }

    public void onsearchID(String msg,DefaultListModel<String> model,String[] list){
        if(list == friendList){
            for (String s : friendList) {
                int index =s.indexOf(" ");
                String friendNames = s.substring(0,index);
                if(msg.startsWith(friendNames)){
                    model.addElement(s);
                }
            }
        }else if(list == groupList){
            for (String s : groupList) {
                if(msg.equals(s)){
                    model.addElement(s);
                }
            }
        }
    }

    public void initFriendList(){
        //初始化model
        friendModel = new DefaultListModel<>();
        //初始化好友列表
        flushFriendList();
        friendJList = new JList<>(friendModel);
        //背景透明
        ((JLabel)friendJList.getCellRenderer()).setOpaque(false);
        friendJList.setOpaque(false);
        //设置每个列表的高
        friendJList.setFixedCellHeight(20);
        friendJList.setFont(new Font("宋体",Font.PLAIN,18));
        friendJList.setSelectionBackground(new Color(0xBBFFFF));
        //打开聊天窗口
        friendJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    for(int i = 0;i < friendModel.size();i++){
                        if(friendModel.get(i).equals(friendJList.getSelectedValue())){
                            int index = friendJList.getSelectedValue().indexOf(" ");
                            String chatName = friendJList.getSelectedValue().substring(0,index);
                            new ChatFrame(socket,nickName,chatName);
                            new Send(socket).sendMsg(EnMsgType.EN_MSG_OPEN_CHAT.toString() + " " + nickName + ":" + chatName);
                            System.out.println("创建了第" + ++sum + "个聊天窗口");
                            friendJList.clearSelection();
                        }
                    }
                }
            }
        });
    }

    public void initGroupList(){
        //初始化model
        groupModel = new DefaultListModel<>();
        //初始化好友列表
        flushGroupList();
        groupJList = new JList<>(groupModel);
        //背景透明
        ((JLabel)groupJList.getCellRenderer()).setOpaque(false);
        groupJList.setOpaque(false);
        //设置每个列表的高
        groupJList.setFixedCellHeight(20);
        groupJList.setFont(new Font("宋体",Font.PLAIN,18));
        groupJList.setSelectionBackground(new Color(0xBBFFFF));
        //打开聊天窗口
        groupJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    for(int i = 0;i < groupModel.size();i++){
                        if(groupModel.get(i).equals(groupJList.getSelectedValue())){
                            String chatGroupName = groupJList.getSelectedValue();
                            new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString() + " " + userid + ":" + nickName + ":" + chatGroupName);
                            try {
                                Handle.queue.take();
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                            new GroupFrame(socket,userid,nickName,chatGroupName,Handle.groupMembers);

                            groupJList.clearSelection();
                        }
                    }
                }
            }
        });
    }

    public void onFlush(){
        //如果是好友列表
        if(listPanel.getViewport().getView().equals(friendJList)){
            int code = 0;
            friendJList.removeAll();
            friendModel = new DefaultListModel<>();
            new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_FRIEND.toString() + " " + userid);
            try {
                //阻塞线程，等待发送的消息接收并且处理后
                code = (int) Handle.queue.take();
                System.out.println("刷新的：" + code);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if(code == 300){
                flushFriendList();
            }
            //高度自定义，防止点击空白处自动获取list的最后一行
            if(friendList.length != 0){
                listPanel.setSize(new Dimension(295, (friendModel.size())* friendJList.getFixedCellHeight() + 3));
            }else {
                listPanel.setSize(new Dimension(295, 0));
            }
            friendJList.setModel(friendModel);
            //如果是群列表
        }else if(listPanel.getViewport().getView().equals(groupJList)){
            int code = 0;
            groupJList.removeAll();
            groupModel = new DefaultListModel<>();
            new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_GROUP_INFROMATION.toString());
            try {
                //阻塞线程，等待发送的消息接收并且处理后
                code = (int) Handle.queue.take();
                System.out.println("刷新的：" + code);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if(code == 400){
                flushGroupList();
            }
            //高度自定义，防止点击空白处自动获取list的最后一行
            if(groupList.length != 0){
                listPanel.setSize(new Dimension(295, (groupModel.size())* groupJList.getFixedCellHeight() + 3));
            }else {
                listPanel.setSize(new Dimension(295, 0));
            }
            groupJList.setModel(groupModel);
        }
    }

}
