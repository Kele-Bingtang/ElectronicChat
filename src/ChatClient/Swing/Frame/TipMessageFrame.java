package ChatClient.Swing.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TipMessageFrame extends JFrame {


    public TipMessageFrame(){

    }

    /**
     * 修改密码错误提示
     * @param title 标题
     * @param tipMessage 内容
     */
    public void SuccOrFail(String title,String tipMessage){
        //这是标题
        setTitle(title);
        //置顶
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(300,160);
        setLocation(800,350);
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
        setVisible(true);
        //无法更改窗口大小
        setResizable(false);
        Container container = getContentPane();

        JLabel sendTip = new JLabel(tipMessage);
        sendTip.setFont(new Font("黑体",Font.PLAIN,15));
        sendTip.setBounds(15,15,300,80);
        container.add(sendTip);
        JButton sureButton = new JButton("确定");
        sureButton.setBounds(110,90,75,25);
        container.add(sureButton);

        sureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


    }

    /**
     * 有人发送消息给你 提示
     * @param title 标题
     * @param tipMessage 内容
     * @param isCenter 是否居中
     */
    public void sendMessageTip(String title,String tipMessage,boolean isCenter){
        //设置标题
        setTitle(title);
        //置顶
        setAlwaysOnTop(true);
        setLayout(null);
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
        setSize(300,160);
        //居中位置和右下底部
        if(isCenter){
            setLocation(800,350);
        }else {
            setLocation(1620,920);

        }
        setVisible(true);
        //无法更改窗口大小
        setResizable(false);
        Container container = getContentPane();

        JLabel sendTip = new JLabel(tipMessage);
        sendTip.setFont(new Font("黑体",Font.PLAIN,16));
        sendTip.setBounds(30,15,300,100);
        container.add(sendTip);
        JButton sureButton = new JButton("确定");
        sureButton.setBounds(110,90,75,25);
        container.add(sureButton);

        sureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

}
