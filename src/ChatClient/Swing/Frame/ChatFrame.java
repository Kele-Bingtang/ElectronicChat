package ChatClient.Swing.Frame;

import ChatClient.Client.Send;
import ChatClient.cons.EnMsgType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 聊天窗口
 */
public class ChatFrame extends JFrame{
    //背景
    JLabel label;
    //显示消息的窗口，进行Map包装，一个聊天对象对应一个窗口(displayTextPanel)
    public JTextPane displayTextPanel;
    //暂时存放消息，防止覆盖
    StringBuilder sb = new StringBuilder();
    //时间的格式
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //通信
    Socket socket;
    //聊天对象昵称
    String chatName;
    //客户端昵称
    String nickName;
    //当前打开的聊天窗口所对应的好友在好友列表中的序号
    int listIndex;
    //一个聊天对象对应一个聊天窗口(实现多窗口聊天)
    public static Map<String ,JTextPane> TextPaneMap = new HashMap<>();
    //发送消息到聊天窗口，一个聊天对象对应一个消息缓冲区(StringBuilder)
    //与jTextPaneMap对应，key相同，根据key将消息放到指定聊天窗口
    public static Map<String,StringBuilder> messageToFrame = new HashMap<>();

    //大小
    int width = 600;
    int height = 600;
    //发送端实例
    Send send;

    /**
     * 构造函数
     * @param socket 通信
     * @param nickName 昵称
     * @param chatName 聊天对象
     * @param listIndex 聊天对象在好友列表的索引
     */
    public ChatFrame(Socket socket,String nickName,String chatName,int listIndex){
        this.socket = socket;
        this.chatName = chatName;
        //从登录窗口获取昵称
        this.nickName = nickName;
        this.listIndex = listIndex;
        send  = new Send(socket);
        init();
    }

    /**
     * 初始化聊天窗口
     */
    public void init(){
        setLayout(new BorderLayout());
        //大小
        setSize(width, height);
        //位置
        setLocation(650,200);
        //设置窗口不能调节大小
        setResizable(false);
        //设置聊天窗口总是前置
        // setAlwaysOnTop(true);
        //标题字体样式和居中
        String title = nickName + "与" + chatName + "聊天";
        setFont(new Font("微软雅黑",Font.PLAIN,18));
        Font f = getFont();
        //获得一个新的 FontMetrics用于找出的高度和宽度信息有关指定对象 Font在和特定字符字形的 Font
        FontMetrics fm = getFontMetrics(f);
        int x = fm.stringWidth(title);
        int y = fm.stringWidth(" ");
        int z = getWidth()/2 - (x/2);
        int w = z/y;
        String pad = "";
        // \s为空白字符,把pad转为w个长度的空白字符
        pad = String.format("%"+w+"s", pad);
        setTitle(pad + title);
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
        displayTextPanel.setFont(new Font("微软雅黑",Font.PLAIN,18));

        JTextPane inputTextPanel = new JTextPane();
        //输入聊天文本面板
        JScrollPane inputPanel = new JScrollPane(inputTextPanel);
        inputPanel.setOpaque(false);
        inputPanel.getViewport().setOpaque(false);
        inputPanel.setPreferredSize(new Dimension(100,100));
        inputTextPanel.setFont(new Font("微软雅黑",Font.PLAIN,18));
        inputTextPanel.setOpaque(false);

        //发送按钮初始化
        //按钮
        JButton sendButton = new JButton("发送");
        sendButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        //刚开始没有焦点
        sendButton.setFocusable(false);
        sendButton.setOpaque(false);
        sendButton.setBackground(Color.GRAY);

        sendButton.addActionListener(e -> {
            //从输入框获取消息
            String msg = inputTextPanel.getText();
            //清空输入框
            inputTextPanel.setText("");
            //指定消息格式
            String message = nickName + "  " + sf.format(new Date()) + "\n" + "    " + msg;  //消息格式
            //获取聊天窗口的key(聊天对象)
            Set<String> set = TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatName)){
                    messageToFrame.get(key).append(message).append("\n");
                    TextPaneMap.get(key).setText(messageToFrame.get(key).toString());
                }
            }
            //发送消息出去
            sendMsg(EnMsgType.EN_MSG_SINGLE_CHAT.toString() + " " + message + ":" + chatName);
        });

        //表情按钮初始化
        JButton emjioButton = new JButton("表情");
        emjioButton.setFont(new Font("微软雅黑",Font.PLAIN,18));
        emjioButton.setFocusable(false);
        emjioButton.setOpaque(false);
        emjioButton.setBackground(Color.GRAY);
        emjioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //打开emoji窗口
                new EmojiFrame(getX(),getY() + 115,inputTextPanel);
            }
        });


        //查看历史记录
        JButton historyButtuon = new JButton("历史记录");
        historyButtuon.setFont(new Font("微软雅黑",Font.PLAIN,18));
        historyButtuon.setOpaque(false);
        historyButtuon.setBackground(Color.GRAY);
        historyButtuon.addActionListener(e -> {
            //发送给服务器
            new Send(socket).sendMsg(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString() + " " + nickName + ":" + chatName);
        });

        //聊天窗口底部Panel
        JPanel SouthPanel = new JPanel();
        SouthPanel.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setOpaque(false);
        toolBar.setFloatable(false);
        toolBar.addSeparator(new Dimension(20,20));
        toolBar.add(sendButton);
        toolBar.addSeparator(new Dimension(20,20));
        toolBar.add(emjioButton);
        toolBar.addSeparator(new Dimension(width - 240,20));
        toolBar.add(historyButtuon);
        //工具栏位于底部Panel的顶部
        SouthPanel.add(toolBar,BorderLayout.NORTH);
        SouthPanel.add(inputPanel,BorderLayout.CENTER);
        SouthPanel.setOpaque(false);

        label = new JLabel(new ImageIcon("src/ChatClient/Image/singleChat.png"));
        label.setSize(width,height);
        label.setLayout(new BorderLayout());
        label.setOpaque(false);
        label.add(displayPanel,BorderLayout.CENTER);
        label.add(SouthPanel,BorderLayout.SOUTH);
        add(label,BorderLayout.CENTER);
        setIconImage(new ImageIcon("src/ChatClient/Image/8Icon.png").getImage());
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Send(socket).sendMsg(EnMsgType.EN_MSG_SINGLE_CLOSE.toString() + " " + listIndex);
            }
        });
    }

    /**
     * 发送给发送端，进行发送服务器
     * @param message 消息
     */
    public void sendMsg(String message){
        send.sendMsg(message);
    }

}
