package demo.wang.cn.download.intercepter;

import demo.wang.cn.download.callback.WeLoaderProgressListener;
import okhttp3.Interceptor;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoaderInterceptorFactory {

    public static Interceptor createProgressInterceptor(WeLoaderProgressListener weLoaderProgressListener) {
        return new WeLoaderProgressInterceptor(weLoaderProgressListener);
    }


}
