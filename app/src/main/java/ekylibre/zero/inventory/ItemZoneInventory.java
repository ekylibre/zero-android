package ekylibre.zero.inventory;


import android.content.ClipData;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ekylibre.APICaller.Instance;
import ekylibre.APICaller.Plant;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

public class ItemZoneInventory {
    public String dateInventory;
    public String zone;
    String Icon_zone;
    String shape;
    int zone_id;

    public String getDateInventory() {
        return dateInventory;
    }

    public void setDateInventory(String dateInventory) {
        this.dateInventory = dateInventory;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getIcon_zone() {
        return Icon_zone;
    }

    public void setIcon_zone(String icon_zone) {
        Icon_zone = icon_zone;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public int getZone_id() {
        return zone_id;
    }

    public void setZone_id(int zone_id) {
        this.zone_id = zone_id;
    }

    public ItemZoneInventory(String dateInventory, String zone, String icon_zone) {
        this.dateInventory = dateInventory;
        this.zone = zone;

        Icon_zone = icon_zone;

    }

    public static List<ItemZoneInventory> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d("TAG", "Get JSONArray => /api/v1/building_divisions || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/building_divisions", params);
        List<ItemZoneInventory> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new ItemZoneInventory(json.getJSONObject(i)));
        }
        return array;
    }



    public ItemZoneInventory(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Zone : " + object.toString());

        zone_id = object.getInt("id");
        zone = object.getString("name");
        //dateInventory =  object.getString("date_zone");
        shape = object.getString("shape");
    }



    public ItemZoneInventory(String zone) {
        this.zone = zone;
        this.dateInventory = null;
        this.Icon_zone = null;
    }

    @Override
    public String toString() {
        return dateInventory + ',' + zone ;
    }
}


/*

 */