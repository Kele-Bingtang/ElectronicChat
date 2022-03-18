package ChatServer.server;

import Utils.IOUtils;

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
    //存储每一个客户端
    public static List<Channel> all = new ArrayList<>();
    //key：用户id，value：好友id(用List是多个好友)
    public static Map<String,List<String>> useridMap = new HashMap<>();
    ////key：群名，value：进群的用户(用List是多个用户)
    public static Map<String,List<String>> groupMap = new HashMap<>();
    //存储多个用户
    public static List<String> groupNameList = new ArrayList<>();
    //key的key：用户id, key的value：聊天对象，value：聊天记录
    public static Map<Map<String,String>,StringBuilder> singleHistory = new HashMap<>();
    //key：群id，value：聊天记录
    public static Map<String,StringBuilder> groupHistory = new HashMap<>();


    public static void main(String[] args) {
        new Server().startServer();
    }

    /**
     * 初始化服务器
     */
    public void startServer(){
        ServerSocket serverSocket = null;
        System.out.println("----服务端----");
        try {
            //连接8888端口
            serverSocket = new ServerSocket(9087);
            while(true){
                //连接客户端
                Socket client = serverSocket.accept();
                Channel channel = new Channel(client);
                all.add(channel);
                new Thread(channel).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建端口失败");
            IOUtils.close(serverSocket);
        }
    }

}
