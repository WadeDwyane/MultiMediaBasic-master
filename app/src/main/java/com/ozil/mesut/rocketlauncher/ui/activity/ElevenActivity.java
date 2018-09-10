package com.ozil.mesut.rocketlauncher.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author kui.liu
 * @time 2018/6/28 17:28
 */
public class ElevenActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaController.MediaPlayerControl {

    private static final String TAG = "ElevenActivity";
    @BindView(R.id.videoview)
    VideoView mVideoView;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;

    private String mPath;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;
    private Display mDisplay;
    private MediaController mMediaController;

    @Override
    protected void initListener() {
        //为VideoView设置一个控制器
        mVideoView.setMediaController(new MediaController(this));
        mSurfaceHolder.addCallback(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
    }

    @Override
    protected void initData() {
        mPath = Environment.getExternalStorageDirectory().getPath()
                + "/running.mp4";
        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        //确保表面是一个推送缓冲区表面, 用于视频播放和摄像头预览
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mMediaPlayer = new MediaPlayer();
        mDisplay = getWindowManager().getDefaultDisplay();

        mMediaController = new MediaController(this);
        mMediaController.setMediaPlayer(this);
        mMediaController.setAnchorView(findViewById(R.id.main_view));
        mMediaController.setEnabled(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_eleven;
    }

    @OnClick({R.id.btn_playvideo_by_intent, R.id.btn_playvideo_by_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_playvideo_by_intent:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(mPath)), "video/mp4");
                startActivity(intent);
                break;
            case R.id.btn_playvideo_by_video:
                mVideoView.setVideoURI(Uri.fromFile(new File(mPath)));
                mVideoView.start();
                break;
            case R.id.btn_playvideo_by_mediaplayer:
                //调用start来开始播放
                mMediaPlayer.start();
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        //指定MediaPlayer使用该表面进行播放
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        }else {
            mMediaController.show();
        }
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion called");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //mediaplayer进入准备就绪状态
        Log.i(TAG, "onPrepared called");
        //获取视频宽度
        int videoWidth = mp.getVideoWidth();
        //获取视频高度
        int videoHeight = mp.getVideoHeight();

        //为视频设置合理的宽高
        if (videoWidth > mDisplay.getWidth() ||
                videoHeight > mDisplay.getHeight()) {
            float widthRatio = (float) videoWidth / (float) mDisplay.getWidth();
            float heightRatio = (float) videoHeight / (float) mDisplay.getHeight();

            if (widthRatio > 1 || heightRatio > 1) {
                if (heightRatio > widthRatio) {
                    videoWidth = (int) Math.ceil((float) videoWidth / heightRatio);
                    videoHeight = (int) Math.ceil((float) videoHeight / heightRatio);
                } else {
                    videoWidth = (int) Math.ceil((float) videoWidth / widthRatio);
                    videoHeight = (int) Math.ceil((float) videoHeight / widthRatio);
                }
            }
        }
        mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.i(TAG, "onSeekComplete called");
    }

    @Override
    public void start() {
        mMediaController.show();
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
