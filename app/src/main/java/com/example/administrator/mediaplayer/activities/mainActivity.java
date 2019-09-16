package com.example.administrator.mediaplayer.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.administrator.mediaplayer.R;
import com.example.administrator.mediaplayer.Seekbarflush;
import com.example.administrator.mediaplayer.adapters.VideolistAdapter;
import com.example.administrator.mediaplayer.entityClass.Video;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/7/8.
 */

public class mainActivity extends Activity {
    private VideolistAdapter videolistAdapter;
    private ListView listView;
    private EditText searchview;
    private Button button;
    private boolean a=true;
    List<Video> videos=new ArrayList();
    Bundle bundle;
    String scontent=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        listView = (ListView) findViewById(R.id.videolist);
        searchview=findViewById(R.id.searchtext);
        button=findViewById(R.id.Search);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        listView.requestFocus();
        listView.requestFocusFromTouch();
        listView.setSelector(R.drawable.listviewselector);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("onCreate", "获取列表");
                    linkVideo(i);
                }
            });


            listView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keycode, KeyEvent event) {
                    ListView vi = (ListView) view;
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        int i = listView.getSelectedItemPosition();
                        switch (keycode) {
                            case (KeyEvent.KEYCODE_DPAD_UP):
                                i = i - 1;
                                vi.setSelection(i);
                                if (i == 0 && !searchview.hasFocus()) {
                                    searchview.setFocusable(true);
                                }
                                break;
                            case (KeyEvent.KEYCODE_DPAD_DOWN):
                                i = i + 1;
                                vi.setSelection(i);
                                break;
                            case (KeyEvent.KEYCODE_ENTER):
                                if (0 == event.getRepeatCount()) {
                                    linkVideo(i);
                                }
                                break;
                        }
                    }
                    return false;
                }
            });

            searchview.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                    EditText ei = (EditText) view;
                    InputMethodManager imm = (InputMethodManager) ei.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && 0 == keyEvent.getRepeatCount()) {
                            ei.setFocusable(false);
                        } else if (keyEvent.getAction() == KeyEvent.ACTION_UP && 0 == keyEvent.getRepeatCount()) {
                            listView.setFocusable(true);
                        }
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    } else if (keycode == KeyEvent.KEYCODE_ENTER) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && 0 == keyEvent.getRepeatCount()) {
                            scontent = searchview.getText().toString();
                            videos.clear();
                            getVideos(scontent);
                            videolistAdapter = new VideolistAdapter(mainActivity.this, videos);
                            listView.setAdapter(videolistAdapter);
                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    } else if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    }
                    return false;
                }
            });


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scontent = searchview.getText().toString();
                    videos.clear();
                    getVideos(scontent);
                    videolistAdapter = new VideolistAdapter(mainActivity.this, videos);
                    listView.setAdapter(videolistAdapter);
                    Log.d("视频路径", "setOnClickListener");
                }
            });

    }

    protected void onStart(){
        super.onStart();
        if (a){
            getVideos(scontent);
            a=false;
        }
        videolistAdapter = new VideolistAdapter(mainActivity.this,videos);
        listView.setAdapter(videolistAdapter);
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
    }

    public String setTime(long total) {
        long hour = total / (1000 * 60 * 60);
        long letf1 = total % (1000 * 60 * 60);
        long minute = letf1 / (1000 * 60);
        long left2 = letf1 % (1000 * 60);
        long second = left2 / 1000;
        if (0 == hour) {
            return minute + "分" + second+"秒";
        } else {
            return hour + "小时" + minute + "分" + second+"秒";
        }
    }

    public String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }


    private void getVideos(String namey) {
        Cursor c = null;
        ContentResolver cr = mainActivity.this.getApplicationContext().getContentResolver();
        try {
            c = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 视频的id
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 视频名称
                String resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)); //分辨率
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));// 大小
                long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时长
                long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));//修改时间
                String videotime = setTime(duration);
                int videopath=c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String videosize=bytes2kb(size);
                Log.d("视频路径",c.getString(videopath));
                if (namey==null){
                    Video video = new Video(name, videotime, c.getString(videopath),videosize);
                    videos.add(video);
                }else if (namey.equals("")){
                    Video video = new Video(name, videotime, c.getString(videopath),videosize);
                    videos.add(video);
                }
                else if (name.indexOf(namey)!=-1){
                    Video video = new Video(name, videotime, c.getString(videopath),videosize);
                    videos.add(video);
                }else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public void linkVideo(int i){
        Intent intent = new Intent(mainActivity.this, VideoshowActivity.class);
        bundle = new Bundle();
        bundle.putCharSequence("name",videos.get(i).getVideoname());
        bundle.putCharSequence("path", videos.get(i).getLocalpath());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}