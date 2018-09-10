package com.ozil.mesut.rocketlauncher.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ozil.mesut.rocketlauncher.R;


/**
 * @author kui.liu
 * @time 2018/6/27 17:00
 * @desc 这是一个后台播放音频的服务
 */
public class PlayAudioService extends Service implements MediaPlayer.OnCompletionListener {
    public static String TAG = "PlayAudioService";
    private MediaPlayer mMediaPlayer;

    private IBinder mIBinder = new PlayAudioBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "playaudioservice onBind");
        return mIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "playaudioservice onCreated");
        mMediaPlayer = MediaPlayer.create(this, R.raw.flac_music);
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "playaudioservice onStartCommand");
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //服务停止
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "playaudioservice onDestroy");
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        super.onDestroy();
    }

    public class PlayAudioBinder extends Binder {
        public PlayAudioService getService() {
            return PlayAudioService.this;
        }
    }

    public void haveFun() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            //将音频回播2.5秒
            mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 2500);
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.i(TAG, "playaudioservice unbindService");
        super.unbindService(conn);
    }
}
