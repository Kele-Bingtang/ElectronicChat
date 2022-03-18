package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.Swing.Frame.MainFrame;
import ChatClient.cons.EnMsgType;
import Utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

/**
 * 创建群聊界面
 */
public class CeateGroupFrame extends JFrame {
    //判断是否按确定按钮
    boolean isSure = false;
    //输入新昵称的文本框
    JTextField grupNameField;
    //界面宽度
    int width = 400;
    //界面高度
    int height = 350;

    /**
     * 初始化界面，获得主界面的昵称
     * @param socket 通信
     * @param userid 用户id
     */
    public CeateGroupFrame(Socket socket, String userid){
        setTitle("创建群聊");
        setLayout(null);
        //设置大小
        setBounds(700,250,width,height);
        //获取最大的面板
        Container container = getContentPane();

        JLabel label1 = new JLabel("群ID：");
        //字体格式
        label1.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label1.setBounds(85,25,150,20);
        //添加
        container.add(label1);

        //群ID
        JTextField groupIDField = new JTextField(20);
        //字体格式
        groupIDField.setFont(new Font("宋体",Font.PLAIN,20));
        //位置和大小
        groupIDField.setBounds(85,50,200,40);
        container.add(groupIDField);

        JLabel label2 = new JLabel("群昵称：");
        //字体格式
        label2.setFont(new Font("宋体",Font.PLAIN,18));
        //位置和大小
        label2.setBounds(85,140,150,20);
        container.add(label2);

        grupNameField = new JTextField (20);
        //字体格式
        grupNameField.setFont(new Font("宋体",Font.PLAIN,20));
        //位置和大小
        grupNameField.setBounds(85 ,165,200,40);
        container.add(grupNameField);

        JButton sureButton = new JButton("确定");
        sureButton.setFont(new Font("宋体",Font.PLAIN,18));
        sureButton.setBounds(280,250,80,30);
        //设置背景色
        sureButton.setBackground(Color.GRAY);
        //按钮透明
        sureButton.setOpaque(false);
        container.add(sureButton);

        JLabel label = new JLabel(new ImageIcon(ImageUtils.getImageUrl("createGroup.png")));
        label.setBounds(0, 0, width, height);
        container.add(label,new Integer(Integer.MIN_VALUE));

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        sureButton.addActionListener(e -> {
            if(groupIDField.getText().equals("") || groupIDField.getText().contains(":") || grupNameField.getText().equals("") ||  grupNameField.getText().equals(":")){
                //发送给服务器
                new TipMessageFrame().SuccOrFail("失败","群ID或者群昵称非法(空或者特殊字符串)");
            }else {
                //发送给服务器
                new Send(socket).sendMsg(EnMsgType.EN_MSG_CREATE_GROUP.toString() + " " + userid + ":" + groupIDField.getText() + ":" + grupNameField.getText());
            }
            setVisible(false);
            //关闭自己
            dispose();
        });

    }
}
