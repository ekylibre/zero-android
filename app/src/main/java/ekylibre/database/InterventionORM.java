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
    private int         id;
    private String      type;
    private String      procedureName;
    private int         number;
    private boolean     requestCompliant;
    private String      state;
    private String      name;
    private String      startedAt;
    private String      stoppedAt;
    private String      description;
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

        id = object.getInt("id");
        name = object.getString("name");
        type = object.getString("nature");
        procedureName = object.getString("procedure_name");
        number = object.getInt("number");
        startedAt = object.getString("started_at");
        stoppedAt = object.getString("stopped_at");
        description = object.getString("description");
        params = object.getJSONArray("parameters");
        this.context = context;
    }

    @Override
    public void reset()
    {
        id = 0;
        name = null;
        type = null;
        procedureName = null;
        number = 0;
        startedAt = null;
        stoppedAt = null;
        description = null;
        params = null;
    }

    @Override
    public void setFromJson(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object InterventionCaller : " + object.toString());

            id = object.getInt("id");
            name = object.getString("name");
            type = object.getString("nature");
            procedureName = object.getString("procedure_name");
            number = object.getInt("number");
            startedAt = object.getString("started_at");
            stoppedAt = object.getString("stopped_at");
            description = object.getString("description");
            params = object.getJSONArray("parameters");
    }

    @Override
    public void saveInDataBase()
    {
        insertIntervention();
        putParamsInBase(new InterventionParametersORM(account, context, getId()), params);
    }

    @Override
    public void setFromBase(int id)
    {
        Cursor curs = contentResolver.query(ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_ALL,
                id + " == " + ZeroContract.Interventions._ID,
                null,
                null);
        if (curs == null)
            return;
        this.id = curs.getInt(0);
        procedureName = curs.getString(2);
        requestCompliant = curs.getInt(3) == 0 ? false : true;
        this.state = curs.getString(4);

    }

    private void putParamsInBase(InterventionParametersORM orm, JSONArray jsonArray)
    {
        if (jsonArray == null)
            return;
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
        return (id);
    }

    public String getName()
    {
        return (name);
    }

    public int getNumber()
    {
        return (number);
    }

    public String getType()
    {
        return (type);
    }

    public String getProcedureName()
    {
        return (procedureName);
    }

    public String getStartedAt()
    {
        return (startedAt);
    }

    public String getStoppedAt()
    {
        return (stoppedAt);
    }

    public String getDescription()
    {
        return (description);
    }

}
