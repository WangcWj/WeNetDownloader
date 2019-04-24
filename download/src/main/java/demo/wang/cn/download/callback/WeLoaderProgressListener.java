package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public interface WeLoaderProgressListener extends WeloaderBaseCallback{

    void progress(long count, long read, boolean isFinish);

}
