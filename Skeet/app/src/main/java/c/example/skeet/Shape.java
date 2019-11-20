package c.example.skeet;

public interface Shape {
    void draw(float[] mvpMatrix);
    void advance();
    float getX();
    float getY();
    float getAngle();
    boolean isOffScreen();
}
