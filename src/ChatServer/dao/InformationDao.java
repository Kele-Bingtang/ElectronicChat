package ChatServer.dao;

import ChatServer.bean.Information;

import java.util.List;

/**
 * 与数据库的information表交互
 */
public interface InformationDao {

    //注册后的userid存入数据库
    void registerUserid(String userid);

    //获取全部用户信息
    List<Information> getAllImformation();

    //根据用户id获得用户的信息(昵称、签名、状态)
    Information getImformationByUserid(String userid);

    //通过昵称获取用户名
    Information getUserIDByNickName(String nickName);

    //修改的昵称存储到数据库
    void modifyNickName(String userid,String nickName);

    //修改的个性签名存储到数据库
    void modifySignature(String userid,String signature);

    //修改的头像id存储到数据库
    void modifyIconID(String userid, int iconID);

    //修改用户状态(在线、离线等)
    void modifyStatus(String userid,String status);

}
