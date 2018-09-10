package com.ozil.mesut.rocketlauncher.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import butterknife.BindView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/21 16:32
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class FirstActivity extends BaseActivity{

    @BindView(R.id.imv_one)
    ImageView mImageView;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;

    protected void initListener() {

    }

    @Override
    public void initData() {
        //采用ImageView绘制图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lake);
        mImageView.setImageBitmap(bitmap);

        //采用SurfaceView绘制图片
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (null == holder) {
                    return;
                }

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);

                //获取bitmap
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.beauty);
                //先锁定当前surfaceview的画布
                Canvas canvas = holder.lockCanvas();
                //执行绘制操作
                canvas.drawBitmap(bitmap1, 0, 0, paint);
                //解除当前surfaceview的画布
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        //采用自定义view的形式绘制ImageView
        /*mCustomImageView.setResId(R.mipmap.ozil);
        mCustomImageView.requestLayout();*/
    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_one;
    }
}
