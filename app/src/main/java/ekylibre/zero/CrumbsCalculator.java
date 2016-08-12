package ekylibre.zero;

import android.location.Location;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class CrumbsCalculator
{
    private final int pointPerMeter = 3;
    private float       averageSpeed = 1;
    private ArrayList<Crumb> tmpCrumbList;
    private Crumb finalCrumb = new Crumb();
    private final String TAG = "CrumbsCalculator";

    public CrumbsCalculator()
    {
        tmpCrumbList = new ArrayList<>();
    }

    public boolean isSampleReady(Location location, String type)
    {
        if (location.getAccuracy() > 5 || !type.equals("point"))
            return (false);

        Log.d(TAG, "====================================================================");

        Log.d(TAG, "ACCURACY =  = = = =  "  + location.getAccuracy());

        Crumb newCrumb = new Crumb(location);
        tmpCrumbList.add(newCrumb);
        updateAverageSpeed();
        if (tmpCrumbList.size() >= 3)
        {
            setFinalCrumb();
            Log.d(TAG, "==========");
            Log.d(TAG, "==========");
            Log.d(TAG, "==========");
            Log.d(TAG, "==========");
            Log.d(TAG, "==========");
            Log.d(TAG, "==========");
            return (true);
        }
        return (false);
    }

    private void setFinalCrumb()
    {
        finalCrumb.speed = averageSpeed;
        finalCrumb.date = getAverageDate();
        finalCrumb.latitude = averageLatitude();
        finalCrumb.longitude = averageLongitude();
        clearTmpList();
        Log.d(TAG, "Final Crumb set => tmp list");
        Log.d(TAG, "SIZE = " + tmpCrumbList.size());
    }

    private long getAverageDate()
    {
        int i = -1;
        long total = 0;
        Crumb crumb;

        while (++i < tmpCrumbList.size())
        {
            crumb = tmpCrumbList.get(i);
            total += crumb.date;
        }
        return (total / i);
    }

    private void    clearTmpList()
    {
        int i = -1;
        int max = tmpCrumbList.size();

        while (++i < max)
        {
            tmpCrumbList.remove(0);
        }
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

    public long getNexPointDelay()
    {
        if (averageSpeed == 0)
            return (100);
        Log.d(TAG, "DELAY FOR NEXT POINT =>" + (long)((1 / (averageSpeed * pointPerMeter)) *  100));
        return ((long)((1 / (averageSpeed * pointPerMeter)) *  100));
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

        Log.d(TAG, "SIZE of tmpList " + tmpCrumbList.size());

        while (++i < tmpCrumbList.size())
        {
            crumb = tmpCrumbList.get(i);
            total += crumb.speed;
        }
        if (i != 0)
            averageSpeed = total / i;
        else
            averageSpeed = 0;
        Log.d(TAG, "AVERAGE SPEED =>" + averageSpeed);
    }

}
