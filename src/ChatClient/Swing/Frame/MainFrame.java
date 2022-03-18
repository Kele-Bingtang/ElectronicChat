package ChatClient.Swing.Frame;


import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.controller.Handle;
import ChatClient.Swing.Dialog.ChangeNickName;
import ChatClient.Swing.Dialog.ChangePassword;
import ChatClient.Swing.Dialog.ChangeSignature;
import Utils.ImageUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 主界面
 */
public class MainFrame extends JFrame {
    //主界面
    JFrame frame = new JFrame();
    //昵称
    public static JLabel nickNameLabel;
    //个性签名
    public static JTextField signatureField;
    //滚动面板
    public JScrollPane CenterPanel,listPanel;
    //列表
    public static JList<String> friendJList,groupJList;
    //头像id
    int headIconID;
    //好友列表数组
    private String[] friendList,groupList;
    //列表s
    public static DefaultListModel<String> friendModel,groupModel;
    //判断是否已经打开此私聊对象的窗口
    public static HashMap<Integer,Boolean> isopenFriend = new HashMap<Integer, Boolean>();
    //判断是否已经打开此聊天群组的窗口
    public static HashMap<Integer,Boolean> isopenGroup = new HashMap<Integer, Boolean>();

    //通信
    Socket socket;
    //打开窗口的个数
    int sum = 0;

    //界面宽度
    int width = 320;
    //界面高度
    int height = 800;
    //顶部面板大小
    int topPanelWidth = width;
    int topPanelHeight = height - 650;  //  180
    //中间面板大小
    int centerPanelWidth = width;
    int centerPanelHeight = height - 250;  //550
    //底部面板大小
    int buttomPanelWidth = width;
    int buttomPanelHeight = height - 710;  //90

    //自己的userid
    String userid;
    //自己的昵称
    String nickName;
    //自己的个性签名
    String signature;

    public MainFrame(Socket socket,String userid,int headIconID,String nickName,String signature,String[] friendList,String[] groupList){
        this.socket = socket;
        //获取用户名
        this.userid = userid;
        //获取昵称
        this.nickName = nickName;
        //获取头像id
        this.headIconID = headIconID;
        //获取个性签名
        this.signature = signature;
        //回去好友列表
        this.friendList = friendList;
        this.groupList = groupList;
        System.out.println("------" + nickName + "客户端------");
    }

    /**
     * 初始化主界面
     */
    public void init(){

        //初始化topPanel
        //面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setLocation(0,0); //初始位置
        topPanel.setSize(new Dimension(topPanelWidth, topPanelHeight));  //大小
        topPanel.setOpaque(false);//背景

        //初始化buttomPanel
        JPanel buttomPanel = new JPanel();
        buttomPanel.setLayout(null);
        buttomPanel.setLocation(0,height - 100);
        buttomPanel.setSize(new Dimension(buttomPanelWidth,buttomPanelHeight));
        buttomPanel.setOpaque(false);

        //headButton(头像)
        //标签
        JButton headButton = new JButton();
        headButton.setBounds(15,20,80,80);
        headButton.setToolTipText("点击可更换头像");
        HeadFrame headFrame = new HeadFrame(socket,userid,headIconID,headButton);
        //初始化头像，从数据库获取头像ID
        headButton.setIcon(HeadFrame.ChangeImgSize((ImageIcon) (HeadFrame.icons.get(headIconID).getIcon()),headButton));
        headButton.addActionListener(e -> {
            headFrame.setVisible(true);
        });
        topPanel.add(headButton);

        //设置nickNameLabel昵称
        nickNameLabel = new JLabel(nickName);
        nickNameLabel.setBounds(100,20,150,30);
        nickNameLabel.setFont(new Font("宋体",Font.BOLD,18));
        nickNameLabel.setForeground(Color.WHITE);
        topPanel.add(nickNameLabel);

        //设置在线状态bBox();
        //状态框
        JComboBox<String> box = new JComboBox<>();
        box.setUI(new MyComBoxUIBackground());
        box.addItem("✅在线");
        box.addItem("\uD83D\uDCBF隐身");
        box.addItem("\uD83D\uDCBB忙碌");
        box.addItem("❎离线");
        box.setBounds(centerPanelWidth - 100, height - 785, 80, 30);
        box.setFont(new Font(null,Font.PLAIN,14));
        topPanel.add(box);

        //设置个性签名文本
        signatureField = new JTextField();
        signatureField.setBounds(100, 65, width - 100, 30);
        signatureField.setOpaque(false);
        signatureField.setFont(new Font("宋体",Font.PLAIN,15));
        signatureField.setForeground(Color.WHITE);
        signatureField.setBorder(BorderFactory.createEmptyBorder());
        if(signature.equals("")){
            signatureField.setText("编辑个性签名");
        }else {
            signatureField.setText(signature);
        }
        signatureField.addMouseListener(new MouseAdapter() {
            //编辑个性文本框点击、鼠标以入、鼠标移出事件监听器
            @Override
            public void mouseClicked(MouseEvent e) {
                if(signatureField.getText().equals("编辑个性签名")){
                    signatureField.setText("");
                }
                //设置背景和透明
                signatureField.setForeground(Color.BLACK);
                signatureField.setOpaque(true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                signatureField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                signatureField.setBorder(BorderFactory.createEmptyBorder());
            }
        });

        signatureField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //签名文本框失去焦点
                if(signatureField.getText().equals("")){
                    signatureField.setText("编辑个性签名");
                }
                signatureField.setForeground(Color.WHITE);
                signatureField.setOpaque(false);
            }
        });
        topPanel.add(signatureField);

        //搜索符号
        JLabel searchlabel = new JLabel("\uD83D\uDD0D 搜索");
        searchlabel.setFont(new Font(null,Font.PLAIN,16));
        searchlabel.setForeground(new Color(255,255,255, 215));
        searchlabel.setBounds(10, 120, 60, 20);
        topPanel.add(searchlabel);

        //设置搜索栏
        JTextField searchField = new JTextField();
        searchField.setToolTipText("按回车键进行查询，退出自动清空关键字");
        searchField.setBounds(0, 120, topPanelWidth, 30);
        searchField.setFont(new Font("宋体",Font.PLAIN,18));
        searchField.setOpaque(false);
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        topPanel.add(searchField);
        //搜索文本框监听器
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchField.setForeground(Color.BLACK);
                searchField.setOpaque(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                searchField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchField.setBorder(BorderFactory.createEmptyBorder());
            }
        });

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                //搜索文本框失去焦点
                searchField.setForeground(Color.WHITE);
                searchField.setOpaque(false);
                searchField.setText("");
            }
        });

        //回车键即可查询
        searchField.addActionListener(e -> {
            onSearch(searchField);

        });

        //初始化好友列表
        initFriendList();
        initGroupList();

        //初始化CenterPanel
        //初始化面板二
        CenterPanel = new JScrollPane();
        //初始化好友列表面板
        listPanel = new JScrollPane();
        //好友
        JLabel frinedLabel = new JLabel("好友");
        frinedLabel.setFont(new Font("宋体",Font.PLAIN,20));
        frinedLabel.setForeground(Color.BLACK);

        //群聊
        JLabel groupLabel = new JLabel("群聊");
        groupLabel.setFont(new Font("宋体",Font.PLAIN,20));
        groupLabel.setForeground(new Color(0, 0, 0, 185));

        //刷新
        //刷新JList列表
        JLabel fluashLabel = new JLabel("刷新");
        fluashLabel.setFont(new Font("宋体",Font.PLAIN,18));
        fluashLabel.setToolTipText("点击即可刷新好友或者群聊的列表");
        fluashLabel.setForeground(new Color(0, 0, 0, 185));

        //工具栏，存储中间面板的标题(好友和群聊)
        JToolBar titleBar = new JToolBar();
        titleBar.setBounds(0,0,centerPanelWidth,centerPanelHeight - 520);
        titleBar.setFloatable(false);
        titleBar.addSeparator(new Dimension(50,centerPanelHeight -520));
        titleBar.add(frinedLabel);
        titleBar.addSeparator(new Dimension(50,centerPanelHeight -520));
        titleBar.add(groupLabel);
        titleBar.addSeparator(new Dimension(50,centerPanelHeight -520));
        titleBar.add(fluashLabel);
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder());

        //线，放在标题下面
        JLabel lineOnTool = new JLabel();
        lineOnTool.setBounds(0,centerPanelHeight - 522,centerPanelWidth,1);
        lineOnTool.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));
        //好友下划线
        JLabel line1 = new JLabel();
        line1.setBounds(45,centerPanelHeight - 522,50,1);
        line1.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
        //群聊下划线
        JLabel line2 = new JLabel();
        line2.setBounds(135,centerPanelHeight - 522,50,1);
        line2.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0)));
        line2.setVisible(false);

        CenterPanel.add(line1);
        CenterPanel.add(line2);
        CenterPanel.add(lineOnTool);
        CenterPanel.add(titleBar);

        frinedLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //显示好友下的线，隐藏群聊下的线
                line1.setVisible(true);
                line2.setVisible(false);
                //列表为好友
                listPanel.setViewportView(friendJList);
                frinedLabel.setForeground(Color.BLACK);
                groupLabel.setForeground(new Color(0, 0, 0, 185));
                if(friendList.length != 0){
                    //创建高度与好友列表高度一致
                    listPanel.setSize(new Dimension(295, (friendModel.size())* friendJList.getFixedCellHeight() + 3));
                }else {
                    //高度为空
                    listPanel.setSize(new Dimension(295,0));
                }
            }
        });

        groupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //显示群聊下的线，隐藏好友下的线
                line1.setVisible(false);
                line2.setVisible(true);
                //列表为群聊
                listPanel.setViewportView(groupJList);
                groupLabel.setForeground(Color.BLACK);
                frinedLabel.setForeground(new Color(0, 0, 0, 185));
                if(groupList.length != 0){
                    //创建高度与群聊列表高度一致
                    listPanel.setSize(new Dimension(295, (groupModel.size())* groupJList.getFixedCellHeight() + 3));
                }else {
                    //高度为空
                    listPanel.setSize(new Dimension(295,0));
                }
            }
        });

        fluashLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onFlush();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fluashLabel.setForeground(Color.BLACK);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                fluashLabel.setForeground(new Color(0, 0, 0, 185));
            }
        });


        listPanel.setViewportView(friendJList);
        //高度自定义，防止点击空白处自动获取list的最后一行
        //listPanel存放list,根据list高度变化而变化，防止点击空白处，list自动获取最后一行
        listPanel.setBounds(0,centerPanelHeight - 510,centerPanelWidth,(friendModel.size())* friendJList.getFixedCellHeight() + 3);

        listPanel.getViewport().setOpaque(false);
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder());

        CenterPanel.setLocation(0,height - 650);
        CenterPanel.setSize(new Dimension(centerPanelWidth,centerPanelHeight));
        CenterPanel.getViewport().setOpaque(false);
        CenterPanel.setOpaque(false);
        CenterPanel.add(listPanel);

        //下拉列表
        JComboBox<String> box_1 = new JComboBox<>();
        //重写UI，美化UI
        box_1.setUI(new MyComBoxUIBackground());
        box_1.setBounds(8, 20, 120, 30);
        box_1.setFont(new Font(null,Font.PLAIN,18));
        box_1.setBorder(BorderFactory.createEmptyBorder());

        String Item0 = "\uD83D\uDD12\uD83D\uDD28\uD83D\uDD13";
        String Item1 = "修改密码";
        String Item2 = "修改昵称";
        String Item3 = "修改签名";
        box_1.addItem(Item0);
        box_1.addItem(Item1);
        box_1.addItem(Item2);
        box_1.addItem(Item3);

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
                        //响应点击确定事件
                        String oldPassword = changePassword.getOldtValues();
                        String newPassword = changePassword.getNewValues();
                        //判断旧密码和新密码是否为空或者相同
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
                        //响应点击确定事件
                        String newNickName = changeNickName.getValues();
                        //判断旧昵称和新昵称是否为空或者非法或者一致
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
                        //响应点击确定事件
                        String newSignature = changeSignature.getValues();
                        //判断旧签名和新签名是否为空或者非法
                        if(newSignature.equals("")){
                            signatureField.setText("编辑个性签名");
                        } else if(newSignature.contains(" ") || newSignature.contains(":")){
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
        box_2.setFont(new Font(null,Font.PLAIN,18));
        box_2.setBounds(width - 150, 20, 120, 30);
        box_2.setUI(new MyComBoxUIBackground());

        String ItemIcon = "\uD83D\uDC65";
        String addFriend = "添加好友";
        String deleteFriend = "删除好友";
        String createGroup = "创建群聊";
        box_2.addItem(ItemIcon);
        box_2.addItem(addFriend);
        box_2.addItem(deleteFriend);
        box_2.addItem(createGroup);

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
                }else if(box_2.getSelectedItem().equals(createGroup)){
                    //创建群聊
                    new CeateGroupFrame(socket,userid);
                }
            }
        });

        buttomPanel.add(box_2);

        //容器
        frame = new JFrame();
        //设置窗体信息
        frame.setTitle("界面");
        //给窗体设置图片
        frame.setIconImage(new ImageIcon(ImageUtils.getImageUrl("8Icon.png")).getImage());

        JLabel label = new JLabel(new ImageIcon(ImageUtils.getImageUrl("mainbj3.png")));
        label.setBounds(0, 0, width, height);
        //获取窗口的第二层，将label放入
        frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE)); //MAX是覆盖，MIN是后置

        //获取frame的顶层容器,并设置为透明
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setOpaque(false);
        frame.setLayout(null);
        frame.setLocation(1200, 50);
        frame.setSize(width, height);
        //开局失去焦点
        frame.setFocusable(true);
        frame.setVisible(true);
        //无法调节窗口大小
        frame.setResizable(false);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //点击面板给昵称获得焦点，促使文本框失去焦点
        topPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nickNameLabel.requestFocus();
            }
        });
        CenterPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nickNameLabel.requestFocus();
            }
        });
        buttomPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nickNameLabel.requestFocus();
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

    /**
     * 刷新好友列表
     * 添加好友，删除好友后进行刷新，获取新好友的信息
     * 好友上线后进行刷新，获取好友的新状态
     */
    public void flushFriendList(){
        int i = 0;
        friendList = Handle.friends;
        //判断是否为空
        if(Arrays.toString(friendList).trim().length() != 0){
            for (String s : friendList) {
                if(!s.equals("")){
                    friendModel.addElement(s);
                    isopenFriend.put(i,false);
                    i++;
                }
            }
        }
    }
    /**
     * 刷新群聊列表
     * 添加群聊，删除群聊后进行刷新，获取新好友的信息
     */
    public void flushGroupList(){
        int i = 0;
        groupList = Handle.groups;
        //判断是否为空
        if(Arrays.toString(groupList).trim().length() != 0){
            for (String s : groupList) {
                if(!s.equals("")) {
                    groupModel.addElement(s);
                    isopenGroup.put(i,false);
                    i++;
                }
            }
        }
    }

    /**
     * 查询功能，根据好友昵称进行查询
     * @param searchField 查询文本框
     */
    public void onSearch(JTextField searchField){
        //判断当前列表是好友列表还是群聊列表
        if(listPanel.getViewport().getView().equals(friendJList)){
            String msg = searchField.getText().trim();
            //查询关键词为空，获取全部好友信息
            if(msg.length() == 0){
                friendModel.clear();
                flushFriendList();
                return;
            }
            //根据查询关键词，获取相应的好友信息
            friendModel.clear();
            onsearchByNickName(msg,friendModel,friendList);
        }
        //判断当前列表是好友列表还是群聊列表
        else if(listPanel.getViewport().getView().equals(groupJList)){
            String msg = searchField.getText().trim();
            //查询关键词为空，获取全部好友信息
            if(msg.length() == 0){
                groupModel.clear();
                flushGroupList();
                return;
            }
            //根据查询关键词，获取相应的好友信息
            groupModel.clear();
            onsearchByNickName(msg,groupModel,groupList);
        }
    }

    /**
     * 根据好友昵称进行查询
     * @param msg 好友昵称
     * @param model 显示数据的表
     * @param list 好友列表或者群聊列表
     */
    public void onsearchByNickName(String msg,DefaultListModel<String> model,String[] list){
        //如果是好友列表
        if(list == friendList){
            for (String s : friendList) {
                int index =s.indexOf(" ");
                //获取好友昵称
                String friendNames = s.substring(0,index);
                if(msg.startsWith(friendNames)){
                    model.addElement(s);
                }
            }
            //如果是群聊列表
        }else if(list == groupList){
            for (String s : groupList) {
                if(msg.equals(s)){
                    model.addElement(s);
                }
            }
        }
    }

    /**
     * 初始化好友列表
     */
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
        friendJList.setFixedCellHeight(30);
        friendJList.setFont(new Font("宋体",Font.PLAIN,18));
        friendJList.setForeground(Color.BLACK);
        friendJList.setSelectionBackground(new Color(0xBBFFFF));
        //打开聊天窗口
        friendJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //相应点击事件
                if(e.getValueIsAdjusting()){
                    //判断点击的是哪个
                    for(int i = 0;i < friendModel.size();i++){
                        if(friendModel.get(i).equals(friendJList.getSelectedValue())){
                            //获取" "的索引
                            int index = friendJList.getSelectedValue().indexOf(" ");
                            //判断是否打开窗口(打开了无法再次打开相同的窗口)
                            if(!isopenFriend.get(friendJList.getSelectedIndex())) {
                                //打开新的窗口，并且当该窗口没有关闭，禁止再次打开该窗口
                                isopenFriend.put(friendJList.getSelectedIndex(), true);
                                //获取聊天对象昵称
                                String chatName = friendJList.getSelectedValue().substring(0, index);
                                //打开聊天窗口，把相应的数据传入
                                new ChatFrame(socket, nickName, chatName,friendJList.getSelectedIndex());
                                //发送给服务器
                                new Send(socket).sendMsg(EnMsgType.EN_MSG_OPEN_CHAT.toString() + " " + nickName + ":" + chatName);
                                System.out.println("创建了第" + ++sum + "个聊天窗口");
                            }
                            //点击完清空点击选项
                            friendJList.clearSelection();
                        }
                    }
                }
            }
        });
    }

    /**
     * 初始群聊列表
     */
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
        groupJList.setFixedCellHeight(30);
        groupJList.setFont(new Font("宋体",Font.PLAIN,18));
        groupJList.setForeground(Color.BLACK);
        groupJList.setSelectionBackground(new Color(0xBBFFFF));
        //打开聊天窗口
        groupJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //相应点击事件
                if(e.getValueIsAdjusting()){
                    //判断点击的是哪个
                    for(int i = 0;i < groupModel.size();i++){
                        if(groupModel.get(i).equals(groupJList.getSelectedValue())){
                            //获取" "的索引
                            int index = groupJList.getSelectedValue().indexOf(" ");
                            //判断是否打开窗口(打开了无法再次打开相同的窗口)
                            if(!isopenGroup.get(groupJList.getSelectedIndex())) {
                                //打开新的窗口，并且当该窗口没有关闭，禁止再次打开该窗口
                                isopenGroup.put(groupJList.getSelectedIndex(), true);
                                //获取群聊对象昵称
                                String chatGroupName = groupJList.getSelectedValue().substring(0, index);
                                //发送给服务器
                                new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString() + " " + userid + ":" + nickName + ":" + chatGroupName);
                                try {
                                    //等待获取群成员信息
                                    Handle.queue.take();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                                //获取群成员信息，打开群聊窗口，传入相应的数据
                                new GroupFrame(socket, userid, nickName, chatGroupName, Handle.groupMembers,groupJList.getSelectedIndex());
                            }
                            //点击完清空点击选项
                            groupJList.clearSelection();
                        }
                    }
                }
            }
        });
    }

    /**
     * 刷新功能的实现
     */
    public void onFlush(){
        //如果是好友列表
        if(listPanel.getViewport().getView().equals(friendJList)){
            int code = 0;
            friendJList.removeAll();
            friendModel = new DefaultListModel<>();
            //发送给服务器
            new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_FRIEND.toString() + " " + userid);
            try {
                //阻塞线程，等待发送的消息接收并且处理后
                code = (int) Handle.queue.take();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if(code == 300){
                //实现刷新
                flushFriendList();
            }
            //高度自定义，防止点击空白处自动获取list的最后一行
            if(friendList.length != 0){
                listPanel.setSize(new Dimension(295, (friendModel.size())* friendJList.getFixedCellHeight() + 3));
            }else {
                //高度为0
                listPanel.setSize(new Dimension(295, 0));
            }
            //重新填入model显示数据
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
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if(code == 400){
                //实现刷新
                flushGroupList();
            }
            //高度自定义，防止点击空白处自动获取list的最后一行
            if(groupList.length != 0){
                listPanel.setSize(new Dimension(295, (groupModel.size())* groupJList.getFixedCellHeight() + 3));
            }else {
                //高度为0
                listPanel.setSize(new Dimension(295, 0));
            }
            //重新填入model显示数据
            groupJList.setModel(groupModel);
        }
    }

    /**
     * 关闭与XXX的聊天窗口，重置isopen,可打开新的与XXX的聊天给窗口
     * @param closeIndex 聊天对象索引
     */
    public static void friendClose(int closeIndex){
        isopenFriend.put(closeIndex,false);
    }

    /**
     * 关闭与XXX的聊天窗口，重置isopen,可打开新的与XXX的聊天给窗口
     * @param closeIndex 聊天对象索引
     */
    public static void groupClose(int closeIndex){
        isopenGroup.put(closeIndex,false);
    }

}
