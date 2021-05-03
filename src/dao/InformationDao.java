package dao;

import bean.Information;

public interface InformationDao {
    //获取用户信息
    Information getImformation(String userid);


    //通过昵称获取用户名
    Information getUserID(String nickName);

    //修改的昵称存储到数据库
    void storeNickName(String userid,String nickName);

    //修改的个性签名存储到数据库
    void storeSignature(String userid,String signature);

}