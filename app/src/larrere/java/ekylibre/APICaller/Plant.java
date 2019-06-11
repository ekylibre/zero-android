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

public class Plant
{
    private static final String TAG = "Plant";
    private static final DecimalFormat df = new DecimalFormat("###.#", DecimalFormatSymbols.getInstance(Locale.FRENCH));


    public int mId;
    public int mActivityID;
    public String mName;
    public String mVariety;
    public String mActivityName;
    public String net_surface_area;
    public Date deadAt;
    //shape

    public static List<Plant> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException, ParseException {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/plants || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/plants", params);
        List<Plant> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new Plant(json.getJSONObject(i)));
        }
        return array;
    }

    public Plant(JSONObject object) throws JSONException, ParseException {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Plant : " + object.toString());

        mId = object.getInt("id");
        mName = object.getString("name");
        mVariety = object.getString("variety");

        if (!object.isNull("dead_at"))
            deadAt = iso8601.parse(object.getString("dead_at"));
        if (!object.isNull("activity_id"))
            mActivityID = object.getInt("activity_id");
        if (!object.isNull("activity_name"))
            mActivityName = object.getString("activity_name");
        //mShape = (float)object.getDouble("shape");

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

    public int getId() {
        return mId;
    }

    public int getActivityID() {
        return mActivityID;
    }

    public String getName() {
        return mName;
    }

    public String getVariety() {
        return mVariety;
    }

    public String getmActivityName() {
        return mActivityName;
    }
}
