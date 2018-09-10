package com.ozil.mesut.rocketlauncher.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * @author kui.liu
 * @time 2018/7/2 14:29
 */
public class Square {

    private FloatBuffer fb;
    private ShortBuffer sb;
    static final int COORDS_PER_VERTEX = 3;

    public float[] squareCoords = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f
    };

    private short drawOrder[] = {0, 1, 2, 0, 1, 2};

    public Square() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(squareCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        fb = byteBuffer.asFloatBuffer();
        fb.put(squareCoords);
        fb.position(0);

        ByteBuffer drawByteBuffer = ByteBuffer.allocate(drawOrder.length * 2);
        drawByteBuffer.order(ByteOrder.nativeOrder());
        sb = drawByteBuffer.asShortBuffer();
        sb.put(drawOrder);
        drawByteBuffer.position(0);
    }
}
