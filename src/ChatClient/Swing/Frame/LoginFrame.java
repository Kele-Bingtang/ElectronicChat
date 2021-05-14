package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.controller.Handle;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

public class LoginFrame extends JFrame {

    Socket socket;
    //账号文本
    private JTextField userField;
    //密码文本框
    JPasswordField passField;
    //标签
    private JLabel useridLabel;
    private JLabel passwordLabel;
    private JLabel lineLabel_1;
    private JLabel lineLabel_2;

    int X,Y;
    int width = 490;
    int height = 380;

    private JComboBox<String> useridBox;

    public LoginFrame(Socket socket){
        this.X = 700;
        this.Y = 300;
        this.socket = socket;
        initLogin();
    }

    public LoginFrame(Socket socket,int X,int Y){
        this.socket = socket;
        this.X = X;
        this.Y = Y;
        initLogin();
    }

    //存储图形用户界面
    public void initLogin(){
        //容器
        Container container = getContentPane();
        container.setBackground(Color.WHITE);

        //初始化账号文本
        userField = new JTextField();
        //初始化密码文本
        //密码文本
        passField = new JPasswordField();
        //初始化登录按钮
        //登录按钮
        JButton loginButton = new JButton("安全登录", new ImageIcon("src/ChatClient/Image/2.png"));
        //上沿显示标签
        JLabel topLabel1 = new JLabel(new ImageIcon("src/ChatClient/Image/topIcon.png"));
        JLabel topLabel2 = new JLabel(new ImageIcon("src/ChatClient/Image/icon(10).jpg"));
        //账号标签
        useridLabel = new JLabel(new ImageIcon("src/ChatClient/Image/useridIcon1.png"));
        //密码标签
        passwordLabel = new JLabel(new ImageIcon("src/ChatClient/Image/pwdIcon1.png"));
        //下拉框
        useridBox = new JComboBox<String>();
        //忘记密码
        JLabel forgetPwdLabel = new JLabel("找回密码");
        //注册账号
        JLabel registerLabel = new JLabel("注册账号");
        //二维码
        JLabel twoCodeLabel = new JLabel("二维码");
        //一条线
        lineLabel_1 = new JLabel();
        lineLabel_2 = new JLabel();
        //自动登录
        //复选框
        JCheckBox autoLoginCheckBox = new JCheckBox("自动登录");
        //记住密码
        JCheckBox autogetPwdCheckBox = new JCheckBox("记住密码");
        //标签变为可选状态
        forgetPwdLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        twoCodeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topLabel1.setBounds(0,0,495,140);
        topLabel2.setBounds(200,60,80,80);
        container.add(topLabel2);
        container.add(topLabel1);

        //设置图标位置
        useridLabel.setBounds(108,175,30,20);
        passwordLabel.setBounds(108,215,30,20);
        //添加标签
        container.add(useridLabel);
        container.add(passwordLabel);
        //设置密码文本与账号文本大小
        userField.setBounds(140,169,225,30);
        userField.setBorder(BorderFactory.createEmptyBorder());
        userField.setFont(new Font(null,Font.PLAIN,20));
        userField.setForeground(Color.GRAY);

        passField.setBounds(140,210,235,30);
        passField.setFont(new Font(null,Font.PLAIN,20));
        passField.setForeground(Color.GRAY);
        passField.setBorder(BorderFactory.createEmptyBorder());
        //设置下划线
        lineLabel_1.setBounds(115,201,260,1);
        lineLabel_1.setBorder(BorderFactory.createLineBorder( new Color(151, 151, 151)));
        userField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                useridLabel.setIcon(new ImageIcon("src/ChatClient/Image/useridIcon2.png"));
                lineLabel_1.setBorder(BorderFactory.createLineBorder(new Color(30,144,255)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                useridLabel.setIcon(new ImageIcon("src/ChatClient/Image/useridIcon1.png"));
                lineLabel_1.setBorder(BorderFactory.createLineBorder( new Color(186, 186, 186, 171)));
            }
        });

        lineLabel_2.setBounds(115,240,15,1);
        lineLabel_2.setBorder(BorderFactory.createLineBorder( new Color(186, 186, 186, 171)));

        passField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordLabel.setIcon(new ImageIcon("src/ChatClient/Image/pwdIcon2.png"));
                lineLabel_2.setBorder(BorderFactory.createLineBorder(new Color(30,144,255)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordLabel.setIcon(new ImageIcon("src/ChatClient/Image/pwdIcon1.png"));
                lineLabel_2.setBorder(BorderFactory.createLineBorder( new Color(186, 186, 186, 171)));
            }
        });

        //初始化下拉菜单
        initComBox();


        //添加文本框
        container.add(lineLabel_1);
        container.add(lineLabel_2);
        container.add(userField);
        container.add(passField);
        container.add(useridBox);

        //设置忘记密码、注册账号标签
        forgetPwdLabel.setFont(new Font("宋体",Font.PLAIN,13));
        forgetPwdLabel.setBounds(320,250,135,20);
        forgetPwdLabel.setForeground(Color.GRAY);

        registerLabel.setFont(new Font("宋体",Font.PLAIN,15));
        registerLabel.setForeground(Color.GRAY);
        registerLabel.setBounds(10,315,80,20);
        //添加设置二维码
        twoCodeLabel.setFont(new Font("宋体",Font.PLAIN,15));
        twoCodeLabel.setForeground(Color.GRAY);
        twoCodeLabel.setBounds(430,320,80,25);
        container.add(twoCodeLabel);
        //添加标签
        container.add(registerLabel);
        container.add(forgetPwdLabel);
        //设置边框信息
        loginButton.setBorderPainted(false);//无边框
        loginButton.setBounds(110,280,270,45);
        //添加按钮
        container.add(loginButton);

        //设置、添加复选框的位置大小
        autoLoginCheckBox.setFont(new Font("宋体", Font.PLAIN,13));
        autoLoginCheckBox.setBounds(110,250,95,20);
        autoLoginCheckBox.setForeground(Color.GRAY);
        autoLoginCheckBox.setBackground(Color.WHITE);
        autogetPwdCheckBox.setFont(new Font("宋体",Font.PLAIN,13));
        autogetPwdCheckBox.setBounds(215,250,95,20);
        autogetPwdCheckBox.setForeground(Color.GRAY);
        autogetPwdCheckBox.setBackground(Color.WHITE);
        container.add(autoLoginCheckBox);
        container.add(autogetPwdCheckBox);

        setLayout(null);

        //设置窗体的标题
        setTitle("第八组");
        //给标题设置图片
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
        //窗口不可变
        setResizable(false);
        //开局失去焦点
        setFocusable(true);
        //置顶
        setAlwaysOnTop(true);
        //初始化窗体位置大小
        setBounds(X,Y,width,height);
        //窗口可见
        setVisible(true);


        //设置窗体的关闭方式
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = userField.getText().trim();
                String password = new String(passField.getPassword());
                JDialog dialog = new JDialog();
                dialog.setLocation(-145,0);
                dialog.setVisible(true);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowOpened(WindowEvent e) {
                        loginButton.setEnabled(false);
                    }
                    @Override
                    public void windowClosed(WindowEvent e) {
                        loginButton.setEnabled(true);
                    }
                });
                if(username.equals("") || username.contains(" ") || password.equals("") || password.contains(" ")){
                    new TipMessageFrame().SuccOrFail("错误","用户名或者密码为空");
                    //取消置顶
                    setAlwaysOnTop(false);
                    dialog.dispose();
                }else {
                    new Send(socket).sendMsg(EnMsgType.EN_MSG_LOGIN.toString() + " " +username + ":" + password);
                    int code = 0;
                    try {
                        //获取quenue，没有获取之前卡在这一步，别处必须offer或者put
                        code = (int) Handle.queue.take();
                        System.out.println("验证登录");
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    if(code == 200){
                        new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_INFORMATION.toString() + " " + username);
                        new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_GROUP_INFROMATION.toString() + " " + username);
                        new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_FRIEND.toString() + " " + username);
                        try {
                            //获取quenue，没有获取之前卡在这一步，别处必须offer或者put
                            Handle.queue.take();
                            code = (int) Handle.queue.take();
                            System.out.println("获取个人信息，好友列表，群组列表:" + code);
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        //关闭自己
                        if(code == 300 || code == 400){
                            dispose();
                            MainFrame mainFrame = new MainFrame(socket,username,Handle.headIconID,Handle.nickName,Handle.signature,Handle.friends,Handle.groups);
                            mainFrame.init();
                            dialog.dispose();
                        }
                    }else {
                        //取消置顶
                        setAlwaysOnTop(false);
                        dialog.dispose();
                    }
                }
            }
        });

        //注册
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame(socket,getX(),getY());
                setVisible(false);
                dispose();

            }
        });

    }

    public void initComBox(){
        //去掉箭头边框
        useridBox.setUI(new MyComBoxUI());

        //设置下拉框
        useridBox.setBounds(140,169,235,30);
        useridBox.setFont(new Font(null,Font.PLAIN,18));
        useridBox.setBackground(Color.WHITE);
        useridBox.setForeground(Color.LIGHT_GRAY);
        useridBox.setBorder(BorderFactory.createEmptyBorder());
        useridBox.setOpaque(false);

        //设置选项内容
        String item1 = "kele";
        String item2 = "bing";
        String item3 = "lsb";
        String item4 = "myq";

        useridBox.addItem(item1);
        useridBox.addItem(item2);
        useridBox.addItem(item3);
        useridBox.addItem(item4);

        //添加选择项背景色
        useridBox.setRenderer(new MyCellRenderer());

        useridBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(useridBox.getSelectedItem().equals(item1)){
                    userField.setText(item1);
                }else if(useridBox.getSelectedItem().equals(item2)){
                    userField.setText(item2);
                }else if(useridBox.getSelectedItem().equals(item3)){
                    userField.setText(item3);
                }else if(useridBox.getSelectedItem().equals(item4)){
                    userField.setText(item4);
                }
            }
        });

    }

}




