package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public interface WeLoaderProgressCallback extends WeloaderBaseCallback{

    void downLoanProgress(long read, long count, float percentage);
}
