package ekylibre.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;

/**
 * Created by antoine on 22/04/16.
 */
public class PlantCounting {
    int mId;
    String mRead_at;
    public static List<PlantCounting> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;

        JSONArray json = instance.getJSONArray("/api/v1/plants_counting", params);

        List<PlantCounting> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ ){

            array.add(new PlantCounting(json.getJSONObject(i)));
        }

        return array;
    }

    public PlantCounting(JSONObject object) throws JSONException{

        Log.d("zero", "Object Plant : " + object.toString());

        mId = object.getInt("id");
        mRead_at = object.getString("read_at");
    }
}