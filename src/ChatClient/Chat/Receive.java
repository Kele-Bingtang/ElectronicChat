package ChatClient.Chat;


import Utils.SxUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 接收端
 */
public class Receive implements Runnable{
    Socket socket;
    DataInputStream dis = null;
    boolean isRunning;  //判断是否继续多线程
    Send send;

    /**
     * 构造函数
     * @param socket 通信媒介
     */
    public Receive(Socket socket){
        this.socket = socket;
        send = new Send(socket);
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
            return dis.readUTF();  //返回读出的值
        } catch (IOException e) {
            //当服务器停止运行，会报错，不打印e.printStackTrace()，执行提示
            System.out.println("服务终端停止运行，请刷新或者等待终端重启，再次输出消息会停止运行您的客户端");
            this.release();
        }
        return "";
    }

    @Override
    public void run() {
        String msg = "";
        while(isRunning){
            msg = getMsg();  //获取消息
            send.sendMsgToChat(msg);
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        isRunning = false;
        SxUtils.close(dis,socket);
    }
}
