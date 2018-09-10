package com.ozil.mesut.rocketlauncher.util;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/26 10:48
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class H264EncoderUtil {

    public int width;
    public int height;
    public int frameRate;
    private MediaCodec mMediaCodec;
    private BufferedOutputStream mBos;
    public boolean isRunning = false;

    public ArrayBlockingQueue<byte[]> yuv420Queue = new ArrayBlockingQueue<>(10);
    private final static int TIMEOUT_USEC = 12000;
    public byte[] configbyte;

    public H264EncoderUtil(int width, int height, int frameRate) {
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;

        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
        //设置颜色
//        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 5);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        try {
            mMediaCodec = MediaCodec.createDecoderByType("video/avc");
            mMediaCodec.configure(mediaFormat, null, null, 0);
            mMediaCodec.start();
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFile() {
        //设置输出路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            mBos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 开始编码
     */
    public void startEncode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                byte[] input = null;
                long pts = 0;
                long generateIndex = 0;

                while (isRunning) {
                    if (yuv420Queue.size() > 0) {
                        input = yuv420Queue.poll();
                        byte[] yuv420sp = new byte[width * height * 3 / 2];
                        //必须要转格式,否则录出来视频为绿色
                        NV21ToNV12(input, yuv420sp, width, height);
                        input = yuv420sp;
                    }

                    if (null != null) {
                        try {
                            ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
                            ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
                            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);
                            if (inputBufferIndex >= 0) {
                                pts = computePresentationTime(generateIndex);
                                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                                inputBuffer.clear();
                                inputBuffer.put(input);
                                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, pts, 0);
                                generateIndex += 1;
                            }

                            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                            int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                            while (outputBufferIndex > 0) {
                                ByteBuffer outputByteBuffer = outputBuffers[outputBufferIndex];
                                byte[] outData = new byte[bufferInfo.size];
                                outputByteBuffer.get(outData);
                                if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                                    configbyte = new byte[bufferInfo.size];
                                    configbyte = outData;
                                } else if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_SYNC_FRAME) {
                                    byte[] keyFrame = new byte[bufferInfo.size + configbyte.length];
                                    System.arraycopy(configbyte, 0, keyFrame, 0, configbyte.length);
                                    System.arraycopy(outData, 0, keyFrame, configbyte.length, outData.length);
                                    mBos.write(keyFrame, 0, keyFrame.length);
                                } else {
                                    mBos.write(outData, 0, outData.length);
                                }
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    } else {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

                //停止解编码器并释放资源
                mMediaCodec.stop();
                mMediaCodec.release();

                //关闭数据流
                try {
                    mBos.flush();
                    mBos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 将NV21转化成NV12
     *
     * @param nv21
     * @param nv12
     * @param width
     * @param height
     */
    private void NV21ToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        if (null == nv12 || null == nv21) {
            return;
        }

        int frameSize = width * height;
        int i, j = 0;
        System.arraycopy(nv12, 0, nv21, 0, frameSize);

        for (i = 0; i < frameSize; i++) {
            nv12[i] = nv21[i];
        }

        for (j = 0; j < frameSize / 2; j += 2) {
            nv12[frameSize + j - 1] = nv21[j + frameSize];
        }

        for (j = 0; j < frameSize / 2; j += 2) {
            nv12[frameSize + j] = nv21[j + frameSize - 1];
        }

    }

    /**
     * 停止编码
     */
    public void stopEncode() {
        isRunning = false;
    }

    /**
     * 将数据添加到队列中
     *
     * @param data
     */
    public void putData(byte[] data) {
        if (yuv420Queue.size() > 10) {
            yuv420Queue.poll();
        }
        yuv420Queue.add(data);
    }

    /**
     * 根据帧数生成时间戳
     */
    private long computePresentationTime(long frameIndex) {
        return 132 + frameIndex * 1000000 / frameRate;
    }
}
