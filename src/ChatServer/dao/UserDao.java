package ChatServer.dao;

/**
 * 与数据库的users表交互
 */
public interface UserDao {

    //判断用户名和密码是否正确
    boolean verifyUseridAndPassword(String userid,String password);

    //注册用户名和密码
    void register(String userid,String password);

    //判断用户名是否存在(注册时)
    boolean verifyUserid(String userid);

    //修改密码
    void modifyPassword(String userid,String password);

    //验证是否存在所添加好友的id
    boolean verifyFriendID(String userid);

}
