package ekylibre.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ekylibre.exceptions.HTTPException;

/**
 * Created by antoine on 22/04/16.
 */
public class PlantsCounting {
    int mId;
    String mRead_at;
    public static List<PlantsCounting> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;

        JSONArray json = instance.getJSONArray("/api/v1/plants_counting", params);

        List<PlantsCounting> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ ){

            array.add(new PlantsCounting(json.getJSONObject(i)));
        }

        return array;
    }

    public PlantsCounting(JSONObject object) throws JSONException{

        Log.d("zero", "Object Plants : " + object.toString());

        mId = object.getInt("id");
        mRead_at = object.getString("read_at");
    }
}
