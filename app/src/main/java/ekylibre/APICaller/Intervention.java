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

/**************************************
 * Created by pierre on 8/31/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/
public class Intervention
{
    private static final String TAG = "Plant";
    private int     mId;
    private String  mProcedureName;
    private int     number;
    private String  mName;
    private String  mStartedAt;
    private String  mStoppedAt;
    private String  mDescription;



    public static List<Intervention> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/interventions || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/interventions", params);
        List<Intervention> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new Intervention(json.getJSONObject(i)));
        }
        return array;
    }

    public Intervention(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Intervention : " + object.toString());

        mId = object.getInt("id");
        mName = object.getString("name");

    }

    public int getId() {
            return mId;
        }


    public String getName() {
            return mName;
        }

}
