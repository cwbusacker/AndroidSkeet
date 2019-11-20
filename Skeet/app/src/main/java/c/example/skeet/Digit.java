package c.example.skeet;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

    public class Digit{

        Point pt;
        Point velocity;
        Random random;

        float angle;
        private FloatBuffer vertexBuffer;
        private final int mProgram;

        private int positionHandle;
        private int colorHandle;

        private int vertexCount;
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
        float digitCoords[];

        // Set color with red, green, blue and alpha (opacity) values
        float color[] = { 1.0f, 0.0f, 0.0f, 1.0f };


        public boolean isOffScreen() {
            return pt.getX() > 1.0f || pt.getY() > 1.0f || pt.getY() < -1.0f || pt.getX() < -0.75f;
        }

        public float getAngle()
        {
            return angle;
        }


        public void advance()
        {
            pt.addPoint(velocity);
    /*   if(pt.getX() > 1.0f || pt.getY() > 1.0f || pt.getY() < -1.0f || pt.getX() < -0.75f)
        {
            float newy = random.nextFloat();
            boolean negy = random.nextInt() % 2 == 0;
            newy = negy ? -newy : newy;
            pt.setXY(1.0f, newy);
            float y = random.nextFloat() / 100;
            if(!negy)
                y *= -1;
            velocity.setXY(-random.nextFloat() / 100 - 0.005f, y);


        }*/
            angle+=10;

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


        public Digit(int digit) {


            int NUMBER_OUTLINES[][] =
                    {{0, 0, 7, 0, 7, 0, 7,10, 7,10, 0,10, 0,10, 0, 0, -1,-1, -1,-1}, //0

                            {7,   0, 7,   10, -1,   -1, -1,   -1, -1,   -1, -1,  -1, -1,   -1, -1,   -1, -1,   -1, -1,     -1}, //1

                            {0,    0, 7,   0,  7,    0,  7,    5,  7,    5,  0,   5,  0,    5,  0,   10,  0,    10,  7,    10}, //2

                            {0,    0, 7,   0,  7,    0,  7,    10, 7,    10, 0,  10,  4,    5,  7,    5, -1,    -1, -1,   -1}, //3

                            {0,    0, 0,   5,  0,    5,  7,     5, 7,    0,  7,  10, -1,   -1, -1,   -1, -1,    -1, -1,   -1}, //4

                            {7,    0, 0,   0,  0,    0,  0,     5, 0,    5,  7,   5,  7,    5,  7,   10,  7,    10,  0,   10},//5

                            {7,    0, 0,   0,  0,    0,  0,    10, 0,    10, 7,  10,  7,   10,  7,    5,  7,     5,  0,    5},//6

                            {0,    0, 7,   0,  7,    0,  7,    10, -1,   -1,-1,  -1, -1,   -1, -1,   -1, -1,    -1, -1,   -1},//7

                            {0,    0, 7,   0,  0,    5,  7,    5,   0,   10, 7,   10, 0,    0,  0,   10,  7,     0,  7,   10},//8

                            {0,    0, 7,   0,  7,    0,  7,   10,   0,    0, 0,    5, 0,    5,  7,    5, -1,    -1, -1,   -1} //9

            };

            // initialize vertex byte buffer for shape coordinates
            ByteBuffer bb = ByteBuffer.allocateDirect(
                    // (number of coordinate values * 4 bytes per float)
                    digitCoords.length * 4);
            // use the device hardware's native byte order
            bb.order(ByteOrder.nativeOrder());

            // create a floating point buffer from the ByteBuffer
            vertexBuffer = bb.asFloatBuffer();
            // add the coordinates to the FloatBuffer
            vertexBuffer.put(digitCoords);
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

