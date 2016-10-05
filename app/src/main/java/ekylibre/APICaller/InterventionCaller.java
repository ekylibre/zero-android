package ekylibre.APICaller;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.database.InterventionORM;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 8/31/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/
public class InterventionCaller extends BaseCaller
{
    private static final String TAG = "InterventionCaller";
    public static final String TARGET = "target";
    public static final String REQUESTED = "request";
    public static final String RECORDED = "record";
    public static final String REQUEST = "request";
    public static final String RECORD = "record";

    List<InterventionORM> listIntervention;


    public void post(Instance instance, JSONObject attributes)
    {
        JSONObject params = attributes;
        //TODO :: Update path to API
        JSONObject json = instance.post("/api/v1/interventions", params);
    }

    public void get(Instance instance, String attributes) throws
            JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/interventions || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/interventions", params);
        listIntervention = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            listIntervention.add(new InterventionORM(json.getJSONObject(i)));
        }
    }

}
