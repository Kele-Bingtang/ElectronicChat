package ChatClient.Chat;


import Swing.Frame.LoginFrame;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        new Client().startClient();
    }

    public void startClient(){
        Socket socket = getConnect();
        new LoginFrame(socket);
        new Thread(new Receive(socket)).start();
    }

    /**
     * 与8888端口进行连接
     * @return 连接媒介
     */
    public static Socket getConnect(){
        Socket socket = null;
        try {
            socket = new Socket("localhost",8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }
}
