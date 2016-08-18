package ekylibre.zero.tracking;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import ekylibre.api.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.util.AccountTool;
import ekylibre.zero.util.UpdatableActivity;

public class MapsActivity extends UpdatableActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Account mAccount;
    private Marker  currentMarker = null;

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
        resetPrecedentMarkers(getIntent().getIntExtra(TrackingActivity._interventionID, 0));
        if (lastKnownLocation != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
    }


    @Override
    public void onPing(Intent intent)
    {
        resetPrecedentMarkers(intent.getIntExtra(TrackingActivity._interventionID, 0));

    }

    private Cursor queryMarkers(int interventionID) {
        if (interventionID == 0)
            return (null);
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

    private void resetPrecedentMarkers(int interventionID)
    {
        ArrayList<LatLng> pointList = new ArrayList<>();
        Cursor cursor = queryMarkers(interventionID);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                LatLng point = new LatLng(cursor.getDouble(0), cursor.getDouble(1));
                pointList.add(point);
            }
            drawLines(pointList);
           if (cursor.moveToLast())
               setMarker(cursor.getDouble(0), cursor.getDouble(1));
        }
    }

    private void drawLines(ArrayList<LatLng> pointList) {
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        int i = -1;
        while (++i < pointList.size())
        {
            LatLng point = pointList.get(i);
            options.add(point);
        }
        mMap.addPolyline(options);
        if (i > 0)
        {
            LatLng currentPos = pointList.get(i - 1);
        }
    }

    private void setMarker(double latitude, double longitude)
    {
        if (currentMarker != null)
            currentMarker.remove();
        currentMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pos_marker)))
                .position(new LatLng(latitude, longitude))
                .title(""));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));


    }

}
