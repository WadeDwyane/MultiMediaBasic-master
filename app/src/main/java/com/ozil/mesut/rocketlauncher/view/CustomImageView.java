package com.ozil.mesut.rocketlauncher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ozil.mesut.rocketlauncher.R;


/**
 * @author Administrator
 * @version $Rev$
 * @time 2018/6/21 17:01
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */

public class CustomImageView extends View {

    Paint paint = new Paint();
    Bitmap bitmap;

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.beauty);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != bitmap) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }
}
