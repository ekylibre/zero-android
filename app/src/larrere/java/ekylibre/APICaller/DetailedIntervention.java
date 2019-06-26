package ekylibre.APICaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ekylibre.exceptions.HTTPException;


public class DetailedIntervention {

    public static long create(Instance instance, JSONObject attributes)
            throws JSONException, IOException, HTTPException {

        JSONObject json = instance.post("/api/v1/interventions", attributes);

        return json.isNull("id") ? -1 : json.getLong("id");
    }
}
