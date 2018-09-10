package com.ozil.mesut.rocketlauncher.ui.activity;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/25 16:15
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class FifthActivity extends BaseActivity {
    private static String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
    private MediaExtractor mExtractor;
    private MediaMuxer mMediaMuxer;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        Thread thread = new Thread(new ProcessThread());
        thread.start();
    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_five;
    }

    public class ProcessThread implements Runnable {

        @Override
        public void run() {
            try {
                process();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean process() throws IOException {
        mExtractor = new MediaExtractor();
        //设置资源路径
        mExtractor.setDataSource(SDCARD_PATH + "/running.mp4");

        int mVideoTrackIndex = -1;
        int frameRate = 0;
        //遍历源文件通道数
        for (int i = 0; i < mExtractor.getTrackCount(); i++) {
            //获取指定通道的通道格式
            MediaFormat mediaFormat = mExtractor.getTrackFormat(i);
            String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
            if (!mime.startsWith("video/")) {
                continue;
            }

            if (null == mediaFormat) {
                continue;
            }

            //            frameRate = mediaFormat.getInteger(MediaFormat.KEY_FRAME_RATE);
            mExtractor.selectTrack(i);

            //生成音频或者视频文件
            mMediaMuxer = new MediaMuxer(SDCARD_PATH + "/out.mp4",
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoTrackIndex = mMediaMuxer.addTrack(mediaFormat);
            mMediaMuxer.start();
        }

        if (null == mMediaMuxer) {
            return false;
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer bf = ByteBuffer.allocate(500 * 1024);
        int sampleSize = 0;
        //采样大小
        while ((sampleSize = mExtractor.readSampleData(bf, 0)) > 0) {
            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT;
            //            info.presentationTimeUs += 1000 * 1000/frameRate;
            mMediaMuxer.writeSampleData(mVideoTrackIndex, bf, info);
            mExtractor.advance();
        }

        mExtractor.release();
        mMediaMuxer.stop();
        mMediaMuxer.release();

        return true;
    }

}
