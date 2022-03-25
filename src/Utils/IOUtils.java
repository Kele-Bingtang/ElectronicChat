package Utils;

import java.io.Closeable;

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
