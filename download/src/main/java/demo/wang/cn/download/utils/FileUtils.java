package demo.wang.cn.download.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/23
 */
public class FileUtils {


    public static boolean handleFile(File file){
        if(null == file){
            return false;
        }
        if(file.exists()){
            file.delete();
        }
        return true;
    }

    public static File handleFile(String filePath){
        if(!TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            handleFile(file);
            return file;
        }
        return null;
    }

    public static void delectedFile(File file){
        if(file.exists()){
            file.delete();
        }
    }

}
