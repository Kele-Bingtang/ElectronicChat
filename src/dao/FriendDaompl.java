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
        List<String> list = new ArrayList<>();
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM friend WHERE userid = ?";
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();

            while(rs.next()){
                list.add(rs.getString("friendName"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            SxUtils.close(rs,pstt,conn);
        }

        return list;
    }
}
