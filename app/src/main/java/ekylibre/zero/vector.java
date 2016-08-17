package ekylibre.zero;

/**************************************
 * Created by pierre on 8/17/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class vector
{
    public double x;
    public double y;
    public double absX;
    public double absY;

    public vector(double x1, double x2, double y1, double y2)
    {
        setAbsValue();
        setVectorCoord(x1, x2, y1, y2);
    }

    public vector()
    {
        this.x = 0;
        this.y = 0;
        setAbsValue();
    }

    public void setAbsValue()
    {
        this.absX = Math.abs(this.x);
        this.absY = Math.abs(this.y);
    }

    public void setVectorCoord(double x1, double x2, double y1, double y2)
    {
        this.x = x2 - x1;
        this.y = y2 - y1;
    }
}
