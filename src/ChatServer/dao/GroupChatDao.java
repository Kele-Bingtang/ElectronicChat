package ChatServer.dao;

import ChatServer.bean.Group;

import java.util.List;

/**
 * 与数据库的groupChat表交互
 */
public interface GroupChatDao {

    //获取群的全部信息
    List<Group> getGroupInformation();

    //根据群名查询群id
    Group getGroupidByName(String groupName);

}
