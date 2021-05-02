package ChatServer;

import Utils.SxUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Channel implements Runnable{
    Socket server;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    boolean isRunning;
    String nickName;
    String chatName;
    public Channel(Socket server){
        this.server = server;
        this.isRunning = true;
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
     * @return 消息
     */
    public String getMsg(){
        String msg = "";
        try {
            msg = dis.readUTF();
            if(null == nickName){
                nickName = msg;
                System.out.println(nickName + " 登录了客户端");
                msg = "";
            }else if(!msg.startsWith(nickName)){
                chatName = msg;
                msg = "";
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
     * 群聊：获取自己的消息，发给其他人
     * 私聊：约定数据格式：@xxx：msg
     * @param msg 消息
     */
    public void sendOthers(String msg,boolean isSys){

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

    @Override
    public void run() {
        while(isRunning){
            String msg = getMsg();
            if(!msg.equals("")){
                //sendMsg(msg);
                System.out.println(msg);
                sendOthers(msg,false);
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
