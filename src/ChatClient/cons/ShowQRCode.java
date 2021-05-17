package ChatClient.cons;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 显示二维码
 */
public class ShowQRCode {
    /**
     * 把文件以日期格式排序，升序
     *
     * @param path 路径
     * @return list
     */
    public static List<File> getFileSort(String path) {
        List<File> list = getFiles(path, new ArrayList<File>());
        if (list.size() > 0) {
            list.sort(new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 0;
                    } else if (file.lastModified() > newFile.lastModified()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 获得所有的目录下的文件
     *
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {

        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if(file.getName().endsWith(".png")){
                    files.add(file);
                }
            }
        }
        return files;
    }
}
