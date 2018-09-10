package com.ozil.mesut.rocketlauncher.ui.activity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/25 15:08
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ForthActivity extends BaseActivity
        implements TextureView.SurfaceTextureListener {

    @BindView(R.id.textureview)
    TextureView mTextureView;
    private Camera mCamera;

    @Override
    protected void initListener() {

    }

    @Override
    public void initData() {
        // 打开相机
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        //使用Texture预览相机数据
        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_four;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (null != mCamera) {
            try {
                mCamera.setPreviewTexture(surface);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
