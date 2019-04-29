package demo.wang.cn.download.callback;

import java.io.IOException;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public interface WeLoaderProgressListener extends WeloaderBaseCallback{

    void progress(long count, long read, boolean isFinish);

    void progressException(IOException e);

}
