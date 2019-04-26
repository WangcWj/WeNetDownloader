package demo.wang.cn.downloanmanger;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import demo.wang.cn.download.WeLoader;
import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.WeLoaderFailCallback;
import demo.wang.cn.download.callback.WeLoaderProgressCallback;
import demo.wang.cn.download.callback.WeLoaderStartCallback;

public class MainActivity extends AppCompatActivity {

    private String downloan = "https://alissl.ucdl.pp.uc.cn/fs08/2019/04/18/1/106_9f50c8679626bf1dfc5b142950466b41.apk?yingid=wdj_web&fname=QQ&pos=wdj_web%2Fdetail_normal_dl%2F0&appid=566489&packageid=200773575&apprd=566489&iconUrl=http%3A%2F%2Fandroid-artworks.25pp.com%2Ffs08%2F2019%2F04%2F22%2F1%2F110_e0307eec68bd5b09aff129f49b473c28_con.png&pkg=com.tencent.mobileqq&did=7392e93e39d5020003b750bb763c44a9&vcode=1024&md5=b7a1a1e14e8384aa5a3fe68eac03ed41";

    private TextView textView;
    WeLoader build;
    private String downloadName;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.progress);
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);
        if (i < 0) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.STORAGE}, 100);
        }

        findViewById(R.id.downloan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                setDownloan("/wang" + index);
            }
        });
    }

    private void setDownloan(String apkName) {
        String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        downloadName = apkName;
        String filePath = absolutePath + apkName + ".apk";
        if (null == build) {
            build = new WeLoader.Builder()
                    .url(downloan)
                    .file(filePath)
                    .addListener(baseCallback)
                    .build();
        }
        build.execute();
    }


    private BaseCallback baseCallback = new BaseCallback() {
        @Override
        public void downLoanProgress(long read, long count, float percentage) {
            setTextDesc(read, count);
        }

        @Override
        public void downLoanStart() {
            Log.e("WANG", "MainActivity.downLoanStart." + downloadName);
        }

        @Override
        public void downLoanFail(Exception e) {
            Log.e("WANG", "MainActivity.downLoanFail." + downloadName + e);
        }

        @Override
        public void downLoanCancel() {
            Log.e("WANG", "MainActivity.downLoanCancel." + downloadName);
        }

        @Override
        public void downLoanFinish() {
            Log.e("WANG", "MainActivity.downLoanFinish." + downloadName);
        }
    };

    private void setTextDesc(long read, long count) {

    }

    @Override
    protected void onDestroy() {
        build.destroy();
        super.onDestroy();
    }
}
