package Swing.Dialog;

import Swing.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ChangeSignature extends JDialog{

    boolean isExit = false;

    JTextArea newSignatrueField;

    public ChangeSignature(MainFrame mainFrame, String signature){
        super(mainFrame,"修改签名",true);
        setLayout(null);
        setSize(400,350);
        Container container = getContentPane();

        JLabel label1 = new JLabel("当前的签名：");
        label1.setBounds(20,25,100,20);
        container.add(label1);

        JTextArea oldSignatrueField = new JTextArea();
        oldSignatrueField.setBounds(20,50,300,65);
        oldSignatrueField.setFont(new Font("黑体",Font.PLAIN,18));
        oldSignatrueField.setText(signature);
        oldSignatrueField.setLineWrap(true);
        oldSignatrueField.setWrapStyleWord(true);
        oldSignatrueField.setEditable(false);
        container.add(oldSignatrueField);

        JLabel label2 = new JLabel("修改的签名：");
        label2.setBounds(20,120,100,20);
        container.add(label2);

        newSignatrueField = new JTextArea();
        newSignatrueField.setBounds(20,150,300,65);
        newSignatrueField.setFont(new Font("黑体",Font.PLAIN,18));
        newSignatrueField.setLineWrap(true);
        newSignatrueField.setWrapStyleWord(true);
        container.add(newSignatrueField);

        JButton sureButton = new JButton("确定");
        sureButton.setBounds(250,250,80,30);
        container.add(sureButton);

        sureButton.addActionListener(e -> {
            isExit = true;
            setVisible(false);
        });
    }

    public boolean isClick(){
        setLocation(700,300);
        setVisible(true);
        return isExit;
    }

    public String getValues(){
        return newSignatrueField.getText();
    }
}
