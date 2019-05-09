package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**
 * Created by antoine on 22/04/16.
 */

public class LandParcel {

    private static final String TAG = "LandParcel";
    public int id;
    public String name;
    public String net_surface_area;

    public static List<LandParcel> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/land_parcels || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/land_parcels", attributes);
        List<LandParcel> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new LandParcel(json.getJSONObject(i)));

        return array;
    }

    private LandParcel(JSONObject object) throws JSONException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object LandParcel : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");

        if (!object.isNull("net_surface_area"))
            net_surface_area = object.getString("net_surface_area");
    }
}
