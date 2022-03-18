package Utils;

import java.sql.*;

/**
 * 连接数据库
 * 关闭资源
 */
public class JDBCUtils {

    /**
     * 测试数据库连接是否成功
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = getConnection();
        System.out.println(connection);
    }

    public static Connection getConnection(){
        String driveName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://112.74.169.231:3306/electronic_chat?useSSL-=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String userName = "root";
        String passWord = "YoungKbt1234";
        Connection conn = null;
        try {
            Class.forName(driveName);
            conn = DriverManager.getConnection(url,userName,passWord);
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
