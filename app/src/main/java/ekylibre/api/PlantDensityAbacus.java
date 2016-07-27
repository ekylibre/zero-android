package ekylibre.api;

import android.util.Log;

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
public class PlantDensityAbacus
{
    private int mid;
    private String mName;
    private String mVariety;
    private double mGerminationPercentage;
    private String mSeedingDensityUnit;
    private String mSamplingLenghtUnit;
    public JSONArray mItems;


    public static List<PlantDensityAbacus> all(Instance instance, JSONObject attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        JSONObject params = attributes;
        JSONArray json = instance.getJSONArray("/api/v1/plant_density_abaci", params);
        List<PlantDensityAbacus> array = new ArrayList<>();

        for(int i = 0; i < json.length(); i++)
        {
            array.add(new PlantDensityAbacus(json.getJSONObject(i)));
        }
        return (array);
    }

    public PlantDensityAbacus(JSONObject object) throws JSONException
    {
        List<PlantDensityAbacusItem> items = new ArrayList<>();
        Log.d("zero", "Object PlantDensityAbacus : " + object.toString());

        mid = object.getInt("id");
        mName = object.getString("name");
        mVariety = object.getString("variety_name");
        mGerminationPercentage = object.getDouble("germination_percentage");
        mSeedingDensityUnit = object.getString("seeding_density_unit");
        mSamplingLenghtUnit = object.getString("sampling_length_unit");
        mItems = object.getJSONArray("items");
    }

    public PlantDensityAbacus(int id, String name, String variety, double germinationPercentage, String seedingDensityUnit, String samplingLenghtUnit, List<PlantDensityAbacusItem> items )
    {
        mid = id;
        mName = name;
        mVariety = variety;
        mGerminationPercentage = germinationPercentage;
        mSeedingDensityUnit = seedingDensityUnit;
        mSamplingLenghtUnit = samplingLenghtUnit;
    }

    public int getId() {
        return mid;
    }

    public String getName() {
        return mName;
    }

    public String getVariety() {
        return mVariety;
    }

    public double getGerminationPercentage() {
        return mGerminationPercentage;
    }

    public String getSeedingDensityUnit() {
        return mSeedingDensityUnit;
    }

    public String getSamplingLenghtUnit() {
        return mSamplingLenghtUnit;
    }

    public double getItemDensityValue(int index)
            throws JSONException
    {
        JSONObject object;
        object = mItems.getJSONObject(index);
        return (object.getDouble("seeding_density_value"));
    }

    public double getItemPlantCount(int index)
            throws JSONException
    {
        JSONObject object;
        object = mItems.getJSONObject(index);
        return (object.getInt("plants_count"));
    }

    public double getItemID(int index)
        throws JSONException
    {
        JSONObject object;
        object = mItems.getJSONObject(index);
        return (object.getInt("id"));
    }
}
