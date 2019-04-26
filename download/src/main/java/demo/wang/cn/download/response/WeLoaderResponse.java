package demo.wang.cn.download.response;


import java.io.File;

import demo.wang.cn.download.callback.WeloaderBaseCallback;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/19
 */
public interface WeLoaderResponse extends Callback {

    /**
     * 获取当前断点的位置
     * @return long
     */
    long getBreakPoint();

    /**
     * 设置当前下载是否已经开始
     * @param start
     */
    void setStart(boolean start);

    /**
     * 设置回掉接口的实现类
     * @param mBaseCallback
     */
    void setBaseCallback(WeloaderBaseCallback mBaseCallback);

    /**
     * 切换到主线程中区运行回掉函数
     * @param flag 回掉事件的标记
     * @param arg 参数
     */
    void runMainThread(String flag, Object... arg);

    /**
     * 在当前线程中调用回掉函数
     * @param flag 回掉事件的标记
     * @param arg 参数
     */
    void notifyCallBack(String flag, Object... arg);

    /**
     * 一遍下载一遍保存文件
     * @param startPoint 文件开始保存的点
     * @param mSaveFile 要保存的文件
     * @param response OkHttp 的Response
     */
    void saveFile(long startPoint, File mSaveFile, Response response);

    boolean isRunning();

    /**
     * 已经创建好
     */
    void create();

    /**
     * 此次任务将要销毁
     */
    void destroy();
}
