package Swing;

import ChatClient.Chat.Send;
import ChatClient.load.LoadDatas;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatFrame extends JFrame{
    //背景
    JLabel label;
    public static JTextPane displayTextPanel;

    //暂时存放消息，防止覆盖
    //加static是因为之加载一次
    public static StringBuilder sb = new StringBuilder();
    public StringBuilder historyBui;

    //时间的格式
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Socket socket;
    //聊天对象昵称
    String chatName;
    //是否打开窗口，没有则自动
    public static boolean isCreate = false;

    public static Map<String,StringBuilder> history = new HashMap<>();

    public ChatFrame(Socket socket,String chatName){
        isCreate = true;
        this.socket = socket;
        this.chatName = chatName;
        init();
    }

    public void init(){
        setLayout(new BorderLayout());
        setSize(550, 500);
        setLocation(700,250);
        //设置窗口不能调节大小
        setResizable(false);
        //设置聊天窗口总是前置
        setAlwaysOnTop(true);
        setTitle(new LoadDatas().getNickName(LoginFrame.userField.getText()) + "与" + chatName + "聊天");
        //聊天信息框不可编辑
        displayTextPanel = new JTextPane();
        displayTextPanel.setEditable(false);
        //聊天内容显示文本面板
        JScrollPane displayPanel = new JScrollPane(displayTextPanel);

        //聊天信息框设置为透明
        displayPanel.getViewport().setOpaque(false);
        displayPanel.setOpaque(false);
        displayTextPanel.setOpaque(false);
        displayTextPanel.setFont(new Font("微软雅黑",Font.PLAIN,15));

        JTextPane inputTextPanel = new JTextPane();
        //输入聊天文本面板
        JScrollPane inputPanel = new JScrollPane(inputTextPanel);
        inputPanel.setOpaque(false);
        inputPanel.getViewport().setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(100,100));
        inputTextPanel.setFont(new Font("微软雅黑",Font.PLAIN,15));
        inputTextPanel.setOpaque(false);


        //发送按钮初始化
        //按钮
        JButton sendButton = new JButton("发送");
        sendButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        sendButton.setFocusable(false);//刚开始没有焦点
        sendButton.setOpaque(false);

        sendButton.addActionListener(e -> {
            String nickName = new LoadDatas().getNickName(LoginFrame.userField.getText());
            String msg = inputTextPanel.getText();
            inputTextPanel.setText("");
            String message = nickName + "  " + sf.format(new Date()) + "\n" + "    " + msg;  //开头
            sb.append(message).append("\n");
            displayTextPanel.setText(sb.toString());  //输入的信息，无法输入历史信息
            historyBui = new StringBuilder();
            //存储聊天记录
            setHitory(message);
            //发送你要聊天的对象
            sendMsg(chatName);
            //发送消息出去
            sendMsg(message);
        });

        //表情按钮初始化
        JButton emjioButton = new JButton("表情");
        emjioButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        emjioButton.setFocusable(false);

        //查看历史记录
        JButton historyButtuon = new JButton("历史记录");
        historyButtuon.setFont(new Font("微软雅黑",Font.PLAIN,18));
        historyButtuon.addActionListener(e -> {
            getHitory();
        });

        JPanel paneLeftSouth = new JPanel();
        paneLeftSouth.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.add(sendButton);
        toolBar.add(emjioButton);
        toolBar.addSeparator(new Dimension(300,20));
        toolBar.add(historyButtuon);
        paneLeftSouth.add(toolBar,BorderLayout.NORTH);
        paneLeftSouth.add(inputPanel,BorderLayout.CENTER);
        paneLeftSouth.setOpaque(false);

        label = new JLabel(/*new ImageIcon("src/ChatClient/Image/聊天背景1.png")*/);
        label.setLayout(new BorderLayout());
        label.setOpaque(false);
        label.add(displayPanel,BorderLayout.CENTER);
        label.add(paneLeftSouth,BorderLayout.SOUTH);
        add(label,BorderLayout.CENTER);
        setIconImage(new ImageIcon("src/ChatClient/Image/3.png").getImage());
        setVisible(true);
    }

    public void sendMsg(String message){
       new Send(socket).sendMsg(message);
    }

    public void ceateNew(String meesage){
        sb.append(meesage).append("\n");
        displayTextPanel.setText(sb.toString());;
    }

    /**
     * 存储历史记录
     */
    public void setHitory(String message){
        Set<String> keySet = history.keySet();
        for (String key : keySet) {
            if (key.equals(chatName)) {
                historyBui.append(history.get(key).toString());
            }
        }
        historyBui.append(message).append("\n");
        history.put(chatName,historyBui);
        System.out.println("存储" + chatName + "的聊天记录");
    }
    /**
     * 获取历史记录
     */
    public void getHitory(){
        //获取历史记录先清空记录
        System.out.println("进入历史记录");
        displayTextPanel.setText("");
        Set<String> set = history.keySet();
        for (String key : set) {
            if (key.equals(chatName)) {
                System.out.println("查询与" + key + "的聊天记录");
                displayTextPanel.setText(history.get(key).toString());
            }
            System.out.println(key + "的历史记录：" + history.get(key).toString());
        }
    }
}
