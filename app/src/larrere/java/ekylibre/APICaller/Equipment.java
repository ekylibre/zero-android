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

public class Equipment {

    private static final String TAG = "Equipment";

    public int id;
    public String name;
    public String number;
    public String workNumber;
    public String variety;
    public String abilities;
    public Date deadAt;

    public static List<Equipment> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/equipments || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/equipments", attributes);
        List<Equipment> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Equipment(json.getJSONObject(i)));

        return array;
    }

    private Equipment(JSONObject object) throws JSONException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Equipment : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        number = object.getString("number");
        workNumber = object.isNull("work_number") ? null : (object.getString("work_number").equals("") ? null : object.getString("work_number"));
        variety = object.getString("variety");
        deadAt = object.isNull("dead_at") ? null : (iso8601.parse(object.getString("dead_at")));
        abilities = computeAbilities(object.getJSONArray("abilities"));
    }

    private String computeAbilities(JSONArray array) throws JSONException {

        StringBuilder sb = new StringBuilder();

        // Compute abilities
        for (int i=0; i < array.length(); i++)
            sb.append("can ").append(array.getString(i)).append(",");

        // Compute varieties
        List<String> varieties = Grammar.computeItemAbilities("is " + variety);
        for (String var : varieties) {
            sb.append(var);
            if (varieties.indexOf(var) < varieties.size() - 1)
                sb.append(",");
        }

        return sb.toString();
    }
}
