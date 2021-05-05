package ChatClient.controller;

import ChatServer.load.EnMsgType;
import Swing.Frame.TipMessageFrame;

import java.util.concurrent.SynchronousQueue;


public class Handle {
    //阻塞线程，take时候 不offer 会阻塞
    public static SynchronousQueue<Object> queue = new SynchronousQueue<>();
    public static String nickName;
    public static String signature;
    public static String[] friends;

    public boolean handling(String message){
        if(message.equals(EnMsgType.EN_MSG_LOGIN_Fail.toString())){
            System.out.println("用户名或密码错误，或者用户名不存在，请注册");
            queue.offer(100);
            return true;
        }else if(message.equals(EnMsgType.EN_MSG_LOGIN_SUCC.toString())){
            queue.offer(200);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_LOGIN.toString())){
            int index1 = message.indexOf(" ");
            int index2 = message.indexOf(":");
            String userid = message.substring(index1 + 1,index2);
            String nickName = message.substring(index2 + 1);
            System.out.println(nickName + "已经上线啦");
            new TipMessageFrame().sendMessageTip("上线通知",nickName + "已经上线啦",false);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
            //一对一聊天
            int index1 = message.indexOf(" ");
            int index2 = message.indexOf(":");
            String chatName = message.substring(index1 + 1,index2);
            System.out.println(chatName + "打开和你的聊天窗口");
            return true;
        } else if(message.equals(EnMsgType.EN_MSG_ADD_FRIEND.toString())){
            //添加好友
            System.out.println("添加好友成功");
            new TipMessageFrame().sendMessageTip("提示","添加好友成功",true);
            return true;
        } else if(message.equals(EnMsgType.EN_MSG_ADD_FRIEND_Fail.toString())){
            //添加好友失败
            System.out.println("添加好友失败");
            new TipMessageFrame().sendMessageTip("提示","添加好友失败",true);
            return true;
        } else if(message.equals(EnMsgType.EN_MSG_DEL_FRIEND.toString())){
            //删除好友
            System.out.println("删除好友成功");
            new TipMessageFrame().sendMessageTip("提示","删除好友成功",true);
            return true;
        } else if(message.equals(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString())){
            //修改昵称
            System.out.println("修改昵称成功");
            new TipMessageFrame().sendMessageTip("提示","修改昵称成功",true);
            return true;
        }else if(message.equals(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString())){
            //修改签名
            System.out.println("修改签名成功");
            new TipMessageFrame().sendMessageTip("提示","修改签名成功",true);
            return true;
        }else if(message.equals(EnMsgType.EN_MSG_PASSWORD_ERROR.toString())){
            //旧密码错误
            new TipMessageFrame().modifypasswordSuccOrFail("密码错误","您输入的密码错误，请输入正确的密码");
            return true;
        } else if (message.equals(ChatClient.cons.EnMsgType.EN_MSG_PASSWORD_SUCC.toString())){
            //修改密码成功
            new TipMessageFrame().modifypasswordSuccOrFail("修改密码成功","您已经成功修改密码，请记住您的密码");
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_INFORMATION.toString())){
            int index1 = message.indexOf(" ");
            int index2 = message.indexOf(":");
            nickName = message.substring(index1 + 1,index2);
            signature = message.substring(index2 + 1);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_FRIEND.toString())){
            int index1 = message.indexOf(" ");
            String realMessage = message.substring(index1 + 1);
            friends = realMessage.split(":");
            queue.offer(300);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_EXIT.toString())){
            int index = message.indexOf(" ");
            String friendName = message.substring(index + 1);
            System.out.println(friendName + "下线了");
            new TipMessageFrame().sendMessageTip("上线通知",friendName + "下线啦",false);

            return true;
        }else {
            return false;
        }
    }



}
