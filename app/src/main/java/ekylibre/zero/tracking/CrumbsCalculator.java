package ekylibre.zero.tracking;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class CrumbsCalculator
{
    private final int           MAX_ACCURACY = 6;
    private final int           VALUES_FOR_AVERAGE = 8;
    private final float         MAX_SPEED_ACCEPTED_WITHOUT_CHECK = 5;

    private double              vectorCoef = 1.0;

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

    public boolean isCrumbAccurate(Location location, String type)
    {
        if (location.getAccuracy() > MAX_ACCURACY || !type.equals("point"))
        {
            vectorCoef++;
            return (false);
        }
        Log.d(TAG, "====================================================================");
        Log.d(TAG, "ACCURACY =  = = = =  "  + location.getAccuracy());
        if (firstPass)
        {
            firstSet(location);
            return (true);
        }
        else if (!currVector.set)
        {
            firstVectorSet(location);
            return (true);
        }
        else
        {
            prevCrumb.copyCrumb(currCrumb);
            currCrumb.setCrumb(location);
            prevVector.copyVector(currVector);
            currVector.setVectorCoord(prevCrumb.getLongitude(), currCrumb.getLongitude(),
                    prevCrumb.getLatitude(), currCrumb.getLatitude());
            currVector.applyCoef(vectorCoef);
            vectorCoef = 1.0;
            updateAverageVector();
            Log.d(TAG, "Average vector = " + averageVector.norm);
            Log.d(TAG, "Curr vector = " + currVector.norm);
            if (checkCrumb())
            {
                listVector.add(currVector.getInstance());
                setFinalCrumb();
                return (true);
            }
        }
        vectorCoef++;
        return (false);
    }

    private void firstVectorSet(Location location)
    {
        currVector.set = true;
        prevCrumb.copyCrumb(currCrumb);
        currCrumb.setCrumb(location);
        currVector.setVectorCoord(prevCrumb.getLongitude(), currCrumb.getLongitude(),
                prevCrumb.getLatitude(), currCrumb.getLatitude());
        currVector.applyCoef(vectorCoef);
        vectorCoef = 1.0;
        listVector.add(currVector.getInstance());
        setFinalCrumb();
        if (listVector.size() < VALUES_FOR_AVERAGE)
            currVector.set = false;
    }

    private void firstSet(Location location)
    {
        firstPass = false;
        currCrumb.setCrumb(location);
        setFinalCrumb();
    }

    private boolean checkCrumb()
    {
        if ((currVector.norm < averageVector.norm * (1 + 4.00) && currVector.norm > averageVector.norm * (1 - 0.80)))
        {
            return (true);
        }
        else
        {
            // Here we should be able to make the vector smoother
            // There is some work on the end of this algo because we'll don't ignore vector anymore
            // ATM I will just ignore the vector and change variables as if he had never existed
            currCrumb.copyCrumb(prevCrumb);
            return (false);
        }
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
            Log.d(TAG, "Norm[" + i + "] = " + vector.norm);
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
