package demo.wang.cn.downloanmanger;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/25
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
