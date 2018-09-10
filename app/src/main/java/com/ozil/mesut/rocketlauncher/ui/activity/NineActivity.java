package com.ozil.mesut.rocketlauncher.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.service.PlayAudioService;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author kui.liu
 * @time 2018/6/27 15:15
 */
public class NineActivity extends BaseActivity
        implements View.OnClickListener, MediaPlayer.OnCompletionListener, View.OnTouchListener {
    private static String TAG = "NineActivity";
    private static int RECORD_AUDIO_BY_INTENT_REQUEST_CODE = 0x001;

    @BindView(R.id.btn_use_inner_audio_player)
    Button mBtnInnerAudioPlayer;

    @BindView(R.id.view)
    View mView;

    @BindView(R.id.btn_playaudio)
    Button mBtnPlayAudio;

    private MediaPlayer mMediaPlayer;
    private int position;
    private PlayAudioService mPlayAudioService;
    private Intent mPlayAudioIntent;
    private MediaPlayer mMediaPlayer1;
    private ServiceConnection mServiceConnection;
    private Uri mAudioUri;
    private MediaPlayer mMediaPlayer2;

    @Override
    protected void initListener() {
        mMediaPlayer.setOnCompletionListener(this);
        mView.setOnTouchListener(this);
    }

    @Override
    protected void initData() {
        //启动媒体播放器
        mMediaPlayer = MediaPlayer.create(this, R.raw.flac_music);

        //创建和服务的连接
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mPlayAudioService = ((PlayAudioService.PlayAudioBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mPlayAudioService = null;
            }
        };

        //创建一个播放音乐的服务
        mPlayAudioIntent = new Intent(this, PlayAudioService.class);
    }

    @Override
    protected void initView() {
        mBtnPlayAudio.setEnabled(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_nine;
    }

    @OnClick({R.id.btn_use_inner_audio_player, R.id.btn_create_user_custom_player,
            R.id.btn_start_play, R.id.btn_stop_play, R.id.btn_visit_audio,
            R.id.btn_play_audio_in_service, R.id.btn_stop_audio_in_service, R.id.btn_have_fun,
            R.id.btn_captureaudio, R.id.btn_playaudio, R.id.btn_captureaudio_byaudiorecord,
            R.id.btn_playaudio_byaudiorecord})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_use_inner_audio_player:
                //通过意图打开内部的音频播放器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File audioFile = new File(Environment.getExternalStorageDirectory().getPath()
                        + "/white_noise.mp3");
                intent.setDataAndType(Uri.fromFile(audioFile), "audio/mp3");
                startActivity(intent);
                break;
            case R.id.btn_create_user_custom_player:
                //开始播放
                mMediaPlayer.start();
                break;
            case R.id.btn_start_play:
                mMediaPlayer.start();
                break;
            case R.id.btn_stop_play:
                mMediaPlayer.pause();
                break;
            case R.id.btn_visit_audio:
                visitAudio();
                break;
            case R.id.btn_play_audio_in_service:
                //开启播放音乐的服务
                startService(mPlayAudioIntent);
                bindService(mPlayAudioIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_stop_audio_in_service:
                unbindService(mServiceConnection);
                stopService(mPlayAudioIntent);
                break;
            case R.id.btn_have_fun:
                mPlayAudioService.haveFun();
                break;
            case R.id.btn_captureaudio:
                Intent intent1 = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent1, RECORD_AUDIO_BY_INTENT_REQUEST_CODE);
                break;
            case R.id.btn_playaudio:
                mMediaPlayer2 = MediaPlayer.create(this, mAudioUri);
                mMediaPlayer2.setOnCompletionListener(this);
                mMediaPlayer2.start();
                mBtnPlayAudio.setEnabled(true);
                break;
            case R.id.btn_captureaudio_byaudiorecord:
                //通过AudioRecord获取音频
                isStarted = true;
                startRecordAudio();
                break;
            case R.id.btn_playaudio_byaudiorecord:

                break;
        }
    }

    private boolean isStarted;
    private int blockSize = 1024 * 8;

    private void startRecordAudio() {
        int sampleRateInHz = 8000;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncondingFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfiguration, audioEncondingFormat);
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRateInHz, channelConfiguration, audioEncondingFormat, bufferSize);
        short[] buffer = new short[blockSize];
        audioRecord.startRecording();

        while (isStarted) {
            audioRecord.read(buffer, 0, blockSize);
        }
        audioRecord.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (RECORD_AUDIO_BY_INTENT_REQUEST_CODE == requestCode) {
                mAudioUri = data.getData();
                mBtnPlayAudio.setEnabled(true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void visitAudio() {
        String[] columns = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.IS_RINGTONE,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.IS_NOTIFICATION,
                MediaStore.Audio.Media.IS_ALARM
        };

        Cursor cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, null);
        int fileColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int displayColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
        int mimeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE);

        if (cursor.moveToFirst()) {
            String audioPath = cursor.getString(fileColumn);
            String mimeType = cursor.getString(mimeColumn);
            Log.i(TAG, "audioPath = " + audioPath + ", mimeType = " + mimeType);

            File file = new File(audioPath);
            //启动自带的音频播放器来播放
            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), mimeType);
            startActivity(intent);*/
            //运用MediaPlayer来播放
            mMediaPlayer1 = MediaPlayer.create(this, Uri.fromFile(file));
            mMediaPlayer1.start();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.equals(mMediaPlayer)) {
            mMediaPlayer.start();
            mMediaPlayer.seekTo(position);
        }

        if (mp.equals(mMediaPlayer2)) {
            mBtnPlayAudio.setEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        if (null != mMediaPlayer1) {
            mMediaPlayer1.stop();
            mMediaPlayer1.release();
        }

        if (null != mMediaPlayer2) {
            mMediaPlayer2.stop();
            mMediaPlayer2.release();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mMediaPlayer.isPlaying()) {
                position = (int) (event.getX() * mMediaPlayer.getDuration() / mView.getWidth());
                Log.i(TAG, "position = " + position);
                mMediaPlayer.seekTo(position);
            }
        }
        return true;
    }
}
