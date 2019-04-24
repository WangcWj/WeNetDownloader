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


    public static void handleFile(File file){
        if(null == file){
            return;
        }
        if(file.exists()){
            file.delete();
        }
    }

    public static File handleFile(String filePath){
        if(!TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            handleFile(file);
            return file;
        }
        return null;
    }

}
