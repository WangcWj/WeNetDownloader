package demo.wang.cn.download;

import java.io.File;
import java.util.concurrent.TimeUnit;

import demo.wang.cn.download.callback.InnerFinishCallBack;
import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.WeLoaderCancelCallback;
import demo.wang.cn.download.callback.WeLoaderStartCallback;
import demo.wang.cn.download.intercepter.WeLoaderInterceptorFactory;
import demo.wang.cn.download.lifecircle.WeLoaderLifeCircle;
import demo.wang.cn.download.request.WeLoaderRequest;
import demo.wang.cn.download.response.WeLoaderResponse;
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
            mWeLoaderResponse.runMainThread(WeLoaderCancelCallback.class);
        }
    }

    private void request(long startPoint) {
        Request request = mWeRequest.createRequest(startPoint);
        mOkCall = okHttpClient.newCall(request);
        mOkCall.enqueue(mWeLoaderResponse);
        mWeLoaderResponse.runMainThread(WeLoaderStartCallback.class);
    }

    /**
     * 运行环境: 子线程运行.
     * @param response
     */
    @Override
    public void downLoanFinish(Response response) {
        File targetFile = mWeRequest.getTargetFile();
        mWeLoaderResponse.saveFile(mWeLoaderResponse.getBreakPoint(),targetFile,response);
    }

    @Override
    public void create() {
        //还没想到这个方法可以用来弄啥
        isDestroy = false;
    }

    @Override
    public void destroy() {
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
                    .addNetworkInterceptor(WeLoaderInterceptorFactory.createProgressInterceptor(weLoaderResponse));
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

        public Builder addAllListener(BaseCallback baseCallback) {
            weLoaderResponse.setAllCallback(baseCallback);
            return this;
        }

        public WeLoader build() {
            okHttpClient = builder.build();
            return new WeLoader(this);
        }
    }
}