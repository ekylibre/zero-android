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
    private final int           pointPerMeter = 1;
    private final int           maxAccuracy = 5;
    private float               averageSpeed = 1;
    private Crumb               prevCrumb = new Crumb();
    private Crumb               currCrumb = new Crumb();
    private Vector              prevVector = new Vector();
    private Vector              currVector = new Vector();
    private Crumb               finalCrumb = new Crumb();
    private final String        TAG = "CrumbsCalculator";

    public CrumbsCalculator()
    {
    }

    public boolean isSampleReady(Location location, String type)
    {
        if (location.getAccuracy() > maxAccuracy || !type.equals("point"))
            return (false);

        Log.d(TAG, "====================================================================");

        Log.d(TAG, "ACCURACY =  = = = =  "  + location.getAccuracy());

        Crumb newCrumb = new Crumb(location);
        return (false);
    }

    private void setFinalCrumb()
    {
        finalCrumb.speed = currCrumb.speed;
        finalCrumb.date = currCrumb.date;
        finalCrumb.latitude = currCrumb.latitude;
        finalCrumb.longitude = currCrumb.longitude;
        Log.d(TAG, "LATITUDE > " + finalCrumb.latitude + " ## AND ## LONGITUDE > " + finalCrumb.longitude);
        Log.d(TAG, "Final Crumb set => tmp list");
    }

    public Crumb getFinalCrumb()
    {
        return (this.finalCrumb);
    }

}
