package dao;


public interface UserDao {

    //判断是否成功登录
    boolean getInformation(String userid,String password);

    //验证密码
    boolean verifyPassword(String userid,String oldPassword);

    //修改密码
    void modifyPassword(String userid,String password);

    //验证是否存在所添加好友的id
    boolean verifyFriendID(String userid);

}
