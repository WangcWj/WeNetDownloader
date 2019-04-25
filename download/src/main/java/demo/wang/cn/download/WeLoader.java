package demo.wang.cn.download;

import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import demo.wang.cn.download.callback.InnerFinishCallBack;
import demo.wang.cn.download.callback.WeLoaderProgressListener;
import demo.wang.cn.download.callback.WeloaderBaseCallback;
import demo.wang.cn.download.constant.WeLoaderConstant;
import demo.wang.cn.download.intercepter.WeLoaderInterceptorFactory;
import demo.wang.cn.download.lifecircle.WeLoaderLifeCircle;
import demo.wang.cn.download.request.WeLoaderRequest;
import demo.wang.cn.download.request.WeLoaderRequestImp;
import demo.wang.cn.download.response.WeLoaderResponse;
import demo.wang.cn.download.response.WeLoaderResponseImp;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoader implements WeLoaderLifeCircle, InnerFinishCallBack {

    private OkHttpClient okHttpClient;
    private WeLoaderRequest mWeRequest;
    private WeLoaderResponse mWeLoaderResponse;
    private Call mOkCall;
    private boolean isDestroy = false;


    public WeLoader(Builder builder) {
        okHttpClient = builder.okHttpClient;
        mWeRequest = builder.weRequest;
        mWeLoaderResponse = builder.weLoaderResponse;
        mWeLoaderResponse.setBaseCallback(this);
        create();
    }

    public void execute() {
        request(0);
    }

    public void keepOn() {
        request(mWeLoaderResponse.getBreakPoint());
    }

    public void stop() {
        cancel();
    }

    private void cancel() {
        if (null != mOkCall && !mOkCall.isCanceled()) {
            mOkCall.cancel();
            mWeLoaderResponse.notifyCallBack(WeLoaderConstant.CANCEL_INS);
        }
    }

    private void request(long startPoint) {
        Request request = mWeRequest.createRequest(startPoint);
        mOkCall = okHttpClient.newCall(request);
        mWeLoaderResponse.setStart(true);
        mOkCall.enqueue(mWeLoaderResponse);
    }

    /**
     * 运行环境: 子线程运行.
     *
     * @param response
     */
    @Override
    public void downLoanFinish(Response response) {
        File targetFile = mWeRequest.getTargetFile();
        mWeLoaderResponse.saveFile(mWeLoaderResponse.getBreakPoint(), targetFile, response);
    }

    @Override
    public void create() {
        //还没想到这个方法可以用来弄啥
        isDestroy = false;
        mWeLoaderResponse.create();
        mWeRequest.create();
    }

    @Override
    public void destroy() {
        isDestroy = true;
        cancel();
        mWeLoaderResponse.destroy();
        mWeRequest.destroy();
        okHttpClient = null;
        mOkCall = null;
        mWeRequest = null;
        mWeLoaderResponse = null;
    }

    public static class Builder {
        private OkHttpClient.Builder builder;
        private WeLoaderRequest weRequest;
        private WeLoaderResponse weLoaderResponse;
        private OkHttpClient okHttpClient;

        public Builder() {
            weRequest = new WeLoaderRequestImp();
            weLoaderResponse = new WeLoaderResponseImp();
            builder = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .addNetworkInterceptor(WeLoaderInterceptorFactory.createProgressInterceptor((WeLoaderProgressListener) weLoaderResponse));
        }

        public Builder url(String url) {
            weRequest.setUrl(url);
            return this;
        }

        public Builder file(String filePath) {
            weRequest.setFilePath(filePath);
            return this;
        }

        public Builder file(File file) {
            weRequest.setTargetFile(file);
            return this;
        }

        public Builder addListener(WeloaderBaseCallback baseCallback) {
            weLoaderResponse.setBaseCallback(baseCallback);
            return this;
        }

        public WeLoader build() {
            okHttpClient = builder.build();
            return new WeLoader(this);
        }
    }
}