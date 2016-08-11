package ekylibre.zero;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class TrackingListener implements LocationListener {

    private TrackingListenerWriter mWriter;
    private String mType;
    private Bundle mOptions;

    public TrackingListener(TrackingListenerWriter writer) {
        this(writer, "point");
    }

    public TrackingListener(TrackingListenerWriter writer, String type) {
        this(writer, "point", null);
    }

    public TrackingListener(TrackingListenerWriter writer, String type, Bundle options) {
        mWriter  = writer;
        mType    = type;
        mOptions = options;
    }

    public void onLocationChanged(Location location) {
        mWriter.writeCrumb(location, mType, mOptions);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}
    
    public void onProviderDisabled(String provider) {}

}
