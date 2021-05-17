package Utils;

import java.sql.*;

/**
 * 连接数据库
 * 关闭资源
 */
public class JDBCUtils {

    public static Connection getConnection(){
        String userName = "kele";
        String passWord = "kele1234";
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@112.74.169.231:1521:orcl",userName,passWord);
            if(null == conn){
                System.out.println("数据库连接失败");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet rs, PreparedStatement pstt, Connection conn) {
        try {
            if(null != rs){
                rs.close();
            }
            if(null != pstt){
                pstt.close();
            }
            if(null != conn){
                conn.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void close(PreparedStatement pstt, Connection conn) {
        try {
            if(null != pstt){
                pstt.close();
            }
            if(null != conn){
                conn.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
