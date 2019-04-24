package demo.wang.cn.download.response;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import demo.wang.cn.download.callback.InnerFinishCallBack;
import demo.wang.cn.download.callback.WeloaderBaseCallback;
import demo.wang.cn.download.lifecircle.WeLoaderLifeCircle;
import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.WeLoaderFailCallback;
import demo.wang.cn.download.callback.WeLoaderFinishCallback;
import demo.wang.cn.download.callback.WeLoaderProgressCallback;
import demo.wang.cn.download.callback.WeLoaderProgressListener;
import demo.wang.cn.download.utils.CallBackUtils;
import demo.wang.cn.download.utils.ThreadUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/19
 */
public class WeLoaderResponse implements Callback, WeLoaderProgressListener, WeLoaderLifeCircle, Runnable {

    private volatile long mCurrentPoint = 0L;
    private Map<String, WeloaderBaseCallback> callbacks;
    private boolean isDestroy;
    private volatile boolean isStart = false;

    public long getBreakPoint() {
        return mCurrentPoint;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    /**
     * 。。。
     *
     * @param mBaseCallback
     */
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
        runMainThread(BaseCallback.FAIL_INS, e);
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
            runMainThread(BaseCallback.FAIL_INS, e);
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
            isStart = false;
            //切线程
            runMainThread(BaseCallback.DOWNLOAN_FINISH_INS);
            return;
        }
        if (isStart) {
            isStart = false;
            runMainThread(BaseCallback.START_INS);
        }
        mCurrentPoint = read;
        runMainThread(BaseCallback.PROGRESS_INS, count, read);
    }

    /**
     * 运行环境: 子线程运行
     *
     * @param response
     */
    private void notifyManagerFinishListener(Response response) {
        notifyCallBack(BaseCallback.INNER_FINISH_INS, response);
    }

    public void runMainThread(String flag, Object... arg) {
        ThreadUtils.runThread(false, new Runnable() {
            @Override
            public void run() {
                notifyCallBack(flag, arg);
            }
        });
    }

    private void notifyCallBack(String flag, Object... arg) {
        WeloaderBaseCallback weLoaderBaseCallback = callbacks.get(flag);
        if (!checkNull(weLoaderBaseCallback)) {
            CallBackUtils.notifyCallBack(weLoaderBaseCallback, flag, arg);
        }
        WeloaderBaseCallback allCallback = callbacks.get(BaseCallback.ALL);
        if (!checkNull(allCallback)) {
            CallBackUtils.notifyCallBack(allCallback, flag, arg);
        }
    }

    private boolean checkNull(Object baseCallback) {
        if (null == baseCallback || isDestroy) {
            return true;
        }
        return false;
    }

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
            while ((len = in.read(buffer)) != -1) {
                mappedBuffer.put(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            runMainThread(BaseCallback.FAIL_INS, e);
        } finally {
            try {
                in.close();
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
    public void run() {

    }

    @Override
    public void create() {
        isDestroy = false;
    }

    @Override
    public void destroy() {
        isDestroy = true;
        callbacks.clear();
    }

}
