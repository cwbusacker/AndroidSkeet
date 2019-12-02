package c.example.skeet;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Rectangle  extends  Shape{



    public Rectangle(float ratio){
        setColor(.8f, 0.412f, 0f, 1.0f);
        vertexCount = 6;
        angle = 45;

        triangleCoords = new float[18];
        triangleCoords[0] = 0.15f;
        triangleCoords[1] = 0.0622008459f;
        triangleCoords[2] = 0f;

        triangleCoords[3] = -0.15f;
        triangleCoords[4] = -0.0311004243f;
        triangleCoords[5] = 0f;

        triangleCoords[6] = 0.15f;
        triangleCoords[7] = -0.0311004243f;
        triangleCoords[8] = 0f;

        triangleCoords[9] = 0.15f;
        triangleCoords[10] = 0.0622008459f;
        triangleCoords[11] = 0f;

        triangleCoords[12] = -0.15f;
        triangleCoords[13] = 0.0622008459f;
        triangleCoords[14] = 0f;

        triangleCoords[15] = -0.15f;
        triangleCoords[16] = -0.0311004243f;
        triangleCoords[17] = 0f;


        pt = new Point(-ratio, -1f);
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


    public void rotate(int amount) {

        angle += amount;

        if(angle < 0)
            angle = 0;

        if(angle > 90)
            angle = 90;

    }








}
