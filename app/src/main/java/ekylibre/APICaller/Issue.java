package ekylibre.APICaller;

import ekylibre.exceptions.HTTPException;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by antoine on 29/03/16.
 */
public class Issue
{
    public static long create(Instance instance, JSONObject attributes)
            throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;
        JSONObject json = instance.post("/api/v1/issues", params);
        long id = json.getLong("id");

        return id;
    }
}
