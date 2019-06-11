package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.BuildConfig;

public class Output {

    private static final String TAG = "Output";

    public int id;
    public String name;
    public String variety;
    public String number;
    public String abilities;

    public static List<Output> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/outputs || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/variants", attributes);
        List<Output> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Output(json.getJSONObject(i)));

        return array;
    }

    private Output(JSONObject object) throws JSONException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Object Output : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        variety = object.getString("variety");
        number = object.getString("number");

        abilities = computeAbilities(object.getJSONArray("nature_abilities"));
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
