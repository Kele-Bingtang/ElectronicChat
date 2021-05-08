package ChatServer.server;

import ChatServer.controller.Process;
import ChatServer.load.EnMsgType;
import Utils.SxUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

/**
 * 管道
 * 获取客户端发给另一个客户端的消息
 * 处理客户端发给另一个客户端的消息
 * 返回客户端发给另一个客户端的消息
 */
public class Channel implements Runnable{
    Socket server;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    boolean isRunning;
    String userid;
    String nickName;
    String chatUserid;
    History history;
    Process process;


    public Channel(Socket server){
        this.server = server;
        this.isRunning = true;
        history = new History();
        process = new Process();
        try {
            dis = new DataInputStream(server.getInputStream());
            dos = new DataOutputStream(server.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            this.release();
        }
    }

    /**
     * 获取客户端发来的信息，并发回去
     * 格式：聊天对象:自己昵称 消息
     * @return 消息
     */
    public String getMsg(){
        String msg = "";
        try {
            msg = dis.readUTF();

        } catch (IOException e) {  //无法消息会报错
            this.release();     //不打印e.printStackTrace，因为可以直接在服务端打印xxx离开聊天室而不报错
        }
        return msg;
    }
    /**
     * 发送消息给客户端
     * @param msg 消息
     */
    public void sendMsg(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            System.out.println("重新启动一个客户端");
            this.release();
        }
    }
    /**
     * @param msg 消息
     */
    public void sendMsgToOther(String msg){
        for(Channel other : Server.all){
            if(other == this){
                continue;
            } else if(other.userid.equals(chatUserid)){
                System.out.println("消息：" + msg);
                other.sendMsg(msg);
                break;
            }

        }
    }


    /**
     * 群聊：获取自己的消息，发给其他人
     * 私聊：约定数据格式：@xxx msg
     * @param msg 消息
     */
    public void sendMsgToAll(String msg,boolean isSys){
        int index1 = msg.indexOf("\n");
        //去掉前面的空格
        String realMessage = msg.substring(index1 + 1).trim();
        boolean isPrivte = realMessage.contains("@");
        if(isPrivte){  //是否为私聊
            //把前面的日期和昵称截取
            String nickNameAndDate = msg.substring(0,index1);
            int index2 = realMessage.indexOf(" ");
            int index3 = realMessage.lastIndexOf(":");
            String targetName = realMessage.substring(1,index2);
            String groupName = realMessage.substring(index3 + 1);
            realMessage = realMessage.substring(index2 + 1,index3);
            for(Channel other : Server.all){
                if(other.nickName.equals(targetName)){
                    other.sendMsg(EnMsgType.EN_MSG_GROUP_SINGLE_CHAT.toString() + " " + nickNameAndDate + "   悄悄对您说：\n    " + realMessage + ":" + groupName);
                    break;
                }
            }
        }else {
            for(Channel other : Server.all){  //遍历判断是否是自己的客户端
                if(other == this){
                    continue;
                }
                if(!isSys){
                    other.sendMsg(EnMsgType.EN_MSG_GROUP_CHAT.toString() + " " + msg);  //群聊消息
                }else {
                    if(msg.startsWith(EnMsgType.EN_MSG_OPEN_GROUP.toString())){
                        int index = msg.indexOf(" ");
                        String realMassage = msg.substring(index + 1);
                        other.sendMsg(EnMsgType.EN_MSG_OPEN_GROUP.toString() + " " + "系统消息：" + realMassage);  //系统消息
                    }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
                        int index = msg.indexOf(" ");
                        String realMassage = msg.substring(index + 1);
                        other.sendMsg(EnMsgType.EN_MSG_GROUP_EXIT.toString() + " " + "系统消息：" + realMassage);
                    }
                }
            }
        }
    }

    /**
     * 发送消息给自己
     * @param msg 消息
     */
    public void sendMsgToMy(String msg) {
        for (Channel other : Server.all) {
            if (other == this) {
                sendMsg(msg);
            }
        }
    }

    /**
     * 发送给自己的好友
     * 仅限登陆后的自己发给登陆前的好友：我已经上线
     * 自己无法发给 比自己登陆后的好友
     * @param message 消息 LOGIN
     */
    public void sendMsgToFriend(String message){
        if(null != Server.useridMap.get(this.userid)){
            for (Channel friendChanel : Server.all){
                //不能发给自己
                if(friendChanel == this){
                    continue;
                }
                for(String frienduserid : Server.useridMap.get(this.userid)){
                    if(frienduserid.equals(friendChanel.userid)){
                        friendChanel.sendMsg(message);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while(isRunning){
            String msg = getMsg();
            if(!msg.equals("")){
                if(msg.startsWith(EnMsgType.EN_MSG_LOGIN.toString())){
                    //处理登陆消息，如果密码正确则执行else语句，否则执行if语句
                    String handleMessage = process.Processing(msg);
                    if(handleMessage.equals(EnMsgType.EN_MSG_LOGIN_Fail.toString())){
                        //告诉自己登陆失败，密码错误
                        sendMsgToMy(handleMessage);
                    }else {
                        //获取登陆的userid
                        int index1 = handleMessage.indexOf(" ");
                        int index2 = handleMessage.indexOf(":");
                        userid = handleMessage.substring(index1 + 1,index2);
                        nickName = handleMessage.substring(index2 + 1);
                        System.out.println(nickName + "(" + userid + ")" + " 客户端建立了连接");
                        //获取好友列表
                        process.getChannelFriend(userid);
                        //告诉自己密码正确，可以登录客户端
                        sendMsgToMy(EnMsgType.EN_MSG_LOGIN_SUCC.toString());
                        //告诉好友我已经上线了
                        sendMsgToFriend(handleMessage);
                    }
                }else if(msg.startsWith(EnMsgType.EN_MSG_OPEN_CHAT.toString())){
                    //一对一聊天
                    //获取聊天对象的userid
                    chatUserid = process.Processing(msg);
                    //告诉其他人我打开和你的聊天窗口
                    sendMsgToOther(msg);
                }else if(msg.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
                    String realMassage = process.Processing(msg);
                    //发送消息给别人
                    sendMsgToOther(realMassage);
                }else if(msg.startsWith(EnMsgType.EN_MSG_EXIT.toString())){
                    String handleMessage = process.Processing(msg);
                    //发给好友，我已经下线
                    sendMsgToFriend(handleMessage);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString())){
                    int index3 = msg.lastIndexOf(":");
                    String chatGroupName = msg.substring(index3 + 1);
                    String message = process.Processing(msg);
                    //把登录进来的id和昵称发给所有人，告诉其他人我进群了
                    sendMsgToAll(message,true);
                    //发送给自己，在群里的其他人信息
                    StringBuilder sb = new StringBuilder();
                    for(String s : Server.groupMap.get(chatGroupName)){
                        sb.append(s).append(":");
                    }
                    sendMsgToMy(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString() + " " + sb.toString());
                }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
                    String message = process.Processing(msg);
                    //告诉其他人我已经退群
                    sendMsgToAll(message,true);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_CHAT.toString())){
                    //群聊标识
                    int index = msg.indexOf(" ");
                    String realMessage = msg.substring(index + 1);
                    sendMsgToAll(realMessage,false);
                }else if(msg.startsWith("EN_MSG")){
                    //处理响应按钮 发送的消息
                    String result = process.Processing(msg);
                    if(null != result){
                        sendMsgToMy(result);
                    }else {
                        sendMsgToOther(msg);
                    }
                } else {
                    sendMsgToOther(msg);
                }

            }
        }
    }



    /**
     * 释放资源
     */
    public void release(){
        isRunning = false;
        SxUtils.close(dis,dos,server);
    }
}
