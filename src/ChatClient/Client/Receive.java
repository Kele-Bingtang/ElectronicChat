package ChatClient.Client;


import ChatClient.controller.Handle;
import Utils.IOUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 接收端
 */
public class Receive implements Runnable{
    //通信
    Socket socket;
    //接收消息
    DataInputStream dis = null;
    //判断是否继续多线程
    boolean isRunning;
    //发送端对象
    Send send;
    //处理端对象
    Handle handle;

    /**
     * 构造函数
     * @param socket 通信媒介
     */
    public Receive(Socket socket){
        this.socket = socket;
        send = new Send(socket);
        handle = new Handle();
        this.isRunning = true;
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            this.release();
        }
    }

    /**
     * 获取消息
     * @return 消息
     */
    public String getMsg(){
        try {
            //返回读出的值
            return dis.readUTF();
        } catch (IOException e) {
            //当服务器停止运行，会报错，不打印e.printStackTrace()，执行提示
            System.out.println("服务终端停止运行，请刷新或者等待终端重启，再次输出消息会停止运行您的客户端");
            this.release();
        }
        return "";
    }

    /**
     * 写run方法，多线程执行的核心
     */
    @Override
    public void run() {
        String msg = "";
        while(isRunning){
            //获取消息
            msg = getMsg();
            //判断消息是否有EN_MSG开头
            if(msg.startsWith("EN_MSG")){
                //传入处理端处理
                boolean isHandle = handle.handling(msg);
                if(!isHandle){
                    //发送给聊天界面
                    send.sendMsgToChat(msg);
                }
            }else {
                send.sendMsgToChat(msg);
            }

        }
    }

    /**
     * 释放资源
     */
    public void release(){
        isRunning = false;
        IOUtils.close(dis,socket);
    }
}
