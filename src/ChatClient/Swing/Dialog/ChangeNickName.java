package ChatClient.Swing.Dialog;

import ChatClient.Swing.Frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChangeNickName extends JDialog {

    boolean isExit = false;

    JTextField newNameField;

    int width = 400;
    int height = 350;

    public ChangeNickName(MainFrame mainFrame, String NickName){
        super(mainFrame,"修改昵称",true);
        setLayout(null);
        setSize(width,height);
        Container container = getContentPane();

        JLabel label1 = new JLabel("当前的昵称：");
        label1.setFont(new Font("宋体",Font.PLAIN,18));
        label1.setBounds(85,25,150,20);
        container.add(label1);

        JTextField oldNameField = new JTextField(20);
        oldNameField.setFont(new Font("宋体",Font.PLAIN,20));
        oldNameField.setBounds(85,50,200,40);
        container.add(oldNameField);

        JLabel label2 = new JLabel("修改的昵称：");
        label2.setFont(new Font("宋体",Font.PLAIN,18));
        label2.setBounds(85,140,150,20);
        container.add(label2);

        newNameField = new JTextField (20);
        newNameField.setFont(new Font("宋体",Font.PLAIN,20));
        newNameField.setBounds(85 ,165,200,40);
        container.add(newNameField);

        JButton sureButton = new JButton("确定");
        sureButton.setFont(new Font("宋体",Font.PLAIN,18));
        sureButton.setBounds(280,250,80,30);
        sureButton.setOpaque(false);
        sureButton.setBackground(Color.GRAY);
        container.add(sureButton);

        JLabel label = new JLabel(new ImageIcon("src/ChatClient/Image/changeNickName.png"));
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
        return newNameField.getText();
    }
}
