package Swing.Dialog;

import ChatClient.controller.Handle;
import Swing.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangeNickName extends JDialog {

    boolean isExit = false;

    JTextField newNameField;

    public ChangeNickName(MainFrame mainFrame, String NickName){
        super(mainFrame,"修改昵称",true);
        setLayout(null);
        setSize(400,350);
        Container container = getContentPane();

        JLabel label1 = new JLabel("当前的昵称：");
        label1.setBounds(85,25,100,20);
        container.add(label1);

        JTextField oldNameField = new JTextField(20);
        oldNameField.setBounds(85,50,200,40);
        oldNameField.setFont(new Font("黑体",Font.PLAIN,18));
        oldNameField.setText(NickName);
        oldNameField.setEditable(false);
        container.add(oldNameField);

        JLabel label2 = new JLabel("修改的昵称：");
        label2.setBounds(85,125,100,20);
        container.add(label2);

        newNameField = new JTextField(20);
        newNameField.setBounds(85,150,200,40);
        newNameField.setFont(new Font("黑体",Font.PLAIN,18));
        container.add(newNameField);

        JButton sureButton = new JButton("确定");
        sureButton.setBounds(250,250,80,30);
        container.add(sureButton);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isExit = false;
                dispose();
            }
        });

        sureButton.addActionListener(e -> {
            isExit = true;
            setVisible(false);
            dispose();
        });

    }
    public boolean isClick(){
        setLocation(700,300);
        setVisible(true);
        return isExit;
    }

    public String getValues(){
        return newNameField.getText();
    }
}
