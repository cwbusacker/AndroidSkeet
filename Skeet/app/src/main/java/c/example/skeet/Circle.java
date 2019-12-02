package c.example.skeet;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Circle extends Shape {

    public Circle() {
        vertexCount = 48;
        random = new Random();
        setColor(0f, 0.5f,0.0f,  0f);
        float y = random.nextFloat();
        if(random.nextInt(2) == 0)
            y *= -1;
        pt = new Point(1.0f, y);

        float negative = 1;
        if(y > 0)
            negative = -1;
        velocity = new Point(-random.nextFloat() / 100 - 0.01f, random.nextFloat() / 100 * negative);

        //we cant draw a circle in GL
        //we need to create a triagle and connect the points!!!!
        triangleCoords = new float[144];

        //16 triangles create the circle. 365 degrees around the circle
        float degrees = 365.0f / 16.0f;
        for(int i = 0; i < 16; i++)
        {
            //each point for the triangle
            triangleCoords[i * 9] = (float)(0.1f * cos(degrees * i * 3.14/180));
            triangleCoords[i * 9 + 1] = (float) (0.1f * sin(degrees * i * 3.14/ 180));
            triangleCoords[i * 9 + 2] = 0.0f;

            triangleCoords[i * 9 + 3] = 0.0f;
            triangleCoords[i * 9 + 4] = 0.0f;
            triangleCoords[i * 9 + 5] = 0.0f;

            triangleCoords[i * 9 + 6] = (float) (0.1f * cos(degrees * (i + 1) * 3.14 / 180));
            triangleCoords[i * 9 + 7] = (float) (0.1f * sin(degrees * (i + 1) * 3.14 / 180));
            triangleCoords[i * 9 + 8] = 0.0f;


        }
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

    public void applyGravity() {
        velocity.setY(velocity.getY() - 0.0008f);
    }

    public Circle(Point p, float angle, double m) {
        setColor(0.1843f, 0.4705f, 0.929f, 0f);
        vertexCount = 48;
        random = new Random();
        pt = new Point(p);
        velocity = new Point(-0.05f * (float) cos(angle * 3.14/180), 0.05f * (float) sin(angle * 3.14/180));

        //we cant draw a circle in GL
        //we need to create a triagle and connect the points!!!!
        triangleCoords = new float[144];

        //16 triangles create the circle. 365 degrees around the circle
        float degrees = 365.0f / 16.0f;
        for(int i = 0; i < 16; i++)
        {
            //each point for the triangle
            triangleCoords[i * 9] = (float)(0.1f * m * cos(degrees * i * 3.14/180));
            triangleCoords[i * 9 + 1] = (float) (0.1f * m * sin(degrees * i * 3.14/ 180));
            triangleCoords[i * 9 + 2] = 0.0f;

            triangleCoords[i * 9 + 3] = 0.0f;
            triangleCoords[i * 9 + 4] = 0.0f;
            triangleCoords[i * 9 + 5] = 0.0f;

            triangleCoords[i * 9 + 6] = (float) (0.1f * m * cos(degrees * (i + 1) * 3.14 / 180));
            triangleCoords[i * 9 + 7] = (float) (0.1f * m *sin(degrees * (i + 1) * 3.14 / 180));
            triangleCoords[i * 9 + 8] = 0.0f;


        }
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
