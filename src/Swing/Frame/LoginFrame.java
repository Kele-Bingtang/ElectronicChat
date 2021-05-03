
package Swing.Frame;

import dao.UserDaoImpl;

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
            UserDaoImpl userDao = new UserDaoImpl();
            boolean isLogin = userDao.getInformation(username,password);
            if(isLogin){
                setVisible(false);
                MainFrame mainFrame = new MainFrame(socket);
                mainFrame.init();
            }else {
                System.out.println("用户名或密码错误，或者用户名不存在，请注册");
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
