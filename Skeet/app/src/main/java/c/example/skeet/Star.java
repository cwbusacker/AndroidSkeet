package c.example.skeet;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Star extends Shape {


    public Star() {
        setColor(1.0f, 0f,0f,0f);
        dAngle = 10;
        vertexCount = 9;
        random = new Random();
        float y = random.nextFloat();
        if(random.nextInt(2) == 0)
            y *= -1;
        pt = new Point(1.0f, y);

        float negative = 1;
        if(y > 0)
            negative = -1;
        velocity = new Point(-random.nextFloat() / 100 - .01f, random.nextFloat() / 100 * negative);

        float m = 0.1f;
        triangleCoords = new float[27];
        //16 triangles create the circle. 365 degrees around the circle

        triangleCoords[0] = -0.362f * m;
        triangleCoords[1] = -0.118f * m;
        triangleCoords[2] = 0.0f * m;

        triangleCoords[3] = 0.0f * m;
        triangleCoords[4] = 1.0f * m;
        triangleCoords[5] = 0.0f * m;

        triangleCoords[6] = 0.587f * m;
        triangleCoords[7] = -0.809f * m;
        triangleCoords[8] = 0.0f * m;



        triangleCoords[9] = 0.362f * m;
        triangleCoords[10] = -0.118f * m;
        triangleCoords[11] = 0.0f * m;

        triangleCoords[12] = 0.0f * m;
        triangleCoords[13] = 1.0f * m;
        triangleCoords[14] = 0.0f * m;

        triangleCoords[15] = -0.587f * m;
        triangleCoords[16] = -0.809f * m;
        triangleCoords[17] = 0.0f * m;



        triangleCoords[18] = -0.951f * m;
        triangleCoords[19] = 0.309f * m;
        triangleCoords[20] = 0.0f * m;

        triangleCoords[21] = 0.951f * m;
        triangleCoords[22] = 0.309f * m;
        triangleCoords[23] = 0.0f * m;

        triangleCoords[24] = 0.0f * m;
        triangleCoords[25] = -0.3823f * m;
        triangleCoords[26] = 0.0f * m;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);



        // that will call the compiler
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);


        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }
}
