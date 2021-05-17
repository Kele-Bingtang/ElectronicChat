package ChatClient.Client;

import ChatClient.Swing.Frame.GroupFrame;
import ChatClient.cons.EnMsgType;
import ChatClient.Swing.Frame.ChatFrame;
import ChatClient.Swing.Frame.TipMessageFrame;
import ChatClient.controller.Handle;
import Utils.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.Set;

/**
 * 发送自己的消息到服务器
 * 发送别人的消息到自己的客户端
 */
public class Send{
    //通信
    Socket socket;
    //发送消息媒介
    DataOutputStream dos = null;
    //判断多线程是否运行
    boolean isRunning;

    public Send(Socket socket){
        this.socket = socket;
        try {
            isRunning = true;
            dos = new DataOutputStream(socket.getOutputStream());
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
            //写消息
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
            //EN_MSG_SINGLE_CHAT + " " + nickName + "  " + new Date() + "\n" + 聊天消息
            //获取空格的索引
            int index1 = message.indexOf(" ");
            //先把前面的EN_MSG_SINGLE_CHAT去掉
            message = message.substring(index1 + 1);
            //message为 nickName + "  " + new Date() + "\n" + 聊天消息
            //获取空格索引
            int index2 = message.indexOf(" ");
            String chatName = message.substring(0,index2);
            //获取聊天窗口的聊天对象(遍历判断)
            Set<String> set = ChatFrame.TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatName)){
                    //如果窗口没有打开
                    if(!Handle.isOpenChat){
                        //产生一个自定义窗口
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
            //截取空格索引
            int index1 = message.indexOf(" ");
            //去点EN_MSG_SYSTEM
            message = message.substring(index1 + 1);
            //获取相应字符串的索引
            int index2 = message.indexOf("迎");
            int index3 = message.indexOf("来");
            int index4 = message.indexOf("群");
            //获取昵称
            String nickName = message.substring(index2 + 1,index3);
            //获取群聊对象
            String chatGroupName = message.substring(index3 + 2,index4);
            //获取聊天窗口的群聊对象(遍历判断)
            Set<String> set = GroupFrame.showUserNamePaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    //存储消息
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    //显示消息
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    //显示群成员
                    GroupFrame.showUserNamePaneMap.get(key).setText(GroupFrame.showUserNamePaneMap.get(key).getText() + nickName + "\n");
                    break;
                }
            }
        }
        //群聊
        else if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_GROUP_CHAT.toString())){
            //EN_MSG_GROUP_CHAT + " " + message + ":" + chatGroupName
            //获取空格索引
            int index1 = message.indexOf(" ");
            //获取冒号索引
            int index2 = message.lastIndexOf(":");
            //获取群聊名称
            String chatGroupName = message.substring(index2 + 1);
            //获取真正的消息
            message = message.substring(index1 + 1,index2);
            //获取聊天窗口的群聊对象(遍历)
            Set<String> set = GroupFrame.TextPaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    //获得聊天消息
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    //将发来的消息显示在群聊对象的聊天窗口
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    break;
                }
            }
        }
        //群内的 @ 私聊
        else if(!message.equals("") && message.startsWith(EnMsgType.EN_MSG_GROUP_SINGLE_CHAT.toString())){
            //EN_MSG_GROUP_SINGLE_CHAT  + " " + nickNameAndDate + "   悄悄对您说：\n    " + realMessage + ":" + groupName
            //获取空格索引
            int index1 = message.indexOf(" ");
            //获取最后一个冒号索引
            int index2 = message.lastIndexOf(":");
            //获取群聊名称
            String chatGroupName = message.substring(index2 + 1);
            //截取私聊的消息
            message = message.substring(index1 + 1,index2);
            //获取聊天窗口的群聊对象(遍历)
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
            //获取空格索引
            int index1 = message.indexOf(" ");
            //截取："系统消息：" + nickName + "(" + userid + ")离开了" + chatGroupName + "群"
            message = message.substring(index1 + 1);
            //获取对应的索引
            int index2 = message.indexOf("：");
            int index3 = message.indexOf("离");
            int index4 = message.indexOf("群");
            //获取昵称
            String nickName = message.substring(index2 + 1,index3);
            //获取群聊名称
            String chatGroupName = message.substring(index3 + 3,index4);
            //获取聊天窗口的群聊对象(遍历)
            Set<String> set = GroupFrame.showUserNamePaneMap.keySet();
            for (String key: set) {
                if(key.equals(chatGroupName)){
                    //存储消息
                    GroupFrame.messageToFrame.get(key).append(message).append("\n");
                    //将发来的消息显示在群聊对象的聊天窗口
                    GroupFrame.TextPaneMap.get(key).setText(GroupFrame.messageToFrame.get(key).toString());
                    //获取昵称首位字符串的索引
                    int dex1 = GroupFrame.showUserNamePaneMap.get(key).getText().indexOf(nickName);
                    //索引加上该长度，变成昵称尾部字符串的索引
                    int dex2 = dex1 + nickName.length();
                    StringBuilder stringBuilder = new StringBuilder();
                    //存储界面以及有的消息
                    stringBuilder.append(GroupFrame.showUserNamePaneMap.get(key).getText());
                    //+1是把换行符去掉
                    stringBuilder.delete(dex1,dex2 + 1);
                    //将发来的消息显示在群聊对象的聊天窗口
                    GroupFrame.showUserNamePaneMap.get(key).setText(stringBuilder.toString());
                    break;
                }
            }
        }
        //获取与私聊对象历史聊天记录
        else if(message.startsWith(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString())){
            //EN_MSG_GET_SINGLE_HISTORY + " " + Server.singleHistory.get(nameKeyMap).toString() + ":" + chatName
            //获取空格索引
            int index1 = message.indexOf(" ");
            //获取最后一个冒号的索引
            int index2 = message.lastIndexOf(":");
            //获取群聊对象
            String chatName = message.substring(index2 + 1);
            //获取存储在服务器的历史记录
            String history = message.substring(index1 + 1,index2);
            //"null"自定义固定格式，为空
            if(!history.equals("null")){
                //将历史记录显示在自己的聊天窗口
                ChatFrame.TextPaneMap.get(chatName).setText(history);
            }
        }
        //获取群的历史聊天记录
        else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString())){
            //EN_MSG_GET_GROUP_HISTORY + " " + groupHistoryMap.toString() + ":" + chatGroupName
            //获取空格索引
            int index1 = message.indexOf(" ");
            //获取最后一个冒号的索引
            int index2 = message.lastIndexOf(":");
            //获取聊天对象
            String chatGroupName = message.substring(index2 + 1);
            //获取存储在服务器的历史记录
            String history = message.substring(index1 + 1,index2);
            if(!history.equals("null")){
                //将历史记录显示在自己的聊天窗口
                GroupFrame.TextPaneMap.get(chatGroupName).setText(history);
            }
        }
    }


    /**
     * 关闭资源
     */
    public void release(){
        this.isRunning = false;
        IOUtils.close(dos);
    }

}
