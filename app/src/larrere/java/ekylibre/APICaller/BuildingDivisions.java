package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

import static ekylibre.util.Helper.iso8601;

/**
 * Created by antoine on 22/04/16.
 */

public class BuildingDivisions {

    private static final String TAG = "BuildingDivisions";
    private static final DecimalFormat df = new DecimalFormat("###.#", DecimalFormatSymbols.getInstance(Locale.FRENCH));

    public int id;
    public String name;
    public String net_surface_area;
    public Date deadAt;

    public static List<BuildingDivisions> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/building_divisions || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/building_divisions", attributes);
        List<BuildingDivisions> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new BuildingDivisions(json.getJSONObject(i)));

        return array;
    }

    private BuildingDivisions(JSONObject object) throws JSONException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object building_divisions : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");

        if (!object.isNull("dead_at"))
            deadAt = iso8601.parse(object.getString("dead_at"));

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
