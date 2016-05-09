package ekylibre.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by antoine on 22/04/16.
 */
public class PlantCountingItem {
    private List<Integer> mValues;
    private Float mAverage;
    private String mObservations;

    public PlantCountingItem(List<Integer> values, Float average, String observations){
        mValues = values;
        mAverage = average;
        mObservations = observations;
    }
}
