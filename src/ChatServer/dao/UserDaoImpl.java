package ChatServer.dao;

import Utils.JDBCUtils;
import Utils.IOUtils;
import Utils.MD5;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 实现UserDao接口
 */
public class UserDaoImpl implements UserDao{

    /**
     * 判断用户名和密码是否正确
     * @param userid 用户id
     * @param password 密码
     * @return true false
     */
    @Override
    public boolean verifyUseridAndPassword(String userid, String password) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM users WHERE userid = ? AND password = ?";
        //判断查询成功标记，初始化默认为false
        boolean flag = false;
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            //数据库的密码是MD5加密后的
            pstt.setString(2, MD5.encoderByMd5(password));
            rs = pstt.executeQuery();
            if(rs.next()){
                //存在则改为true
                flag = true;
            }
        } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return flag;
    }

    /**
     * 注册用户名和密码
     * @param userid 用户名
     * @param password 密码
     */
    @Override
    public void register(String userid, String password) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "INSERT INTO users(userid,password) VALUES(?,?)";
        PreparedStatement pstt = null;

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            //MD5加密密码
            pstt.setString(2,MD5.encoderByMd5(password));
            pstt.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }

    }

    /**
     * 判断用户名是否存在(注册时)
     * @param userid 用户名
     * @return true or false
     */
    @Override
    public boolean verifyUserid(String userid) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM users WHERE userid = ?";
        //判断查询成功标记，初始化默认为false
        boolean flag = false;
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            if(rs.next()){
                //存在则改为true
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return flag;
    }

    /**
     * 修改密码
     * @param userid 用户id
     * @param password 密码
     */
    @Override
    public void modifyPassword(String userid, String password) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE users SET password = ? WHERE userid = ?";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,MD5.encoderByMd5(password));
            pstt.setString(2,userid);

            pstt.executeUpdate();
        } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 验证是否存在所添加好友的id
     * @param userid 添加id
     * @return true false
     */
    @Override
    public boolean verifyFriendID(String userid) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "SELECT * FROM users WHERE userid = ?";
        //判断查询成功标记，初始化默认为false
        boolean isExit = false;
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            while (rs.next()){
                //存在则改为true
                isExit = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }

        return isExit;
    }
}
