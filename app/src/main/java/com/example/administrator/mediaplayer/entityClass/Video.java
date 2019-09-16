package com.example.administrator.mediaplayer.entityClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2019/7/9.
 */

public class Video {
    private String videoname;
    private String localpath;
    private String videotime;
    private String videosize;
    private Bitmap bitmap;
    private int high;
    private int width;
    MediaMetadataRetriever media = new MediaMetadataRetriever();

    public Video(String videoname,  String videotime,String localpath,String videosize){
        this.videoname=videoname;
        this.videotime=videotime;
        this.localpath=localpath;
        this.videosize=videosize;
//        this.high=high;
//        this.width=width;
        this.bitmap=getvideopicture();
    }

    public String getVideosize() {
        return videosize;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getLocalpath() {
        return localpath;
    }

    public void setLocalpath(){
        this.localpath=localpath;
    }

    public String getVideotime() {
        return videotime;
    }

    public void setVideotime(String videotime) {
        this.videotime = videotime;
    }

    public int getHigh() {
        return high;
    }

    public int getWidth() {
        return width;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public Bitmap getvideopicture(){
        media.setDataSource(localpath);
        bitmap = media.getFrameAtTime();
        high=bitmap.getHeight();
        width=bitmap.getWidth();
        return bitmap;
    }
}
