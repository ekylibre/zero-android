package ekylibre.api;

import ekylibre.exceptions.HTTPException;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import org.json.JSONException;
import org.json.JSONObject;

public class CrumbPoster
{
    public static long create(Instance instance, JSONObject attributes) throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;
        JSONObject json = instance.post("/api/v1/crumbs", params);
        long id = json.getLong("id");
        
        return id;
    }

}
