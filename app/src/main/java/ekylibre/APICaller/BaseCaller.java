package ekylibre.APICaller;

import android.support.annotation.CallSuper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ekylibre.database.BasicORM;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/

abstract class BaseCaller implements BasicCaller
{
    private final String TAG = "BaseCaller";
    protected String APIPath = "/api/v1/";
    protected JSONArray jsonFromAPI;

    @CallSuper
    @Override
    public void post(Instance instance, JSONObject attributes)
    {
        try
        {
            JSONObject json = instance.post(APIPath, attributes);
        }
        catch (JSONException | IOException | HTTPException e)
        {
            e.printStackTrace();
        }
    }

    @CallSuper
    @Override
    public void get(Instance instance, String attributes)
    {
        if (BuildConfig.DEBUG) Log.d(TAG, "Get JSONArray => " + APIPath + " |o| params = " +
                attributes);
        try
        {
            jsonFromAPI = instance.getJSONArray(APIPath, attributes);
        }
        catch (JSONException | IOException | HTTPException e)
        {
            e.printStackTrace();
        }
    }

    protected void putInBase(BasicORM orm)
    {
        int i = -1;
        while (++i < jsonFromAPI.length())
        {
            orm.reset();
            try
            {
                orm.setFromJson(jsonFromAPI.getJSONObject(i));
                orm.saveInDataBase();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
