package ekylibre.zero;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class CrumbsCalculator
{
    private final int pointPerMeter = 3;
    private float       averageSpeed;
    private ArrayList<Crumb> tmpCrumbList;
    private Crumb finalCrumb = new Crumb();

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


    //TODO TMP COMMENT POINT PAR SEC PAS CALC !! FINIR CETTE METHODE + METHODE DE CALCUL + WRITE CRUMB TRACKING ACTIVITY !!
    private void setFinalCrumb()
    {
        finalCrumb.speed = averageSpeed;
        finalCrumb.date = new Date().getTime();
        finalCrumb.latitude = averageLatitude();
        finalCrumb.longitude = averageLongitude();
    }

    private double averageLatitude()
    {
        int i = -1;
        double total = 0;
        Crumb crumb;

        while (++i < tmpCrumbList.size())
        {
            crumb = tmpCrumbList.get(i);
            total += crumb.latitude;
        }
        return (total / i);
    }

    private double averageLongitude()
    {
        int i = -1;
        double total = 0;
        Crumb crumb;

        while (++i < tmpCrumbList.size())
        {
            crumb = tmpCrumbList.get(i);
            total += crumb.longitude;
        }
        return (total / i);
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
