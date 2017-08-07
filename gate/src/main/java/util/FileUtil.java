package util;

import handler.GateServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by hunger on 2017/8/4.
 */
public class FileUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /***
     *  过滤后缀名，遍历文件夹文件
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

    /**
     * 后缀名
     *  过滤后缀名遍历文件夹文件
     * @param pathStr
     * @param sufixStr
     * @return
     */
    public static File[] getFilesByPathAndSuffix(String pathStr,
                                                 final String sufixStr) {
        File path = new File(pathStr);
        return getFilesByPathAndSuffix(path, sufixStr);
    }

    /**
     * 创建文件
     * @param destFileName
     * @return
     */
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
            LOGGER.error("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            LOGGER.error("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            LOGGER.info("目标文件所在目录不存在，准备创建它！");
            if(!file.getParentFile().mkdirs()) {
                LOGGER.error("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                LOGGER.info("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                LOGGER.error("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    /**
     * 创建文件夹
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            LOGGER.error("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            LOGGER.info("创建目录" + destDirName + "成功！");
            return true;
        } else {
            LOGGER.error("创建目录" + destDirName + "失败！");
            return false;
        }
    }
}
