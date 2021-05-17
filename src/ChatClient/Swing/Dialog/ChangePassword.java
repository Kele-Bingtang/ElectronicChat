package ChatClient.Swing.Dialog;

import ChatClient.Swing.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 修改密码界面
 */
public class ChangePassword extends JDialog{
    ////判断是否按确定按钮
    boolean isSure = false;
    //输入旧密码新密码的文本框
    JPasswordField newPasswordField,oldPasswordField;
    //界面宽度
    int width = 400;
    //界面高度
    int height = 350;

    /**
     * 初始化界面
     * @param mainFrame 主界面
     */
    public ChangePassword(MainFrame mainFrame){
        super(mainFrame,"修改密码",true);
        setLayout(null);
        //设置大小
        setSize(400,350);
        //获取最大的面板
        Container container = getContentPane();
        setIconImage(new ImageIcon("src/Image/8Icon.png").getImage());

        JLabel label1 = new JLabel("输人当前的密码：");
        //字体格式
        label1.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label1.setBounds(85,25,150,20);
        container.add(label1);

        //旧密码框
        oldPasswordField = new JPasswordField (20);
        //字体格式
        oldPasswordField.setFont(new Font(null,Font.PLAIN,18));
        //位置和大小
        oldPasswordField.setBounds(85,50,200,40);
        container.add(oldPasswordField);

        JLabel label2 = new JLabel("输人新的的密码：");
        //字体格式
        label2.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label2.setBounds(85,125,150,20);
        container.add(label2);

        //新密码框
        newPasswordField = new JPasswordField (20);
        //字体格式
        newPasswordField.setFont(new Font(null,Font.PLAIN,18));
        //位置和大小
        newPasswordField.setBounds(85 ,165,200,40);
        container.add(newPasswordField);

        JButton sureButton = new JButton("确定");
        //字体格式
        sureButton.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        sureButton.setBounds(280,250,80,30);
        //设置背景色
        sureButton.setBackground(Color.GRAY);
        //按钮透明
        sureButton.setOpaque(false);
        container.add(sureButton);

        JLabel label = new JLabel(new ImageIcon("src/ChatClient/Image/modifyPassword.png"));
        label.setBounds(0, 0, width, height);
        container.add(label,new Integer(Integer.MIN_VALUE));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isSure = false;
                dispose();
            }
        });

        sureButton.addActionListener(e -> {
            isSure = true;
            setVisible(false);
            dispose();
        });

    }
    /**
     * 是否点击确定按钮
     * @return isSure
     */
    public boolean isClick(){
        setLocation(700,300);
        setVisible(true);
        return isSure;
    }

    /**
     * 获取旧密码框的旧密码
     * @return 旧密码
     */
    public String getOldtValues(){
        return new String(oldPasswordField.getPassword());
    }
    /**
     * 获取新密码框的新密码
     * @return 新密码
     */
    public String getNewValues(){
        return new String(newPasswordField.getPassword());
    }
}
