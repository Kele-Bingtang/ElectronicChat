package ChatServer.dao;

import ChatServer.bean.Group;
import ChatServer.bean.Information;
import Utils.JDBCUtils;
import Utils.SxUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupChatDaoImpl implements groupChatDao{

    @Override
    public List<Group> getGroupInformation() {
        Group group;
        List<Group> groupList = new ArrayList<>();
        String sql = "SELECT * FROM groupChat";
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
            SxUtils.close(rs,pstt,conn);
        }
        return groupList;
    }

    /**
     * 假设群名不重复
     * @param groupName
     * @return
     */
    @Override
    public Group getGroupid(String groupName) {
        Group group = new Group();
        String sql = "SELECT * FROM groupChat WHERE groupName = ?";
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
            SxUtils.close(rs,pstt,conn);
        }
        return group;
    }

}
