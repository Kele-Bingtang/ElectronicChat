package Swing.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TipMessageFrame extends JFrame {
    /**
     * 修改密码错误提示
     */
    public void modifypasswordFail(){
        setTitle("密码错误");
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(300,160);
        setLocation(800,350);
        setVisible(true);
        //无法更改窗口大小
        setResizable(false);
        Container container = getContentPane();

        JLabel sendTip = new JLabel("您输入的密码错误，请输入正确的密码");
        sendTip.setFont(new Font("黑体",Font.PLAIN,15));
        sendTip.setBounds(15,18,300,80);
        container.add(sendTip);
        JButton sureButton = new JButton("确定");
        sureButton.setBounds(110,90,75,25);
        container.add(sureButton);

        sureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    public void modifypasswordSucc(){
        setTitle("修改密码成功");
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(300,160);
        setLocation(800,350);
        setVisible(true);
        //无法更改窗口大小
        setResizable(false);
        Container container = getContentPane();

        JLabel sendTip = new JLabel("您已经成功修改密码，请记住您的密码");
        sendTip.setFont(new Font("黑体",Font.PLAIN,15));
        sendTip.setBounds(15,18,300,80);
        container.add(sendTip);
        JButton sureButton = new JButton("确定");
        sureButton.setBounds(110,90,75,25);
        container.add(sureButton);

        sureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    /**
     * 有人发送消息给你 提示
     * @param chatName 发送者
     */
    public void sendMessageTip(String chatName){
        setTitle("消息提醒");
        setAlwaysOnTop(true);
        setLayout(null);
        setSize(300,160);
        setLocation(1620,920);
        setVisible(true);
        //无法更改窗口大小
        setResizable(false);
        Container container = getContentPane();

        JLabel sendTip = new JLabel(chatName + "发消息给您了");
        sendTip.setFont(new Font("黑体",Font.PLAIN,18));
        sendTip.setBounds(30,15,200,80);
        container.add(sendTip);
        JButton sureButton = new JButton("确定");
        sureButton.setBounds(110,90,75,25);
        container.add(sureButton);

        sureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

}
