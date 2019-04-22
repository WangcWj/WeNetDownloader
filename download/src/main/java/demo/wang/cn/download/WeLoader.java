package demo.wang.cn.download;


import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.intercepter.IntercepterFactory;
import demo.wang.cn.download.request.WeLoaderRequest;
import demo.wang.cn.download.response.WeLoaderResponse;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoader implements WeLifeCircle {

    private OkHttpClient okHttpClient;
    private WeLoaderRequest mWeRequest;
    private WeLoaderResponse mWeLoaderResponse;
    private Call mOkCall;
    private boolean isDestroy = false;


    public WeLoader(Builder builder) {
        okHttpClient = builder.okHttpClient;
        mWeRequest = builder.weRequest;
        mWeLoaderResponse = builder.weLoaderResponse;
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
        if (null != mOkCall && !mOkCall.isCanceled() && !mOkCall.isExecuted()) {
            mOkCall.cancel();
            mWeLoaderResponse.noticeCancelListener();
        }
    }

    private void request(long startPoint) {
        Request request = mWeRequest.createRequest(startPoint);
        mOkCall = okHttpClient.newCall(request);
        mOkCall.enqueue(mWeLoaderResponse);
        mWeLoaderResponse.noticeStartListener();
    }

    @Override
    public void create() {
        //还没想到这个方法可以用来弄啥
        isDestroy = false;
    }

    @Override
    public void destroy(){
        isDestroy = true;
        mWeLoaderResponse.destroy();
        mWeRequest.destroy();
        cancel();
        okHttpClient = null;
        mOkCall = null;
    }

    public static class Builder {
        private OkHttpClient.Builder builder;
        private WeLoaderRequest weRequest;
        private WeLoaderResponse weLoaderResponse;
        private OkHttpClient okHttpClient;

        public Builder() {
            weRequest = new WeLoaderRequest();
            weLoaderResponse = new WeLoaderResponse();
            builder = new OkHttpClient.Builder()
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .addNetworkInterceptor(IntercepterFactory.createProgressIntercepter(weLoaderResponse));
        }

        public Builder url(String url) {
            weRequest.setUrl(url);
            return this;
        }

        public Builder file(String filrPath) {
            weRequest.setFilePath(filrPath);
            return this;
        }

        public Builder file(File file) {
            weRequest.setTargetFile(file);
            return this;
        }

        public Builder addListener(BaseCallback baseCallback) {
            weLoaderResponse.setBaseCallback(baseCallback);
            return this;
        }

        public WeLoader build() {
            okHttpClient = builder.build();
            return new WeLoader(this);
        }
    }
}