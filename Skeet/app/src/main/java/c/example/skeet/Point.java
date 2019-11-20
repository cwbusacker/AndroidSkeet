package c.example.skeet;

    public class Point {
        private float x;
        private float y;

        //non-default constructor
        Point(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

       // this combine the velocity and the position
       // and putting it in the position
        void addPoint(Point p)
        {
            x += p.getX();
            y += p.getY();
        }

        void setX(float x)
        {
            this.x = x;
        }

        void setY(float y)
        {
            this.y = y;
        }

        float getX()
        {
            return x;
        }

        float getY()
        {
            return y;
        }

        void addX(float dx)
        {
            x+= dx;
        }

        void addY(float dy)
        {
            y+= dy;
        }

        void setXY(float x, float y)
        {
            this.x = x;
            this.y = y;
        }








}
