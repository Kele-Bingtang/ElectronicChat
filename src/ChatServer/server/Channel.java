package ChatServer.server;

import ChatServer.controller.Process;
import ChatServer.load.EnMsgType;
import Utils.SxUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
    String nickName;
    String chatName;
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
            //登录的时候先将昵称发来获取
            if(null == nickName){
                nickName = msg;
                System.out.println(nickName + " 登录了客户端");
                msg = "";
            }else {
                int index = msg.indexOf(":");
                chatName = msg.substring(0,index);
            }
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
            e.printStackTrace();
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
            }
            else if(other.nickName.equals(chatName)){
                System.out.println(nickName + "的目标：" + chatName);
                other.sendMsg(msg);

                break;
            }
        }
    }

    public void sendMsgToMy(String msg) {
        for (Channel other : Server.all) {
            if (other == this) {
                sendMsg(msg);
            }
        }
    }

    @Override
    public void run() {
        while(isRunning){
            String msg = getMsg();
            if(!msg.equals("")){
                //是按钮响应消息，则发送给自己
                if(msg.startsWith("EN_MSG")){
                    //处理响应按钮 发送的消息
                    String result = process.Processing(msg);
                    if(null != result){
                        sendMsgToMy(result);
                    }else {
                        sendMsgToOthers(msg,false);
                    }
                } else {
                    System.out.println("消息：" + msg);
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
