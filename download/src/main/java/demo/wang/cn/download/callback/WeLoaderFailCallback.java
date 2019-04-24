package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public interface WeLoaderFailCallback extends WeloaderBaseCallback {

    void downLoanFail(Exception e);
}
