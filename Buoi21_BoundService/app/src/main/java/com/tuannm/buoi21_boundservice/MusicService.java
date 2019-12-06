package com.tuannm.buoi21_boundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private List<Song> mSongs;

    private IBinder mBinder;

    private int mCurrentIndex;

    private int mState;

    @Override
    public void onCreate() {
        super.onCreate();
        fakeSongsData();
        mBinder = new MusicBinder();
    }

    public int getState() {
        return mState;
    }

    private void fakeSongsData() {
        mSongs = new ArrayList<>();

        Song song1 = new Song("Chan Tinh", R.raw.chan_tinh);
        Song song2 = new Song("Mai Mai", R.raw.mai_mai);
        Song song3 = new Song("Nguoi Tinh Mua Dong", R.raw.nguoi_tinh_mua_dong);
        Song song4 = new Song("The Lazy Song", R.raw.the_layzy_song);
        Song song5 = new Song("Tinh Thoi Xot Xa", R.raw.tinh_thoi_xot_xa);
        Song song6 = new Song("Tuyet Roi Mua He", R.raw.tuyet_roi_mua_he);

        mSongs.add(song1);
        mSongs.add(song2);
        mSongs.add(song3);
        mSongs.add(song4);
        mSongs.add(song5);
        mSongs.add(song6);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

    public void playOrPauseSong() {

        /**
         * Khi nhac chua choi hoac da dung thi goi choi
         */
        if (mState == MediaState.IDLE || mState == MediaState.STOPPED) {
            Song song = getCurrentSong();

            MediaPlayer mediaPlayer = MediaPlayer.create(this, song.getResId());
            setMediaPlayer(mediaPlayer);
            mMediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);

            mState = MediaState.PLAYING;

            return;
        }

        /**
         * Khi nhac dang choi thi -> pause
         */
        if (mState == MediaState.PLAYING) {
            mMediaPlayer.pause();
            mState = MediaState.PAUSED;
            return;
        }

        /**
         * Dang Paused -> Playing
         * dang pause ma goi start() thi no choi tiep bai do.
         */
        mMediaPlayer.start();
        mState = MediaState.PLAYING;

    }

    private Song getCurrentSong() {
        return mSongs.get(mCurrentIndex);
    }

    public void nextSong() {
        if (mCurrentIndex < mSongs.size() - 1) {
            mCurrentIndex++;
        } else {
            mCurrentIndex = 0;
        }
        stopSong();
        playOrPauseSong();
    }

    private void stopSong() {
        mMediaPlayer.stop();
        mState = MediaState.STOPPED;
    }

    public void previousSong() {
        if (mCurrentIndex > 0) {
            mCurrentIndex--;
        } else {
            mCurrentIndex = mSongs.size() - 1;
        }
        stopSong();
        playOrPauseSong();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextSong();
    }


    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mState = MediaState.IDLE;
        }
        super.onDestroy();
    }
}
