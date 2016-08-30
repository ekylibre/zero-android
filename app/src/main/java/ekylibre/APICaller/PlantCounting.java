package ekylibre.APICaller;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
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
public class PlantCounting
{
    int     mId;
    String  mRead_at;

    public static List<PlantCounting> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;
        JSONArray json = instance.getJSONArray("/api/v1/plant_countings", params);
        List<PlantCounting> array = new ArrayList<>();

        for (int i = 0; i < json.length(); i++)
        {
            array.add(new PlantCounting(json.getJSONObject(i)));
        }

        return (array);
    }

    public static long create(Instance instance, JSONObject attributes)
            throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        JSONObject json = instance.post("/api/v1/plant_countings", attributes);
        long id = json.getLong("id");

        return (id);
    }

    public PlantCounting(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Plant : " + object.toString());

        mId = object.getInt("id");
        mRead_at = object.getString("read_at");
    }
}
