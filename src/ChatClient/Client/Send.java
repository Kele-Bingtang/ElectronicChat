package ChatClient.Client;

import ChatClient.Swing.Frame.GroupFrame;
import ChatClient.Swing.Frame.MainFrame;
import ChatClient.cons.EnMsgType;
import ChatClient.Swing.Frame.ChatFrame;
import ChatClient.Swing.Frame.TipMessageFrame;
import ChatClient.controller.Handle;
import Utils.SxUtils;

import java.io.*;
import java.net.Socket;
import java.util.Set;

/**
 * 发送消息到另一个客户端
 */
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

        //指定好友聊天
        if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
            //EN_MSG_SINGLE_CHAT + " " + message
            int index1 = message.indexOf(" ");
            //先把前面的EN_MSG_SINGLE_CHAT去掉
            message = message.substring(index1 + 1);
            //message为 nickName + "  " + new Date() + "\n" + 聊天消息
            int index2 = message.indexOf(" ");
            String chatName = message.substring(0,index2);
            System.out.println(message);
            //获取聊天窗口的聊天对象
            Set<String> set = ChatFrame.TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatName)){
                    //弄一个界面，五秒自动关闭
                    if(!Handle.isOpenChat){
                        new TipMessageFrame().sendMessageTip("消息提醒",chatName + "发消息给你了",false);
                    }
                    //获得聊天消息
                    ChatFrame.messageToFrame.get(key).append(message).append("\n");
                    //将发来的消息显示在聊天对象的聊天窗口
                    ChatFrame.TextPaneMap.get(key).setText(ChatFrame.messageToFrame.get(key).toString());
                    break;
                }
            }
        }
        else if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_OPEN_GROUP.toString())){
            //EN_MSG_SYSTEM + " " + "系统消息：欢迎" + nickName + "(" + userid + ")来到" + chatGroupName + "群里"
            int index1 = message.indexOf(" ");
            message = message.substring(index1 + 1);
            int index2 = message.indexOf("迎");
            int index3 = message.indexOf("来");
            int index4 = message.indexOf("群");
            String nickName = message.substring(index2 + 1,index3);
            String chatGroupName = message.substring(index3 + 2,index4);
            Set<String> set = GroupFrame.showUserNamePaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    GroupFrame.showUserNamePaneMap.get(key).setText(GroupFrame.showUserNamePaneMap.get(key).getText() + nickName + "\n");
                    break;
                }
            }
        }
        //群聊
        else if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_GROUP_CHAT.toString())){
            //EN_MSG_GROUP_CHAT + " " + message + ":" + chatGroupName
            int index1 = message.indexOf(" ");
            int index2 = message.lastIndexOf(":");
            String chatGroupName = message.substring(index2 + 1);
            message = message.substring(index1 + 1,index2);
            Set<String> set = GroupFrame.TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    //获得聊天消息
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    //将发来的消息显示在聊天对象的聊天窗口
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    break;
                }
            }
        }else if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_GROUP_SINGLE_CHAT.toString())){
            int index1 = message.indexOf(" ");
            int index2 = message.lastIndexOf(":");
            String chatGroupName = message.substring(index2 + 1);
            message = message.substring(index1 + 1,index2);
            Set<String> set = GroupFrame.TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    //获得聊天消息
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    //将发来的消息显示在聊天对象的聊天窗口
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    break;
                }
            }
        }else if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
            //EN_MSG_SYSTEM + " " + "系统消息：" + nickName + "(" + userid + ")离开了" + chatGroupName + "群"
            int index1 = message.indexOf(" ");
            message = message.substring(index1 + 1);
            int index2 = message.indexOf("：");
            int index3 = message.indexOf("离");
            int index4 = message.indexOf("群");
            String nickName = message.substring(index2 + 1,index3);
            String chatGroupName = message.substring(index3 + 3,index4);
            Set<String> set = GroupFrame.showUserNamePaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    int dex1 = GroupFrame.showUserNamePaneMap.get(key).getText().indexOf(nickName);
                    int dex2 = dex1 + nickName.length();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(GroupFrame.showUserNamePaneMap.get(key).getText());
                    //+1是把换行符去掉
                    stringBuilder.delete(dex1,dex2 + 1);

                    GroupFrame.showUserNamePaneMap.get(key).setText(stringBuilder.toString());
                    break;
                }
            }
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString())){
            int index1 = message.indexOf(" ");
            int index2 = message.lastIndexOf(":");
            String chatName = message.substring(index2 + 1);
            String history = message.substring(index1 + 1,index2);
            if(!history.equals("null")){
                //将发来的消息显示在聊天对象的聊天窗口
                ChatFrame.TextPaneMap.get(chatName).setText(history);
            }
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString())){
            //EN_MSG_GET_GROUP_HISTORY + " " + groupHistoryMap.toString() + ":" + chatGroupName
            int index1 = message.indexOf(" ");
            int index2 = message.lastIndexOf(":");
            String chatGroupName = message.substring(index2 + 1);
            String history = message.substring(index1 + 1,index2);
            if(!history.equals("null")){
                GroupFrame.TextPaneMap.get(chatGroupName).setText(history);
            }
        }
    }


    /**
     * 关闭资源
     */
    public void release(){
        this.isRunning = false;
        SxUtils.close(dos);
    }

}
