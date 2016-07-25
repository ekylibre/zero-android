package ekylibre.api;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ekylibre.exceptions.HTTPException;

/**
 * Created by antoine on 22/04/16.
 */
public class PlantCountingItem {
    private List<Integer> mValues;
    private Float mAverage;
    private String mObservations;

    public PlantCountingItem(List<Integer> values, Float average, String observations)
    {
        mValues = values;
        mAverage = average;
        mObservations = observations;
    }

    public static long create(Instance instance, JSONObject attributes)
            throws JSONException, ClientProtocolException, IOException, HTTPException
    {
        JSONObject json = instance.post("/api/v1/plant_counting_items", attributes);
        long id = json.getLong("id");

        return (id);
    }
}
