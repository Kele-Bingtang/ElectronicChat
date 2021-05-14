package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.controller.Handle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.Socket;

public class RegisterFrame extends JFrame {

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

    public RegisterFrame(Socket socket,int X,int Y){
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
        JButton registerButton = new JButton("注册");
        JButton returnButton = new JButton("返回");
        //上沿显示标签
        JLabel topLabel1 = new JLabel(new ImageIcon("src/ChatClient/Image/topIcon.png"));
        JLabel topLabel2 = new JLabel(new ImageIcon("src/ChatClient/Image/tx1.png"));
        //账号标签
        useridLabel = new JLabel(new ImageIcon("src/ChatClient/Image/useridIcon1.png"));
        //密码标签
        passwordLabel = new JLabel(new ImageIcon("src/ChatClient/Image/pwdIcon1.png"));
        //二维码
        JLabel twoCodeLabel = new JLabel("二维码");
        //一条线
        lineLabel_1 = new JLabel();
        lineLabel_2 = new JLabel();
        //自动登录
        //标签变为可选状态
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

        JLabel tipLabel = new JLabel("请填写正确的用户名和密码，点击注册");
        tipLabel.setForeground(Color.GRAY);
        tipLabel.setFont(new Font(null,Font.PLAIN,15));
        tipLabel.setBounds(110,250,280,20);

        //添加文本框
        container.add(lineLabel_1);
        container.add(lineLabel_2);
        container.add(userField);
        container.add(passField);
        container.add(tipLabel);

        //添加设置二维码
        twoCodeLabel.setFont(new Font("宋体",Font.PLAIN,15));
        twoCodeLabel.setForeground(Color.GRAY);
        twoCodeLabel.setBounds(430,320,80,25);
        container.add(twoCodeLabel);
        //设置边框信息
        returnButton.setBorderPainted(false);//无边框
        returnButton.setBounds(110,280,70,45);


        registerButton.setBorderPainted(false);//无边框
        registerButton.setBounds(305,280,70,45);

        //添加按钮
        container.add(returnButton);
        container.add(registerButton);

        setLayout(null);

        //设置窗体的标题
        setTitle("第八组");
        //给标题设置图片
        setIconImage(new ImageIcon("src/Image/8Icon.png").getImage());
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
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword());
                if(username.equals("") || username.contains(" ") || password.equals("") || password.contains(" ")){
                    new TipMessageFrame().SuccOrFail("错误","用户名或者密码为空");
                }else {
                    new Send(socket).sendMsg(EnMsgType.EN_MSG_REGISTER.toString() + " " +username + ":" + password);
                    try {
                        Handle.queue.take();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
                new LoginFrame(socket,getX(),getY());
                setVisible(false);
                dispose();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(socket,getX(),getY());
                setVisible(false);
                dispose();
            }
        });
    }
}
