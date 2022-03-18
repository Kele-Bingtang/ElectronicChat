package ChatServer.server;

import ChatServer.controller.Process;
import ChatServer.load.EnMsgType;
import Utils.IOUtils;

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
    //通信
    Socket server;
    //输出流，读消息
    DataInputStream dis = null;
    //输入流，写消息
    DataOutputStream dos = null;
    //判断是否运行
    boolean isRunning;
    //用户id(一个用户id一个Chanel)
    String userid;
    //用户id的昵称
    String nickName;
    //处理端实例
    Process process;

    public Channel(Socket server){
        this.server = server;
        this.isRunning = true;
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
            //不打印e.printStackTrace，因为可以直接在服务端打印xxx离开聊天室而不报错
            this.release();
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
     */
    public void sendMsgToOther(String msg){
        //解析消息，获取聊天对象
        int index = msg.lastIndexOf(":");
        String chatName = msg.substring(index + 1);
        String realMessage = msg.substring(0,index);
        System.out.println(msg);
        System.out.println(chatName);
        //不发给自己
        for(Channel other : Server.all){
            if(other == this){
                continue;
            } else if(other.nickName.equals(chatName)){
                other.sendMsg(realMessage);
                break;
            }

        }
    }


    /**
     * 群聊：获取自己的消息，发给其他人
     * 私聊：约定数据格式：@xxx msg
     * @param msg 消息
     */
    public void sendMsgToAll(String msg,boolean isSys){
        int index1 = msg.indexOf("\n");
        //去掉前面的空格
        String realMessage = msg.substring(index1 + 1).trim();
        boolean isPrivte = realMessage.contains("@");
        //是否为私聊
        if(isPrivte){
            //把前面的日期和昵称截取
            String nickNameAndDate = msg.substring(0,index1);
            int index2 = realMessage.indexOf(" ");
            int index3 = realMessage.lastIndexOf(":");
            String targetName = realMessage.substring(1,index2);
            String groupName = realMessage.substring(index3 + 1);
            realMessage = realMessage.substring(index2 + 1,index3);
            for(Channel other : Server.all){
                //指定每个人
                if(other.nickName.equals(targetName)){
                    other.sendMsg(EnMsgType.EN_MSG_GROUP_SINGLE_CHAT.toString() + " " + nickNameAndDate + "   悄悄对您说：\n    " + realMessage + ":" + groupName);
                    break;
                }
            }
        }else {
            for(Channel other : Server.all){  //遍历判断是否是自己的客户端
                if(other == this){
                    continue;
                }
                if(!isSys){
                    //全部人，除自己
                    other.sendMsg(EnMsgType.EN_MSG_GROUP_CHAT.toString() + " " + msg);  //群聊消息
                }else {
                    if(msg.startsWith(EnMsgType.EN_MSG_OPEN_GROUP.toString())){
                        int index = msg.indexOf(" ");
                        String realMassage = msg.substring(index + 1);
                        //进入的系统消息
                        other.sendMsg(EnMsgType.EN_MSG_OPEN_GROUP.toString() + " " + "系统消息：" + realMassage);
                    }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
                        int index = msg.indexOf(" ");
                        String realMassage = msg.substring(index + 1);
                        //退出的系统消息
                        other.sendMsg(EnMsgType.EN_MSG_GROUP_EXIT.toString() + " " + "系统消息：" + realMassage);
                    }
                }
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

    /**
     * 重写run方法，多线程执行的核心
     */
    @Override
    public void run() {
        while(isRunning){
            //获取客户端发来的消息
            String msg = getMsg();
            //消息不为空
            if(!msg.equals("")){
                System.out.println("消息或操作：" + msg);
                if(msg.startsWith(EnMsgType.EN_MSG_LOGIN.toString())){
                    //处理登陆消息，如果密码正确则执行else语句，否则执行if语句
                    String handleMessage = process.Processing(msg);
                    if(handleMessage.equals(EnMsgType.EN_MSG_LOGIN_FAIL.toString())){
                        //告诉自己登陆失败，密码错误
                        sendMsgToMy(handleMessage);
                    }else {
                        //获取登陆的userid
                        int index1 = handleMessage.indexOf(" ");
                        int index2 = handleMessage.indexOf(":");
                        userid = handleMessage.substring(index1 + 1,index2);
                        nickName = handleMessage.substring(index2 + 1);
                        System.out.println(nickName + "(" + userid + ")" + " 客户端建立了连接");
                        //获取好友列表
                        process.getChannelFriend(userid);
                        //告诉自己密码正确，可以登录客户端
                        sendMsgToMy(EnMsgType.EN_MSG_LOGIN_SUCC.toString());
                        //告诉好友我已经上线了
                        sendMsgToFriend(handleMessage);
                    }
                }else if(msg.startsWith(EnMsgType.EN_MSG_OPEN_CHAT.toString())){
                    //打开私聊窗口
                    String realMessage = process.Processing(msg);
                    //告诉其他人我打开和你的聊天窗口
                    sendMsgToOther(realMessage);
                }else if(msg.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
                    //私聊
                    String realMassage = process.Processing(msg);
                    //发送消息给别人
                    sendMsgToOther(realMassage);
                }else if(msg.startsWith(EnMsgType.EN_MSG_SINGLE_CLOSE.toString())){
                    //关闭私聊窗口
                    //聊天窗口关闭，发送信息给主界面解除对当前聊天对象的窗口锁定
                    sendMsgToMy(msg);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString())){
                    //EN_MSG_GET_GROUP_MENBER + " " + userid + ":" + nickName + ":" + chatGroupName
                    int index3 = msg.lastIndexOf(":");
                    String chatGroupName = msg.substring(index3 + 1);
                    String message = process.Processing(msg);
                    //把登录进来的id和昵称发给所有人，告诉其他人我进群了
                    sendMsgToAll(message,true);
                    //存储群成员的信息，用:区分开(方便截取)
                    StringBuilder sb = new StringBuilder();
                    for(String s : Server.groupMap.get(chatGroupName)){
                        sb.append(s).append(":");
                    }
                    //发送给自己，在群里的其他人信息
                    sendMsgToMy(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString() + " " + sb.toString());
                }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_CHAT.toString())){
                    //EN_MSG_GROUP_CHAT + " " + message + ":" + chatGroupName
                    //存储群的聊天记录
                    process.Processing(msg);
                    //群聊标识
                    int index = msg.indexOf(" ");
                    String realMessage = msg.substring(index + 1);
                    sendMsgToAll(realMessage,false);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
                    String message = process.Processing(msg);
                    //告诉其他人我已经退群
                    sendMsgToAll(message,true);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GROUP_CLOSE.toString())){
                    //告诉自己退群，可再次进群，否则无法重复进群
                    sendMsgToMy(msg);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString())){
                    //私聊历史聊天记录
                    String singleHistory = process.Processing(msg);
                    sendMsgToMy(singleHistory);
                }else if(msg.startsWith(EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString())){
                    //群聊历史聊天记录
                    String groupHistory = process.Processing(msg);
                    sendMsgToMy(groupHistory);
                }else if(msg.startsWith(EnMsgType.EN_MSG_EXIT.toString())){
                    //退出客户端
                    String handleMessage = process.Processing(msg);
                    //发给好友，我已经下线
                    sendMsgToFriend(handleMessage);
                }else if(msg.startsWith("EN_MSG")){
                    //处理响应按钮 发送的消息
                    String result = process.Processing(msg);
                    if(null != result){
                        sendMsgToMy(result);
                    }
                }

            }
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        isRunning = false;
        IOUtils.close(dis,dos,server);
    }
}
