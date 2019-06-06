package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.BuildConfig;

import static ekylibre.util.Helper.iso8601;

/**
 * Created by antoine on 22/04/16.
 */

public class Output {

    private static final String TAG = "Output";

    public int id;
    public String name;
    public String reference_name;
    public String quantity_unit_name;
    public String variety;
    public String abilities;
    public Date deadAt;

    public static List<Output> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/outputs || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/outputs", attributes);
        List<Output> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Output(json.getJSONObject(i)));

        return array;
    }

    private Output(JSONObject object) throws JSONException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Output : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        reference_name = object.getString("reference_name");
        quantity_unit_name = object.isNull("quantity_unit_name") ? null : object.getString("quantity_unit_name");
        if (!object.isNull("variety"))
            variety = object.getString("variety");

        if (!object.isNull("dead_at"))
            deadAt = iso8601.parse(object.getString("dead_at"));

        abilities = null;
        if (!object.isNull("abilities")) {
            JSONArray array = object.getJSONArray("abilities");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i < array.length(); i++)
                sb.append("can ").append(array.getString(i)).append(",");

            // Fill varieties
            List<String> varieties = Grammar.computeItemAbilities("is " + variety);
            for (String var : varieties) {
                sb.append(var);
                if (varieties.indexOf(var) < varieties.size() - 1)
                    sb.append(",");
            }

            abilities = sb.toString();
        }
    }
}
