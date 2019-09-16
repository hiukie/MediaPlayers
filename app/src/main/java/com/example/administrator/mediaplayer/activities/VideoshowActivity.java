package com.example.administrator.mediaplayer.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.mediaplayer.R;
import com.example.administrator.mediaplayer.Seekbarflush;

import java.io.IOException;

/**
 * Created by Administrator on 2019/7/9.
 */

public class VideoshowActivity extends Activity {
    TextView videofullname1;
    Button play1;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    SeekBar seekBar;

    private boolean noplay=true;//视频不在播放
    private boolean novisible;//按钮不可见
    private boolean isplaying=false;//是否在视频播放中（包括播到一半暂停）
    private boolean isstart=false;
    private boolean seekBarT;
    private float downX,downY;

    private MediaPlayer mediaPlayer;
    private int screenWidth;
    private String name,path;
    private Seekbarflush seekbarflush;
    public Handler myHandler;
    ContentResolver cr;
    WindowManager.LayoutParams lp ;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoshow);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name=bundle.getString("name");
        path=bundle.getString("path");
// --------------------------------------------------------------------------------------------
        play1=(Button) findViewById(R.id.playvideo);
        seekBar=(SeekBar)findViewById(R.id.seekBar2);
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        videofullname1=(TextView)findViewById(R.id.videofullname);
        cr = this.getContentResolver();
        surfaceHolder=surfaceView.getHolder();
        mediaPlayer=new MediaPlayer();
        seekbarflush=new Seekbarflush(mediaPlayer,this);
        lp = getWindow().getAttributes();
        myHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case (1):
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        seekBar.setMax(mediaPlayer.getDuration());
                        Log.d("位置", String.valueOf(mediaPlayer.getCurrentPosition()));
                        break;
                }
            }
        };
//-----------------------------------------------------------------------------------------------
        videofullname1.setText(name);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        play1.bringToFront();
        videofullname1.bringToFront();
        seekBar.setProgress(0);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
//-----------------------------------------------------------------------------------------------//
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBarT){
                    if (mediaPlayer!=null){
                        mediaPlayer.seekTo(i);
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
                seekBarT=true;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (noplay==false){
                    mediaPlayer.start();
                }
                seekBarT=false;
            }
        });
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case (MotionEvent.ACTION_DOWN):
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        if (noplay==false&&novisible==false){
                            play1.setVisibility(View.VISIBLE);
                            videofullname1.setVisibility(View.VISIBLE);
                            seekBar.setVisibility(View.VISIBLE);
                            novisible=true;
                        }else if (noplay==false&&novisible==true){
                            play1.setVisibility(View.GONE);
                            videofullname1.setVisibility(View.GONE);
                            seekBar.setVisibility(View.GONE);
                            novisible=false;
                        }
                        break;
                    case(MotionEvent.ACTION_MOVE):
                        float distanceX = motionEvent.getX() - downX;
                        float distanceY = motionEvent.getY() - downY;

                            if (downX > screenWidth - 200 && Math.abs(distanceX) < 50 && distanceY > 50)
                            {
                                // 减小音量
                                setVolume(false);
                            }
                            else if (downX > screenWidth - 200 && Math.abs(distanceX) < 50 && distanceY < -50)
                            {
                                //  增加音量
                                setVolume(true);
                            }
//----------------------------------------------------------------------------------------------------------

                        if (downX < screenWidth - 550 && distanceY<-100 && Math.abs(distanceX)<50) {
                            if (lp.screenBrightness<1){
                                lp.screenBrightness= (float) (lp.screenBrightness+0.02);
                                getWindow().setAttributes(lp);
                            }else if(lp.screenBrightness>=1) {
                                lp.screenBrightness=1;
                                getWindow().setAttributes(lp);
                            }else if (lp.screenBrightness<=0){
                                lp.screenBrightness=1;
                                getWindow().setAttributes(lp);
                            }
                            float a=lp.screenBrightness;
                            Log.d("", "亮度增加"+String.valueOf(a));
                        }
                        if (downX < screenWidth - 550 && Math.abs(distanceX)<50 && distanceY>100 ) {
                            if (lp.screenBrightness>0){
                                lp.screenBrightness=(float) (lp.screenBrightness-0.02);
                                getWindow().setAttributes(lp);
                            }else if (lp.screenBrightness<=0){
                                lp.screenBrightness=0;
                                getWindow().setAttributes(lp);
                            }
                            float a=lp.screenBrightness;
                            Log.d("","亮度减少"+String.valueOf(a));
                        }


//----------------------------------------------------------------------------------------------------------


                        //  播放进度调节
                            if (Math.abs(distanceY) < 50 && distanceX > 100)
                            {
                                // 快进
                                int currentT = mediaPlayer.getCurrentPosition();//播放的位置
                                mediaPlayer.seekTo(currentT + 15000);
                                downX = motionEvent.getX();
                                downY = motionEvent.getY();
                            }
                            else if (Math.abs(distanceY) < 50
                                    && distanceX < -100)
                            {
                                // 快退
                                int currentT = mediaPlayer.getCurrentPosition();
                                mediaPlayer.seekTo(currentT - 15000);
                                downX = motionEvent.getX();
                                downY = motionEvent.getY();
                            }
                        break;
                }
                return true;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                noplay=true;
                isplaying=false;
                play1.setVisibility(View.VISIBLE);
                play1.setBackgroundResource(R.drawable.play);
            }
        });

        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noplay == true) {//播放按键
                    play1.setBackgroundResource(R.drawable.pause);
                    noplay=false;
                    if (isplaying==false){
                        play();
                        isplaying=true;
                    }else if (isplaying==true){
                        mediaPlayer.start();
                    }
                    play1.setVisibility(View.GONE);
                    videofullname1.setVisibility(View.INVISIBLE);
                    seekBar.setVisibility(View.INVISIBLE);

                }else if(noplay == false){//暂停按键
                    play1.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                    noplay=true;
                    novisible=false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        noplay=true;
        isplaying=false;
        play1.setVisibility(View.VISIBLE);
        play1.setBackgroundResource(R.drawable.play);
        mediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        seekbarflush.interrupt();
    }

    public void play(){
        mediaPlayer.reset();
        mediaPlayer.setDisplay(surfaceHolder);
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            if (!isstart){
                seekbarflush.start();
                isstart=true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void setVolume(boolean flag)
    {
        // 获取音量管理器
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 获取当前音量
        long curretnV = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (flag)
        {
            manager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
        }
        else
        {
            manager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
        }
    }
}
