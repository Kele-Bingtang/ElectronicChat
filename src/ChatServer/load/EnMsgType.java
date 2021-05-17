package ChatServer.load;

/**
 * 服务器处理端识别的自定义命令
 */
public enum EnMsgType {
    EN_MSG_LOGIN, //用户登录消息
    EN_MSG_LOGIN_Fail, //用户登录失败
    EN_MSG_LOGIN_SUCC, //用户登录失败
    EN_MSG_REGISTER, //用户注册
    EN_MSG_OPEN_CHAT, //一对一聊天消息
    EN_MSG_SINGLE_CHAT, //一对一聊天消息
    EN_MSG_MODIFY_NICKNAME,//修改昵称
    EN_MSG_MODIFY_SIGNATURE, //修改个性签名
    EN_MSG_GET_INFORMATION, //获取登录用户的信息
    EN_MSG_MODIFY_PASSWORD,//修改密码
    EN_MSG_PASSWORD_ERROR,//旧密码错误，修改失败
    EN_MSG_PASSWORD_SUCC, //修密码成功
    EN_MSG_GET_FRIEND,   //获取好友列表
    EN_MSG_ADD_FRIEND,   //添加好友
    EN_MSG_ADD_FRIEND_Fail, //添加好友失败
    EN_MSG_DEL_FRIEND,   //删除好友
    EN_MSG_EXIT,    //退出客户端
    EN_MSG_GET_GROUP_INFROMATION,  //获取群信息
    EN_MSG_GET_GROUP_MENBER,  //获取群成员信息
    EN_MSG_GROUP_CHAT,  //群聊
    EN_MSG_GROUP_SINGLE_CHAT,  //群的私聊
    EN_MSG_OPEN_GROUP,  //进入群
    EN_MSG_GROUP_EXIT,  //退出群
    EN_MSG_GET_SINGLE_HISTORY,  //获取与聊天对象的聊天历史记录
    EN_MSG_GET_GROUP_HISTORY,  //获取群的聊天历史记录
    EN_MSG_MODIFY_ICON,    //修改头像ID
    EN_MSG_SINGLE_CLOSE,   //关闭私聊聊天窗口
    EN_MSG_GROUP_CLOSE,   //关闭私聊聊天窗口

}
