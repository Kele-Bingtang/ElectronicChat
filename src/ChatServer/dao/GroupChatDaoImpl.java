package ChatServer.dao;

import ChatServer.bean.Group;
import Utils.JDBCUtils;
import Utils.IOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现groupChatDao接口
 */
public class GroupChatDaoImpl implements GroupChatDao {

    /**
     * 获取群的全部信息
     * @return groupList 存储群的全部信息
     */
    @Override
    public List<Group> getGroupInformation() {
        Group group;
        List<Group> groupList = new ArrayList<>();
        String sql = "SELECT * FROM groupChat";
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            rs = pstt.executeQuery();
            while(rs.next()){
                group = new Group();
                group.setUserid(rs.getString("userid"));
                group.setGroupid(rs.getString("groupid"));
                group.setGroupName(rs.getString("groupName"));
                group.setGroupUserid(rs.getString("groupuserid"));
                groupList.add(group);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return groupList;
    }

    /**
     * 根据群名查询群id
     * 假设群名不重复
     * @param groupName 群名
     * @return group的groupid
     */
    @Override
    public Group getGroupidByName(String groupName) {
        Group group = new Group();
        String sql = "SELECT * FROM groupChat WHERE groupName = ?";
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,groupName);
            rs = pstt.executeQuery();
            while(rs.next()){
                group.setGroupid(rs.getString("groupid"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return group;
    }

}
