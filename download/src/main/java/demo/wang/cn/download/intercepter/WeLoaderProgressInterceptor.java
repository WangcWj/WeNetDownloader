package demo.wang.cn.download.intercepter;

import java.io.IOException;

import demo.wang.cn.download.callback.WeLoaderProgressListener;
import demo.wang.cn.download.response.WeLoaderProgressResponseBody;
import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoaderProgressInterceptor implements Interceptor {

    private WeLoaderProgressListener weLoaderProgressListener;

    public WeLoaderProgressInterceptor(WeLoaderProgressListener weLoaderProgressListener) {
        this.weLoaderProgressListener = weLoaderProgressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response proceed = chain.proceed(chain.request());
        return proceed.newBuilder().body(new WeLoaderProgressResponseBody(proceed.body(), weLoaderProgressListener)).build();
    }
}
