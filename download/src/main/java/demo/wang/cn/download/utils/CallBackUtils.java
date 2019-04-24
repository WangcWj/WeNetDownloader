package demo.wang.cn.download.utils;

import demo.wang.cn.download.callback.InnerFinishCallBack;
import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.WeLoaderCancelCallback;
import demo.wang.cn.download.callback.WeLoaderFailCallback;
import demo.wang.cn.download.callback.WeLoaderFinishCallback;
import demo.wang.cn.download.callback.WeLoaderProgressCallback;
import demo.wang.cn.download.callback.WeLoaderSaveFileFinishCallback;
import demo.wang.cn.download.callback.WeLoaderStartCallback;
import demo.wang.cn.download.callback.WeloaderBaseCallback;
import okhttp3.Response;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/23
 */
public class CallBackUtils {

    public static Class switchCallBack(WeloaderBaseCallback baseCallback) {
        if (baseCallback instanceof WeLoaderProgressCallback) {
            return WeLoaderProgressCallback.class;
        } else if (baseCallback instanceof WeLoaderStartCallback) {
            return WeLoaderStartCallback.class;
        } else if (baseCallback instanceof WeLoaderFinishCallback) {
            return WeLoaderFinishCallback.class;
        } else if (baseCallback instanceof WeLoaderFailCallback) {
            return WeLoaderFailCallback.class;
        } else if (baseCallback instanceof InnerFinishCallBack) {
            return InnerFinishCallBack.class;
        } else if (baseCallback instanceof WeLoaderCancelCallback) {
            return WeLoaderCancelCallback.class;
        } else if (baseCallback instanceof WeLoaderSaveFileFinishCallback) {
            return WeLoaderSaveFileFinishCallback.class;
        }
        return null;
    }

    public static void notifyCallBack(WeloaderBaseCallback baseCallback, Object... arg) {
        if (baseCallback instanceof WeLoaderProgressCallback) {
            callbackProgress(baseCallback, arg);
        } else if (baseCallback instanceof WeLoaderStartCallback) {
            ((WeLoaderStartCallback) baseCallback).downLoanStart();
        } else if (baseCallback instanceof WeLoaderFinishCallback) {
            ((WeLoaderFinishCallback) baseCallback).downLoanFinish();
        } else if (baseCallback instanceof WeLoaderFailCallback) {
            callbackFail(baseCallback, arg);
        } else if (baseCallback instanceof WeLoaderCancelCallback) {
            ((WeLoaderCancelCallback) baseCallback).downLoanCancel();
        } else if (baseCallback instanceof WeLoaderSaveFileFinishCallback) {
            ((WeLoaderSaveFileFinishCallback) baseCallback).saveFileFinish();
        } else if (baseCallback instanceof InnerFinishCallBack) {
            callbackFinish(baseCallback, arg);
        }
    }

    private static void callbackProgress(WeloaderBaseCallback baseCallback, Object[] arg) {
        if (arg.length > 1) {
            long count = (long) arg[0];
            long read = (long) arg[1];
            ((WeLoaderProgressCallback) baseCallback).downLoanProgress(read, count, 0);
        }
    }

    private static void callbackFail(WeloaderBaseCallback baseCallback, Object[] arg) {
        if (arg.length > 0) {
            Exception e = (Exception) arg[0];
            if (null != e) {
                ((WeLoaderFailCallback) baseCallback).downLoanFail(e);
            }
        }
    }

    private static void callbackFinish(WeloaderBaseCallback baseCallback, Object[] arg) {
        if (arg.length > 0) {
            Response response = (Response) arg[0];
            if (null != response) {
                ((InnerFinishCallBack) baseCallback).downLoanFinish(response);
            }
        }
    }


}
