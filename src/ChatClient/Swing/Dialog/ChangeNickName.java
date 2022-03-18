package ChatClient.Swing.Dialog;

import ChatClient.Swing.Frame.MainFrame;
import Utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 修改昵称界面
 */
public class ChangeNickName extends JDialog {
    //判断是否按确定按钮
    boolean isSure = false;
    //输入新昵称的文本框
    JTextField newNameField;
    //界面宽度
    int width = 400;
    //界面高度
    int height = 350;

    /**
     * 初始化界面，获得主界面的昵称
     * @param mainFrame 主界面
     * @param nickName 昵称
     */
    public ChangeNickName(MainFrame mainFrame, String nickName){
        super(mainFrame,"修改昵称",true);
        setLayout(null);
        //设置大小
        setSize(width,height);
        //获取最大的面板
        Container container = getContentPane();

        JLabel label1 = new JLabel("当前的昵称：");
        //字体格式
        label1.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label1.setBounds(85,25,150,20);
        //添加
        container.add(label1);

        //旧昵称框
        JTextField oldNameField = new JTextField(20);
        //字体格式
        oldNameField.setFont(new Font("宋体",Font.PLAIN,20));
        //位置和大小
        oldNameField.setBounds(85,50,200,40);
        //设置为无法编辑
        oldNameField.setEditable(false);
        //把从主界面获取得昵称显示
        oldNameField.setText(nickName);
        container.add(oldNameField);

        JLabel label2 = new JLabel("修改的昵称：");
        //字体格式
        label2.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label2.setBounds(85,140,150,20);
        container.add(label2);

        newNameField = new JTextField (20);
        //字体格式
        newNameField.setFont(new Font("宋体",Font.PLAIN,20));
        //位置和大小
        newNameField.setBounds(85 ,165,200,40);
        container.add(newNameField);

        JButton sureButton = new JButton("确定");
        sureButton.setFont(new Font("宋体",Font.PLAIN,18));
        sureButton.setBounds(280,250,80,30);
        //设置背景色
        sureButton.setBackground(Color.GRAY);
        //按钮透明
        sureButton.setOpaque(false);
        container.add(sureButton);

        JLabel label = new JLabel(new ImageIcon(ImageUtils.getImageUrl("changeNickName.png")));
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
     * 获取新昵称框的数据
     * @return 新密码框的数据
     */
    public String getValues(){
        return newNameField.getText();
    }
}
