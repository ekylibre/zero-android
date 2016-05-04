package ekylibre.api;

import android.graphics.drawable.shapes.Shape;
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

public class Plants {
    int mId;
    String mName;
    String mVariety;
    //shape
    Boolean mActive;

    public static List<Plants> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;

        JSONArray json = instance.getJSONArray("/api/v1/plants", params);

        List<Plants> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ ){

            array.add(new Plants(json.getJSONObject(i)));
        }

        return array;
    }

    public Plants(JSONObject object) throws JSONException{

        Log.d("zero", "Object Plants : " + object.toString());

        mId = object.getInt("id");
        mName = object.getString("name");
        mVariety =object.getString("variety_name");
        //mShape = (float)object.getDouble("shape");
        mActive = object.getBoolean("active");
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getVariety() {
        return mVariety;
    }

    public Boolean getActive() {
        return mActive;
    }


}
