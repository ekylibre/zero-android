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

/**
 * Created by antoine on 22/04/16.
 */

public class PlantCaller extends BaseCaller
{
    private static final String TAG = "Plant";

    public PlantCaller(Account account, Context context)
    {
        APIPath += "plants";
        super.account = account;
        super.context = context;
    }

    @Override
    public void get(String attributes)
    {
        super.get(attributes);
        putInBase(new PlantORM(account, context));
    }
}
