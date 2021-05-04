package ChatServer.controller;

import ChatServer.load.EnMsgType;
import ChatServer.load.GetDataFromDao;

public class Process {
    static GetDataFromDao getDataFromDao = new GetDataFromDao();

    public static void main(String[] args) {
        String m = new Process().Processing("askele:kele123:kele1234567");
    }

    public String Processing(String message){

        if(message.startsWith(EnMsgType.EN_MSG_ADD_FRIEND.toString())){
            //添加好友
            return addFriends(message);

        }else if(message.startsWith(EnMsgType.EN_MSG_DEL_FRIEND.toString())){
            //删除好友

        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_NICKNAME.toString())){
            //修改昵称
            return modifyNickName(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_SIGNATURE.toString())){
            //修改签名
            return modifySignature(message);
        }else if(message.startsWith(EnMsgType.EN_MSG_MODIFY_PASSWORD.toString())){
            //修改密码
            return modifyPassword(message);
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
        }
        return EnMsgType.EN_MSG_ADD_FRIEND.toString();
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
        int index1 = message.indexOf(" ");
        int index2 = message.indexOf(":");
        int index3 = message.lastIndexOf(":");
        String userid = message.substring(index1 + 1,index2);
        String oldPassword = message.substring(index2 + 1,index3);
        String newPassword = message.substring(index3 + 1);

        boolean isRight = getDataFromDao.verifyPassword(userid,oldPassword);
        if(isRight){
            getDataFromDao.modifyPassword(userid,newPassword);
            return EnMsgType.EN_MSG_OK.toString();
        }else {
            return EnMsgType.EN_MSG_PASSWORD_ERROR.toString();
        }

    }

}
