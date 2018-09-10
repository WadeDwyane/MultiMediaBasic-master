package com.ozil.mesut.rocketlauncher.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import ca.uol.aig.fftpack.RealDoubleFFT;

/**
 * @author kui.liu
 * @time 2018/6/28 11:18
 */
public class TenActivity extends BaseActivity {

    @BindView(R.id.btn_start_sound)
    Button mBtnPlay;
    @BindView(R.id.btn_stop_sound)
    Button mBtnStop;
    @BindView(R.id.btn_start_audioview)
    Button mBtnStartAudioView;
    @BindView(R.id.btn_stop_audioview)
    Button mBtnStopAudioView;
    @BindView(R.id.imv_audioview)
    ImageView mImageView;

    private boolean keepGoing;
    private int synth_frequnency = 440;
    private AudioTrack mAudioTrack;
    private int blockSize = 512;
    private boolean isRecording;
    private RealDoubleFFT transformer;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private AudioSynthesisTask mAudioSynthesisTask;
    private AudioViewTask mAudioViewTask;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        transformer = new RealDoubleFFT(blockSize);
        mBitmap = Bitmap.createBitmap(256, 100, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(20);
        mImageView.setImageBitmap(mBitmap);
    }

    @Override
    protected void initView() {
        mBtnStop.setEnabled(false);
//        mBtnStopAudioView.setEnabled(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ten;
    }

    @OnClick({R.id.btn_start_sound, R.id.btn_stop_sound, R.id.btn_start_audioview,
            R.id.btn_stop_audioview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_sound:
                keepGoing = true;
                mAudioSynthesisTask = new AudioSynthesisTask();
                mAudioSynthesisTask.execute();
                mBtnStop.setEnabled(true);
                mBtnPlay.setEnabled(false);
                break;
            case R.id.btn_stop_sound:
                keepGoing = false;
                mBtnPlay.setEnabled(true);
                mBtnStop.setEnabled(false);
                break;
            case R.id.btn_start_audioview:
                isRecording = true;
                mAudioViewTask = new AudioViewTask();
                mAudioViewTask.execute();
                break;
            case R.id.btn_stop_audioview:
                isRecording = false;
                if (null != mAudioViewTask) {
                    mAudioViewTask.cancel(true);
                }
                break;
        }
    }

    /**
     * 对输入音频实现音频频率可视化的异步任务
     */
    public class AudioViewTask extends AsyncTask<Void, double[], Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int sampleSize = 8000;
            int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            int bufferSize = AudioRecord.getMinBufferSize(sampleSize, channelConfiguration, audioFormat);

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleSize, channelConfiguration, audioFormat, bufferSize);

            //使用buffer接收来自AudioRecord的原始样本的PCM数据
            short[] buffer = new short[blockSize];
            //使用transformBuffer存储经过傅里叶转换的双精度数据
            double[] transformBuffer = new double[blockSize];

            audioRecord.startRecording();
            while (isRecording) {
                int bufferResult = audioRecord.read(buffer, 0, blockSize);
                for (int i = 0; i < blockSize && i < bufferResult; i++) {
                    //期望值在-1.0-1.0之间
                    transformBuffer[i] = (double) buffer[i] / 32768.0;
                }
                transformer.ft(transformBuffer);
                publishProgress(transformBuffer);
            }

            audioRecord.stop();
            return null;
        }

        @Override
        protected void onProgressUpdate(double[]... values) {
            mCanvas.drawColor(Color.BLACK);
            for (int i = 0; i < values[0].length; i++) {
                int x = i;
                int downY = (int) (100 - (values[0][i] * 10));
                int upY = 100;
                mCanvas.drawLine(x, downY, x, upY, mPaint);
            }
            mImageView.invalidate();
            super.onProgressUpdate(values);
        }
    }

    /**
     * 这是一个通过算法生成音频, 并使用AudioTrack来播放的异步任务
     */
    public class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int sampleRate = 11025;
            int minSize = AudioTrack.getMinBufferSize(sampleRate,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minSize,
                    AudioTrack.MODE_STREAM);

            mAudioTrack.play();

            /*short[] buffer = {8120, 9087, 2389, -1980, -11098, 7865, 4542,
                            2907, -19999, -23098, 8976, 3450, -18906, -2345,
                            12765, -8963, -203, -9054, 7863, 23986, 30987};*/

            //通过算法,生成一个正弦波的声音
            short[] buffer = new short[minSize];
            float angle_frequency = (float) (2 * Math.PI * synth_frequnency / sampleRate);
            float angle = 0;

            while (keepGoing) {
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (short) (Short.MAX_VALUE * ((float) Math.sin(angle)));
                    angle += angle_frequency;
                }
                mAudioTrack.write(buffer, 0, buffer.length);
            }

            return null;
        }
    }
}

