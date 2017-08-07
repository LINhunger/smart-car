package util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by hunger on 2017/8/4.
 */
public class FileUtil {




    /***
     *
     * @param path
     * @param sufixStr
     *            :后缀名
     * @return
     */
    public static File[] getFilesByPathAndSuffix(File path,
                                                 final String sufixStr) {
        File[] fileArr = path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if ((sufixStr.isEmpty() || (dir.isDirectory() && name
                        .endsWith(sufixStr)))) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        return fileArr;

    }

    /***
     * 后缀名
     *
     * @param pathStr
     * @param sufixStr
     * @return
     */
    public static File[] getFilesByPathAndSuffix(String pathStr,
                                                 final String sufixStr) {
        File path = new File(pathStr);
        return getFilesByPathAndSuffix(path, sufixStr);
    }
}
