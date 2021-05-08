
package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.controller.Handle;

import java.awt.*;
import java.net.Socket;
import javax.swing.*;

/**
 * 登陆窗口
 */
public class LoginFrame extends JFrame {
    private static Socket socket;
    private JLabel label1;
    public static JTextField userField = new JTextField();
    private JLabel label2;
    private JTextField passField;
    private JButton button1;
    private JButton button2;

    public static void main(String[] args) {
        new LoginFrame(socket);
    }

    public LoginFrame(Socket socket) {
        this.socket = socket;
        initComponents();
    }

    private void initComponents() {
        label1 = new JLabel();
        userField = new JTextField();      //用户名
        label2 = new JLabel();
        passField = new JTextField();  //密码
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- 用户名 ----
        label1.setText("\u7528\u6237\u540d");
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(70, 60), label1.getPreferredSize()));
        contentPane.add(userField);
        userField.setBounds(130, 60, 165, 25);

        //---- 密码 ----
        label2.setText("\u5bc6\u7801");
        contentPane.add(label2);
        label2.setBounds(75, 115, 35, label2.getPreferredSize().height);
        contentPane.add(passField);
        passField.setBounds(130, 115, 165, 25);

        //---- 登录按钮 ----
        button1.setText("\u767b\u5f55");
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(85, 170), button1.getPreferredSize()));

        button1.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();
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
                    MainFrame mainFrame = new MainFrame(socket,username,Handle.nickName,Handle.signature,Handle.friends,Handle.groups);
                    mainFrame.init();
                }

            }else {
                System.out.println("失败");
            }
        });

        //---- button2 ----
        button2.setText("\u9000\u51fa");
        contentPane.add(button2);
        button2.setBounds(new Rectangle(new Point(240, 170), button2.getPreferredSize()));
        button2.addActionListener(e -> {
            System.exit(1);
        });

        contentPane.setPreferredSize(new Dimension(400, 300));
        pack();
        setLocation(740 ,335);
        setVisible(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
