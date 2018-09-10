package com.ozil.mesut.rocketlauncher.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

import java.io.FileNotFoundException;

import butterknife.BindView;

/**
 * @author kui.liu
 * @time 2018/6/27 10:50
 */
public class EightActivity extends BaseActivity {

    @BindView(R.id.imv_show)
    ImageView mImageView;
    @BindView(R.id.imv_select_show)
    ImageView mImageView1;

    public static int REQUEST_SELECT_PICTURE_CODE = 0x0001;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Canvas mCanvas1 = new Canvas();
    private Paint mPaint1;

    @Override
    protected void initListener() {
        findViewById(R.id.btn_create_btp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始绘制
                //绘制一个点
                mCanvas.drawPoint(300, 200, mPaint);

                //绘制一条直线
                int startx = 50;
                int starty = 100;
                int endx = 150;
                int endy = 210;
                mCanvas.drawLine(startx, starty, endx, endy, mPaint);

                //绘制一个矩形(指定上下左右的点)
                float leftx = 10.0f;
                float rightx = 20.0f;
                float topy = 10.0f;
                float bottomy = 20.0f;
                //                RectF rectF = new RectF(leftx, topy, rightx, bottomy);
                //                canvas.drawRect(rectF, paint);
                mCanvas.drawRect(leftx, topy, rightx, bottomy, mPaint);

                //绘制一个椭圆
                float oLeftx = 200.0f;
                float oRightx = 250.0f;
                float oTopy = 100.0f;
                float oBottomy = 170.0f;
                //                canvas.drawOval(oLeftx, oTopy, oRightx, oBottomy, paint);

                //绘制一个圆
                float povitx = 300.0f;
                float povity = 600.0f;
                float radius = 20.0f;
                mCanvas.drawCircle(povitx, povity, radius, mPaint);

                //绘制路径
                Path path = new Path();
                path.moveTo(400, 0);
                path.lineTo(290, 39);
                path.lineTo(900, 600);
                path.lineTo(897, 233);
                path.lineTo(400, 0);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2.0f);
                mCanvas.drawPath(path, mPaint);

                //绘制文本
                mPaint.setTextSize(20);
                float textX = 600;
                float textY = 50;
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(20.0f);
                //设置字体样式
                mPaint.setTypeface(Typeface.DEFAULT_BOLD);
                mCanvas.drawText("Hello World", textX, textY, mPaint);

                //设置自定义的样式
                Typeface typeface = Typeface.createFromAsset(getAssets(),
                        "fontawesome-webfont.ttf");
                mPaint.setTypeface(typeface);
                mPaint.setTextSize(40);
                float text1X = 400;
                float text1y = 1000;
                mCanvas.drawText("自定义的文本", text1X, text1y, mPaint);

                //设置路径上的文本
                mCanvas.drawTextOnPath("Text is on path", path, 0, 0, mPaint);
                mImageView.invalidate();
                //                mImageView.setImageBitmap(mBitmap);
            }
        });


        mImageView.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            float downY = 0;
            float upX = 0;
            float upY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        upX = event.getX();
                        upY = event.getY();
                        mCanvas.drawLine(downX, downY, upX, upY, mPaint);
                        mImageView.invalidate();
                        downX = upX;
                        downY = upY;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        findViewById(R.id.btn_select_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_SELECT_PICTURE_CODE);
            }
        });

        mImageView1.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            float downY = 0;
            float upX = 0;
            float upY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        upX = event.getX();
                        upY = event.getY();
                        mCanvas1.drawLine(downX, downY, upX, upY, mPaint1);
                        mImageView1.invalidate();
                        downY = upY;
                        downX = upX;
                        break;
                    case MotionEvent.ACTION_UP:
                        /*upX = event.getX();
                        upY = event.getY();
                        mCanvas1.drawLine(downX, downY, upX, upY, mPaint1);
                        mImageView1.invalidate();*/
                        break;
                }


                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap mBitmap;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE_CODE) {
                Uri uri = data.getData();
                Display display = getWindowManager().getDefaultDisplay();
                float dw = display.getWidth();
                float dh = display.getHeight();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                try {
                    //                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                    int weightRadio = (int) Math.ceil(options.outWidth / dw);
                    int heightRadio = (int) Math.ceil(options.outHeight / dh);

                    if (heightRadio > 1 && weightRadio > 1) {
                        if (heightRadio > weightRadio) {
                            //高度比率较大
                            options.inSampleSize = heightRadio;
                        } else {
                            options.inSampleSize = weightRadio;
                        }
                    }

                    options.inJustDecodeBounds = false;
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().
                            openInputStream(uri), null, options);

                    //创建位图对象,绘制第一个位图对象
                    Bitmap alterBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
                    mCanvas1 = new Canvas(alterBitmap);
                    mPaint1 = new Paint();
                    mPaint1.setColor(Color.RED);
                    mPaint1.setStrokeWidth(40);
                    Matrix matrix = new Matrix();
                    mCanvas1.drawBitmap(mBitmap, matrix, mPaint1);
                    mImageView1.setImageBitmap(alterBitmap);



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initData() {
        //位图创建及配置
        mBitmap = Bitmap.createBitmap(
                getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(),
                Bitmap.Config.ARGB_8888
        );

        //创建画布
        mCanvas = new Canvas(mBitmap);

        //创建画笔
        mPaint = new Paint();
        //画笔颜色
        mPaint.setColor(Color.RED);
        //画笔样式
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //画笔大小
        mPaint.setStrokeWidth(20.0f);

        mImageView.setImageBitmap(mBitmap);
    }

    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_eight;
    }
}
