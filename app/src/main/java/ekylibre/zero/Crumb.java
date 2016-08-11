package ekylibre.zero;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

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
}
