package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**
 * Created by antoine on 22/04/16.
 */

public class Input {

    private static final String TAG = "Input";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.FRENCH);

    public int id;
    public String name;
    public String reference_name;
    public String quantity_unit_name;
    public String variety;
    public String abilities;
    public Date deadAt;

    public static List<Input> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/inputs || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/inputs", attributes);
        List<Input> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Input(json.getJSONObject(i)));

        return array;
    }

    private Input(JSONObject object) throws JSONException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Input : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        reference_name = object.getString("reference_name");
        quantity_unit_name = object.getString("quantity_unit_name");
        if (!object.isNull("variety"))
            variety = object.getString("variety");

        if (!object.isNull("dead_at"))
            deadAt = sdf.parse(object.getString("dead_at"));

        abilities = null;
        if (!object.isNull("abilities")) {
            JSONArray array = object.getJSONArray("abilities");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i < array.length(); i++)
                sb.append("can ").append(array.getString(i)).append(",");
            sb.append("is ").append(variety);
            abilities = sb.toString();
        }
    }
}
