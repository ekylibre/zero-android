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

public class ItemType {
    public String name;
    int type_id;
    int cat_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public ItemType(int type_id, String name) {
        this.type_id = type_id;
        this.name = name;
    }

    public static List<ItemType> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d("TAG", "Get JSONArray => /api/v1/types || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/types", params);
        List<ItemType> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new ItemType(json.getJSONObject(i)));
        }
        return array;
    }



    public ItemType(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Zone : " + object.toString());
        type_id = object.getInt("id");
        name = object.getString("name");
        cat_id = object.getInt("category_id");


    }

    public ItemType( String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return type_id + " : "+ name;
    }
}

