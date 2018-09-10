package com.ozil.mesut.rocketlauncher.view;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * @author kui.liu
 * @time 2018/7/2 11:35
 */
public class MyRender implements GLSurfaceView.Renderer {
    TriAngle mTriAngle;
    Square mSquare;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //初始化三角形
        mTriAngle = new TriAngle();
        //初始化四边形
        mSquare = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTriAngle.draw();
    }

    /**
     * Shader包含了GLSL代码, 必须在使用前编译,要编译这些代码,在render类型创建一个工具方法
     *
     * @param type
     * @param shaderType
     * @return
     */
    public static int loadShader(int type, String shaderType) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderType);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
