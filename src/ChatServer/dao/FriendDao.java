package ChatServer.dao;

import ChatServer.bean.Information;

import java.util.List;

/**
 * 与数据库的firend表交互
 */
public interface FriendDao {

    //获取好友列表
    public List<Information> getFriends(String userid);

    //添加好友
    void addFriend(String userid,String friendid);

    //删除好友
    void deleteFriend(String userid,String friendid);

}
