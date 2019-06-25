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
import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.BuildConfig;

import static ekylibre.util.Helper.iso8601;

public class Product {

    private static final String TAG = "Product";
    private static final DecimalFormat df = new DecimalFormat("###.#", DecimalFormatSymbols.getInstance(Locale.FRENCH));

    public int id;
    public String name;
    public String number;
    public String workNumber;
    public String variety;
    public String abilities;
    public String population;
    public String unit;
    public String containerName;
    public Date deadAt;
    public String netSurfaceArea;

    public static List<Product> all(Instance instance, String attributes)
            throws JSONException, IOException, HTTPException, ParseException {

//        if (BuildConfig.DEBUG)
//            Log.d(TAG, "Get JSONArray => /api/v1/products || params = " + attributes);

        JSONArray json = instance.getJSONArray("/api/v1/products", attributes);
        List<Product> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
            array.add(new Product(json.getJSONObject(i)));

        return array;
    }

    private Product(JSONObject object) throws JSONException, ParseException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Product object -> " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        variety = object.getString("variety");

        abilities = computeAbilities(object);

        if (object.has("number") && !object.isNull("number"))
            number = object.getString("number");

        if (object.has("work_number") && !object.isNull("work_number") && !object.getString("work_number").equals(""))
            workNumber = object.getString("work_number");

        if (object.has("variant_unit_name") && !object.isNull("variant_unit_name"))
            unit = object.getString("variant_unit_name");

        if (object.has("population") && !object.isNull("population")) {
            StringBuilder sb = new StringBuilder();
            sb.append(df.format(Float.parseFloat(object.getString("population"))));
            if (unit != null && !unit.equals(""))
                sb.append(" ").append(unit);
            population = sb.toString();
        }

        if (object.has("container_name") && !object.isNull("container_name"))
            containerName = object.getString("container_name");

        if (object.has("dead_at") && !object.isNull("dead_at"))
            deadAt = iso8601.parse(object.getString("dead_at"));
//            deadAt = iso8601Parser.parseDateTime(object.getString("dead_at")).toDate();

        if (object.has("net_surface_area") && !object.isNull("net_surface_area"))
            netSurfaceArea = getSurfaceArea(object.getJSONObject("net_surface_area"));
    }

    private String computeAbilities(JSONObject object) throws JSONException {

        StringBuilder sb = new StringBuilder();

        if (object.has("abilities") && !object.isNull("abilities")) {
            JSONArray array = object.getJSONArray("abilities");
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

    private String getSurfaceArea(JSONObject object) throws JSONException {
        String surfaceArea;
        String unit = object.getString("unit").equals("hectare") ? "ha" : object.getString("unit");
        String[] numbers = object.get("value").toString().split("/");
        if (numbers.length > 1) {
            float surface = (Float.valueOf(numbers[0]) / Float.valueOf(numbers[1]));
            surfaceArea = df.format(surface) + " " + unit;
        } else
            surfaceArea = numbers[0] + " " + unit;

        return surfaceArea;
    }
}
