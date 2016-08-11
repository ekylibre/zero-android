package ekylibre.zero;

import android.location.Location;

import java.util.ArrayList;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class CrumbsCalculator
{
    private final int pointPerMeter = 3;
    private float       averageSpeed;
    private ArrayList<Crumb> tmpCrumbList;
    private Crumb finalCrumb = null;

    public CrumbsCalculator()
    {
        tmpCrumbList = new ArrayList<>();
    }

    public boolean isSampleReady(Location location, String type)
    {
        if (!type.equals("point"))
            return (false);
        Crumb newCrumb = new Crumb(location);
        tmpCrumbList.add(newCrumb);
        updateAverageSpeed();
        if (averageSpeed - tmpCrumbList.size() == 0)
        {
            setFinalCrumb();
            return (true);
        }
        return (false);
    }

    private void setFinalCrumb()
    {
        finalCrumb.speed = averageSpeed;
        finalCrumb.
    }

    public Crumb getFinalCrumb()
    {
        return (this.finalCrumb);
    }

    private void updateAverageSpeed()
    {
        int i = -1;
        int total = 0;
        Crumb crumb;

        while (++i < tmpCrumbList.size())
        {
            crumb = tmpCrumbList.get(i);
            total += crumb.speed;
        }
        averageSpeed = total / i;
    }

}
