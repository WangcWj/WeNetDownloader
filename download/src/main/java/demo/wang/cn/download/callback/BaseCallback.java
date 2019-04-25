package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public abstract  class BaseCallback implements WeLoaderCancelCallback,
        WeLoaderFailCallback,
        WeLoaderProgressCallback,
        WeLoaderStartCallback,WeLoaderFinishCallback,WeloaderBaseCallback {


    @Override
    public void downLoanCancel() {

    }

    @Override
    public void downLoanFail(Exception e) {

    }

    @Override
    public void downLoanFinish() {

    }

    @Override
    public void downLoanProgress(long read, long count, float percentage) {

    }

    @Override
    public void downLoanStart() {

    }
}
