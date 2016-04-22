package ekylibre.api;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;

/**
 * Created by antoine on 21/04/16.
 */
public class PlantDensityAbacus {

    private String mName;
    private String mVariety;
    private double mGerminationPercentage;
    private String mSeedingDensityUnit;
    private String mSamplingLenghtUnit;
    private List<PlantDensityAbacusItem> mItems;


    public static List<PlantDensityAbacus> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;

        JSONArray json = instance.getJSONArray("/api/v1/issues", params);

        List<PlantDensityAbacus> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ ){

            array.add(new PlantDensityAbacus(json.getJSONObject(i)));
        }

        return array;
    }

    public PlantDensityAbacus(JSONObject object) throws JSONException{

        List<PlantDensityAbacusItem> items = new ArrayList<>();

        mName = object.getString("name");
        mVariety =object.getString("variety");
        mGerminationPercentage = object.getDouble("germination_percentage");
        mSeedingDensityUnit = object.getString("seeding_density_unit");
        mSamplingLenghtUnit =  object.getString("sampling_lenght_unit");
        mItems = items;
    }

    public PlantDensityAbacus(String name, String variety, double germinationPercentage, String seedingDensityUnit, String samplingLenghtUnit, List<PlantDensityAbacusItem> items ){
        mName = name;
        mVariety = variety;
        mGerminationPercentage = germinationPercentage;
        mSeedingDensityUnit = seedingDensityUnit;
        mSamplingLenghtUnit = samplingLenghtUnit;
        mItems = items;
    }
}
