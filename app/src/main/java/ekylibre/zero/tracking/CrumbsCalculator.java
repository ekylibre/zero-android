package ekylibre.zero.tracking;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class CrumbsCalculator
{
    private final int           MAX_ACCURACY = 10;
    private final int           VALUES_FOR_AVERAGE = 8;
    private final float         MAX_SPEED_ACCEPTED_WITHOUT_CHECK = 5;
    private String              lastProcedureNature;

    private double              vectorCoef = 1.0;

    private Crumb               prevCrumb = new Crumb();
    private Crumb               currCrumb = new Crumb();
    private Crumb               finalCrumb = new Crumb();

    private Vector              currVector = new Vector();
    private Vector              averageVector = new Vector();

    private ArrayList<Vector>   listVector = new ArrayList<>();
    private final String        TAG = "CrumbsCalculator";
    private boolean             firstPass = true;

    public CrumbsCalculator(String lastProcedureNature)
    {
        this.lastProcedureNature = lastProcedureNature;
    }

    public boolean isCrumbAccurate(Location location, String type, Bundle metadata)
    {
        if (location.getAccuracy() > MAX_ACCURACY && type.equals("point"))
        {
            vectorCoef++;
            return (false);
        }
        else if (type.equals("stop"))
        {
            setLast(location, metadata, type);
            return (true);
        }
        if (BuildConfig.DEBUG) Log.d(TAG, "====================================================================");
        if (BuildConfig.DEBUG) Log.d(TAG, "ACCURACY =  = = = =  "  + location.getAccuracy());
        if (firstPass)
        {
            firstSet(location, metadata, type);
            return (true);
        }
        else if (!currVector.set)
        {
            firstVectorSet(location, metadata, type);
            return (true);
        }
        else
        {
            Vector newVector = new Vector();
            newVector.set = true;
            prevCrumb.copyCrumb(currCrumb);
            currCrumb.setCrumb(location, metadata, type);

            newVector.setVectorCoord(prevCrumb.getLongitude(), currCrumb.getLongitude(),
                    prevCrumb.getLatitude(), currCrumb.getLatitude());
            newVector.applyCoef(vectorCoef);

            updateAverageVector();
            if (BuildConfig.DEBUG) Log.d(TAG, "Average vector = " + averageVector.norm);
            if (BuildConfig.DEBUG) Log.d(TAG, "Curr vector = " + newVector.norm);
            if (checkCrumb(newVector))
            {
                listVector.add(currVector.getInstance());
                setFinalCrumb();
                return (true);
            }
        }
        return (false);
    }

    public void setLast(Location location, Bundle metadata, String type)
    {
        currCrumb.setCrumb(location, metadata, type);
        setFinalCrumb();
    }

    private void firstVectorSet(Location location, Bundle metadata, String type)
    {
        currVector.set = true;
        prevCrumb.copyCrumb(currCrumb);
        currCrumb.setCrumb(location, metadata, type);
        currVector.setVectorCoord(prevCrumb.getLongitude(), currCrumb.getLongitude(),
                prevCrumb.getLatitude(), currCrumb.getLatitude());
        currVector.applyCoef(vectorCoef);
        vectorCoef = 1.0;
        listVector.add(currVector.getInstance());
        setFinalCrumb();
        if (listVector.size() < VALUES_FOR_AVERAGE)
            currVector.set = false;
        else
            currVector.set = true;
    }

    private void firstSet(Location location, Bundle metadata, String type)
    {
        metadata = new Bundle();
        firstPass = false;
        metadata.putString("procedure_nature", lastProcedureNature);
        currCrumb.setCrumb(location, metadata, "start");
        setFinalCrumb();
        if (BuildConfig.DEBUG) Log.d(TAG, "TYPE => " + type);
    }

    private boolean checkCrumb(Vector vector)
    {
        if ((vector.norm < averageVector.norm * (1 + 0.90) && vector.norm > averageVector.norm * (1 - 0.80)))
        {
            currVector.copyVector(vector);
            vectorCoef = 1.0;
            return (true);
        }
        else
        {
            // Here we should be able to make the vector smoother
            // to place a non-real crumb which will match well with the global path
            // There is some work on the end of this algo because we'll don't ignore vector anymore
            // ATM I will just ignore the vector and change variables as if he had never existed
            vectorCoef++;
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
            if (BuildConfig.DEBUG) Log.d(TAG, "Norm[" + i + "] = " + vector.norm);
        }
        newX /= i;
        newY /= i;
        averageVector.setVectorFinalCoord(newX, newY);
    }

    private void setFinalCrumb()
    {
        finalCrumb.copyCrumb(currCrumb);
        if (BuildConfig.DEBUG) Log.d(TAG, "LATITUDE > " + finalCrumb.latitude + " ## AND ## LONGITUDE > " + finalCrumb.longitude);
        if (BuildConfig.DEBUG) Log.d(TAG, "Final Crumb set => tmp list");
    }

    public Crumb getFinalCrumb()
    {
        return (this.finalCrumb);
    }

}
