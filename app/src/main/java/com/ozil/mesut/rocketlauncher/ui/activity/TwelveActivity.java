package com.ozil.mesut.rocketlauncher.ui.activity;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.ozil.mesut.rocketlauncher.R;
import com.ozil.mesut.rocketlauncher.base.BaseActivity;

/**
 * @author kui.liu
 * @time 2018/6/29 10:38
 */
public class TwelveActivity extends BaseActivity {

    public static String TAG = "TwelveActivityTAG";
    String[] videoColumns = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.MIME_TYPE
    };

    String[] thumbColumns = {
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID
    };

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        Cursor cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoColumns, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                Log.i(TAG, "id = " + id + ", data = " + data + ", title = " + title + ", contentType = " + mimeType);

                Cursor thumbCursor = managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails._ID + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    String thumbData = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    Log.i(TAG, "thunmData = " + thumbData);
                }
            }
        }
    }


    @Override
    protected void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_twelve;
    }
}
