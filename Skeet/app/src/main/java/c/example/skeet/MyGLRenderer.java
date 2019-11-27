package c.example.skeet;

import android.content.pm.VersionedPackage;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;

import javax.microedition.khronos.opengles.GL10;

class MyGLRenderer implements GLSurfaceView.Renderer {


    private  Rectangle rifle;
    private Shape shape;
    private Random r;
    private boolean newBullet;
    private float ratio;

    List<Circle> Bullet;
    //private Digit d;
    private float[] vPMatrix = new float[16];
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

    public void setRifleAngle(int rotateAmount)
    {
        rifle.rotate(rotateAmount);
    }

    public void fireBullet()
    {
        newBullet = true;

    }
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        newBullet = false;

        Bullet = new ArrayList();
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
        float[] scratch2 = new float[16];
        float[] rifleRotateM = new float[16];
        float[] saveVPM;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        saveVPM = vPMatrix.clone();

        //handle the bird stuff!
        shape.advance();
        if(shape.isOffScreen())
        {
            int nextBird = r.nextInt(2);
            if(nextBird == 0)
                shape = new Star();
            else
                shape = new Circle();
        }

        Matrix.setRotateM(rotationMatrix, 0, shape.getAngle(), 0f, 0f, -1.0f);
        Matrix.translateM(vPMatrix, 0, shape.getX(), shape.getY(), 0);
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
        shape.draw(scratch);


        vPMatrix = saveVPM.clone();
        //move the rifle to the corner
        Matrix.translateM(vPMatrix, 0, rifle.getX(), rifle.getY(), 0);
        Matrix.setRotateM(rifleRotateM, 0, rifle.getAngle(), 0f,0f, -1.0f);
        Matrix.multiplyMM(scratch2, 0, vPMatrix, 0, rifleRotateM, 0);



        if(newBullet) {
            Circle bullet = new Circle(rifle.getPoint(), rifle.getAngle(), 0.3f);
            Bullet.add(bullet);
            newBullet = false;
        }

        List<Integer> removeIndexes = new ArrayList();
        int count = 0;
        for(Circle b : Bullet)
        {
            if(b.isOffScreen()) {
                removeIndexes.add(count);
                continue;
            }
            b.advance();
            b.applyGravity();
            vPMatrix = saveVPM.clone();
            Matrix.translateM(vPMatrix, 0, b.getX(), b.getY(), 0.0f);
            b.draw(vPMatrix);
            count++;
        }

        rifle.draw(scratch2); // vPMatrix is views * Projection

        for(Integer i : removeIndexes)
        {
            Bullet.remove(i);
        }


    }
    // vPMatrix is an abbreviation for "Model View Projection Matrix"


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        rifle = new Rectangle(-ratio);
    }

}
