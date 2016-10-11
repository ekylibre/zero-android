package ekylibre.APICaller;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.database.InterventionORM;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 8/31/16.      *
 * ekylibre.APICaller for zero-android*
 *************************************/
public class InterventionCaller extends BaseCaller
{
    private static final String TAG = "InterventionCaller";
    public static final String TARGET = "target";
    public static final String REQUESTED = "request";
    public static final String RECORDED = "record";
    public static final String REQUEST = "request";
    public static final String RECORD = "record";

    InterventionCaller(Account account, Context context)
    {
        APIPath += "interventions";
        super.account = account;
        super.context = context;
    }

    @Override
    public void post(Instance instance, JSONObject attributes)
    {
        APIPath = "/api/v1/intervention_participations";
        super.post(instance, attributes);
        APIPath = "/api/v1/interventions";
    }

    @Override
    public void get(String attributes)
    {
        super.get(attributes);
        putInBase(new InterventionORM(account, context));
    }
}
