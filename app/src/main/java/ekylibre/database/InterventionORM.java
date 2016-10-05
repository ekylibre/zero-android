package ekylibre.database;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.database for zero-android    *
 *************************************/

public class InterventionORM implements BasicORM
{
    private int         mId;
    private String      mType;
    private String      mProcedureName;
    private int         mNumber;
    private String      mName;
    private String      mStartedAt;
    private String      mStoppedAt;
    private String      mDescription;
    private JSONArray params;

    public InterventionORM(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object InterventionCaller : " + object.toString());

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

    @Override
    public void saveInDataBase()
    {

    }

    @Override
    public void setFromBase()
    {

    }
}
