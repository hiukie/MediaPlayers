package com.example.administrator.mediaplayer.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.mediaplayer.R;
import com.example.administrator.mediaplayer.entityClass.Video;
import java.util.List;


/**
 * Created by Administrator on 2019/7/9.
 */


public class VideolistAdapter extends BaseAdapter {
    private List<Video>videoList;
    private Context context;
    private List <Bitmap> pictureList;
    int defaultSelection;


    public VideolistAdapter(Context context, List<Video>videoList){
        this.context=context;
        this.videoList=videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int i) {
        return videoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
         view= LayoutInflater.from(context).inflate(R.layout.videoitem,null);
        ((ImageView)view.findViewById(R.id.videopicture)).setImageBitmap(videoList.get(i).getBitmap());
//        ((ImageView)view.findViewById(R.id.userpicture)).setImageResource(R.drawable.people);
        ((TextView)view.findViewById(R.id.Videonamey)).setText(videoList.get(i).getVideoname());
        ((TextView)view.findViewById(R.id.videocapcity)).setText(videoList.get(i).getVideosize());
        ((TextView)view.findViewById(R.id.videotime)).setText(videoList.get(i).getVideotime());
        return view;
    }
}
