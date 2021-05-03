package Utils;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 释放资源
 */
public class SxUtils {

    public static void close(Closeable... targets){
        for (Closeable target : targets) {
            try {
                if(null != target){
                    target.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
