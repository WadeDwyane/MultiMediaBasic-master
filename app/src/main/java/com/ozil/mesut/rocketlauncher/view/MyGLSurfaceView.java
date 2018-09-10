package com.ozil.mesut.rocketlauncher.view;

import android.content.Context;
import android.opengl.GLSurfaceView;


/**
 * @author kui.liu
 * @time 2018/7/2 11:33
 */
public class MyGLSurfaceView extends GLSurfaceView {

    public MyRender mMyRender;

    public MyGLSurfaceView(Context context) {
        super(context);
        //设置EGL的版本
        setEGLContextClientVersion(2);

        mMyRender = new MyRender();
        setRenderer(mMyRender);
    }
}
