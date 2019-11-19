package c.example.skeet;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Circle {

    Point pt;
    Point velocity;
    Random random;

    private FloatBuffer vertexBuffer;
    private final int mProgram;

    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = 48;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex



    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private float x, y;
    // Use to access and set the view transformation
    private int vPMatrixHandle;


    // that is a function to color it
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float triangleCoords[];

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 1.0f, 0.0f, 0.0f, 1.0f };

    public void advance()
    {
        pt.addPoint(velocity);
        if(pt.getX() > 1.0f || pt.getY() > 1.0f || pt.getY() < -1.0f || pt.getX() < -1.0f)
        {
            float newy = random.nextFloat();
            boolean negy = random.nextInt() % 2 == 0;
            newy = negy ? -newy : newy;
            pt.setXY(1.0f, newy);
            float y = random.nextFloat() / 100;
            if(!negy)
                y *= -1;
            velocity.setXY(-random.nextFloat() / 100, y);


        }

    }

    public float getX()
    {
        return pt.getX();
    }

    public float getY()
    {
        return pt.getY();
    }



    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);


        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


    public Circle() {
        random = new Random();
        pt = new Point(1.0f, 0.0f);
        velocity = new Point(random.nextFloat() / 10, random.nextFloat() / 10);

        triangleCoords = new float[144];
        float degrees = 360.0f / 16.0f;
        for(int i = 0; i < 16; i++)
        {
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
}
