package util;

import constant.GlobalConfig;
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
        File tempfile = new File(GlobalConfig.PICTURE_PATH + 2+ "\\" +GlobalConfig.TEMP_NAME);
        tempfile.deleteOnExit();
        tempfile.createNewFile();
    }

    @Test
    public void createFile() throws Exception {
//        File picturefile = new File("J:\\project_smartCar\\temp"+ "\\" +GlobalConfig.PICTURE_NAME);
//        System.out.println( picturefile.delete());
//        FileUtil.renameFile(new File("J:\\project_smartCar\\temp" + "\\" +GlobalConfig.TEMP_NAME), new File("J:\\project_smartCar\\temp"+ "\\" +GlobalConfig.PICTURE_NAME));
    }

    @Test
    public void createDir() throws Exception {
//            long startTime = System.currentTimeMillis();
//        FileUtil.renameFile(new File("J:\\project_smartCar\\temp" + "\\" +GlobalConfig.PICTURE_NAME), new File("J:\\project_smartCar\\temp"+ "\\" +GlobalConfig.TEMP_NAME));
//        System.out.println(System.currentTimeMillis() - startTime);
//        long startTime = System.currentTimeMillis();
//        File filename = new File("J:\\project_smartCar\\temp\\00001.jpg");
//        filename.deleteOnExit();
//        System.out.println(System.currentTimeMillis() - startTime);
    }





}