package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;
import ChatClient.cons.GroupMap;
import ChatClient.controller.Handle;
import ChatServer.server.History;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 聊天窗口
 */
public class GroupFrame extends JFrame{
    //背景
    JLabel label;
    //显示消息的窗口，进行Map包装，一个聊天对象对应一个窗口(displayTextPanel)
    public JTextPane displayTextPanel,showMemberTextPanel;

    //暂时存放消息，防止覆盖
    StringBuilder sb = new StringBuilder();
    //时间的格式
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //通信
    Socket socket;
    //聊天对象昵称
    String chatGroupName;
    //客户端昵称
    String nickName;
    //用户id
    String userid;

    String[] groupMembers;
    //历史记录，一个聊天对象对应一个聊天记录
    //public static Map<String,StringBuilder> history = new HashMap<>();
    //一个聊天群对应一个聊天窗口(实现多窗口聊天)
    public static Map<String ,JTextPane> TextPaneMap = new HashMap<>();
    //发送消息到聊天窗口，一个聊天对象对应一个消息缓冲区(StringBuilder)
    //与jTextPaneMap对应，key相同，根据key将消息放到指定聊天窗口
    public static Map<String,StringBuilder> messageToFrame = new HashMap<>();

    //显示群成员的列表
    public static Map<String, JTextPane> showUserNamePaneMap = new HashMap<>();
    //历史记录类
    History history;

    Send send;
    public GroupFrame(Socket socket, String userid, String nickName, String chatGroupName,String[] groupMembers){
        this.socket = socket;
        this.userid = userid;
        this.chatGroupName = chatGroupName;
        history = new History();
        //从登录窗口获取昵称
        this.nickName = nickName;
        this.groupMembers = groupMembers;
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
        setTitle(nickName + "与" + chatGroupName + "聊天");
        //聊天信息框不可编辑
        displayTextPanel = new JTextPane();
        displayTextPanel.setEditable(false);
        //聊天内容显示文本面板
        JScrollPane displayPanel = new JScrollPane(displayTextPanel);
        //存入打开聊天对象的聊天窗口，聊天对象不可覆盖，聊天窗口可覆盖
        TextPaneMap.put(chatGroupName,displayTextPanel);
        //每个聊天对象有自己的输入环境(消息缓冲区)
        messageToFrame.put(chatGroupName,sb);

        //显示群成员
        showMemberTextPanel = new JTextPane();
        showMemberTextPanel.setEditable(false);
        JScrollPane showMemberPanel = new JScrollPane(showMemberTextPanel);
        showUserNamePaneMap.put(chatGroupName,showMemberTextPanel);
        showMemberPanel.getViewport().setOpaque(false);
        showMemberPanel.setOpaque(false);
        showMemberTextPanel.setOpaque(false);
        showMemberTextPanel.setFont(new Font("微软雅黑",Font.PLAIN,15));
        showMemberTextPanel.setPreferredSize(new Dimension(150,300));
        //获取聊天窗口的key(成员信息)
        Set<String> set = showUserNamePaneMap.keySet();
        for (String key: set) {
            if(key.equals(chatGroupName)){
                StringBuilder members = new StringBuilder();
                members.append("群成员").append("\n");
                if(null != groupMembers){
                    for (String groupMember : groupMembers) {
                        members.append(groupMember).append("\n");
                    }
                }
                showUserNamePaneMap.get(key).setText(members.toString());
            }
        }

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
            Set<String> keySet = TextPaneMap.keySet();
            for (String key: keySet) {
                if(key.equals(chatGroupName)){
                    messageToFrame.get(key).append(message).append("\n");
                    TextPaneMap.get(key).setText(messageToFrame.get(key).toString());
                }
            }
            //发送消息出去(告诉服务都这是群聊)
            sendMsg(EnMsgType.EN_MSG_GROUP_CHAT.toString() + " " + message + ":" + chatGroupName);
        });

        //表情按钮初始化
        JButton emjioButton = new JButton("表情");
        emjioButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        emjioButton.setFocusable(false);

        //查看历史记录
        JButton historyButtuon = new JButton("历史记录");
        historyButtuon.setFont(new Font("微软雅黑",Font.PLAIN,18));
        historyButtuon.addActionListener(e -> {
            //获取key值
            Set<String> keySet = History.historyMap.keySet();
            for (String key : keySet) {
                if (key.equals(chatGroupName)) {
                    //先清空，再放历史记录
                    displayTextPanel.setText("");
                    displayTextPanel.setText(History.historyMap.get(chatGroupName).toString());
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
        label.add(showMemberPanel,BorderLayout.EAST);
        add(label,BorderLayout.CENTER);
        setIconImage(new ImageIcon("src/ChatClient/Image/3.png").getImage());
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Send(socket).sendMsg(EnMsgType.EN_MSG_GROUP_EXIT.toString() + " " + userid + ":" + nickName + ":" + chatGroupName);
            }
        });

    }

    public void sendMsg(String message){
        send.sendMsg(message);
    }

}
