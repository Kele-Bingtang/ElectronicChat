package dao;

import Utils.JDBCUtils;
import Utils.SxUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendDaompl implements FriendDao{

    //获取好友列表
    @Override
    public List<String> getFriends(String userid) {
        List<String> friendList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        Connection conn = JDBCUtils.getConnection();
        //查询好友列表的userid
        String sql1 = "SELECT friendid FROM friend WHERE userid = ?";
        //根据好友userid查询好友信息
        String sql2 = "SELECT * FROM information WHERE userid = ?";
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql1);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();

            while(rs.next()){
                friendList.add(rs.getString("friendid"));
            }

            pstt = conn.prepareStatement(sql2);
            for (int i = 0; i < friendList.size(); i++) {
                pstt.setString(1,friendList.get(i));
                rs = pstt.executeQuery();
                while(rs.next()){
                    list.add(rs.getString("nickName"));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            SxUtils.close(rs,pstt,conn);
        }

        return list;
    }

    /**
     * 根据用户id添加好友
     * @param userid 用户id
     * @param friendid 好友id
     */
    @Override
    public void addFriend(String userid, String friendid) {
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        String sql = "INSERT INTO friend(userid,friendid) VALUES(?,?)";

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            pstt.setString(2,friendid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            SxUtils.close(pstt,conn);
        }
    }
}
