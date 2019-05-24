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

public class Input {

    private static final String TAG = "Equipment";
    public int id;
    public String name;
    public String reference_name;
    public String quantity_unit_name;
    public String variety;
    public String abilities;

    public static List<Input> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/inputs || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/inputs", attributes);
        List<Input> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Input(json.getJSONObject(i)));

        return array;
    }

    private Input(JSONObject object) throws JSONException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Input : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        reference_name = object.getString("reference_name");
        quantity_unit_name = object.getString("quantity_unit_name");
        if (!object.isNull("variety"))
            variety = object.getString("variety");

        abilities = null;
        if (!object.isNull("abilities")) {
            JSONArray array = object.getJSONArray("abilities");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i < array.length(); i++)
                sb.append(array.getString(i)).append(",");
            sb.append(variety);
            abilities = sb.toString();
        }
    }
}
