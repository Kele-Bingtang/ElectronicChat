package ChatServer.load;

public enum EnMsgType {
    EN_MSG_LOGIN, //用户登录消息
    EN_MSG_CHAT, //一对一聊天消息
    EN_MSG_OK, //处理消息成功
    EN_MSG_MODIFY_NICKNAME,//修改昵称
    EN_MSG_MODIFY_SIGNATURE, //修改个性签名
    EN_MSG_GETINFORMATION , //获取登录用户的信息
    EN_MSG_PASSWORD_ERROR,//旧密码错误
    EN_MSG_MODIFY_PASSWORD,//修改密码
    EN_MSG_GET_ID,     //获取用户的id
    EN_MSG_GET_FRIEND,   //获取昵称
    EN_MSG_ADD_FRIEND ,   //添加好友
    EN_MSG_DEL_FRIEND ,   //删除好友
    EN_MSG_ACTIVE_STATE,    //好友在线状态
}
