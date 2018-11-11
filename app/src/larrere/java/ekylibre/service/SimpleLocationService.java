package ekylibre.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import ekylibre.zero.ObservationActivity;

import static android.location.LocationProvider.AVAILABLE;
import static android.location.LocationProvider.OUT_OF_SERVICE;
import static android.location.LocationProvider.TEMPORARILY_UNAVAILABLE;


public class SimpleLocationService extends Service {

    private static final String TAG = "LocationProvider";

    private static final int INTERVAL = 10000;  // 10 sec
    private static final int DISTANCE = 1;

    private LocationManager locationManager = null;

    private class LocationListener implements android.location.LocationListener {

        Location location;

        LocationListener(String provider) {
            location = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            this.location.set(location);
            ObservationActivity.location = location;
            Log.i(TAG, "New Location " + location.getAccuracy());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast toast = Toast.makeText(getBaseContext(), "Le GPS de votre smartphone est éteint...", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
            toast.show();
        }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case TEMPORARILY_UNAVAILABLE:
                    break;
                case OUT_OF_SERVICE:
                    break;
                case AVAILABLE:
                    break;
            }
        }
    }

    LocationListener locationListener = new LocationListener(LocationManager.GPS_PROVIDER);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        initializeLocationManager();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, locationListener);
        } catch (SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore --> " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(locationListener);
            } catch (Exception ex) {
                Log.e(TAG, "fail to remove location listeners, ignore" + ex.getMessage());
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
}