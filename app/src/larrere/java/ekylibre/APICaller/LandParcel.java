package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**
 * Created by antoine on 22/04/16.
 */

public class LandParcel {

    private static final String TAG = "LandParcel";
    private static final DecimalFormat df = new DecimalFormat("###.#", DecimalFormatSymbols.getInstance(Locale.FRENCH));

    public int id;
    public String name;
    public String net_surface_area;

    public static List<LandParcel> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/land_parcels || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/land_parcels", attributes);
        List<LandParcel> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new LandParcel(json.getJSONObject(i)));

        return array;
    }

    private LandParcel(JSONObject object) throws JSONException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object LandParcel : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");

        if (!object.isNull("net_surface_area")) {
            JSONObject json = object.getJSONObject("net_surface_area");
            String unit = json.getString("unit").equals("hectare") ? "ha" : json.getString("unit");
            String[] numbers = json.get("value").toString().split("/");
            if (numbers.length > 1) {
                Float surface = (Float.valueOf(numbers[0]) / Float.valueOf(numbers[1]));
                net_surface_area = df.format(surface) + " " + unit;
            } else
                net_surface_area = numbers[0] + " " + unit;
        }
    }
}
