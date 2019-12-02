package c.example.skeet;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.util.Random;

class Shape {
    protected Point pt;
    protected Point velocity;
    protected Random random;

    protected float angle = 0;
    protected float dAngle = 0;
    protected FloatBuffer vertexBuffer;
    protected int mProgram;

    private int positionHandle;
    private int colorHandle;

    protected int vertexCount = 0;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

    protected final String vertexShaderCode =
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

    // Use to access and set the view transformation
    private int vPMatrixHandle;


    // that is a function to color it
    protected final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float triangleCoords[];

    public void draw(float[] mvpMatrix)
    {
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
    public void advance()
    {
        pt.addPoint(velocity);
        angle += dAngle;
    }

    public boolean isOffScreen()
    {
        return pt.getX() > 1.0f || pt.getY() > 1.0f || pt.getY() < -1.0f || pt.getX() < -0.75f;
    }


    //GETTERS

    public float getX()
    {
        return pt.getX();
    }

    public float getY()
    {
        return pt.getY();
    }

    public float getDx() { return velocity.getX(); }

    public float getDy() { return velocity.getY(); }


    public Point getPoint() { return pt; }

    public float getAngle()
    {
        return angle;
    }



    //SETTERS
    public void setX(float x) { pt.setX(x);}

    public void setY(float y) {pt.setY(y);}

    protected void setColor(float red, float green, float blue, float alpha)
    {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
        color[3] = alpha;
    }

}
