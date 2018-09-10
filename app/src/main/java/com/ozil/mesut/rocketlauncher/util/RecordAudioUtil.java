package com.ozil.mesut.rocketlauncher.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/21 10:03
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class RecordAudioUtil {

    //设置录音文件的临时目录
    private static final String TMP_FOLDER_NAME = "AnWindEar";
    //缓存文件的目录
    public static String cachePCMFolder;
    public static RecordAudioUtil instance = new RecordAudioUtil();
    private static final String TAG = "RecordAudioUtil";
    //wav文件的路径
    public String wavFilePath;
    //临时的PCM文件
    public File tmpPCMFile;
    //临时的wav文件
    public File tmpWavFile;
    //执行录音的线程
    public Thread mRecordThread;

    //当前状态是空闲状态
    public volatile WindState state = WindState.IDLE;

    private static final int RECORD_AUDIO_BUFFER_TIMES = 1;
    private static final int PLAY_AUDIO_BUFFER_TIMES = 1;
    private static final int AUDIO_FREQUENCY = 44100;

    private static final int RECORD_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    private static final int PLAY_CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private OnStateListener onStateListener;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 表示当前状态
     */
    public enum WindState {
        ERROR,
        IDLE,
        RECORDING,
        STOP_RECORD,
        PLAYING,
        STOP_PLAY
    }

    public interface OnStateListener {
        void onStateChanged(WindState currentState);
    }

    //单例模式
    public static RecordAudioUtil getInstance() {
        if (null == instance) {
            instance = new RecordAudioUtil();
        }
        return instance;
    }

    /*//初始化目录
    public static void init() {
        cachePCMFolder = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + TMP_FOLDER_NAME;
        File folder = new File(cachePCMFolder);
        if (!folder.exists()) {
            boolean f = folder.mkdirs();
            Log.i(TAG, String.format(Locale.CHINA, "PCM目录:%s->%d", cachePCMFolder, f));
        } else {
            for (File file : folder.listFiles()) {
                boolean delete = file.delete();
                Log.i(TAG, String.format(Locale.CHINA, "删除PCM文件:%s->%d", file.getName(), delete));
            }
            Log.i(TAG, String.format(Locale.CHINA, "PCM目录:%s", cachePCMFolder));
        }

        //wav文件
        File wavFile = new File(cachePCMFolder);
        if (!wavFile.exists()) {
            boolean mkdirs = wavFile.mkdirs();
            Log.i(TAG, String.format(Locale.CHINA, "wav目录:%s->%d", cachePCMFolder, mkdirs));
        } else {
            Log.i(TAG, String.format(Locale.CHINA, "wav目录:%s", cachePCMFolder));
        }
    }*/

    /**
     * 开始录音
     * @param createWav
     */
    public synchronized void startRecordAudio(boolean createWav) {
        cachePCMFolder = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + TMP_FOLDER_NAME;
        File folder = new File(cachePCMFolder);
        if (!folder.exists()) {
            boolean f = folder.mkdirs();
            Log.i(TAG, String.format(Locale.CHINA, "PCM目录:%s->%d", cachePCMFolder, f));
        } else {
            for (File file : folder.listFiles()) {
                boolean delete = file.delete();
                Log.i(TAG, String.format(Locale.CHINA, "删除PCM文件:%s->%d", file.getName(), delete));
            }
            Log.i(TAG, String.format(Locale.CHINA, "PCM目录:%s", cachePCMFolder));
        }

        //wav文件
        File wavFile = new File(cachePCMFolder);
        if (!wavFile.exists()) {
            boolean mkdirs = wavFile.mkdirs();
            Log.i(TAG, String.format(Locale.CHINA, "wav目录:%s->%d", cachePCMFolder, mkdirs));
        } else {
            Log.i(TAG, String.format(Locale.CHINA, "wav目录:%s", cachePCMFolder));
        }

        if (!state.equals(WindState.IDLE)) {
            Log.w(TAG, "无法开始录制,当前状态为" + state);
            return;
        }
        try {
            tmpPCMFile = File.createTempFile("recording", ".pcm", new File(cachePCMFolder));
            if (createWav) {
                SimpleDateFormat sdf = new SimpleDateFormat("yymmdd_HHmmss", Locale.CHINA);
                tmpWavFile = new File(cachePCMFolder
                        + File.separator + "r" + sdf.format(new Date()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (null != mRecordThread) {
            mRecordThread.interrupt();
            mRecordThread = null;
        }

        mRecordThread = new RecordAudioThread(createWav);
        mRecordThread.start();
    }

    /**
     * 停止录音
     */
    public synchronized void stopRecord() {
        if (!state.equals(WindState.RECORDING)) {
            return;
        }
        state = WindState.STOP_RECORD;
        notifyState(state);
    }

    /**
     * 播放录制好的PCM文件
     */
    public synchronized void startPlayPCM() {
        if (!isIdle()) {
            return;
        }
        new AudioTrackPlayThread(tmpPCMFile).start();
    }

    /**
     * 播放录制好的wav文件
     */
    public synchronized void startPlayWav() {
        if (!isIdle()) {
            return;
        }
        new AudioTrackPlayThread(tmpWavFile).start();
    }

    /**
     * 停止播放
     */
    public synchronized void stopPlay() {
        if (!state.equals(WindState.PLAYING)) {
            return;
        }
        state = WindState.STOP_PLAY;
    }

    /**
     * 判断当前状态是否是空闲
     *
     * @return
     */
    public synchronized boolean isIdle() {
        return WindState.IDLE.equals(state);
    }

    /**
     * 录音的线程
     */
    public class RecordAudioThread extends Thread {
        AudioRecord mRecord;
        boolean createWav = false;
        int bufferSize = 10240;

        public RecordAudioThread(boolean createWav) {
            this.createWav = createWav;
            bufferSize = AudioRecord.getMinBufferSize(AUDIO_FREQUENCY,
                    RECORD_CHANNEL_CONFIG, AUDIO_ENCODING) * RECORD_AUDIO_BUFFER_TIMES;
            Log.i(TAG, "bufferSize = " + bufferSize);
            mRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, AUDIO_FREQUENCY,
                    RECORD_CHANNEL_CONFIG, AUDIO_ENCODING, bufferSize);
        }

        @Override
        public void run() {
            state = WindState.RECORDING;
            notifyState(state);
            Log.i(TAG, "录音开始");
            try {
                //这里选择是FileOutputStream,而不是DataOutputStream
                FileOutputStream pcmFos = new FileOutputStream(tmpPCMFile);
                FileOutputStream wavFos = new FileOutputStream(tmpWavFile);
                if (createWav) {
                    writeWavFileHeader(wavFos, bufferSize, AUDIO_FREQUENCY, mRecord.getChannelCount());
                }
                mRecord.startRecording();
                byte[] byteBuffer = new byte[bufferSize];
                while (state.equals(WindState.RECORDING) && !isInterrupted()) {
                    int end = mRecord.read(byteBuffer, 0, byteBuffer.length);
                    pcmFos.write(byteBuffer, 0, end);
                    pcmFos.flush();
                    if (createWav) {
                        wavFos.write(byteBuffer, 0, end);
                        wavFos.flush();
                    }
                }
                //录制结束
                mRecord.stop();
                pcmFos.close();
                wavFos.close();
                if (createWav) {
                    //修改Header
                    RandomAccessFile raf = new RandomAccessFile(tmpWavFile, "rw");
                    byte[] wavHeader = generateWavFileHeader(tmpPCMFile.length(),
                            AUDIO_FREQUENCY, mRecord.getChannelCount());
                    raf.seek(0);
                    raf.write(wavHeader);
                    raf.close();
                    Log.i(TAG, "tmpWavFile.length = " + tmpWavFile.length());
                }
            } catch (IOException e) {
                e.printStackTrace();
                notifyState(WindState.ERROR);
            }

            notifyState(state);
            state = WindState.IDLE;
            notifyState(state);
            Log.i(TAG, "录制结束");
        }
    }

    /**
     * 播放的线程
     */
    public class AudioTrackPlayThread extends Thread {
        AudioTrack mAudioTrack;
        int bufferSize = 1024 * 10;
        File audioFile = null;

        public AudioTrackPlayThread(File file) {
            setPriority(Thread.MAX_PRIORITY);
            audioFile = file;
            bufferSize = AudioTrack.getMinBufferSize(AUDIO_FREQUENCY, PLAY_CHANNEL_CONFIG,
                    AUDIO_ENCODING) * PLAY_AUDIO_BUFFER_TIMES;
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    AUDIO_FREQUENCY,
                    PLAY_CHANNEL_CONFIG,
                    AUDIO_ENCODING,
                    bufferSize,
                    AudioTrack.MODE_STREAM);
        }

        @Override
        public void run() {
            super.run();
            state = WindState.PLAYING;
            notifyState(state);
            try {
                FileInputStream fis = new FileInputStream(audioFile);
                mAudioTrack.play();
                byte[] bytes = new byte[bufferSize];
                while (state.equals(WindState.PLAYING)
                        && fis.read(bytes) > 0) {
                    mAudioTrack.write(bytes, 0, bytes.length);
                }
                mAudioTrack.stop();
                mAudioTrack.release();
            } catch (IOException e) {
                e.printStackTrace();
                notifyState(WindState.ERROR);
            }
            state = WindState.STOP_PLAY;
            notifyState(state);
            state = WindState.IDLE;
            notifyState(state);
        }

    }

    public synchronized void notifyState(final WindState state) {
        if (null != onStateListener) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    onStateListener.onStateChanged(state);
                }
            });
        }
    }

    /**
     * 写wav文件头
     *
     * @param os
     * @param totalAudioLength
     * @param longSampleRate
     * @param channels
     * @throws IOException
     */
    private void writeWavFileHeader(FileOutputStream os,
                                    int totalAudioLength,
                                    int longSampleRate,
                                    int channels) throws IOException {
        byte[] header = generateWavFileHeader(totalAudioLength, longSampleRate, channels);
        os.write(header);
    }

    private byte[] generateWavFileHeader(long pcmAudioByteCount, int longSampleRate, int channels) {
        long totalDataLen = pcmAudioByteCount + 36; // 不包含前8个字节的WAV文件总长度
        long byteRate = longSampleRate * 2 * channels;
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';

        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);

        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;

        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;

        //通道数
        header[22] = (byte) channels;
        header[23] = 0;

        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);

        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);

        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (2 * channels);
        header[33] = 0;

        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk

        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (pcmAudioByteCount & 0xff);
        header[41] = (byte) ((pcmAudioByteCount >> 8) & 0xff);
        header[42] = (byte) ((pcmAudioByteCount >> 16) & 0xff);
        header[43] = (byte) ((pcmAudioByteCount >> 24) & 0xff);
        return header;
    }

}

