package demo.wang.cn.download.utils;

import android.util.Log;

import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.InnerFinishCallBack;
import demo.wang.cn.download.callback.WeLoaderCancelCallback;
import demo.wang.cn.download.callback.WeLoaderFailCallback;
import demo.wang.cn.download.callback.WeLoaderFinishCallback;
import demo.wang.cn.download.callback.WeLoaderProgressCallback;
import demo.wang.cn.download.callback.WeLoaderStartCallback;
import demo.wang.cn.download.callback.WeloaderBaseCallback;
import demo.wang.cn.download.constant.WeLoaderConstant;
import okhttp3.Response;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/23
 */
public class CallBackUtils {

    public static String switchCallBack(WeloaderBaseCallback baseCallback) {

        if (baseCallback instanceof BaseCallback) {
            return WeLoaderConstant.ALL;
        } else if (baseCallback instanceof WeLoaderProgressCallback) {
            return WeLoaderConstant.PROGRESS_INS;
        } else if (baseCallback instanceof WeLoaderStartCallback) {
            return WeLoaderConstant.START_INS;
        } else if (baseCallback instanceof WeLoaderFinishCallback) {
            return WeLoaderConstant.DOWNLOAN_FINISH_INS;
        } else if (baseCallback instanceof WeLoaderFailCallback) {
            return WeLoaderConstant.FAIL_INS;
        } else if (baseCallback instanceof InnerFinishCallBack) {
            return WeLoaderConstant.INNER_FINISH_INS;
        } else if (baseCallback instanceof WeLoaderCancelCallback) {
            return WeLoaderConstant.CANCEL_INS;
        }
        return null;
    }

    public static void notifyCallBack(WeloaderBaseCallback baseCallback, String flag, Object... arg) {
        switch (flag) {
            case WeLoaderConstant.PROGRESS_INS:
                callbackProgress(baseCallback, arg);
                break;
            case WeLoaderConstant.START_INS:
                ((WeLoaderStartCallback) baseCallback).downLoanStart();
                break;
            case WeLoaderConstant.DOWNLOAN_FINISH_INS:
                ((WeLoaderFinishCallback) baseCallback).downLoanFinish();
                break;
            case WeLoaderConstant.FAIL_INS:
                callbackFail(baseCallback, arg);
                break;
            case WeLoaderConstant.INNER_FINISH_INS:
                callbackInnerFinish(baseCallback, arg);
                break;
            case WeLoaderConstant.CANCEL_INS:
                callbackCancel((WeLoaderCancelCallback) baseCallback, arg);
                break;
            default:
                break;

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

    private static void callbackCancel(WeLoaderCancelCallback cancelCallback, Object[] arg) {
        if (arg.length > 0) {
            long e = (long) arg[0];
            cancelCallback.downLoanCancel(e);
        }
    }

    private static void callbackInnerFinish(WeloaderBaseCallback baseCallback, Object[] arg) {
        if (baseCallback instanceof InnerFinishCallBack) {
            if (arg.length > 0) {
                Response response = (Response) arg[0];
                if (null != response) {
                    ((InnerFinishCallBack) baseCallback).downLoanFinish(response);
                }
            }
        }
    }


}
