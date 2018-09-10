package com.ozil.mesut.rocketlauncher.ui.activity;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.Choreographer;
import android.view.View;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.util.RecordAudioUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import butterknife.OnClick;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/20 14:27
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class SecondActivity extends BaseActivity {

    public boolean mIsStopRequest;
    public RecordAudioUtil instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = RecordAudioUtil.getInstance();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_two;
    }

    @OnClick({R.id.btn_play, R.id.btn_start_record, R.id.btn_stop_record,
            R.id.btn_play_pcm_record, R.id.btn_play_wav_record, R.id.btn_pause_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                playVideoWithUrl("");
                break;
            case R.id.btn_start_record:
                instance.startRecordAudio(true);
                break;
            case R.id.btn_stop_record:
                instance.stopRecord();
                break;
            case R.id.btn_play_pcm_record:
                instance.startPlayPCM();
                break;
            case R.id.btn_play_wav_record:
                instance.startPlayWav();
                break;
            case R.id.btn_pause_record:
                instance.stopPlay();
                break;
        }
    }

    private void playVideoWithUrl(String url) {
        MediaExtractor extractor = null;
        MediaCodec decoder = null;
        try {
            //创建一个MediaExtractor对象
            extractor = new MediaExtractor();
            //设置MediaExtractor的资源路径,这里可以把mp4的路径传进来
            extractor.setDataSource(this, Uri.parse(url), new HashMap<String, String>());

            //选择我们要解析的轨道
            int trackIndex = selectTrack(extractor);
            if (trackIndex < 0) {
                throw new RuntimeException("No video track found in " + url);
            }

            //选择视频轨道的索引
            extractor.selectTrack(trackIndex);

            //获取轨道的音视频格式,这个格式和Codec有关系
            MediaFormat format = extractor.getTrackFormat(trackIndex);
            String mime = format.getString(MediaFormat.KEY_MIME);

            //创建一个MediaCodec对象
            decoder = MediaCodec.createDecoderByType(mime);

            //设置格式和视频输出的surface
            decoder.configure(format, null, null, 0);
            decoder.start();

            doExtract(extractor, trackIndex, decoder, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解码
     *
     * @param extractor
     * @param trackIndex
     * @param decoder
     * @param frameCallback
     */
    private void doExtract(MediaExtractor extractor,
                           int trackIndex,
                           MediaCodec decoder,
                           Choreographer.FrameCallback frameCallback) {
        final int TIMEOUT_USEC = 1000;

        // 获取MediaCodec的输入队列,是一个数组
        ByteBuffer[] buffers = decoder.getInputBuffers();
        int inputChunk = 0;
        long firstInputTimeNsec = -1;

        boolean outputDone = false;
        boolean inputDone = false;

        //用while做循环
        while (!outputDone) {
            if (mIsStopRequest) {
                return;
            }

            //Feed more data to decoder
            //不停地输入数据直到输入队列满为止
            if (!inputDone) {
                //返回一个索引
                int inputBufferIndex = decoder.dequeueInputBuffer(TIMEOUT_USEC);

                //如果输入队列还有位置
                if (inputBufferIndex > 0) {
                    if (firstInputTimeNsec == -1) {
                        firstInputTimeNsec = System.nanoTime();
                    }
                    ByteBuffer bf = buffers[inputBufferIndex];
                    //用Extractor读取一个数据
                    int chunkSize = extractor.readSampleData(bf, 0);
                    if (chunkSize < 0) {
                        decoder.queueSecureInputBuffer(inputBufferIndex,
                                0, null,
                                0L, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        inputDone = true;
                    } else {
                    }
                }
            }

        }

    }

    /**
     * 设置要解析的轨道
     *
     * @param extractor
     * @return
     */
    public int selectTrack(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("video/")) {
                return i;
            }

        }
        return -1;
    }
}
