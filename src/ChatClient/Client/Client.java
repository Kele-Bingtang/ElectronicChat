package ChatClient.Client;


import ChatClient.Swing.Frame.LoginFrame;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端
 */
public class Client {

    public static void main(String[] args) {
        new Client().startClient();
    }

    public void startClient(){
        Socket socket = getConnect();
        //创建登录界面
        new LoginFrame(socket);
        //创建接收端
        new Thread(new Receive(socket)).start();
    }

    /**
     * 与8888端口进行连接
     * @return 连接媒介
     */
    public static Socket getConnect(){
        Socket socket = null;
        try {
            //连接8888端口
            socket = new Socket("localhost",9087);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("服务器没有启动，无法进行登录操作");
        }
        return socket;
    }
}
