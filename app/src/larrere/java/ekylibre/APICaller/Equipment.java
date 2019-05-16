package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**
 * Created by antoine on 22/04/16.
 */

public class Equipment {

    private static final String TAG = "Equipment";
    public int id;
    public String name;
    public String number;
    public String variety;
    public String abilities;

    public static List<Equipment> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/equipments || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/equipments", attributes);
        List<Equipment> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Equipment(json.getJSONObject(i)));

        return array;
    }

    private Equipment(JSONObject object) throws JSONException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Equipment : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        number = object.getString("number");
        variety = object.getString("variety");

        abilities = null;
        if (!object.isNull("abilities")) {
            JSONArray array = object.getJSONArray("abilities");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i < array.length(); i++) {
                sb.append(array.getString(i));
                sb.append(",");
            }
            sb.append("is ").append(variety);
            abilities = sb.toString();
        }
    }
}
