package c.example.skeet;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;

import javax.microedition.khronos.opengles.GL10;

class MyGLRenderer implements GLSurfaceView.Renderer {


    private  Rectangle rifle;
    private Shape shape;
    private Random r;
    //private Digit d;
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private float[] rotationMatrix = new float[16];
    private int count;
    //Shaders contain OpenGL Shading Language (GLSL) code that
    // must be compiled prior to using it in the OpenGL ES environment.
    // To compile this code, create a utility method in your renderer class:
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        rifle = new Rectangle();
        count = 0;
        r = new Random();
        int nextBird = r.nextInt(2);
        if(nextBird == 0)
            shape = new Star();
        else
            shape = new Circle();
        // Set the background frame color
        //d = new Digit(3);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        float[] scratch = new float[16];
        float[] saveVPM;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation



        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        saveVPM = vPMatrix.clone();
        Matrix.translateM(saveVPM, 0, rifle.getX(), rifle.getY(), 0);

        Matrix.setRotateM(rotationMatrix, 0, shape.getAngle(), 0f, 0f, -1.0f);
        if(shape.isOffScreen())
        {
            int nextBird = r.nextInt(2);
            if(nextBird == 0)
                shape = new Star();
            else
                shape = new Circle();
        }

        shape.advance();
        rifle.draw(saveVPM); // vPMatrix is views * Projection
        //d.draw(vPMatrix);
  //      count++;
//        if(count % 100 == 0) {
          //  d = new Digit(count / 100);
            //  if(count == 10000)
            //  count = 0;
    //    }
        //create a matrix and the translateM will do is change position in the screen
        Matrix.translateM(vPMatrix, 0, shape.getX(), shape.getY(), 0);

        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        shape.draw(scratch);






    }
    // vPMatrix is an abbreviation for "Model View Projection Matrix"


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

}
