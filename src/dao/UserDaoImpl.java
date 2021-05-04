package dao;

import Utils.JDBCUtils;
import Utils.SxUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{


    @Override
    public boolean getInformation(String userid, String password) {
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

    //验证密码
    @Override
    public boolean verifyPassword(String userid, String oldPassword) {
        boolean isReal = false;
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM users WHERE userid = ? AND password = ?";
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            pstt.setString(2,oldPassword);

            rs = pstt.executeQuery();

            while(rs.next()){
                //密码正确
                isReal = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源
            SxUtils.close(rs,pstt,conn);
        }
        return isReal;
    }

    //修改密码
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
