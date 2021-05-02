package ChatClient.Chat;

import Swing.ChatFrame;
import Swing.MainFrame;
import Utils.SxUtils;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Set;

public class Send{
    Socket socket;
    DataOutputStream dos = null;
    boolean isRunning;
    BufferedReader br = null;

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
            int index = message.indexOf(":");
            message = message.substring(index+1);
            System.out.println(message);
            //如果没有打开窗口，则自动打开窗口
            //未完善
            if(!ChatFrame.isCreate){
                int select = JOptionPane.showConfirmDialog(null,nickName + "发来了消息，你是否进行查看，如果不，请去历史记录查看","确定",JOptionPane.OK_CANCEL_OPTION);
                if(select == 0){
                    new ChatFrame(socket, nickName).ceateNew(message,nickName);
                }
            }else {
                //获取聊天窗口的聊天对象
                Set<String> set = ChatFrame.TextPaneMap.keySet();
                for (String key: set) {
                    if(key.equals(nickName)){
                        //获得聊天消息
                        ChatFrame.messageToFrame.get(key).append(message).append("\n");
                        //将发来的消息显示在聊天对象的聊天窗口
                        ChatFrame.TextPaneMap.get(key).setText(ChatFrame.messageToFrame.get(key).toString());
                    }
                }
            }
            //存储别人发来的历史记录
            setOtherSendHistory(message,nickName);
            System.out.println("存储了");
        }
    }

    /**
     * 存储别人发来的历史记录
     * @param message 消息
     * @param nickName 发送者
     */
    public void setOtherSendHistory(String message,String nickName){
        ChatFrame.buff.delete(0,ChatFrame.buff.length());
        Set<String> keySet = ChatFrame.history.keySet();
        for (String key : keySet) {
            //别人发过给你，则存储别人的数据，比如可乐发给冰糖，则冰糖存储可乐发来的消息，存放与  可乐(nickName) 聊天的记录中
            //ChatFrame.history存储的是和  别人(可乐) 的聊天记录
            if (key.equals(nickName)) {
                ChatFrame.buff.append(ChatFrame.history.get(key).toString());
            }
        }
        ChatFrame.buff.append(message).append("\n");
        ChatFrame.history.put(nickName,ChatFrame.buff);
    }

    /**
     * 关闭资源
     */
    public void release(){
        this.isRunning = false;
        SxUtils.close(dos);
    }

}
