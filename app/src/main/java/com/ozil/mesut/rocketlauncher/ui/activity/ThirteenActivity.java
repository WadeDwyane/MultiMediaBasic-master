package com.ozil.mesut.rocketlauncher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ozil.mesut.rocketlauncher.view.MyGLSurfaceView;

/**
 * @author kui.liu
 * @time 2018/7/2 11:18
 */
public class ThirteenActivity extends AppCompatActivity{

    public static String TAG = "ThirteenActivity";
    public MyGLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new MyGLSurfaceView(this);
        setContentView(mGLSurfaceView);
    }
}
