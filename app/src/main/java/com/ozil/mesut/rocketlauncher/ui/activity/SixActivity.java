package com.ozil.mesut.rocketlauncher.ui.activity;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;
import com.ozil.mesut.rocketlauncher.util.H264EncoderUtil;
import com.ozil.mesut.rocketlauncher.util.HardwareUtil;

import java.io.IOException;

import butterknife.BindView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/26 10:29
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SixActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    public static String TAG = "SixActivity";

    public static int PREVIEW_WIDTH = 1280;
    public static int PREVIEW_HEIGHT = 720;
    public static int FRAME_RATE = 30;


    @BindView(R.id.btn_audio_video_mix)
    AppCompatButton mButton;
    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private H264EncoderUtil mH264Encoder;


    @Override
    protected void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SixActivity.this, MediaMuxerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);

        if (HardwareUtil.isSupportH264Codec()) {
            Log.i(TAG, "support H264 code");
        }else {
            Log.i(TAG, "not support H264 code");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_six;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "enter surfaceCreated method");
        //当surface创建后,打开Camera
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_WIDTH);
        //设置相机参数
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //采集到的数据使用H264编码
        mH264Encoder = new H264EncoderUtil(PREVIEW_WIDTH, PREVIEW_HEIGHT, FRAME_RATE);
        mH264Encoder.startEncode();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "enter surfaceChanged method");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "enter surfaceDestroyed method");
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera = null;
        }

        if (null != mH264Encoder) {
            mH264Encoder.stopEncode();
        }

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (null != mH264Encoder) {
            //将数据加入到Encoder中
            mH264Encoder.putData(data);
        }
    }
}
