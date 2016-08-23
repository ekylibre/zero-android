package ekylibre.zero.tracking;

/**************************************
 * Created by pierre on 8/17/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class Vector
{
    public double x;
    public double y;
    public double absX;
    public double absY;
    public double norm;
    public boolean set = false;

    public Vector(double x1, double x2, double y1, double y2)
    {
        setAbsValue();
        setVectorCoord(x1, x2, y1, y2);
    }

    public Vector()
    {
        this.x = 0;
        this.y = 0;
        setAbsValue();
        setNorm();
    }

    public void copyVector(Vector vector)
    {
        this.x = vector.x;
        this.y = vector.y;
        this.set = vector.set;
        setAbsValue();
        setNorm();
    }

    private void setAbsValue()
    {
        this.absX = Math.abs(this.x);
        this.absY = Math.abs(this.y);
    }

    public void setVectorCoord(double x1, double x2, double y1, double y2)
    {
        this.x = x2 - x1;
        this.y = y2 - y1;
        setAbsValue();
        setNorm();
    }

    public void setVectorFinalCoord(double x, double y)
    {
        this.x = x;
        this.y = y;
        setAbsValue();
        setNorm();
    }

    private void setNorm()
    {
        norm = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2))) * 100000;
    }

    public Vector getInstance()
    {
        Vector vector = new Vector();
        vector.copyVector(this);
        return (vector);
    }

    public void applyCoef(double coef)
    {
        this.norm /= coef;
    }
}
