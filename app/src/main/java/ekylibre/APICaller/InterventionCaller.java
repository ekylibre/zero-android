package ekylibre.APICaller;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.database.InterventionORM;
import ekylibre.database.ZeroContract;
import ekylibre.exceptions.HTTPException;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.intervention.InterventionActivity;

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
        super(account, context);
        APIPath += "interventions";
    }

    @Override
    public void post(JSONObject json)
    {
        APIPath = "/api/v1/intervention_participations";
        super.post(json);
        APIPath = "/api/v1/interventions";
    }

    @Override
    public void postUserData(Account account)
    {
        APIPath = "/api/v1/intervention_participations";

        InterventionORM orm = new InterventionORM(account, context);
        Cursor listId = getInterventionId(account);

        postFromId(listId, orm);
        APIPath = "/api/v1/interventions";
    }

    @Override
    public void get(String attributes)
    {
        super.get(attributes);
        putInBase(new InterventionORM(account, context));
    }

    private Cursor getInterventionId(Account account)
    {
        //TODO :: VERIFIER QUE LE OR MARCHE BIEN !!!!!!!
        ContentResolver contentResolver = context.getContentResolver();

        Cursor curs = contentResolver.query(
                ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_NONE,
                "\"" + account.name + "\"" + " LIKE " + ZeroContract.Interventions.USER
                + " AND " + "(" + ZeroContract.Interventions.STATE + " LIKE "
                        + "\"" + InterventionActivity.STATUS_FINISHED + "\""
                        + " OR " + ZeroContract.Interventions.STATE + " LIKE "
                        + "\"" + InterventionActivity.STATUS_IN_PROGRESS + "\"" + ")",
                null,
                null);
        return (curs);
    }

}
