package ChatServer.server;

import ChatServer.controller.Process;
import ChatServer.load.EnMsgType;
import Utils.SxUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
    public String userid;
    public String chatUserid;
    History history;
    Process process;

    String []friends;

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
     * @param isSys 识别是否是系统发的消息
     */
    public void sendMsgToOthers(String msg,boolean isSys){
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
                        String nickName = handleMessage.substring(index2 + 1);
                        System.out.println(nickName + "(" + userid + ")" + " 客户端建立了连接");
                        //获取好友列表
                        process.getChannelFriend(userid);
                        //告诉自己密码正确，可以登录客户端
                        sendMsgToMy(EnMsgType.EN_MSG_LOGIN_SUCC.toString());
                        //告诉好友我已经上线了
                        sendMsgToFriend(handleMessage);
                    }
                }else if(msg.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
                    //一对一聊天
                    //获取聊天对象的userid
                    chatUserid = process.Processing(msg);
                    sendMsgToOthers(msg,false);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GET_FRIEND.toString())){
                    //把所有好友发给登陆的客户端，点击刷新按钮的客户端
                    String friend = process.Processing(msg);
                    //发给自己，获得好友列表
                    sendMsgToMy(EnMsgType.EN_MSG_GET_FRIEND + " " + friend);
                }else if(msg.startsWith(EnMsgType.EN_MSG_EXIT.toString())){
                    String handleMessage = process.Processing(msg);
                    //发给好友，我已经下线
                    sendMsgToFriend(handleMessage);
                }else if(msg.startsWith("EN_MSG")){
                    //处理响应按钮 发送的消息
                    String result = process.Processing(msg);
                    if(null != result){
                        sendMsgToMy(result);
                    }else {
                        sendMsgToOthers(msg,false);
                    }
                } else {
                    sendMsgToOthers(msg,false);
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
