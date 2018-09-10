package com.ozil.mesut.rocketlauncher.ui.activity;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/22 9:42
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class ThirdActivity extends BaseActivity implements SurfaceHolder.Callback {
    public static String TAG = "ThirdActivity";

    private Camera mCamera;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;

    @Override
    protected void initListener() {

    }

    @Override
    public void initData() {
        mSurfaceView.getHolder().addCallback(this);

        //打开摄像头并将摄像头旋转90度
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);

        //Android中Google支持的Camera Preview Callback的YUV常用格式有两种:NV21, YV12
        final Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        //设置闪光灯模式
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        mCamera.setParameters(parameters);

        //预览的回调
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters1 = camera.getParameters();
                int previewFormat = parameters1.getPreviewFormat();
                Log.i(TAG, "format = " + previewFormat);
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_third;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //停止预览
        mCamera.stopPreview();
        //释放掉相机资源
        mCamera.release();
    }
}
