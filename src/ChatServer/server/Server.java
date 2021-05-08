package ChatServer.server;

import Utils.SxUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务端
 */
public class Server {
    public static List<Channel> all = new ArrayList<>();
    public static Map<String,List<String>> useridMap = new HashMap<>();
    public static Map<String,List<String>> groupMap = new HashMap<>();
    public static List<String> groupNameList = new ArrayList<>();

    public static void main(String[] args) {
        new Server().startServer();
    }
    public void startServer(){
        ServerSocket serverSocket = null;
        System.out.println("----服务端----");
        try {
            serverSocket = new ServerSocket(8888);
            while(true){
                Socket client = serverSocket.accept();
                Channel channel = new Channel(client);
                all.add(channel);
                new Thread(channel).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建端口失败");
            SxUtils.close(serverSocket);
        }
    }


}
