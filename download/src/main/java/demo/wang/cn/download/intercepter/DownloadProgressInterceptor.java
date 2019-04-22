package demo.wang.cn.download.intercepter;

import android.util.Log;

import java.io.IOException;

import demo.wang.cn.download.callback.ProgressListener;
import demo.wang.cn.download.response.DownloadProgressResponseBody;
import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class DownloadProgressInterceptor implements Interceptor {

    private ProgressListener progressListener;

    public DownloadProgressInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response proceed = chain.proceed(chain.request());
        return proceed.newBuilder().body(new DownloadProgressResponseBody(proceed.body(), progressListener)).build();
    }
}
