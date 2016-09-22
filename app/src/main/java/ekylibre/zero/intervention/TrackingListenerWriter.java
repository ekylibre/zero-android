package ekylibre.zero.intervention;

import android.location.Location;
import android.os.Bundle;

public interface TrackingListenerWriter {
    public final String LATITUDE = "LATITUDE";
    public final String LONGITUDE = "LONGITUDE";
    public void writeCrumb(Location location, String type, Bundle options);
}
