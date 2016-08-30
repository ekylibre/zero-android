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

public class Plant
{
    private static final String TAG = "Plant";
    int     mId;
    int     mActivityID;
    String  mName;
    String  mVariety;
    //shape

    public static List<Plant> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/plants || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/plants", params);
        List<Plant> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new Plant(json.getJSONObject(i)));
        }
        return array;
    }

    public Plant(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Plant : " + object.toString());

        mId = object.getInt("id");
        mName = object.getString("name");
        mVariety = object.getString("variety");
        if (!object.isNull("activity_id"))
            mActivityID = object.getInt("activity_id");
        //mShape = (float)object.getDouble("shape");
    }

    public int getId() {
        return mId;
    }

    public int getActivityID() {
        return mActivityID;
    }

    public String getName() {
        return mName;
    }

    public String getVariety() {
        return mVariety;
    }




}
