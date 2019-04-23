package ekylibre.zero.intervention;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ekylibre.database.ZeroContract;
import ekylibre.util.AccountTool;
import ekylibre.util.PermissionManager;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;

public class MapsActivity extends UpdatableActivity implements OnMapReadyCallback
{
    private final int  STROKE_GREEN = 0x387e40;
    private final int  FILL_GREEN   = 0x66aad2a5;
    private final int  STROKE_GREY  = 0x333333;
    private final int  FILL_GREY    = 0x66AAAAAA;

    private String TAG = "MAP";
    private GoogleMap mMap;
    private Account mAccount;
    private Marker currentMarker = null;
    private int interventionID = -1;
    private ArrayList<Parcel> arrayLayers = null;
    private GoogleApiClient client;


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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        setPolygonLayers();
        checkIntersection(new LatLng(0, 0));
    }

    private void setPolygonLayers()
    {
        if (BuildConfig.DEBUG) Log.d(TAG, "Setting Polygons");
        Cursor curs = getContentResolver().query(ZeroContract.InterventionParameters.CONTENT_URI,
                ZeroContract.InterventionParameters.PROJECTION_SHAPE,
                ZeroContract.InterventionParameters.FK_INTERVENTION + " == " + interventionID,
                null,
                null);
        if (curs == null || curs.getCount() == 0)
        {
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
                    if (BuildConfig.DEBUG) Log.d(TAG, geoJson);
                    json = new JSONObject(geoJson);

                    GeoJsonLayer layer = new GeoJsonLayer(mMap, json);

                    List<GeoJsonPolygon> polygons = parsePolygons(layer);

                    arrayLayers.add(new Parcel(layer, polygons));
                    setPolygonGrey(layer);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private List<GeoJsonPolygon> parsePolygons(GeoJsonLayer layer)
    {
        List<GeoJsonPolygon> polygons = null;
        GeoJsonMultiPolygon geometry;
        Iterable<GeoJsonFeature> features = layer.getFeatures();
        for (GeoJsonFeature feature : features)
        {
            geometry = (GeoJsonMultiPolygon) feature.getGeometry();
            polygons = geometry.getPolygons();
        }
        return (polygons);
    }

    private void setPolygonGreen(GeoJsonLayer layer)
    {
        GeoJsonPolygonStyle polyStyle = layer.getDefaultPolygonStyle();
        polyStyle.setFillColor(FILL_GREEN);
        polyStyle.setStrokeColor(STROKE_GREEN);
        polyStyle.setStrokeWidth(4f);
    }

    private void setPolygonGrey(GeoJsonLayer layer)
    {
        GeoJsonPolygonStyle polyStyle = layer.getDefaultPolygonStyle();
        polyStyle.setFillColor(FILL_GREY);
        polyStyle.setStrokeColor(STROKE_GREY);
        polyStyle.setStrokeWidth(4f);
    }

    @Override
    public void onPing(Intent intent)
    {
        interventionID = intent.getIntExtra(InterventionActivity._interventionID, 0);
        resetPrecedentMarkers(interventionID);
        LatLng point = new LatLng(
                intent.getDoubleExtra(TrackingListenerWriter.LATITUDE, 0),
                intent.getDoubleExtra(TrackingListenerWriter.LONGITUDE, 0));
        checkIntersection(point);
    }

    private void checkIntersection(LatLng point)
    {

        if (arrayLayers == null)
            return;
        for (Parcel parcel : arrayLayers)
        {
            if (BuildConfig.DEBUG) Log.d(TAG, "======= Checking polygons for parcel =======");
            for (GeoJsonPolygon polygon : parcel.polygons)
            {
                if (BuildConfig.DEBUG) Log.d(TAG, polygon.getCoordinates().toString());
                if (BuildConfig.DEBUG) Log.d(TAG, point.toString());

                if (PolyUtil.containsLocation(point, polygon.getCoordinates().get(0), true))
                    setPolygonGreen(parcel.layer);
                else
                    setPolygonGrey(parcel.layer);
            }
        }
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
