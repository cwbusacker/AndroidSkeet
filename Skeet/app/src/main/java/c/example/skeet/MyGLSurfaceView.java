package c.example.skeet;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView {

        private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer();

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer);
        }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;
    private boolean isClick;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                renderer.fireBullet();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isClick) {

                }
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                float dx = x - previousX;
                float dy = y - previousY;
                // reverse direction of rotation above the mid-line
                if(dx < 0 && dy > 0)
                {
                    renderer.setRifleAngle(-3);
                }
                else if(dx > 0 && dy < 0) {
                    renderer.setRifleAngle(3);
                }
                requestRender();
                break;
            default:
                break;
        }
        previousX = x;
        previousY = y;
        return true;
    }
}
