package demo.wang.cn.download.request;

import java.io.File;

import demo.wang.cn.download.WeLoader;
import demo.wang.cn.download.lifecircle.WeLoaderLifeCircle;
import demo.wang.cn.download.utils.FileUtils;
import okhttp3.Request;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoaderRequestImp implements WeLoaderRequest {

    private String url;
    private File mTargetFile;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public WeLoaderRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public File getTargetFile() {
        return mTargetFile;
    }

    @Override
    public Request createRequest(long startPoints) {
        Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + startPoints + "-")
                .url(url)
                .build();
        return request;
    }

    @Override
    public WeLoaderRequest setFilePath(String mFilePath) {
        File file = FileUtils.handleFile(mFilePath);
        checkFile(file);
        return this;
    }

    @Override
    public WeLoaderRequest setTargetFile(File mTargetFile) {
        FileUtils.handleFile(mTargetFile);
        checkFile(mTargetFile);
        return this;
    }

    private void checkFile(File file) {
        if (null == file) {
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
