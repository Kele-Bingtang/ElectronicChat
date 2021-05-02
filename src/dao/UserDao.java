package dao;


public interface UserDao {

    //判断是否成功登录
    boolean getInformation(String userid,String password);

    //验证密码
    boolean verifyPassword(String oldPassword);

    //修改密码
    void modifyPassword(String password,String userid);


}
