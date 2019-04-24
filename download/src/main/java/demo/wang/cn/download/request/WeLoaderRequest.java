package demo.wang.cn.download.request;
import java.io.File;

import demo.wang.cn.download.lifecircle.WeLoaderLifeCircle;
import demo.wang.cn.download.utils.FileUtils;
import okhttp3.Request;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoaderRequest implements WeLoaderLifeCircle {

    final String TAG = "WeLoaderRequest : ";
    private String url;
    private File mTargetFile;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getTargetFile() {
        return mTargetFile;
    }

    public Request createRequest(long startPoints) {
        Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + startPoints + "-")
                .url(url)
                .build();
        return request;
    }

    public WeLoaderRequest setFilePath(String mFilePath) {
        File file = FileUtils.handleFile(mFilePath);
        checkFile(file);
        return this;
    }

    public void setTargetFile(File mTargetFile) {
        FileUtils.handleFile(mTargetFile);
        checkFile(mTargetFile);
    }

    private void checkFile(File file){
        if(null == file){
            throw new RuntimeException();
        }
        mTargetFile = file;
    }

    @Override
    public void create() {
        //
    }

    @Override
    public void destroy() {
        mTargetFile = null;
    }
}
