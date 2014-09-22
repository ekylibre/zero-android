package ekylibre.zero;

import android.location.Location;
import android.os.Bundle;

public interface TrackingListenerWriter {
    public void writeCrumb(Location location, String type, Bundle options);
}
