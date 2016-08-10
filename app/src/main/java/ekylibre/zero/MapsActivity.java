package ekylibre.zero;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ekylibre.api.ZeroContract;
import ekylibre.zero.util.AccountTool;
import ekylibre.zero.util.UpdatableActivity;

public class MapsActivity extends UpdatableActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAccount = AccountTool.getCurrentAccount(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return  ;
        }


        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        setMarker(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }

    @Override
    public void onPing(Intent intent)
    {
        Cursor cursor = queryMarkers(intent.getIntExtra(TrackingActivity._interventionID, 0));
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                setMarker(cursor.getDouble(0), cursor.getDouble(1));
            }
        }
        setMarker(intent.getDoubleExtra(TrackingListenerWriter.LATITUDE, 0), intent.getDoubleExtra(TrackingListenerWriter.LONGITUDE, 0));
    }

    private Cursor queryMarkers(int interventionID) {
        String[] mProjectionMarkers = {ZeroContract.Crumbs.LATITUDE, ZeroContract.Crumbs.LONGITUDE};

        Cursor cursorMarkers = getContentResolver().query(
                ZeroContract.Crumbs.CONTENT_URI,
                mProjectionMarkers,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                        + " AND " + ZeroContract.Crumbs.FK_INTERVENTION + " == " + interventionID,
                null,
                null);
        return (cursorMarkers);

    }

    private void setMarker(double latitude, double longitude)
    {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
    }

}
