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
public interface WeLoaderRequest extends WeLoaderLifeCircle {

    /**
     * 获取当前下载的URl连接
     *
     * @return
     */
    String getUrl();

    /**
     * 设置本次任务的下载链接
     *
     * @param url
     */
    WeLoaderRequest setUrl(String url);

    /**
     * 获取下载到本地的文件
     *
     * @return
     */
    File getTargetFile();

    /**
     * 创建一个请求
     *
     * @param startPoints 本次请求开始断点
     * @return OkHttp 的Request对象
     */
    Request createRequest(long startPoints);

    /**
     * 本地保存的文件的路径
     *
     * @param mFilePath 文件路径H
     * @return
     */
    WeLoaderRequest setFilePath(String mFilePath);

    /**
     * 本地保存的文件的路径
     *
     * @param mTargetFile File
     */
    WeLoaderRequest setTargetFile(File mTargetFile);

}
