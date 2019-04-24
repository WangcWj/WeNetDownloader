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

import java.util.HashMap;
import java.util.Map;

import demo.wang.cn.download.WeLoader;
import demo.wang.cn.download.callback.BaseCallback;
import demo.wang.cn.download.callback.WeLoaderFinishCallback;
import demo.wang.cn.download.callback.WeLoaderProgressCallback;
import demo.wang.cn.download.callback.WeloaderBaseCallback;

public class MainActivity extends AppCompatActivity implements BaseCallback {

    private String downloan = "https://alissl.ucdl.pp.uc.cn/fs01/union_pack/Wandoujia_844461_web_seo_baidu_homepage.apk";

    private TextView textView;

    private Map<String,String> mp = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.progress);
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);
        if(i < 0){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission_group.STORAGE},100);
        }
        mp.put("wangc","chao");
        mp.put("wang2","chao");
        mp.put("wang3","chao");
        String trim = mp.toString().replace(",", "&").replace("{", "").replace(" ","").replace("}", "").trim();

        Log.e("WANG","MainActivity.onCreate."+trim );

        findViewById(R.id.downloan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String absolutePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                String filePath = absolutePath +"/wang.apk";
                WeLoader build = new WeLoader.Builder()
                        .url(downloan)
                        .file(filePath)
                        .addAllListener(MainActivity.this)
                        .build();
              //  build.execute();

            }
        });
    }

    @Override
    public void downLoanProgress(long read, long count, float percentage) {
        textView.setText("count= "+ count+", read ="+read);
    }

    @Override
    public void downLoanFinish() {
        Log.e("WANG","MainActivity.downLoanFinish." );
    }
}
