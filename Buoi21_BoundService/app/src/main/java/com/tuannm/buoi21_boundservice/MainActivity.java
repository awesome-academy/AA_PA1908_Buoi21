package com.tuannm.buoi21_boundservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImagePlayPause;
    private ImageView mImageNext;
    private ImageView mImagePrevious;

    private MusicService mMusicService;
    private ServiceConnection mServiceConnection;

    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        registerListeners();
    }

    private void registerListeners() {
        mImagePlayPause.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
    }

    private void initComponents() {
        mImagePlayPause = findViewById(R.id.image_play_pause);
        mImageNext = findViewById(R.id.image_next);
        mImagePrevious = findViewById(R.id.image_previous);

        bindToService();
    }

    private void bindToService() {
        mServiceConnection = new ServiceConnection() {
            /**
             * Khi Activity bind (rang buoc) thanh cong voi MusicService thi phuong thuc
             * onServiceConnected se duoc goi.
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
                mMusicService = musicBinder.getService();
                mBound = true;
                Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
            }

            /**
             * Khi ngat ket noi voi Service thi phuong thuc nay duoc goi.
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBound = false;
                Toast.makeText(MainActivity.this, "Disconnected!", Toast.LENGTH_SHORT).show();
            }
        };

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_play_pause:
                playOrPauseSong();
                break;
            case R.id.image_next:
                nextSong();
                break;
            case R.id.image_previous:
                previousSong();
                break;
            default:
                break;
        }
    }

    private void previousSong() {
        if(!mBound){
            return;
        }
        mMusicService.previousSong();
    }

    private void nextSong() {
        if(!mBound){
            return;
        }

        mMusicService.nextSong();
    }

    private void playOrPauseSong() {
        if(!mBound){
            return;
        }

        mMusicService.playOrPauseSong();

        if(mMusicService.getState() == MediaState.PLAYING){
            mImagePlayPause.setImageLevel(1);
        }else {
            mImagePlayPause.setImageLevel(0);
        }
    }


    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        mBound = false;
        super.onDestroy();
    }
}
