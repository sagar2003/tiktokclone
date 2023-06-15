package com.spy.tiktokclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.StorageReferenceUri;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<String> paths;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2=findViewById(R.id.viewpager);
        paths=new ArrayList<>();

        reference= FirebaseStorage.getInstance().getReference();

        reference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference ref : listResult.getItems()){
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            paths.add(uri.toString());
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager2.setAdapter(new adapter_video(viewPager2.getContext(),paths));
                    }
                },1000);
            }
        });



    }
}