package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public interface ProgressListener {

    void progress(long count, long read, boolean isFinish);

}
