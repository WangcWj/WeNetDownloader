package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public abstract class BaseCallback implements WeloaderBaseCallback,
        WeLoaderCancelCallback,
        WeLoaderFailCallback,
        WeLoaderProgressCallback,
        WeLoaderStartCallback,WeLoaderFinishCallback {

    public static final String PROGRESS_INS = "downLoanProgress";
    public static final String FAIL_INS = "downLoanFail";
    public static final String CANCEL_INS = "downLoanCancel";
    public static final String START_INS = "downLoanStart";
    public static final String DOWNLOAN_FINISH_INS = "downLoanFinish";
    public static final String INNER_FINISH_INS = "innerFinish";
    public static final String ALL = "all";

    @Override
    public void downLoanProgress(long read, long count, float percentage) {

    }

    @Override
    public void downLoanFail(Exception e) {

    }

    @Override
    public void downLoanCancel() {

    }

    @Override
    public void downLoanStart() {

    }

    @Override
    public void downLoanFinish() {

    }
}
