package com.example.administrator.mediaplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import com.example.administrator.mediaplayer.R;

/**
 * Created by Administrator on 2019/7/31.
 */

public class PictrueActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictrueview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(PictrueActivity.this,mainActivity.class);
                startActivity(intent);
                finish();
                Log.d("lujing","预加载");
            }
        },1500);
    }
}
