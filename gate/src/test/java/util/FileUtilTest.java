package util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by hunger on 2017/8/4.
 */



public class FileUtilTest {


    @Test
    public void getFilesByPathAndSuffix1() throws Exception {

    }

    @Test
    public void getFilesByPathAndSuffix2() throws Exception {

    }

    @Test
    public void createFile() throws Exception {
            FileUtil.createFile("D:\\JAVA\\IDEA\\IDEA\\smart-carssss\\picture1\\1.jpg");
    }

    @Test
    public void createDir() throws Exception {
            FileUtil.createDir("D:\\JAVA\\IDEA\\IDEA\\smart-carssss\\picture1\\ssd");
    }


    @Test
    public void getFilesByPathAndSuffix() throws Exception {

        File[] files  = FileUtil.getFilesByPathAndSuffix("F:\\壮哉我大QG\\智能小车\\代码\\ffmpeg-20170724-03a9e6f-win64-static\\bin\\temp",".jpg");
        for (int i =0 ;i <files.length;i++) {
//            System.out.println(files[i].getName());
        }
    }


}