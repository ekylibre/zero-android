package ekylibre.zero.intervention;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.google.maps.android.geojson.GeoJsonPolygon;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.util.AccountTool;
import ekylibre.util.PermissionManager;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.home.Zero;

public class MapsActivity extends UpdatableActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Account mAccount;
    private Marker  currentMarker = null;
    private int     interventionID = -1;
    private ArrayList<GeoJsonLayer> arrayLayers = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAccount = AccountTool.getCurrentAccount(this);
        interventionID = getIntent().getIntExtra(InterventionActivity._interventionID, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if (mMap == null)
            this.finish();
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;

        if (!PermissionManager.GPSPermissions(this, this))
            return;

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        resetPrecedentMarkers(getIntent().getIntExtra(InterventionActivity._interventionID, 0));
        if (lastKnownLocation != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        setPolygonLayers();
    }

    private void setPolygonLayers()
    {
        Log.d("MAP", "Setting Polygons");
        Cursor curs = getContentResolver().query(ZeroContract.InterventionParameters.CONTENT_URI,
                ZeroContract.InterventionParameters.PROJECTION_SHAPE,
                ZeroContract.InterventionParameters.FK_INTERVENTION + " == " + interventionID,
                null,
                null);
        if (curs == null || curs.getCount() == 0)
        {
            Log.d("MAP", "Baaaaaaaaaaaaaaaaaaaaaad");
            return;
        }
        arrayLayers = new ArrayList<>();
        while (curs.moveToNext())
        {
            try
            {
                JSONObject json;
                String geoJson = curs.getString(0);
                if (geoJson != null)
                {
                    Log.d("MAP", geoJson);
                    json = new JSONObject(geoJson);
                    GeoJsonLayer layer = new GeoJsonLayer(mMap, json);
                    arrayLayers.add(layer);
                    setPolygonGrey(layer);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        Log.d("MAP", "OKAAAAAAAAAAAAAAAAAAAAAAAAAY");
    }

    private void setPolygonGreen(GeoJsonLayer layer)
    {
        GeoJsonPolygonStyle polyStyle = layer.getDefaultPolygonStyle();
        polyStyle.setFillColor(getResources().getColor(R.color.light_green));
        polyStyle.setStrokeColor(getResources().getColor(R.color.dark_green));
        polyStyle.setStrokeWidth(3f);
        layer.addLayerToMap();
    }

    private void setPolygonGrey(GeoJsonLayer layer)
    {
        GeoJsonPolygonStyle polyStyle = layer.getDefaultPolygonStyle();
        polyStyle.setFillColor(getResources().getColor(R.color.basic_grey));
        polyStyle.setStrokeColor(getResources().getColor(R.color.ultra_dark_grey));
        polyStyle.setStrokeWidth(3f);
        layer.addLayerToMap();
    }

    @Override
    public void onPing(Intent intent)
    {
        interventionID = intent.getIntExtra(InterventionActivity._interventionID, 0);
        resetPrecedentMarkers(interventionID);
    }

    private Cursor queryMarkers(int interventionID)
    {
        if (interventionID == 0)
            return (null);
        String[] mProjectionMarkers = {ZeroContract.Crumbs.LATITUDE, ZeroContract.Crumbs.LONGITUDE};

        Cursor cursorMarkers = getContentResolver().query(
                ZeroContract.Crumbs.CONTENT_URI,
                mProjectionMarkers,
                ZeroContract.Plants.USER + " LIKE " + "\"" + mAccount.name + "\""
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

    private void drawLines(ArrayList<LatLng> pointList)
    {
        PolylineOptions options = new PolylineOptions().width(7).color(Color.BLUE).geodesic(true);
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
        if (mMap == null)
            return;
        currentMarker = mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(
//                        BitmapFactory.decodeResource(getResources(), R.drawable.pos_marker)))
                .position(new LatLng(latitude, longitude))
                .title(""));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));


    }

}
