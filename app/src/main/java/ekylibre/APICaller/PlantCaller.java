package ekylibre.APICaller;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.database.PlantORM;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 10/10/16.     *
 * ekylibre.database for zero-android *
 *************************************/

public class PlantCaller extends BaseCaller
{
    private static final String TAG = "Plant";

    public PlantCaller(Account account, Context context)
    {
        super(account, context);
        APIPath += "plants";
        super.account = account;
        super.context = context;
    }

    @Override
    public void postUserData(Account account)
    {
    }

    @Override
    public void get(String attributes)
    {
        super.get(attributes);
        putInBase(new PlantORM(account, context));
    }
}
