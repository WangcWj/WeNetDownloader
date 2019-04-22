package demo.wang.cn.download.response;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
public class WeLoaderResponse implements Callback, ProgressListener {

    private volatile long mCurrentPoint = 0L;
    private Map<String, BaseCallback> callbacks = new HashMap<>();


    public long getBreakPoint() {
        return mCurrentPoint;
    }

    public void setBaseCallback(BaseCallback mBaseCallback) {
        String name = mBaseCallback.getClass().getName();
        if (!callbacks.containsKey(name)) {
            callbacks.put(name, mBaseCallback);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        long legth = response.body().bytes().length;

    }

    @Override
    public void progress(long count, long read, boolean isFinish) {
        mCurrentPoint = read;
        noticeProgressListener(count, read);
    }

    public void noticeCancelListener() {
        BaseCallback callback = getCallbackKey(DownloadCancelCallback.class);
        if (callback instanceof DownloadCancelCallback) {
            ((DownloadCancelCallback) callback).downloanCancel();
        }
    }

    public void noticeStartListener() {
        BaseCallback callback = getCallbackKey(DownloadStartCallback.class);
        if (callback instanceof DownloadStartCallback) {
            ((DownloadCancelCallback) callback).downloanCancel();
        }
    }

    private void noticeFinishListener() {
        BaseCallback callback = getCallbackKey(DownloadStartCallback.class);
        if (callback instanceof DownloadFinishCallback) {
            ((DownloadCancelCallback) callback).downloanCancel();
        }
    }

    private void noticeFailListener(Exception e) {
        BaseCallback callback = getCallbackKey(DownloadFailCallback.class);
        if (callback instanceof DownloadFailCallback) {
            ((DownloadFailCallback) callback).downloanFail(e);
        }
    }

    private void noticeProgressListener(long count, long read) {
        BaseCallback callback = getCallbackKey(DownloadProgressCallback.class);
        if (callback instanceof DownloadProgressCallback) {
            float prcent = (float) read / count;
            ((DownloadProgressCallback) callback).downLoanProgress(read, count, prcent);
        }
    }

    private BaseCallback getCallbackKey(Class clz) {
        String name = clz.getName();
        BaseCallback baseCallback = callbacks.get(name);
        return baseCallback;
    }
}
