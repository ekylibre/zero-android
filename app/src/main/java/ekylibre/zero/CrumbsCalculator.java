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
    private final int           maxAccuracy = 5;
    private Crumb               prevCrumb = new Crumb();
    private Crumb               currCrumb = new Crumb();
    private Crumb               finalCrumb = new Crumb();

    private Vector              prevVector = new Vector();
    private Vector              currVector = new Vector();
    private Vector              averageVector = new Vector();
    private ArrayList<Vector>   listVector = new ArrayList<>();
    private final String        TAG = "CrumbsCalculator";
    private boolean             firstPass = true;

    public CrumbsCalculator()
    {
    }

    public boolean isSampleReady(Location location, String type)
    {
        if (location.getAccuracy() > maxAccuracy || !type.equals("point"))
            return (false);
        Log.d(TAG, "====================================================================");
        Log.d(TAG, "ACCURACY =  = = = =  "  + location.getAccuracy());
        if (firstPass)
        {
            firstPass = false;
            currCrumb.setCrumb(location);
        }
        else if (!currVector.set)
        {
            currVector.set = true;
            prevCrumb.copyCrumb(currCrumb);
            currCrumb.setCrumb(location);
            currVector.setVectorCoord(prevCrumb.getLongitude(), currCrumb.getLongitude(),
                    prevCrumb.getLatitude(), currCrumb.getLatitude());
            listVector.add(currVector);
        }
        else
        {
            prevCrumb.copyCrumb(currCrumb);
            currCrumb.setCrumb(location);
            prevVector.copyVector(currVector);
            currVector.setVectorCoord(prevCrumb.getLongitude(), currCrumb.getLongitude(),
                    prevCrumb.getLatitude(), currCrumb.getLatitude());
            listVector.add(currVector);

        }

        updateAverageVector();
        return (false);
    }

    private void updateAverageVector()
    {
        if (listVector.size() < 2)
            return;
        int i = -1;
        double newX = 0;
        double newY = 0;

        while (++i < listVector.size())
        {
            Vector vector = listVector.get(i);
            newX += vector.x;
            newY += vector.y;
        }
        newX /= i;
        newY /= i;
        averageVector.setVectorFinalCoord(newX, newY);
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
