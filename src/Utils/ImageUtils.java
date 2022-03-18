package Utils;

import java.net.URL;

/**
 * @author Young Kbt
 * @date 2022/3/19 5:28
 * @description 获取图片路径
 */
public class ImageUtils {
    
    public static URL getImageUrl(String fileName) {
        return ImageUtils.class.getResource("/ChatClient/Image/" + fileName);
    }
}
