package ChatServer.controller;

import ChatServer.load.EnMsgType;
import ChatServer.load.GetDataFromDao;
import ChatServer.server.Server;
import ChatServer.bean.Information;

import java.util.*;

/**
 * 处理端
 * 处理客户端发来的消息
 */
public class Process {
    static GetDataFromDao getDataFromDao = new GetDataFromDao();

    /**
     * 处理进程
     * @param message 消息
     * @return 指定命令
     */
    public String Processing(String message){

        if(message.startsWith(EnMsgType.EN_MSG_LOGIN.toString())){
            //返回登陆消息+登陆的昵称
            return verifyAndModifyMessage(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_INFORMATION.toString())){
            //登陆成功后返回用户昵称和签名
            return getInformation(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_FRIEND.toString())){
            //返回登陆成功后好友列表
            return getFriend(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_INFROMATION.toString())){
            //返回回登陆成功群信息
            return getGroupName(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_REGISTER.toString())){
            //注册
            return register(message);
        } else if(message.startsWith(EnMsgType.EN_MSG_ADD_FRIEND.toString())){
            //添加好友
            return addFriends(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_DEL_FRIEND.toString())){
            //删除好友
            return deleteFriends(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_PASSWORD.toString())){
            //修改密码
            return modifyPassword(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString())){
            //修改昵称
            return modifyNickName(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString())){
            //修改签名
            return modifySignature(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_ICON.toString())){
            //修改头像
            return modifyIconID(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_OPEN_CHAT.toString())){
            //返回聊天对象名称
            return sendMsgToOtherOpen(message);
        }else if (message.startsWith(EnMsgType.EN_MSG_SINGLE_CHAT.toString())){
            //发消息聊天
            return sendMsgToOtherAndStorageHistory(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_CREATE_GROUP.toString())){
            //创建群聊
           return createGroup(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_MENBER.toString())){
            //返回群成员信息
            return getGroupMember(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GROUP_CHAT.toString())){
            //返回群聊信息
            return storageGroupHistory(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GROUP_EXIT.toString())){
            //返回退群的成员
            return removeGroupMember(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString())){
            //返回一对一聊天历史记录
            return getSingleHistory(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString())){
            //返回群的聊天历史记录
            return getGroupHistory(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_EXIT.toString())){
            //退出客户端
            return getExit(message);
        }
        return null;
    }


    /**
     * 将消息加上昵称
     * @param message 消息
     * @return 消息+昵称
     */
    public String verifyAndModifyMessage(String message){
        //EN_MSG_LOGIN + " " + userid + ":" + password
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String password = message.substring(index2 + 1);
        //从数据库判断用户名密码是否正确
        boolean isReal = getDataFromDao.verifyUseridAndPassword(userid,password);
        if(isReal){
            //正确则从数据库获取自己的信息
            Information information = getDataFromDao.getImformationByUserid(userid);
            String nickName = information.getNickName();
            //告诉好友我已经上线
            return EnMsgType.EN_MSG_LOGIN + " " + userid + ":" + nickName;
        }else {
            //不正确则返回失败
            return EnMsgType.EN_MSG_LOGIN_FAIL.toString();
        }

    }

    /**
     * 返回昵称和签名和头像id(登录时，刷新时)
     * @param message 消息
     * @return 昵称和个性签名
     */
    public String getInformation(String message){
        //EN_MSG_GET_INFORMATION + " " + userid
        int index = message.indexOf(" ");
        String userid = message.substring(index + 1);
        //根据用户名获取自己的信息(昵称和签名和头像id)
        Information information = getDataFromDao.getImformationByUserid(userid);
        String nickName = information.getNickName();
        String signature = information.getSignNature();
        int iconID = information.getIconID();
        //从数据库修改状态，为在线
        getDataFromDao.modifyStatus(userid,"在线");
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_GET_INFORMATION.toString() + " " + nickName + ":" + signature + ":" + iconID;
    }

    /**
     * 获取好友列表(登录时，刷新时)
     * @param message 消息
     * @return 还有列表
     */
    public String getFriend(String message){
        //EN_MSG_GET_FRIEND + " " + userid
        StringBuilder friendBuil = new StringBuilder();
        int index = message.indexOf(" ");
        String userid = message.substring(index + 1);
        //从数据库根据用户名获取好友列表
        String [] friendNickNameAndStatus = getDataFromDao.getFriendNickNameAndStatus(userid);
        for(String s : friendNickNameAndStatus){
            //每个好友以:隔开
            friendBuil.append(s).append(":");
        }
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_GET_FRIEND + " " + friendBuil.toString();
    }

    /**
     * 获取群聊信息(登录时，刷新时)
     * @param message 消息
     * @return 群信息
     */
    public String getGroupName(String message){
        //EN_MSG_GET_GROUP_INFROMATION + " " + username
        StringBuilder groupBuil = new StringBuilder();
        //从数据库获取群聊名称
        String[] groupName = getDataFromDao.getGroupName();
        for(String s : groupName){
            //群聊名称以:隔开
            groupBuil.append(s).append(":");
        }
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_GET_GROUP_INFROMATION + " " + groupBuil.toString();
    }

    /**
     * 注册用户名和密码
     * @param message 消息
     * @return 成功
     */
    public String register(String message){
        //EN_MSG_REGISTER + " " + userid + ":" + password
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String password = message.substring(index2 + 1);
        //判断注册的用户名是否存在
        boolean isRight = getDataFromDao.verifyUserid(userid);
        //不存在
        if(!isRight){
            //在数据库用户表插入注册信息
            getDataFromDao.register(userid,password);
            //在数据库信息表插入注册信息
            getDataFromDao.registerUserid(userid);
            return EnMsgType.EN_MSG_REGISTER_SUCC.toString();
        }else {
            //存在
            return EnMsgType.EN_MSG_REGISTER_FAIL.toString();
        }

    }

    /**
     * 添加好友
     * @param message 消息
     * @return true or false
     */
    public String addFriends(String message){
        //EN_MSG_ADD_FRIEND + " " + userid + ":" + friendid
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String friendid = message.substring(index2 + 1);
        //从数据库判断是否已经存有好友
        boolean isExist = getDataFromDao.verifyFriendID(friendid);
        if(isExist){
            //在数据库双方互相加好友
            getDataFromDao.addFriend(userid,friendid);
            getDataFromDao.addFriend(friendid,userid);
            return EnMsgType.EN_MSG_ADD_FRIEND.toString();
        }else {
            //没有失败，假设必定成功
            return EnMsgType.EN_MSG_ADD_FRIEND_FAIL.toString();
        }
    }

    /**
     * 删除好友
     * @param message 消息
     * @return 标识符
     */
    public String deleteFriends(String message){
        //EN_MSG_DEL_FRIEND + " " + userid + ":" + friednid
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String friendid = message.substring(index2 + 1);
        //在数据库彼此删除好友
        getDataFromDao.deleteFriend(userid,friendid);
        getDataFromDao.deleteFriend(friendid,userid);
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_DEL_FRIEND.toString();
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
        //从数据库判断输入的旧密码是否是自己的原来密码
        boolean isRight = getDataFromDao.verifyUseridAndPassword(userid,oldPassword);
        if(isRight){
            //正确，数据库修改密码
            getDataFromDao.modifyPassword(userid,newPassword);
            return EnMsgType.EN_MSG_PASSWORD_SUCC.toString();
        }else {
            //正确，提示错误
            return EnMsgType.EN_MSG_PASSWORD_ERROR.toString();
        }

    }

    /**
     * 修改昵称
     * @param message 消息
     * @return 标识符
     */
    public String modifyNickName(String message){
        //EN_MSG_MODIFY_NICKNAME + " " + userid + ":" + newNickName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String newNickName = message.substring(index2 + 1);
        //数据库修改昵称
        getDataFromDao.modifyNickName(userid,newNickName);
        //没有失败，假设必定成功
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
        //数据库修改签名
        getDataFromDao.modifySignature(userid,newSignature);
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString();
    }


    /**
     * 修改头像
     * @param message 消息
     * @return 标识符
     */
    public String modifyIconID(String message){
        //EN_MSG_MODIFY_ICON + " " + userid + ":" + newIconID
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String newIconID = message.substring(index2 + 1);
        //数据库修改头像ID
        getDataFromDao.modifyIconID(userid,Integer.parseInt(newIconID));
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_MODIFY_ICON.toString();
    }


    /**
     * 返回消息
     * @param message 消息
     * @return 对象
     */
    public String sendMsgToOtherOpen(String message){
        //没有失败，假设必定成功
        return message;
    }

    /**
     * 和别人聊天，原封不动发给别人
     * 处理有用消息，存储到聊天历史记录
     * @param message 消息
     * @return 消息
     */
    public String sendMsgToOtherAndStorageHistory(String message){
        //EN_MSG_SINGLE_CHAT + " " + nickName + "  " + new Date() + "\n" + 聊天消息  + ":" + chatName
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
        //没有失败，假设必定成功
        return message;
    }

    /**
     * 存储聊天信息到历史记录
     * @param chatMessage 聊天消息
     * @param nickName 昵称
     * @param chatName 聊天对象
     */
    public void storageSingleHitory(String chatMessage,String nickName,String chatName){
        //nickName + "  " + new Date() + "\n" + 聊天消息
        Map<String,String> doubleNameMap = new HashMap<>();
        StringBuilder chatMessageBuil = new StringBuilder();
        //判断自己的聊天记录是否存在服务器
        //存在则先获取记录，防止覆盖(先遍历找出自己的名字)
        Set<Map<String,String>> mapKeySet = Server.singleHistory.keySet();
        for(Map<String,String> mapKey : mapKeySet){
            Set<String> keySet = mapKey.keySet();
            for(String nameKey : keySet){
                if(nameKey.equals(nickName)){
                    if(mapKey.get(nameKey).equals(chatName)){
                        //存在先获取记录，防止覆盖
                        chatMessageBuil.append(Server.singleHistory.get(mapKey).toString());
                        break;
                    }
                }
            }
        }
        //添加新的聊天记录
        chatMessageBuil.append(chatMessage).append("\n");
        doubleNameMap.put(nickName,chatName);
        //存放服务器
        Server.singleHistory.put(doubleNameMap,chatMessageBuil);
    }

    /**
     * 创建群聊
     * @param message 消息
     * @return 成功消息
     */
    public String createGroup(String message){
        // //EN_MSG_CREATE_GROUP.toString() + " " + userid + ":" + groupID + ":" + groupName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String groupid = message.substring(index2 + 1,index3);
        String groupName = message.substring(index3 + 1);

        //判断是否存在群id
        boolean isRight = getDataFromDao.verifyGroup(groupid);

        if(!isRight){
            //不存在则创建成功
            getDataFromDao.createrGroup(userid,groupid,groupName);
            return EnMsgType.EN_MSG_CREATE_GROUP_SUCC.toString();
        }else {
            //存在则创建失败
            return EnMsgType.EN_MSG_CREATE_GROUP_FAIL.toString();
        }
    }

    /**
     * 显示进入群的群成员，不用访问数据库
     * 消息已经发昵称过来
     * @param message 消息
     * @return 系统消息
     */
    public String getGroupMember(String message){
        //EN_MSG_GET_GROUP_MENBER + " " + userid + ":" + nickName + ":" + chatGroupName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String nickName = message.substring(index2 + 1,index3);
        String chatGroupName = message.substring(index3 + 1);
        //群成员存入服务器
        Server.groupNameList.add(nickName + "(" + userid + ")");
        Server.groupMap.put(chatGroupName,Server.groupNameList);
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_OPEN_GROUP.toString() + " " + "欢迎" + nickName + "(" + userid + ")来到" + chatGroupName + "群里";
    }

    /**
     * 存储群聊的聊天历史记录
     * @param message 消息
     * @return null
     */
    public String storageGroupHistory(String message){
        //EN_MSG_GROUP_CHAT + " " + message + ":" + chatGroupName
        int index1 = message.indexOf(" ");
        int index2 = message.lastIndexOf(":");
        String realMessage = message.substring(index1 + 1,index2);
        String chatGroupName = message.substring(index2 + 1);
        StringBuilder groupMessageBuil = new StringBuilder();
        //判断自己的聊天记录是否存在服务器
        //存在则先获取记录，防止覆盖(先遍历找出群聊的名字)
        Set<String> keySet = Server.groupHistory.keySet();
        for(String groupName : keySet){
            if(groupName.equals(chatGroupName)){
                //存在则先获取记录，防止覆盖
                groupMessageBuil.append(Server.groupHistory.get(chatGroupName).toString());
            }
        }
        //添加新的聊天记录
        groupMessageBuil.append(realMessage).append("\n");
        //存放服务器
        Server.groupHistory.put(chatGroupName,groupMessageBuil);
        return null;
    }

    /**
     * 群成员退出，成员列表删除
     * @param message 消息
     * @return 系统消息
     */
    public String removeGroupMember(String message){
        //EN_MSG_GROUP_EXIT + " " + userid + ":" + nickName + ":" + chatGroupName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String nickName = message.substring(index2 + 1,index3);
        String chatGroupName = message.substring(index3 + 1);
        //退群时，从服务器去掉自己的消息(先遍历找出自己的名字)
        Set<String> key = Server.groupMap.keySet();
        for (String keySet : key){
            if(keySet.equals(chatGroupName)){
                //去掉自己爹信息
                Server.groupMap.get(keySet).remove(nickName + "(" + userid + ")");
            }
        }
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_GROUP_EXIT + " " + nickName + "(" + userid + ")离开了" + chatGroupName + "群";
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
        //判断不为空
        if(null != Server.singleHistory){
            //遍历Map，获取自己的昵称，聊天对象的昵称，聊天消息
            Set<Map<String,String>> doubleNameMapSet = Server.singleHistory.keySet();
            for(Map<String,String> nameKeyMap : doubleNameMapSet){
                //自己与别人的聊天记录不为空
                if(null != nameKeyMap.get(nickName)){
                    Set<String> secondNameSet = nameKeyMap.keySet();
                    for(String secondName : secondNameSet){
                        //获取与聊天对象的聊天记录
                        if(nameKeyMap.get(secondName).equals(chatName)){
                            if(null != Server.singleHistory.get(nameKeyMap)){
                                return EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString() + " " + Server.singleHistory.get(nameKeyMap).toString() + ":" + chatName;
                            }
                        }
                    }
                }
            }
        }
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_GET_SINGLE_HISTORY.toString() + " " + "null" + ":";
    }


    /**
     * 获取群聊的聊天历史记录
     * @param message 消息
     * @return 聊天历史记录
     */
    public String getGroupHistory(String message){
        //EN_MSG_GET_GROUP_HISTORY + " " + chatGroupName
        int index1 = message.indexOf(" ");
        String chatGroupName = message.substring(index1 + 1);
        StringBuilder groupHistoryMap = new StringBuilder();
        //判断不为空
        if(null != Server.groupHistory){
            if(null != Server.groupHistory.get(chatGroupName)){
                //获取指定群聊昵称的聊天记录
                groupHistoryMap.append(Server.groupHistory.get(chatGroupName).toString());
                return EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString() + " " + groupHistoryMap.toString() + ":" + chatGroupName;
            }
        }
        //没有失败，假设必定成功
        return  EnMsgType.EN_MSG_GET_GROUP_HISTORY.toString() + " " + "null" + ":";

    }

    /**
     * 退出客户端，离线
     * @param message 离线消息
     * @return 离线消息
     */
    public String getExit(String message){
        //EN_MSG_EXIT + " " + userid + ":" + nickName
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String nickName = message.substring(index2 + 1);
        //在数据库修改自己的状态
        getDataFromDao.modifyStatus(userid,"离线");
        //没有失败，假设必定成功
        return EnMsgType.EN_MSG_EXIT.toString() + " " + nickName;
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
        for (Information information : list) {
            friendList.add(information.getUid());
            Server.useridMap.put(userid, friendList);
        }

    }



}
