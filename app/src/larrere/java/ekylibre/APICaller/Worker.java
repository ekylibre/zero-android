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

public class Worker {

    private static final String TAG = "Worker";
    public int id;
    public String name;
    public String number;
    private String qualification;

    public static List<Worker> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/workers || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/workers", attributes);
        List<Worker> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Worker(json.getJSONObject(i)));

        return array;
    }

    private Worker(JSONObject object) throws JSONException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Worker : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        qualification = object.getString("number");

        if (!object.isNull("number"))
            number = object.getString("number");
    }
}
