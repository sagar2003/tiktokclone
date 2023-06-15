package com.spy.tiktokclone;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class adapter_video extends RecyclerView.Adapter<adapter_video.ViewHolder> {

    Context context;
    ArrayList<String> paths;

    public adapter_video(Context context, ArrayList<String> paths){
        this.context=context;
        this.paths=paths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_videos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.videoView.setVideoURI(Uri.parse(paths.get(holder.getAdapterPosition())));
        holder.p.setVisibility(View.VISIBLE);
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        holder.p.setVisibility(View.INVISIBLE);
                        mp.setLooping(true);
                        mp.start();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        VideoView videoView;
        ProgressBar p;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView=itemView.findViewById(R.id.videoView);
            p=itemView.findViewById(R.id.progressbar);
        }
    }


}
