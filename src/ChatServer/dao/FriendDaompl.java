package ChatServer.dao;

import Utils.JDBCUtils;
import Utils.IOUtils;
import ChatServer.bean.Information;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现FriendDao接口
 */
public class FriendDaompl implements FriendDao{

    /**
     * 获取好友列表
     * @param userid 用户id
     * @return 存储好友的集合
     */
    @Override
    public List<Information> getFriends(String userid) {
        List<String> friendList = new ArrayList<>();
        List<Information> list = new ArrayList<>();
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        //查询好友列表的userid
        String sql1 = "SELECT friendid FROM friend WHERE userid = ?";
        //根据好友userid查询好友信息
        String sql2 = "SELECT * FROM information WHERE userid = ?";
        PreparedStatement pstt = null;
        //结果集
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql1);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            //存储好友id
            while(rs.next()){
                friendList.add(rs.getString("friendid"));
            }
            //根据好友id获取好友信息
            pstt = conn.prepareStatement(sql2);
            for (int i = 0; i < friendList.size(); i++) {
                pstt.setString(1,friendList.get(i));
                rs = pstt.executeQuery();
                while(rs.next()){
                    Information information = new Information();
                    information.setUid(rs.getString("userid"));
                    information.setNickName(rs.getString("nickName"));
                    information.setSignNature(rs.getString("signature"));
                    information.setStatus(rs.getString("status"));
                    //存入好友信息
                    list.add(information);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
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
        //数据库的连接(封装)
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
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 删除好友
     * @param userid 用户id
     * @param friendid 好友id
     */
    @Override
    public void deleteFriend(String userid, String friendid) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        String sql = "DELETE from friend WHERE userid = ? AND friendid = ?";

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            pstt.setString(2,friendid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }

    }


}
