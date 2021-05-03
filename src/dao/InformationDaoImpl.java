package dao;


import Utils.JDBCUtils;
import Utils.SxUtils;
import bean.Information;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InformationDaoImpl implements InformationDao {

    /**
     * 查询昵称和个性签名
     * @param userid 用户名
     * @return imformation
     */
    @Override
    public Information getImformation(String userid) {
        Information imformation = null;
        String sql = "SELECT * FROM imformation WHERE userid = ?";
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            while(rs.next()){
                imformation = new Information();
                imformation.setNickName(rs.getString("nickName"));
                imformation.setSignNature(rs.getString("signature"));
            }
            if(null == imformation){
                imformation = new Information();
                imformation.setNickName("第八组");
                imformation.setSignNature("欢迎来到第八组简易聊天系统");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            SxUtils.close(rs,pstt,conn);
        }
        return imformation;
    }
    /**
     * 通过昵称获取用户名
     * 昵称没有重复情况下
     */
    @Override
    public Information getUserID(String nickName) {
        Information information = null;
        String sql = "SELECT * FROM imformation WHERE nickName = ?";
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
            JDBCUtils.close(rs,pstt,conn);
        }
        return information;
    }

    /**
     * 修改的昵称存储到数据库
     * @param nickName 昵称
     * @param userid 用户id
     */
    @Override
    public void storeNickName(String userid,String nickName) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE imformation SET nickName = ? WHERE userid = ?";
        PreparedStatement pstt = null;

        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,nickName);
            pstt.setString(2,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 修改的个性签名存储到数据库
     * @param signature 个性签名
     * @param userid 用户id
     */
    @Override
    public void storeSignature(String userid,String signature) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "UPDATE imformation SET signature = ? WHERE userid = ?";
        PreparedStatement pstt = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,signature);
            pstt.setString(2,userid);
            pstt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(pstt,conn);
        }
    }

    /**
     * 获取全部用户信息
     * @return 存储用户信息的集合
     */
    public List<Information> getAllImformation() {
        Information information;
        List<Information> informationList = new ArrayList<>();
        String sql = "SELECT * FROM imformation";
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            rs = pstt.executeQuery();
            while(rs.next()){
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
            SxUtils.close(rs,pstt,conn);
        }
        return informationList;
    }

}
