package com.spy.tiktokclone;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class adapter_video extends RecyclerView.Adapter<adapter_video.ViewHolder> {

    Context context;
    ArrayList<StorageReference> paths;
    StorageReference reference;
    MediaPlayer mp;
    File file;
    FileWriter writer;

    public adapter_video(Context context, ArrayList<StorageReference> paths){
        this.context=context;
        this.paths=paths;
        this.mp=new MediaPlayer();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_videos,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        reference = paths.get(holder.getAdapterPosition());
        file = new File(context.getExternalCacheDir().getPath()+"/"+reference.getName());
        if(file.length()>0){
            holder.videoView.setVideoPath(file.getPath());
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    holder.p.setVisibility(View.INVISIBLE);
                    if(!holder.videoView.isPlaying()){
                        holder.videoView.start();
                    }
                }
            });
        }else{
            reference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    holder.videoView.setVideoPath(file.getPath());
                    holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            holder.p.setVisibility(View.INVISIBLE);
                            if(!holder.videoView.isPlaying()){
                                holder.videoView.start();
                            }
                        }
                    });

                    if(paths.size()>holder.getAdapterPosition()+1){
                        reference = paths.get(holder.getAdapterPosition()+1);
                        file = new File(context.getExternalCacheDir().getPath()+"/"+reference.getName());
                        if(!file.exists()){
                            reference.getFile(file);
                        }
                    }
                }
            });
        }


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
