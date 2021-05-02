package Swing;

import ChatClient.Chat.Send;
import ChatClient.load.LoadDatas;
import ChatServer.History;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatFrame extends JFrame{
    //背景
    JLabel label;
    //显示消息的窗口，进行Map包装，一个聊天对象对应一个窗口(displayTextPanel)
    public JTextPane displayTextPanel;

    //暂时存放消息，防止覆盖
    StringBuilder sb = new StringBuilder();
    //先获取历史记录，然后在历史记录后面存储发来的消息，并且重新覆盖历史记录(对外界)
    public static StringBuilder buff = new StringBuilder();
    //先获取历史记录，然后在历史记录后面存储发来的消息，并且重新覆盖历史记录(本类使用)
    public StringBuilder historyBui;
    //时间的格式
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //通信
    Socket socket;
    //聊天对象昵称
    String chatName;
    //客户端昵称
    String nickName;
    //是否打开窗口，没有则自动打开
    public static boolean isCreate = false;
    //历史记录，一个聊天对象对应一个聊天记录
    //public static Map<String,StringBuilder> history = new HashMap<>();
    //一个聊天对象对应一个聊天窗口(实现多窗口聊天)
    public static Map<String ,JTextPane> TextPaneMap = new HashMap<>();
    //发送消息到聊天窗口，一个聊天对象对应一个消息缓冲区(StringBuilder)
    //与jTextPaneMap对应，key相同，根据key将消息放到指定聊天窗口
    public static Map<String,StringBuilder> messageToFrame = new HashMap<>();

    History history;

    Send send;
    public ChatFrame(Socket socket,String chatName){
        isCreate = true;
        this.socket = socket;
        this.chatName = chatName;
        history = new History();
        //从登录窗口获取昵称
        nickName = new LoadDatas().getNickName(LoginFrame.userField.getText());
        send  = new Send(socket);
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
        setTitle(nickName + "与" + chatName + "聊天");
        //聊天信息框不可编辑
        displayTextPanel = new JTextPane();
        displayTextPanel.setEditable(false);
        //聊天内容显示文本面板
        JScrollPane displayPanel = new JScrollPane(displayTextPanel);
        //存入打开聊天对象的聊天窗口，聊天对象不可覆盖，聊天窗口可覆盖
        TextPaneMap.put(chatName,displayTextPanel);
        //每个聊天对象有自己的输入环境(消息缓冲区)
        messageToFrame.put(chatName,sb);

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
            String msg = inputTextPanel.getText();
            inputTextPanel.setText("");
            String message = nickName + "  " + sf.format(new Date()) + "\n" + "    " + msg;  //消息格式
            //获取聊天窗口的key(聊天对象)
            Set<String> set = TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatName)){
                    messageToFrame.get(key).append(message).append("\n");
                    TextPaneMap.get(key).setText(messageToFrame.get(key).toString());
                }
            }
            //存储聊天记录
            //存储别人发来的历史记录
            history.setHitory(message,chatName);

            //发送你要聊天的对象
            //发送消息出去
            sendMsg(chatName+ ":" + message);
        });

        //表情按钮初始化
        JButton emjioButton = new JButton("表情");
        emjioButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        emjioButton.setFocusable(false);

        //查看历史记录
        JButton historyButtuon = new JButton("历史记录");
        historyButtuon.setFont(new Font("微软雅黑",Font.PLAIN,18));
        historyButtuon.addActionListener(e -> {
            Set<String> set = History.historyMap.keySet();
            for (String key : set) {
                if (key.equals(chatName)) {
                    displayTextPanel.setText("");
                    displayTextPanel.setText(History.historyMap.get(chatName).toString());
                }
            }
        });

        //聊天窗口底部Panel
        JPanel SouthPanel = new JPanel();
        SouthPanel.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.add(sendButton);
        toolBar.add(emjioButton);
        toolBar.addSeparator(new Dimension(300,20));
        toolBar.add(historyButtuon);
        //工具栏位于底部Panel的顶部
        SouthPanel.add(toolBar,BorderLayout.NORTH);
        SouthPanel.add(inputPanel,BorderLayout.CENTER);
        SouthPanel.setOpaque(false);

        label = new JLabel(/*new ImageIcon("src/ChatClient/Image/聊天背景1.png")*/);
        label.setLayout(new BorderLayout());
        label.setOpaque(false);
        label.add(displayPanel,BorderLayout.CENTER);
        label.add(SouthPanel,BorderLayout.SOUTH);
        add(label,BorderLayout.CENTER);
        setIconImage(new ImageIcon("src/ChatClient/Image/3.png").getImage());
        setVisible(true);
    }

    public void sendMsg(String message){
        send.sendMsg(message);
    }


}
