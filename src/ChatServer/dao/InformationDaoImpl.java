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
 * 实现InformationDao接口
 */
public class InformationDaoImpl implements InformationDao {

    /**
     * 注册后的userid存入数据库
     * @param userid
     */
    @Override
    public void registerUserid(String userid) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "INSERT INTO information(userid) VALUES(?)";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 获取全部用户信息
     * @return informationList 存储用户信息的集合
     */
    public List<Information> getAllImformation() {
        Information information;
        List<Information> informationList = new ArrayList<>();
        String sql = "SELECT * FROM information";
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            rs = pstt.executeQuery();
            //循环
            while(rs.next()){
                //information为一行，存入列的值
                information = new Information();
                information.setUid(rs.getString("userid"));
                information.setNickName(rs.getString("nickName"));
                information.setSignNature(rs.getString("signature"));
                information.setStatus(rs.getString("status"));
                informationList.add(information);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return informationList;
    }

    /**
     * 根据用户id获得用户的信息(昵称、签名、状态)
     * @param userid 用户名
     * @return imformation
     */
    @Override
    public Information getImformationByUserid(String userid) {
        Information information = null;
        String sql = "SELECT * FROM information WHERE userid = ?";
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            while(rs.next()){
                information = new Information();
                //昵称为空，则默认初始值
                if(rs.getString("nickName") == null){
                    information.setNickName("昵称");
                }else {
                    //不为空，则获取昵称
                    information.setNickName(rs.getString("nickName"));
                }
                //签名为空，则默认初始值
                if(rs.getString("signature") == null){
                    information.setSignNature("编辑个性签名");
                }else {
                    //不为空，则获取签名
                    information.setSignNature(rs.getString("signature"));
                }
                information.setIconID(rs.getInt("iconid"));
            }
            //全为空，则默认初始值
            if(null == information){
                information = new Information();
                information.setNickName("昵称");
                information.setSignNature("编辑个性签名");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return information;
    }

    /**
     * 通过昵称获取用户名
     * 昵称没有重复情况下
     * @param nickName 昵称
     * @return information的userid
     */
    @Override
    public Information getUserIDByNickName(String nickName) {
        Information information = null;
        String sql = "SELECT * FROM information WHERE nickName = ?";
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,nickName);
            rs = pstt.executeQuery();

            while (rs.next()){
                information = new Information();
                information.setUid(rs.getString("userid"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(rs,pstt,conn);
        }
        return information;
    }

    /**
     * 修改的昵称存储到数据库
     * @param userid 用户id
     * @param nickName 昵称
     */
    @Override
    public void modifyNickName(String userid,String nickName) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE information SET nickName = ? WHERE userid = ?";
        PreparedStatement pstt = null;

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,nickName);
            pstt.setString(2,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 修改的个性签名存储到数据库
     * @param userid 用户id
     * @param signature 个性签名
     */
    @Override
    public void modifySignature(String userid,String signature) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE information SET signature = ? WHERE userid = ?";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,signature);
            pstt.setString(2,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 修改的头像ID存储到数据库
     * @param userid 用户id
     * @param iconID 头像ID
     */
    @Override
    public void modifyIconID(String userid, int iconID) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE information SET iconID = ? WHERE userid = ?";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setInt(1,iconID);
            pstt.setString(2,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 修改用户状态(在线、离线等)
     * @param userid  用户id
     * @param status 状态
     */
    @Override
    public void modifyStatus(String userid,String status) {
        //数据库的连接(封装)
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE information SET status = ? WHERE userid = ?";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,status);
            pstt.setString(2,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //关闭资源(封装方法)
            JDBCUtils.close(pstt,conn);
        }
    }

}
