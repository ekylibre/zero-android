package ekylibre.zero.intervention;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**************************************
 * Created by pierre on 8/11/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/

/*
** This class define a crumb which is a located point in space.
**
** pos is the LatLng definition of the point.
** speed is the speed related to this point returned by android
** type can be "start" "point" or "stop"
** Start and stop points are auto generated
*/
public class Crumb
{
    public float    speed;
    public LatLng   pos;
    public double   latitude;
    public double   longitude;
    public long     date;
    public Bundle   metadata;
    public String   type;

    public Crumb(Location location, Bundle metadata, String type)
    {
        speed = location.getSpeed();
        pos = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        date = new Date().getTime();
        this.type = type;
    }

    public Crumb()
    {
        speed = 0;
        pos = null;
        latitude = 0.0;
        longitude = 0.0;
        date = 0;
        metadata = null;
        type = "point";
    }

    public void setCrumb(Location location, Bundle metadata, String type)
    {
        speed = location.getSpeed();
        pos = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        date = new Date().getTime();
        this.metadata = metadata;
        this.type = type;
    }

    public void copyCrumb(Crumb crumb)
    {
        speed = crumb.getSpeed();
        pos = new LatLng(crumb.getLatitude(), crumb.getLongitude());
        latitude = crumb.getLatitude();
        longitude = crumb.getLongitude();
        date = new Date().getTime();
        metadata = crumb.metadata;
        type = crumb.type;
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

    public Bundle getMetadata()
    {
        return (metadata);
    }

    public String getType()
    {
        return (type);
    }

    public LatLng getPos()
    {
        return (pos);
    }

}
