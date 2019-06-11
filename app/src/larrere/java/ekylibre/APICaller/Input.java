package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.BuildConfig;

/**
 * Created by antoine on 22/04/16.
 */

public class Input {

    private static final String TAG = "Input";

    public int id;
    public String name;
    public String number;
    public String variety;
    public String abilities;
    public BigDecimal population;
    public String containerName;

    public static List<Input> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Get JSONArray => /api/v1/inputs || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/products", attributes);
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
        number = object.getString("number");
        variety = object.getString("variety");
        population = new BigDecimal(object.getString("population"));
        abilities = computeAbilities(object.getJSONArray("abilities"));
        if (!object.isNull("container_name"))
            containerName = object.getString("container_name");
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
