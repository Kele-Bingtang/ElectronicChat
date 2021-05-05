package ChatClient.Swing.Dialog;

import ChatClient.Swing.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangePassword extends JDialog{
    boolean isExit = false;

    JPasswordField newPasswordField,oldPasswordField;

    public ChangePassword(MainFrame mainFrame){
        super(mainFrame,"修改密码",true);
        setLayout(null);
        setSize(400,350);
        Container container = getContentPane();

        JLabel label1 = new JLabel("输人当前的密码：");
        label1.setBounds(85,25,150,20);
        container.add(label1);

        oldPasswordField = new JPasswordField (20);
        oldPasswordField.setBounds(85,50,200,30);
        container.add(oldPasswordField);

        JLabel label2 = new JLabel("输人新的的密码：");
        label2.setBounds(85,125,150,20);
        container.add(label2);

        newPasswordField = new JPasswordField (20);
        newPasswordField.setBounds(85 ,150,200,30);
        container.add(newPasswordField);

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

    public String getOldtValues(){
        return new String(oldPasswordField.getPassword());
    }

    public String getNewValues(){
        return new String(newPasswordField.getPassword());
    }
}
