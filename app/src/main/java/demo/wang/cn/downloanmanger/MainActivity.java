package demo.wang.cn.downloanmanger;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import demo.wang.cn.download.WeLoader;
import demo.wang.cn.download.callback.DownloadProgressCallback;

public class MainActivity extends AppCompatActivity implements DownloadProgressCallback {

    private String downloan = "http://ghost.25pp.com/soft/pppc_setup/wandoujia_3.0.1.3005_25pp_wdjgw_1530607868_setup.exe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int i = ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);
        if(i < 0){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission_group.STORAGE},100);
        }
        findViewById(R.id.downloan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeLoader build = new WeLoader.Builder()
                        .url(downloan)
                        .addListener(MainActivity.this)
                        .build();
                build.execute();

            }
        });
    }

    @Override
    public void downLoanProgress(long read, long count, float percentage) {
        Log.e("WANG","MainActivity.downLoanProgress.percentage = " +percentage +" read  =   "+read);
    }
}
