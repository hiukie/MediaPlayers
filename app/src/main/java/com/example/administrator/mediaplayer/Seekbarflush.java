package com.example.administrator.mediaplayer;

import android.media.MediaPlayer;
import android.os.Message;
import android.util.Log;
import com.example.administrator.mediaplayer.activities.VideoshowActivity;

/**
 * Created by Administrator on 2019/7/25.
 */
public class Seekbarflush extends Thread{
    private MediaPlayer mediaPlayer;
    private VideoshowActivity activity;
    private boolean pause;
    private final Object lock = new Object();


    public Seekbarflush(MediaPlayer mediaPlayer,VideoshowActivity activity){
        this.mediaPlayer=mediaPlayer;
        this.activity=activity;
    }
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            if(mediaPlayer.getCurrentPosition()==mediaPlayer.getDuration())
            {
                return;
            }
            Message message = new Message();
            message.what =1;
            activity.myHandler.sendMessage(message);
            try
            {
                Thread.sleep(1000);
                Log.d("thread","线程！！！！！！！！！");
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }
}
