package ekylibre.zero;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.concurrent.CancellationException;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class Crumb
{
    public float    speed;
    public LatLng   pos;
    public double   latitude;
    public double   longitude;
    public long     date;

    public Crumb(Location location)
    {
        speed = location.getSpeed();
        pos = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        date = new Date().getTime();
    }

    public Crumb()
    {
        speed = 0;
        pos = null;
        latitude = 0.0;
        longitude = 0.0;
        date = 0;
    }

    public double getLatitude()
    {
        return (latitude);
    }

    public double getLongitude()
    {
        return (longitude);
    }

    public long getDate()
    {
        return (date);
    }

    public float getSpeed()
    {
        return (speed);
    }

    public LatLng getPos()
    {
        return (pos);
    }

}
