package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ekylibre.exceptions.HTTPException;


public class Observation {

    public static long create(Instance instance, JSONObject attributes)
            throws JSONException, IOException, HTTPException {

        JSONObject json = instance.post("/api/v1/yield_observations", attributes);
        Log.i("Observation POST", "Result id = " + json.getLong("id"));
        return json.getLong("id");
    }
}
