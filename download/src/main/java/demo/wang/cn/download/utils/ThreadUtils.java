package demo.wang.cn.download.utils;


import demo.wang.cn.download.response.WeLoaderResponseImp;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/23
 */
public class ThreadUtils {


    public static Disposable runThread(boolean isRunIo, Runnable runnable) {
        return Flowable.just(runnable)
                .observeOn(Schedulers.io())
                .observeOn(isRunIo ? Schedulers.io() : AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Runnable>() {
                    @Override
                    public void accept(Runnable runnable) throws Exception {
                        runnable.run();
                    }
                });

    }

    public static Disposable runThread(Flowable<WeLoaderResponseImp.CallBackRunnable> flowable, boolean isRunIo, Runnable runnable) {
        return flowable
                .observeOn(Schedulers.io())
                .observeOn(isRunIo ? Schedulers.io() : AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Runnable>() {
                    @Override
                    public void accept(Runnable runnable) throws Exception {
                        runnable.run();
                    }
                });

    }

}
