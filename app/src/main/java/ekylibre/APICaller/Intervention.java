package ekylibre.APICaller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 8/31/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/
public class Intervention
{
    private static final String TAG = "Intervention";
    public static final String REQUESTED = "request";
    public static final String RECORDED = "record";
    public static final String REQUEST = "request";
    public static final String RECORD = "record";

    private int         mId;
    private String      mType;
    private String      mProcedureName;
    private int         mNumber;
    private String      mName;
    private String      mStartedAt;
    private String      mStoppedAt;
    private String      mDescription;
    private JSONArray   params;

    public static List<Intervention> all(Instance instance, String attributes) throws JSONException, IOException, HTTPException
    {
        // JSONObject params = Instance.BundleToJSON(attributes);
        String params = attributes;
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => /api/v1/interventions || params = " + params);
        JSONArray json = instance.getJSONArray("/api/v1/interventions", params);
        List<Intervention> array = new ArrayList<>();

        for(int i = 0 ; i < json.length() ; i++ )
        {
            array.add(new Intervention(json.getJSONObject(i)));
        }
        return array;
    }

    public Intervention(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Intervention : " + object.toString());

        mId = object.getInt("id");
        mName = object.getString("name");
        mType = object.getString("nature");
        mProcedureName = object.getString("procedure_name");
        mNumber = object.getInt("number");
        mStartedAt = object.getString("started_at");
        mStoppedAt = object.getString("stopped_at");
        mDescription = object.getString("description");
        params = object.getJSONArray("parameters");
    }

    public int getId()
    {
        return (mId);
    }
    public String getName()
    {
        return (mName);
    }
    public int getNumber()
    {
        return (mNumber);
    }
    public String getType()
    {
        return (mType);
    }
    public String getProcedureName()
    {
        return (mProcedureName);
    }
    public String getStartedAt()
    {
        return (mStartedAt);
    }
    public String getStoppedAt()
    {
        return (mStoppedAt);
    }
    public String getDescription()
    {
        return (mDescription);
    }
    public int getParamID(int index)
            throws JSONException
    {
        JSONObject object = params.getJSONObject(index);

        return (object.getInt("id"));
    }
    public String getParamRole(int index)
            throws JSONException
    {
        JSONObject object = params.getJSONObject(index);

        return (object.getString("role"));
    }
    public String getParamName(int index)
            throws JSONException
    {
        JSONObject object = params.getJSONObject(index);

        return (object.getString("name"));
    }
    public String getParamLabel(int index)
            throws JSONException
    {
        JSONObject object = params.getJSONObject(index);

        return (object.getString("label"));
    }
    public int getProductID(int index)
            throws JSONException
    {
        JSONObject product = params.getJSONObject(index).getJSONObject("product");

        return (product.getInt("id"));
    }
    public String getProductName(int index)
            throws JSONException
    {
        JSONObject product = params.getJSONObject(index).getJSONObject("product");

        return (product.getString("name"));
    }

    public int getParamLength()
    {
        return (params.length());
    }
}
