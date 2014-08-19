package ekylibre.rei;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import java.util.HashMap;

public class TrackingListener implements LocationListener {

    private TrackingListenerWriter writer;
    private String type;
    private HashMap options;

    public TrackingListener(TrackingListenerWriter writer) {
        this(writer, "point");
    }

    public TrackingListener(TrackingListenerWriter writer, String type) {
        this(writer, "point", null);
    }

    public TrackingListener(TrackingListenerWriter writer, String type, HashMap<String, String> options) {
        this.writer = writer;
        this.type     = type;
        this.options  = options;
    }

		public void onLocationChanged(Location location) {
        this.writer.writeCrumb(location, this.type, this.options);
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onProviderDisabled(String provider) {}

}
