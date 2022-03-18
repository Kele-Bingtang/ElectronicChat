package ChatClient.Swing.Dialog;

import ChatClient.Swing.Frame.MainFrame;
import Utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 修改签名的界面
 */
public class ChangeSignature extends JDialog{
    //判断是否按确定按钮
    boolean isSure = false;
    //输入新签名的多文本框
    JTextArea newSignatrueField;

    //界面宽度
    int width = 400;
    //界面高度
    int height = 350;

    /**
     * 初始化签名界面
     * @param mainFrame 主界面
     * @param signature 签名
     */
    public ChangeSignature(MainFrame mainFrame, String signature){
        super(mainFrame,"修改签名",true);
        setLayout(null);
        //设置大小
        setSize(width,height);
        //获取最大的面板
        Container container = getContentPane();

        JLabel label1 = new JLabel("当前的签名：");
        //字体格式
        label1.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label1.setBounds(20,25,150,20);
        container.add(label1);

        //旧签名框
        JTextArea oldSignatrueField = new JTextArea();
        //位置和大小
        oldSignatrueField.setBounds(20,50,340,75);
        //字体格式
        oldSignatrueField.setFont(new Font("宋体",Font.PLAIN,20));
        //设为不可编辑
        oldSignatrueField.setEditable(false);
        //显示旧签名
        oldSignatrueField.setText(signature);
        //自动换行
        oldSignatrueField.setLineWrap(true);
        //包裹在字边界（即空格）处
        oldSignatrueField.setWrapStyleWord(true);
        container.add(oldSignatrueField);

        JLabel label2 = new JLabel("修改的签名：");
        //字体格式
        label2.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label2.setBounds(20,135,150,20);
        container.add(label2);

        newSignatrueField = new JTextArea();
        //位置和大小
        newSignatrueField.setBounds(20,160,340,75);
        //字体格式
        newSignatrueField.setFont(new Font("宋体",Font.PLAIN,20));
        //自动换行
        newSignatrueField.setLineWrap(true);
        //包裹在字边界（即空格）处
        newSignatrueField.setWrapStyleWord(true);
        container.add(newSignatrueField);

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

        JLabel label = new JLabel(new ImageIcon(ImageUtils.getImageUrl("changeSignaure.png")));
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
     * 获取新签名框的数据
     * @return 新签名框的数据
     */
    public String getValues(){
        return newSignatrueField.getText();
    }
}
