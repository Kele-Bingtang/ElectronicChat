package dao;

import java.util.List;

public interface FriendDao {

    //获取好友列表
    public List<String> getFriends(String userid);

    //添加好友
    void addFriend(String userid,String friendid);

    //删除好友



}
