package ekylibre.APICaller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.CallSuper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ekylibre.database.BaseORM;
import ekylibre.database.BasicORM;
import ekylibre.database.InterventionORM;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

import static ekylibre.zero.R.string.accountManager;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/

abstract class BaseCaller implements BasicCaller
{
    private final String TAG = "BaseCaller";
    protected String APIPath = "/api/v1/";
    protected JSONArray jsonFromAPI;
    protected Account account;
    protected Context context;
    protected Instance instance;

    public BaseCaller(Account account, Context context)
    {
        this.account = account;
        this.context = context;
        instance = getInstance(account);
    }

    @CallSuper
    @Override
    public void post(JSONObject json)
    {
        try
        {
            instance.post(APIPath, json);
        }
        catch (JSONException | IOException | HTTPException e)
        {
            e.printStackTrace();
        }
    }

    @CallSuper
    @Override
    public void get(String attributes)
    {
        Instance instance = getInstance(account);
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
        if (jsonFromAPI == null)
            return;
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

    /*
    ** @param: listId => list of ids for interventions
    **         orm    => Instantiated orm object
    */
    protected void postFromId(Cursor listId, BasicORM orm)
    {
        JSONObject json;
        if (listId == null || listId.getCount() == 0)
            return;

        while (listId.moveToNext())
        {
            json = createJsonFromOrm(orm, listId.getInt(0));
            post(json);
        }
    }

    protected JSONObject createJsonFromOrm(BasicORM orm, int id)
    {
        JSONObject json;

        orm.reset();
        orm.setFromBase(id);
        try
        {
            json = orm.createJson();
        } catch (JSONException e)
        {
            e.printStackTrace();
            return (null);
        }
        return (json);
    }

    protected Instance getInstance(Account account)
    {
        AccountManager accountManager = AccountManager.get(context);

        Instance instance = null;
        try
        {
            instance = new Instance(account, accountManager);
        }
        catch(AccountsException e)
        {
            if (BuildConfig.DEBUG) Log.e(TAG, "Account manager or user cannot help. Cannot get token.");
        }
        catch(IOException e)
        {
            if (BuildConfig.DEBUG) Log.w(TAG, "IO problem. Cannot get token.");
        }
        return (instance);
    }
}
