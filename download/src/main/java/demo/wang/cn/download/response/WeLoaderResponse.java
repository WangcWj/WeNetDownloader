package demo.wang.cn.download.response;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import demo.wang.cn.download.WeLifeCircle;
import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.DownloadCancelCallback;
import demo.wang.cn.download.callback.DownloadFailCallback;
import demo.wang.cn.download.callback.DownloadFinishCallback;
import demo.wang.cn.download.callback.DownloadProgressCallback;
import demo.wang.cn.download.callback.DownloadStartCallback;
import demo.wang.cn.download.callback.ProgressListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/19
 */
public class WeLoaderResponse implements Callback, ProgressListener, WeLifeCircle {

    private volatile long mCurrentPoint = 0L;
    private Map<String, BaseCallback> callbacks = new HashMap<>();
    private boolean isDestroy;


    public long getBreakPoint() {
        return mCurrentPoint;
    }

    public void setBaseCallback(BaseCallback mBaseCallback) {
        String name = mBaseCallback.getClass().getName();
        if (!callbacks.containsKey(name)) {
            callbacks.put(name, mBaseCallback);
        }
    }

    public void noticeCancelListener() {
        DownloadCancelCallback callback = getCallback(DownloadCancelCallback.class);
        if (!checkNull(callback)) {
            callback.downloanCancel();
        }
    }

    public void noticeStartListener() {
        DownloadStartCallback callback = getCallback(DownloadStartCallback.class);
        if (!checkNull(callback)) {
            callback.downloanStart();
        }
    }

    private void noticeFinishListener(byte[] bytes) {
        DownloadFinishCallback callback = getCallback(DownloadFinishCallback.class);
        if (!checkNull(callback)) {
            callback.downloanFinish(bytes);
        }
    }

    private void noticeFailListener(Exception e) {
        DownloadFailCallback callback = getCallback(DownloadFailCallback.class);
        if (!checkNull(callback)) {
            callback.downloanFail(e);
        }
    }

    private void noticeProgressListener(long count, long read) {
        DownloadProgressCallback callback = getCallback(DownloadProgressCallback.class);
        if (!checkNull(callback)) {
            callback.downLoanProgress(count, read, 0);
        }
    }

    private <T> T getCallback(Class<T> clz) {
        String name = clz.getName();
        T baseCallback = (T) callbacks.get(name);
        return baseCallback;
    }

    private boolean checkNull(BaseCallback baseCallback) {
        if (null == baseCallback || isDestroy) {
            return true;
        }
        return false;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        noticeFailListener(e);
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            byte[] bytes = response.body().bytes();
            noticeFinishListener(bytes);
        } catch (Exception e) {
            noticeFailListener(e);
        }
    }

    @Override
    public void progress(long count, long read, boolean isFinish) {
        mCurrentPoint = read;
        noticeProgressListener(count, read);
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
