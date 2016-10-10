package ekylibre.database;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import ekylibre.zero.BuildConfig;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.database for zero-android    *
 *************************************/

public class InterventionORM extends BaseORM
{
    private int         mId;
    private String      mType;
    private String      mProcedureName;
    private int         mNumber;
    private String      mName;
    private String      mStartedAt;
    private String      mStoppedAt;
    private String      mDescription;
    private JSONArray   params;
    private Context     context;

    public InterventionORM(Account account, Context context)
    {
        super(account, context);
        this.context = context;
    }

    public InterventionORM(JSONObject object, Account account, Context context) throws JSONException
    {
        super(account, context);
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
        this.context = context;
    }

    @Override
    public void reset()
    {
        mId = 0;
        mName = null;
        mType = null;
        mProcedureName = null;
        mNumber = 0;
        mStartedAt = null;
        mStoppedAt = null;
        mDescription = null;
        params = null;
    }

    @Override
    public void setFromJson(JSONObject object) throws JSONException
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

    @Override
    public void saveInDataBase()
    {
        insertIntervention();
        putParamsInBase(new InterventionParametersORM(account, context, getId()), params);
    }

    @Override
    public void setFromBase()
    {
        //TODO create this object from base with parameter ???
    }

    private void putParamsInBase(InterventionParametersORM orm, JSONArray jsonArray)
    {
        int i = -1;
        while (++i < jsonArray.length())
        {
            orm.reset();
            try
            {
                orm.setFromJson(jsonArray.getJSONObject(i));
                orm.saveInDataBase();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void insertIntervention()
    {
        ContentValues cv = new ContentValues();

        cv.put(ZeroContract.Interventions.EK_ID, getId());
        cv.put(ZeroContract.Interventions.NAME, getName());
        cv.put(ZeroContract.Interventions.TYPE, getType());
        cv.put(ZeroContract.Interventions.PROCEDURE_NAME, getProcedureName());
        cv.put(ZeroContract.Interventions.NUMBER, getNumber());
        cv.put(ZeroContract.Interventions.STARTED_AT, getStartedAt());
        cv.put(ZeroContract.Interventions.STOPPED_AT, getStoppedAt());
        cv.put(ZeroContract.Interventions.DESCRIPTION, getDescription());
        cv.put(ZeroContract.Interventions.USER, account.name);
        if (idExists(this.getId(), account))
            contentResolver.update(ZeroContract.Interventions.CONTENT_URI,
                    cv,
                    this.getId() + " == " + ZeroContract.Interventions.EK_ID,
                    null);
        else
        {
            cv.put(ZeroContract.Interventions.UUID, UUID.randomUUID().toString());
            contentResolver.insert(ZeroContract.Interventions.CONTENT_URI, cv);
        }
    }

    private boolean idExists(int refID, Account account)
    {
        Cursor curs = contentResolver.query(
                ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_NONE,
                refID + " == " + ZeroContract.Interventions.EK_ID + " AND "
                        + "\"" + account.name + "\"" + " LIKE " + ZeroContract.Interventions.USER,
                null,
                null);
        if (curs == null || curs.getCount() == 0)
            return (false);
        else
        {
            curs.close();
            return (true);
        }
    }



    /* ****************
    **     Getters
    ** ****************/
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

}
