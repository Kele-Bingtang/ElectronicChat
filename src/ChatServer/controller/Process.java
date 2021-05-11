package ChatServer.controller;

import ChatServer.load.EnMsgType;
import ChatServer.load.GetDataFromDao;
import ChatServer.server.Channel;
import ChatServer.server.Server;
import ChatServer.bean.Information;

import java.util.*;

public class Process {
    static GetDataFromDao getDataFromDao = new GetDataFromDao();


    public String Processing(String message){

        if(message.startsWith(EnMsgType.EN_MSG_ADD_FRIEND.toString())){
            //添加好友
            return addFriends(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_DEL_FRIEND.toString())){
            //删除好友
            return deleteFriends(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString())){
            //修改昵称
            return modifyNickName(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString())){
            //修改签名
            return modifySignature(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_PASSWORD.toString())){
            //修改密码
            return modifyPassword(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_LOGIN.toString())){
            //返回登陆消息+登陆的昵称
            return verifyAndModifyMessage(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_OPEN_CHAT.toString())){
            //返回聊天对象名称
            return sendMsgToOtherOpen(message);
        }else if (message.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
            //发消息聊天
            return sendMsgToOther(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GROUP_CHAT.toString())){
            return storageGroupHitory(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_INFORMATION.toString())){
            //登陆成功后返回用户昵称和签名
            return getInformation(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_FRIEND.toString())){
            //返回登陆成功后好友列表
            return getFriend(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_EXIT.toString())){
            //退出客户端
            return getExit(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_INFROMATION.toString())){
            //返回群信息
        return getGroupName(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString())){
            //返回群成员信息
            return getGroupMember(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
            //返回退群的成员
            return removeGroupMember(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString())){
            //返回一对一聊天历史记录
            return getSingleHistory(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString())){
            //返回群的聊天历史记录
            return getGroupHistory(message);
        }
        return null;
    }

    /**
     * 添加好友
     * @param message 消息
     * @return 标识符
     */
    public String addFriends(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String friendid = message.substring(index2 + 1);
        boolean isExist = getDataFromDao.verifyFriendID(friendid);
        if(isExist){
            //双方互相加好友
            getDataFromDao.addFriend(userid,friendid);
            getDataFromDao.addFriend(friendid,userid);
            return EnMsgType.EN_MSG_ADD_FRIEND.toString();
        }else {
            return EnMsgType.EN_MSG_ADD_FRIEND_Fail.toString();
        }
    }

    /**
     * 删除好友
     * @param message 消息
     * @return 标识符
     */
    public String deleteFriends(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String friendid = message.substring(index2 + 1);
        getDataFromDao.deleteFriend(userid,friendid);
        getDataFromDao.deleteFriend(friendid,userid);

        return EnMsgType.EN_MSG_DEL_FRIEND.toString();
    }
    /**
     * 修改昵称
     * @param message 消息
     * @return 标识符
     */
    public String modifyNickName(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String newNickName = message.substring(index2 + 1);
        getDataFromDao.modifyNickName(userid,newNickName);

        return EnMsgType.EN_MSG_MODIFY_NICKNAME.toString();
    }

    /**
     * 修改签名
     * @param message 消息
     * @return 标识符
     */
    public String modifySignature(String message){
        //EN_MSG_MODIFY_SIGNATURE + " " + userid + ":" + newSignature
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String newSignature = message.substring(index2 + 1);
        getDataFromDao.modifySignature(userid,newSignature);

        return EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString();
    }

    /**
     * 修改密码
     * @param message 消息
     * @return 标识符
     */
    public String modifyPassword(String message){
        //EN_MSG_MODIFY_PASSWORD + " " + userid + ":" + oldPassword + ":" + newPassword
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String oldPassword = message.substring(index2 + 1,index3);
        String newPassword = message.substring(index3 + 1);

        boolean isRight = getDataFromDao.verifyUseridAndPassword(userid,oldPassword);
        if(isRight){
            getDataFromDao.modifyPassword(userid,newPassword);
            return EnMsgType.EN_MSG_PASSWORD_SUCC.toString();
        }else {
            return EnMsgType.EN_MSG_PASSWORD_ERROR.toString();
        }

    }

    /**
     * 获取聊天的对象名称
     * @param message 消息
     * @return 对象
     */
    public String sendMsgToOtherOpen(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.lastIndexOf(":");
        String nickName = message.substring(index1 + 1,index2);
        String chatName = message.substring(index2 + 1);

        return message;
    }

    /**
     * 和别人聊天，原封不动发给别人
     * 处理有用消息，存储到聊天历史记录
     * @param message 消息
     * @return 消息
     */
    public String sendMsgToOther(String message){
        //EN_MSG_SINGLE_CHAT + " " + message
        int index1 = message.indexOf(" ");
        //先把前面的EN_MSG_SINGLE_CHAT去掉
        String allChatMessage = message.substring(index1 + 1);
        //allChatMessage为 nickName + "  " + new Date() + "\n" + 聊天消息  + ":" + chatName
        int index2 = allChatMessage.indexOf(" ");
        int index3 = allChatMessage.lastIndexOf(":");

        String nickName = allChatMessage.substring(0,index2);
        String chatMessage = allChatMessage.substring(0,index3).trim();
        String chatName = allChatMessage.substring(index3 + 1);

        //存储聊天信息到自己的历史记录
        storageSingleHitory(chatMessage,nickName,chatName);
        //存储聊天信息到聊天对象的历史记录
        storageSingleHitory(chatMessage,chatName,nickName);

        return message;
    }

    /**
     * 存储聊天信息到历史记录
     * @param chatMessage 聊天消息
     * @param nickName 昵称
     * @param chatName 聊天对象
     */
    public void storageSingleHitory(String chatMessage,String nickName,String chatName){
        Map<String,String> doubleNameMap = new HashMap<>();
        StringBuilder chatMessageBuil = new StringBuilder();
        Set<Map<String,String>> mapKeySet = Server.singleHistory.keySet();
        for(Map<String,String> mapKey : mapKeySet){
            Set<String> keySet = mapKey.keySet();
            for(String nameKey : keySet){
                if(nameKey.equals(nickName)){
                    if(mapKey.get(nameKey).equals(chatName)){
                        chatMessageBuil.append(Server.singleHistory.get(mapKey).toString());
                        break;
                    }
                }
            }
        }

        chatMessageBuil.append(chatMessage).append("\n");
        doubleNameMap.put(nickName,chatName);
        Server.singleHistory.put(doubleNameMap,chatMessageBuil);
    }

    /**
     * 将消息加上昵称
     * @param message 消息
     * @return 消息+昵称
     */
    public String verifyAndModifyMessage(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String password = message.substring(index2 + 1);
        boolean isReal = getDataFromDao.verifyUseridAndPassword(userid,password);
        if(isReal){
            Information information = getDataFromDao.getinformationByUserid(userid);
            String nickName = information.getNickName();
            return EnMsgType.EN_MSG_LOGIN + " " + userid + ":" + nickName;
        }else {
            return EnMsgType.EN_MSG_LOGIN_Fail.toString();
        }

    }

    /**
     * 返回昵称和签名
     * @param message 消息
     * @return 昵称和个性签名
     */
    public String getInformation(String message){
        int index = message.indexOf(" ");
        String userid = message.substring(index + 1);
        Information information = getDataFromDao.getinformationByUserid(userid);
        String nickName = information.getNickName();
        String signature = information.getSignNature();
        getDataFromDao.modifyStatus(userid,"在线");
        return EnMsgType.EN_MSG_GET_INFORMATION.toString() + " " + nickName + ":" + signature;
    }

    /**
     * 获取好友列表
     * @param message 消息
     * @return 还有列表
     */
    public String getFriend(String message){
        StringBuilder friendBuil = new StringBuilder();
        int index = message.indexOf(" ");
        String userid = message.substring(index + 1);
        String [] friendNickNameAndStatus = getDataFromDao.getFriendNickNameAndStatus(userid);
        for(String s : friendNickNameAndStatus){
            friendBuil.append(s).append(":");
        }

        return EnMsgType.EN_MSG_GET_FRIEND + " " + friendBuil.toString();
    }

    /**
     * 离线
     * @param message 离线消息
     * @return 离线消息
     */
    public String getExit(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String nickName = message.substring(index2 + 1);
        getDataFromDao.modifyStatus(userid,"离线");
        System.out.println(nickName + "离线了");
        return EnMsgType.EN_MSG_EXIT.toString() + " " + nickName;
    }

    public String getGroupName(String message){
        StringBuilder groupBuil = new StringBuilder();

       String[] groupName = getDataFromDao.getGroupName();
       for(String s : groupName){
           groupBuil.append(s).append(":");
       }
       return EnMsgType.EN_MSG_GET_GROUP_INFROMATION + " " + groupBuil.toString();
    }


    /**
     * 把自己的Chanel与好友的Chanel一一对应
     * Map实现
     * @param userid 用户id
     */
    public void getChannelFriend(String userid){
        List<String> friendList = new ArrayList<>();
        //获取好友的全部信息
        List<Information> list = getDataFromDao.getFriends(userid);
        //存储好友的userid
        for(int i = 0;i < list.size();i++){
            friendList.add(list.get(i).getUid());
            Server.useridMap.put(userid,friendList);
        }

    }

    /**
     * 显示进入群的群成员，不用访问数据库
     * 消息已经发昵称过来
     * @param message 消息
     * @return
     */
    public String getGroupMember(String message){
        //EN_MSG_GET_GROUP_MENBER + " " + userid + ":" + nickName + ":" + chatGroupName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String nickName = message.substring(index2 + 1,index3);
        String chatGroupName = message.substring(index3 + 1);
        Server.groupNameList.add(nickName + "(" + userid + ")");
        Server.groupMap.put(chatGroupName,Server.groupNameList);
        return EnMsgType.EN_MSG_OPEN_GROUP.toString() + " " + "欢迎" + nickName + "(" + userid + ")来到" + chatGroupName + "群里";
    }

    /**
     * 一对一聊天获取聊天历史记录
     * @param message 消息
     * @return 聊天历史记录
     */
    public String getSingleHistory(String message){
        //EN_MSG_GET_HISTORY + " " + nickName + ":" + chatName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String nickName = message.substring(index1 + 1,index2);
        String chatName = message.substring(index2 + 1);

        if(null != Server.singleHistory){
            Set<Map<String,String>> doubleNameMapSet = Server.singleHistory.keySet();
            for(Map<String,String> nameKeyMap : doubleNameMapSet){
                if(null != nameKeyMap.get(nickName)){
                    Set<String> secondNameSet = nameKeyMap.keySet();
                    for(String secondName : secondNameSet){
                        if(nameKeyMap.get(secondName).equals(chatName)){
                            System.out.println("5：" + message);
                            if(null != Server.singleHistory.get(nameKeyMap)){
                                return EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString() + " " + Server.singleHistory.get(nameKeyMap).toString() + ":" + chatName;
                            }
                        }
                    }
                }
            }
        }
        return EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString() + " " + "null" + ":";
    }

    public String storageGroupHitory(String message){
        //EN_MSG_GROUP_CHAT + " " + message + ":" + chatGroupName
        int index1 = message.indexOf(" ");
        int index2 = message.lastIndexOf(":");
        String realMessage = message.substring(index1 + 1,index2);
        String chatGroupName = message.substring(index2 + 1);

        StringBuilder groupMessageBuil = new StringBuilder();
        Set<String> keySet = Server.groupHistory.keySet();
        for(String groupName : keySet){
            if(groupName.equals(chatGroupName)){
                groupMessageBuil.append(Server.groupHistory.get(chatGroupName).toString());
            }
        }
        groupMessageBuil.append(realMessage).append("\n");
        Server.groupHistory.put(chatGroupName,groupMessageBuil);
        return null;
    }

    public String getGroupHistory(String message){
        //EN_MSG_GET_GROUP_HISTORY + " " + chatGroupName
        int index1 = message.indexOf(" ");
        String chatGroupName = message.substring(index1 + 1);
        StringBuilder groupHistoryMap = new StringBuilder();

        if(null != Server.groupHistory){
            if(null != Server.groupHistory.get(chatGroupName)){
                groupHistoryMap.append(Server.groupHistory.get(chatGroupName).toString());
                return EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString() + " " + groupHistoryMap.toString() + ":" + chatGroupName;
            }
        }
        return  EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString() + " " + "null" + ":";

    }

    public String removeGroupMember(String message){
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String nickName = message.substring(index2 + 1,index3);
        String chatGroupName = message.substring(index3 + 1);

        Set<String> key = Server.groupMap.keySet();
        for (String keySet : key){
            if(keySet.equals(chatGroupName)){
                Server.groupMap.get(keySet).remove(nickName + "(" + userid + ")");
            }
        }
        return EnMsgType.EN_MSG_GROUP_EXIT + " " + nickName + "(" + userid + ")离开了" + chatGroupName + "群";
    }

}
