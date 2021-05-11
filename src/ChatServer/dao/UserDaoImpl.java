package ChatServer.dao;

import Utils.JDBCUtils;
import Utils.SxUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{

    /**
     * 登陆判断用户名和密码师傅正确
     * @param userid 用户id
     * @param password 密码
     * @return true false
     */
    @Override
    public boolean verifyUseridAndPassword(String userid, String password) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM users WHERE userid = ? AND password = ?";
        //判断查询成功标记
        boolean flag = false;
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            pstt.setString(2,password);
            rs = pstt.executeQuery();
            if(rs.next()){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源
            SxUtils.close(rs,pstt,conn);
        }
        return flag;
    }

    @Override
    public void register(String userid, String password) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "INSERT INTO users(userid,password) VALUES(?,?)";
        PreparedStatement pstt = null;

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            pstt.setString(2,password);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            SxUtils.close(pstt,conn);
        }

    }

    /**
     * 修改密码
     * @param userid 用户id
     * @param password 密码
     */
    @Override
    public void modifyPassword(String userid, String password) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE users SET password = ? WHERE userid = ?";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,password);
            pstt.setString(2,userid);

            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源
            SxUtils.close(pstt,conn);
        }
    }

    /**
     * 验证添加的id是否存在
     * @param userid 添加id
     * @return true false
     */
    @Override
    public boolean verifyFriendID(String userid) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM users WHERE userid = ?";
        boolean isExit = false;
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            while (rs.next()){
                isExit = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            SxUtils.close(rs,pstt,conn);
        }

        return isExit;
    }
}
