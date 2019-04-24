package demo.wang.cn.download.callback;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/22
 */
public abstract class BaseCallback {

    public static final String FILE_FINISH_INS = "saveFileFinish";
    public static final String PROGRESS_INS = "downLoanProgress";
    public static final String FAIL_INS = "downLoanFail";
    public static final String CANCEL_INS = "downLoanCancel";
    public static final String START_INS = "downLoanStart";
    public static final String DOWNLOAN_FINISH_INS = "downLoanFinish";

    protected void saveFileFinish() {

    }

    protected void downLoanProgress(long read, long count, float percentage) {

    }

    protected void downLoanFail(Exception e) {

    }

    protected void downLoanCancel() {

    }

    protected void downLoanStart() {

    }

    protected void downLoanFinish() {

    }
}
