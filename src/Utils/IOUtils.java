package Utils;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 释放资源
 */
public class IOUtils {

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
}
