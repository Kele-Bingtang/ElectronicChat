package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.cons.ShowQRCode;
import ChatClient.controller.Handle;
import trade.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.Socket;
import java.util.List;

/**
 * 注册界面
 */
public class RegisterFrame extends JFrame {
    //通信
    Socket socket;
    //账号文本
    private JTextField userField;
    //密码文本框
    JPasswordField passField;
    //用户标签
    private JLabel useridLabel;
    //密码标签
    private JLabel passwordLabel;
    //用户标签下的线
    private JLabel lineLabel_1;
    //密码标签的线
    private JLabel lineLabel_2;

    //位置
    int X,Y;
    //大小
    int width = 490;
    int height = 380;

    public RegisterFrame(Socket socket,int X,int Y){
        this.socket = socket;
        this.X = X;
        this.Y = Y;
        initLogin();
    }

    /**
     * 初始化登录界面
     */
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
        JLabel topLabel2 = new JLabel(new ImageIcon("src/ChatClient/Image/icon/icon(10).jpg"));
        //账号标签
        useridLabel = new JLabel(new ImageIcon("src/ChatClient/Image/useridIcon1.png"));
        //密码标签
        passwordLabel = new JLabel(new ImageIcon("src/ChatClient/Image/pwdIcon1.png"));
        //二维码
        JLabel twoCodeLabel = new JLabel("打赏");
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

        twoCodeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JFrame frame = new JFrame();
                frame.setLayout(null);
                frame.setAlwaysOnTop(true);
                frame.setLocation(750,325);
                frame.setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
                frame.setSize(400,350);
                JLabel priceLabel = new JLabel("打赏金额：");
                priceLabel.setSize(100,50);
                priceLabel.setLocation(25,100);
                priceLabel.setFont(new Font("微软雅黑",Font.PLAIN,18));
                JTextField priceField = new JTextField(20);
                priceField.setFont(new Font("微软雅黑",Font.PLAIN,18));
                priceField.setSize(200,50);
                priceField.setLocation(125,100);
                frame.add(priceField);
                frame.add(priceLabel);

                JButton sureButton = new JButton("确定");
                sureButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
                sureButton.setOpaque(false);
                sureButton.setBackground(Color.GRAY);
                sureButton.setSize(100,50);
                sureButton.setLocation(225,200);
                frame.add(sureButton);
                frame.setVisible(true);

                sureButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean mkdirs = new File("D:\\二维码").mkdirs();
                        Thread t1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //生成二维码
                                Main main = new Main();
                                main.test_trade_precreate(priceField.getText());

                                //按时间顺序显示E盘下的所有png的图片( join()命令)
                                //在t1线程中把最后一个图片路径拿到，然后作为参数传入
                                List<File> qrCodeList = ShowQRCode.getFileSort("D:\\二维码");
                                new RewardFrame(qrCodeList.get(qrCodeList.size() - 1).getAbsolutePath());
                            }
                        }
                        );
                        Thread t2=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //等待二维码生成后，才能调用下面的类去显示二维码
                            }
                        }
                        );
                        t1.start();
                        try {
                            t1.join();//必须等t1执行完毕
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        t2.start();
                    }
                });
            }
        });
        //设置边框信息
        returnButton.setBorderPainted(false);//无边框
        returnButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        returnButton.setBackground(Color.GRAY);
        returnButton.setOpaque(false);
        returnButton.setBounds(110,280,70,45);


        registerButton.setBorderPainted(false);//无边框
        registerButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        registerButton.setBackground(Color.GRAY);
        registerButton.setOpaque(false);
        registerButton.setBounds(305,280,70,45);

        //添加按钮
        container.add(returnButton);
        container.add(registerButton);

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


        //注册按钮
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword());
                if(username.equals("") || username.contains(" ") || username.contains(":") || password.equals("") || password.contains(" ")){
                    //发送给服务器
                    new TipMessageFrame().SuccOrFail("错误","用户名或者密码非法(空或者特殊字符串)");
                }else {
                    int code = 0;
                    //发送给服务器
                    new Send(socket).sendMsg(EnMsgType.EN_MSG_REGISTER.toString() + " " +username + ":" + password);
                    try {
                        //获取处理发送消息后的code
                        code = (int) Handle.queue.take();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    //注册成功
                    if(code == 150){
                        new LoginFrame(socket,getX(),getY());
                        new TipMessageFrame().SuccOrFail("成功", "注册成功，请记住您的用户名密码");
                        setVisible(false);
                        //关闭自己
                        dispose();
                    }
                }

            }
        });
        //返回按钮
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(socket,getX(),getY());
                setVisible(false);
                //关闭自己
                dispose();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new LoginFrame(socket,getX(),getY());
                setVisible(false);
                //关闭自己
                dispose();
            }
        });
    }
}
