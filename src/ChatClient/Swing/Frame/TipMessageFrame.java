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
     */
    public void SuccOrFail(String title,String tipMessage){
        setTitle(title);
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(300,160);
        setLocation(800,350);
        setVisible(true);
        //无法更改窗口大小
        setResizable(false);
        Container container = getContentPane();

        JLabel sendTip = new JLabel(tipMessage);
        sendTip.setFont(new Font("黑体",Font.PLAIN,15));
        sendTip.setBounds(15,18,300,80);
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
     * @param tipMessage 提示
     */
    public void sendMessageTip(String title,String tipMessage,boolean isCenter){
        setTitle(title);
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(300,160);
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
        sendTip.setFont(new Font("黑体",Font.PLAIN,18));
        sendTip.setBounds(30,15,200,80);
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                dispose();
            }
        });

    }

}
