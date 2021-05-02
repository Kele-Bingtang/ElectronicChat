package ChatClient.Chat;

import ChatServer.History;
import Swing.ChatFrame;
import Utils.SxUtils;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Send{
    Socket socket;
    DataOutputStream dos = null;
    boolean isRunning;
    BufferedReader br = null;

    public static Map<String,StringBuilder> hit = new HashMap<>();

    public Send(Socket socket){
        this.socket = socket;
        try {
            isRunning = true;
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    /**
     * 聊天窗口发送消息给服务端
     * @param msg 消息
     */
    public void sendMsg(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    /**
     * 发送消息到聊天窗口
     * 格式：聊天对象:自己昵称 消息
     * @param message 消息
     */
    public void sendMsgToChat(String message){
        if(!message.equals("")){
            //格式：聊天对象:自己昵称 消息
            int index1 = message.indexOf(":");
            int index2 = message.indexOf(" ");
            //获取聊天对象
            String chatName = message.substring(0,index1);
            //获取发送人昵称
            //解析发送的名称
            String nickName = message.substring(index1+1,index2);
            //解析信息
            message = message.substring(index1 + 1);
            System.out.println(message);

            //弄一个界面，五秒自动关闭
            System.out.println(nickName + "发消息给你了");


            //获取聊天窗口的聊天对象
            Set<String> set = ChatFrame.TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(nickName)){
                    //获得聊天消息
                    ChatFrame.messageToFrame.get(key).append(message).append("\n");
                    //将发来的消息显示在聊天对象的聊天窗口
                    ChatFrame.TextPaneMap.get(key).setText(ChatFrame.messageToFrame.get(key).toString());
                    hit.put(nickName,ChatFrame.messageToFrame.get(key));
                    //存储发来的消息
                    setSendHistory(nickName);
                    break;
                }

            }
        }
    }

    /**
     * 存储聊天历史记录
     * @param nickName 发送者
     */
    public void setSendHistory(String nickName){
        History.buffs.delete(0,History.buffs.length());
        Set<String> keySet = History.historyMap.keySet();
        for (String key : keySet) {
            //别人发过给你，则存储别人的数据，比如可乐发给冰糖，则冰糖存储可乐发来的消息，存放与  可乐(nickName) 聊天的记录中
            //History.historyMap存储的是和  别人(可乐) 的聊天记录
            if (key.equals(nickName)) {
                History.buffs.append(hit.get(key).toString());
            }
        }
        History.historyMap.put(nickName,History.buffs);
    }

    /**
     * 关闭资源
     */
    public void release(){
        this.isRunning = false;
        SxUtils.close(dos);
    }

}
