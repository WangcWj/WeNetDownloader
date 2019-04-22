package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public interface DownloadFailCallback extends BaseCallback {

    void downloanFail(Exception e);
}
