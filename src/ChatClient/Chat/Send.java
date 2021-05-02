package ChatClient.Chat;

import Swing.ChatFrame;
import Utils.SxUtils;

import java.io.*;
import java.net.Socket;

public class Send{
    Socket socket;
    DataOutputStream dos = null;
    boolean isRunning;
    BufferedReader br = null;

    public Send(Socket socket){
        this.socket = socket;
        try {
            isRunning = true;
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    /**
     * 聊天窗口发送消息给服务端
     * @param msg
     */
    public void sendMsg(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    /**
     * 发送消息到聊天窗口
     * @param message
     */
    public void sendMsgToChat(String message){
        if(!message.equals("")){
            System.out.println(message);

            int index = message.indexOf(" ");
            String nickName = message.substring(1,index);//解析发送的名称
            //如果没有打开窗口，则自动打开窗口
            if(!ChatFrame.isCreate){
                new ChatFrame(socket, nickName).ceateNew(message);
            }else {
                ChatFrame.sb.append(message).append("\n");
                ChatFrame.displayTextPanel.setText(ChatFrame.sb.toString());
            }
        }
    }

    /**
     * 关闭资源
     */
    public void release(){
        this.isRunning = false;
        SxUtils.close(dos);
    }

}
