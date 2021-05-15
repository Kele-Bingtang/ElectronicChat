package ChatClient.Swing.Dialog;

import ChatClient.Swing.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangeSignature extends JDialog{

    boolean isExit = false;

    JTextArea newSignatrueField;

    int width = 400;
    int height = 350;
    public ChangeSignature(MainFrame mainFrame, String signature){
        super(mainFrame,"修改签名",true);
        setLayout(null);
        setSize(width,height);
        Container container = getContentPane();

        JLabel label1 = new JLabel("当前的签名：");
        label1.setFont(new Font("宋体",Font.PLAIN,18));
        label1.setBounds(20,25,150,20);
        container.add(label1);

        JTextArea oldSignatrueField = new JTextArea();
        oldSignatrueField.setBounds(20,50,340,75);
        oldSignatrueField.setFont(new Font("宋体",Font.PLAIN,20));
        oldSignatrueField.setEditable(false);
        oldSignatrueField.setText(signature);
        oldSignatrueField.setLineWrap(true);
        oldSignatrueField.setWrapStyleWord(true);
        container.add(oldSignatrueField);

        JLabel label2 = new JLabel("修改的签名：");
        label2.setFont(new Font("宋体",Font.PLAIN,18));
        label2.setBounds(20,135,150,20);
        container.add(label2);

        newSignatrueField = new JTextArea();
        newSignatrueField.setBounds(20,160,340,75);
        newSignatrueField.setFont(new Font("宋体",Font.PLAIN,20));
        newSignatrueField.setLineWrap(true);
        newSignatrueField.setWrapStyleWord(true);
        container.add(newSignatrueField);

        JButton sureButton = new JButton("确定");
        sureButton.setFont(new Font("宋体",Font.PLAIN,18));
        sureButton.setBounds(280,250,80,30);
        sureButton.setOpaque(false);
        sureButton.setBackground(Color.GRAY);
        container.add(sureButton);

        JLabel label = new JLabel(new ImageIcon("src/ChatClient/Image/changeSignaure.png"));
        label.setBounds(0, 0, width, height);
        container.add(label,new Integer(Integer.MIN_VALUE));

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
        return newSignatrueField.getText();
    }
}
