package demo.wang.cn.download.callback;

import okhttp3.Response;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public interface InnerFinishCallBack extends WeloaderBaseCallback {

    void downLoanFinish(Response response);
}
