package ChatClient.controller;

import ChatServer.load.EnMsgType;
import Swing.Frame.TipMessageFrame;

public class Handle {

    public boolean handling(String message){
        if(message.startsWith(EnMsgType.EN_MSG_ADD_FRIEND.toString())){
            //添加好友
            System.out.println("添加好友成功");
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_DEL_FRIEND.toString())){
            //删除好友

            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString())){
            //修改昵称
            System.out.println("修改昵称成功");
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString())){
            //修改签名
            System.out.println("修改签名成功");
            return true;
        }else if(message.equals(EnMsgType.EN_MSG_PASSWORD_ERROR.toString())){
            //旧密码错误
            new TipMessageFrame().modifypasswordFail();
            return true;
        } else if (message.equals(ChatClient.cons.EnMsgType.EN_MSG_OK.toString())){
            //修改密码成功
            new TipMessageFrame().modifypasswordSucc();
            return true;
        }else {
            return false;
        }

    }

}
