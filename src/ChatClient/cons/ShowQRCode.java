package ChatClient.cons;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowQRCode {
    /**
     * Getting files from Folder(Time Sorting by Modify time)
     *
     * @param path
     * @return
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
     * Get all the file from folders(img/{drivesid})
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
