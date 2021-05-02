package dao;


import Utils.JDBCUtils;
import Utils.SxUtils;
import bean.Imformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImformationDaoImpl implements ImformationDao {

    //查询昵称和个性签名
    @Override
    public Imformation getImformation(String userid) {
        Imformation imformation = null;
        String sql = "SELECT * FROM imformation WHERE userid = ?";
        Connection conn = JDBCUtils.getConnection();
        PreparedStatement pstt = null;
        ResultSet rs = null;
        try {
            pstt = conn.prepareStatement(sql);
            pstt.setString(1,userid);
            rs = pstt.executeQuery();
            while(rs.next()){
                imformation = new Imformation();
                imformation.setNickName(rs.getString("nickName"));
                imformation.setSignNature(rs.getString("signature"));
            }
            if(null == imformation){
                imformation = new Imformation();
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
}
