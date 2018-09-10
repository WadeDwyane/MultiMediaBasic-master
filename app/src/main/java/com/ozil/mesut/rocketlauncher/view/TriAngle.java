package com.ozil.mesut.rocketlauncher.view;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * @author kui.liu
 * @time 2018/7/2 13:54
 */
public class TriAngle {

    static int one = 0x10000;

    private IntBuffer vertexBuffer;
    static final int COORDS_PER_VERTEX = 3;
    static final int triangleOrds[] = {
            0, one, 0,
            -one, -one, 0,
            one, -one, 0};

    static final float[] color = {
            0.63671875f, 0.76953125f, 0.22265625f, 1.0f
    };

    public TriAngle() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(triangleOrds.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asIntBuffer();
        vertexBuffer.put(triangleOrds);
        vertexBuffer.position(0);

        //为了绘制形状必须编译Shader代码,添加他们到一个OpenGLES program对象然后链接这个对象
        int vertexShader = MyRender.loadShader(GLES20.GL_VERTEX_SHADER, this.vertexShader);
        int fragmentShader = MyRender.loadShader(GLES20.GL_FRAGMENT_SHADER, this.fragmentShaderCode);

        //创建一个空的OpenGL ES program
        mProgram = GLES20.glCreateProgram();
        //将program与shader绑定
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);
    }

    /**
     * 专门负责绘制的方法
     */
    public void draw() {
        //添加项目到ES 绘制环境
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_INT, false, vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glGetUniformfv(mColorHandle, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleOrds.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public final String vertexShader =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public int mProgram;
}
