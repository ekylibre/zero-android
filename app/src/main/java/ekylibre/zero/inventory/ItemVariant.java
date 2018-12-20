package ekylibre.zero.inventory;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.APICaller.Instance;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

public class ItemVariant {
    public String name;
    int var_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVar_id() {
        return var_id;
    }

    public void setVar_id(int var_id) {
        this.var_id = var_id;
    }

    public ItemVariant(int var_id, String name) {
        this.var_id = var_id;
        this.name = name;
    }

    public static List<ItemVariant> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d("TAG", "Get JSONArray => /api/v1/variants || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/variants", params);
        List<ItemVariant> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new ItemVariant(json.getJSONObject(i)));
        }
        return array;
    }



    public ItemVariant(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Zone : " + object.toString());
        var_id = object.getInt("variant_id");
        name = object.getString("variant_name");

    }


    @Override
    public String toString() {
        return var_id + " : "+ name;
    }
}

