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

public class ItemCategory {
    public String name;
    int cat_id;
    String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public ItemCategory(int cat_id, String name) {
        this.cat_id = cat_id;
        this.name = name;
    }

    public ItemCategory( String name) {
        this.name = name;
    }

    public static List<ItemCategory> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d("TAG", "Get JSONArray => /api/v1/categories || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/categories", params);
        List<ItemCategory> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new ItemCategory(json.getJSONObject(i)));
        }
        return array;
    }



    public ItemCategory(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Zone : " + object.toString());
        cat_id = object.getInt("id");
        name = object.getString("name");
        label = object.getString("label");

    }




    @Override
    public String toString() {
        return cat_id + " : "+ name;
    }
}

