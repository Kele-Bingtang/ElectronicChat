package ChatServer.dao;

import ChatServer.bean.Group;

import java.util.List;

public interface groupChatDao {

    //查询群id
    List<Group> getGroupInformation();
    //根据群名查询群id
    Group getGroupid(String groupName);
    //查询群成员

}
