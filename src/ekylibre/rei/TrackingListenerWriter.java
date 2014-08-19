package ekylibre.rei;

import android.location.Location;

import java.util.HashMap;

public interface TrackingListenerWriter {
    public void writeCrumb(Location location, String type, HashMap<String, String> options);
}
