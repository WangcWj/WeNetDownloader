package demo.wang.cn.download.response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import demo.wang.cn.download.callback.WeloaderBaseCallback;
import demo.wang.cn.download.constant.WeLoaderConstant;
import demo.wang.cn.download.callback.WeLoaderProgressListener;
import demo.wang.cn.download.utils.CallBackUtils;
import demo.wang.cn.download.utils.ThreadUtils;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/19
 */
public class WeLoaderResponseImp implements WeLoaderProgressListener,WeLoaderResponse {

    private volatile long mCurrentPoint = 0L;
    private Map<String, WeloaderBaseCallback> callbacks;
    private boolean isDestroy;
    private volatile boolean isStart = false;
    private volatile boolean isRunning = false;

    @Override
    public long getBreakPoint() {
        return mCurrentPoint;
    }

    @Override
    public void setStart(boolean start) {
        isStart = start;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 。。。
     *
     * @param mBaseCallback
     */
    @Override
    public void setBaseCallback(WeloaderBaseCallback mBaseCallback) {
        if (callbacks == null) {
            callbacks = new HashMap<>(7);
        }
        String flag = CallBackUtils.switchCallBack(mBaseCallback);
        if (null == flag) {
            return;
        }
        if (!callbacks.containsKey(flag)) {
            callbacks.put(flag, mBaseCallback);
        }
    }

    /**
     * 运行环境: 子线程运行
     *
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {
        runMainThread(WeLoaderConstant.FAIL_INS, e);
    }

    /**
     * 运行环境: 子线程运行
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call call, Response response) {
        try {
            //子线程
            notifyManagerFinishListener(response);
        } catch (Exception e) {
            runMainThread(WeLoaderConstant.FAIL_INS, e);
        }
    }

    /**
     * 运行环境: 子线程运行
     *
     * @param count
     * @param read
     * @param isFinish
     */
    @Override
    public void progress(long count, long read, boolean isFinish) {
        if (isFinish) {
            isRunning = false;
            isStart = false;
            //切线程
            runMainThread(WeLoaderConstant.DOWNLOAN_FINISH_INS);
            return;
        }
        if (isStart) {
            isRunning = true;
            isStart = false;
            runMainThread(WeLoaderConstant.START_INS);
        }
        mCurrentPoint = read;
        runMainThread(WeLoaderConstant.PROGRESS_INS, count, read);
    }

    /**
     * 切换到主线程
     * @param flag 事件的标记位
     * @param arg 可变长参数
     */
    @Override
    public void runMainThread(String flag, Object... arg) {
        ThreadUtils.runThread(false, new Runnable() {
            @Override
            public void run() {
                notifyCallBack(flag, arg);
            }
        });
    }

    /**
     * 运行环境: 子线程运行
     * 给{@link demo.wang.cn.download.WeLoader}使用的回调.
     *
     * @param response
     */
    private void notifyManagerFinishListener(Response response) {
        notifyCallBack(WeLoaderConstant.INNER_FINISH_INS, response);
    }

    /**
     * 通知接口回调数据
     * 这里面需要给所有的接口回调数据.
     * @param flag
     * @param arg
     */
    @Override
    public void notifyCallBack(String flag, Object... arg) {
        notifyAllCallBack(flag,arg);
        WeloaderBaseCallback baseCallback = chooseCallBack(flag);
        notifyOne(baseCallback,flag,arg);
    }

    /**
     * 通知{@link demo.wang.cn.download.callback.BaseCallback} 回调内部的方法
     * @param flag
     * @param arg
     */
    private void notifyAllCallBack(String flag, Object[] arg) {
        WeloaderBaseCallback weLoaderBaseCallback = chooseCallBack(WeLoaderConstant.ALL);
        notifyOne(weLoaderBaseCallback,flag,arg);
    }

    /**
     * 获取单个接口的实现类从集合callbacks中.
     *
     * @param flag
     * @return
     */
    private WeloaderBaseCallback chooseCallBack(String flag){
        if(!callbacks.containsKey(flag)){
            return null;
        }
        return callbacks.get(flag);
    }

    /**
     * 调用特定接口的数据
     *
     * @param baseCallback
     * @param flag
     * @param arg
     */
    private void notifyOne(WeloaderBaseCallback baseCallback,String flag ,Object... arg){
        if (!checkNull(baseCallback)) {
            CallBackUtils.notifyCallBack(baseCallback, flag, arg);
        }
    }

    /**
     * 检测接口的实例是否为NULL 并且当界面销毁的时候也返回false
     * @param baseCallback
     * @return
     */
    private boolean checkNull(Object baseCallback) {
        if (null == baseCallback || isDestroy) {
            return true;
        }
        return false;
    }

    /**
     * 保存下载的文件到SD卡上面,支持断点下载
     * @param startPoint 断点
     * @param mSaveFile 目标File
     * @param response byte[]
     */
    @Override
    public void saveFile(long startPoint, File mSaveFile, Response response) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(mSaveFile, "rwd");
            //内存映射文件。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1 && !isDestroy) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            runMainThread(WeLoaderConstant.FAIL_INS, e);
        } finally {
            try {
                in.close();
                response.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void create() {
        isDestroy = false;
    }

    @Override
    public void destroy() {
        isDestroy = true;
        mCurrentPoint = 0;
        callbacks.clear();
    }

}
