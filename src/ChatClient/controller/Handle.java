package ChatClient.controller;

import ChatClient.Swing.Frame.MainFrame;
import ChatClient.cons.EnMsgType;
import ChatClient.Swing.Frame.TipMessageFrame;

import java.util.concurrent.SynchronousQueue;

/**
 * 客户端的处理端
 */
public class Handle {
    //阻塞线程，take时候 不先offer 会阻塞
    public static SynchronousQueue<Object> queue = new SynchronousQueue<>();
    //登录的昵称
    public static String nickName;
    //登录的签名
    public static String signature;
    //登录的头像ID
    public static int headIconID;
    //登录后自己的好友
    public static String[] friends;
    //登录后自己的群
    public static String[] groups;
    //登录后自己的群成员
    public static String[] groupMembers;
    //是否打开一个私聊的聊天窗口(避免重复提示打开与您的聊天窗口)
    public static boolean isOpenChat = false;


    public boolean handling(String message) {
        if (message.equals(EnMsgType.EN_MSG_LOGIN_FAIL.toString())) {
            //EN_MSG_LOGIN_Fail
            //登录失败，返回offer，取消阻塞
            System.out.println("用户名或密码错误，或者用户名不存在，请注册");
            new TipMessageFrame().SuccOrFail("错误", "用户名或密码错误，或者用户名不存在!");
            queue.offer(100);
            return true;
        } else if (message.equals(EnMsgType.EN_MSG_LOGIN_SUCC.toString())) {
            //登录成功，返回offer，取消阻塞
            //EN_MSG_LOGIN_SUCC
            queue.offer(200);
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_LOGIN.toString())) {
            //登录通知(通知好友我已经上线)
            //EN_MSG_LOGIN + " " + userid + ":" + nickName
            //获取空格索引
            int index1 = message.indexOf(" ");
            //获取冒号索引
            int index2 = message.indexOf(":");
            //截取好友的userid
            String userid = message.substring(index1 + 1, index2);
            //截取好友的昵称
            String nickName = message.substring(index2 + 1);
            System.out.println(nickName + "已经上线啦");
            new TipMessageFrame().sendMessageTip("上线通知", nickName + "已经上线啦", false);
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_REGISTER_SUCC.toString())) {
            //注册成功
            //EN_MSG_REGISTER_SUCC
            System.out.println("注册成功");
            //成功弹窗在登陆界面出现后(RegisterFrame里)出现，防止无法置顶
            //返回offer，取消阻塞
            queue.offer(150);
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_REGISTER_FAIL.toString())) {
            //注册失败
            //EN_MSG_REGISTER_FAIL
            System.out.println("注册失败");
            new TipMessageFrame().SuccOrFail("失败", "用户名已经存在，重新输入！");
            //返回offer，取消阻塞
            queue.offer(250);
            return true;
        }  else if (message.startsWith(EnMsgType.EN_MSG_GET_INFORMATION.toString())) {
            //EN_MSG_GET_INFORMATION + " " + nickName + ":" + signature + ":" + headIconiD
            //登录时获取自己的信息
            int index1 = message.indexOf(" ");
            int index2 = message.indexOf(":");
            int index3 = message.lastIndexOf(":");
            nickName = message.substring(index1 + 1, index2);
            signature = message.substring(index2 + 1,index3);
            headIconID= Integer.parseInt(message.substring(index3 + 1));
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_GET_FRIEND.toString())) {
            //EN_MSG_GET_FRIEND + " " + realMessage
            //登录时获取自己的好友列表
            int index1 = message.indexOf(" ");
            String realMessage = message.substring(index1 + 1);
            //realMessage为好友列表，每个好友以:隔开
            friends = realMessage.split(":");
            //返回offer，取消阻塞
            queue.offer(300);
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_GET_GROUP_INFROMATION.toString())) {
            //EN_MSG_GET_GROUP_INFROMATION + " " + realMessage
            //登录时候获取自己群列表
            int index = message.indexOf(" ");
            String realMessage = message.substring(index + 1);
            //realMessage为群组列表，每个群组以:隔开
            groups = realMessage.split(":");
            //返回offer，取消阻塞
            queue.offer(400);
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_OPEN_CHAT.toString())) {
            //EN_MSG_OPEN_CHAT + " " + nickName + ":" + chatName
            //打开聊天窗口
            int index1 = message.indexOf(" ");
            int index2 = message.indexOf(":");
            String chatName = message.substring(index1 + 1);
            //打开聊天窗口
            isOpenChat = true;
            System.out.println(chatName + "打开和你的聊天窗口");
            new TipMessageFrame().sendMessageTip("提示", chatName + "打开和你的聊天窗口", false);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_SINGLE_CLOSE.toString())){
            //EN_MSG_SINGLE_CLOSE + " " + listIndex
            //关闭聊天窗口
            isOpenChat = false;
            int index = message.indexOf(" ");
            String closeIndex = message.substring(index + 1);
            //关闭私聊界面(可重新打开)
            MainFrame.friendClose(Integer.parseInt(closeIndex));
            return true;
        }else if (message.equals(EnMsgType.EN_MSG_PASSWORD_ERROR.toString())) {
            //EN_MSG_PASSWORD_ERROR
            //旧密码错误
            new TipMessageFrame().SuccOrFail("密码错误", "您输入的密码错误，请输入正确的密码");
            return true;
        } else if (message.equals(ChatClient.cons.EnMsgType.EN_MSG_PASSWORD_SUCC.toString())) {
            //EN_MSG_PASSWORD_SUCC
            //修改密码成功
            new TipMessageFrame().SuccOrFail("修改密码成功", "您已经成功修改密码，请记住您的密码");
            return true;
        }else if (message.equals(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString())) {
            //EN_MSG_MODIFY_NICKNAME
            //修改昵称
            System.out.println("修改昵称成功");
            new TipMessageFrame().sendMessageTip("提示", "修改昵称成功", true);
            return true;
        } else if (message.equals(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString())) {
            //EN_MSG_MODIFY_SIGNATURE
            //修改签名
            System.out.println("修改签名成功");
            new TipMessageFrame().sendMessageTip("提示", "修改签名成功", true);
            return true;
        }  else if (message.equals(EnMsgType.EN_MSG_ADD_FRIEND.toString())) {
            //EN_MSG_ADD_FRIEND
            //添加好友
            System.out.println("添加好友成功");
            new TipMessageFrame().sendMessageTip("提示", "添加好友成功", true);
            return true;
        } else if (message.equals(EnMsgType.EN_MSG_ADD_FRIEND_FAIL.toString())) {
            //EN_MSG_ADD_FRIEND_Fail
            //添加好友失败
            System.out.println("添加好友失败");
            new TipMessageFrame().sendMessageTip("提示", "添加好友失败", true);
            return true;
        } else if (message.equals(EnMsgType.EN_MSG_DEL_FRIEND.toString())) {
            //EN_MSG_DEL_FRIEND
            //删除好友
            System.out.println("删除好友成功");
            new TipMessageFrame().sendMessageTip("提示", "删除好友成功", true);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_CREATE_GROUP_SUCC.toString())){
            //EN_MSG_CREATE_GROUP_SUCC
            //创建群聊成功
            System.out.println("创建群聊成功");
            new TipMessageFrame().sendMessageTip("成功","创建群聊成功",true);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_CREATE_GROUP_FAIL.toString())){
            //EN_MSG_CREATE_GROUP_FAIL
            //创建群聊失败
            System.out.println("创建群聊失败");
            new TipMessageFrame().sendMessageTip("失败","群聊id已经存在，重新输入！",true);
            return true;
        } else if (message.startsWith(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString())) {
            //EN_MSG_GET_GROUP_MENBER + " " + message
            int index = message.indexOf(" ");
            message = message.substring(index + 1);
            //realMessage为群成员列表，每个群成员以:隔开
            groupMembers = message.split(":");
            //返回offer，取消阻塞
            queue.offer(500);
            return true;
        }else if(message.startsWith(EnMsgType.EN_MSG_GROUP_CLOSE.toString())){
            //EN_MSG_GROUP_CLOSE + " " + groupID
            //关闭群界面
            int index = message.indexOf(" ");
            String groupID = message.substring(index + 1);
            //关闭群聊界面(可重新打开)
            MainFrame.groupClose(Integer.parseInt(groupID));
            return true;
        }  else if (message.startsWith(EnMsgType.EN_MSG_EXIT.toString())) {
            //EN_MSG_EXIT + " " + friendName
            //退出客户端
            int index1 = message.indexOf(" ");
            String friendName = message.substring(index1 + 1);
            System.out.println(friendName + "下线了");
            new TipMessageFrame().sendMessageTip("下线通知", friendName + "下线啦", false);
            return true;
        }else if (message.startsWith(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString())) {
            //获取私聊对象的历史聊天记录
            return false;
        } else if (message.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())) {
            //聊天对象的消息
            return false;
        } else if (message.startsWith(EnMsgType.EN_MSG_OPEN_GROUP.toString())) {
            //打开群组
            return false;
        } else {
            return false;
        }
    }

}
