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

public class Variant {

    private static final String TAG = "Variant";

    public int id;
    public String name;
    public String number;
    public String variety;
    public String abilities;

    public static List<Variant> all(Instance instance, String attributes)
            throws JSONException, IOException, HTTPException {

        JSONArray json = instance.getJSONArray("/api/v1/variants", attributes);
        List<Variant> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Variant(json.getJSONObject(i)));

        return array;
    }

    private Variant(JSONObject object) throws JSONException {

//        if (BuildConfig.DEBUG)
//            Log.d(TAG, "Product object -> " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        variety = object.getString("variety");

        abilities = computeAbilities(object);

        if (object.has("number") && !object.isNull("number"))
            number = object.getString("number");
    }

    private String computeAbilities(JSONObject object) throws JSONException {

        StringBuilder sb = new StringBuilder();

        if (object.has("nature_abilities") && !object.isNull("nature_abilities")) {
            JSONArray array = object.getJSONArray("nature_abilities");
            for (int i = 0; i < array.length(); i++)
                sb.append("can ").append(array.getString(i)).append(",");
        }

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
