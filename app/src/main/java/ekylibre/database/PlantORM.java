package ekylibre.database;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import ekylibre.zero.BuildConfig;
import ekylibre.zero.home.Zero;

/**************************************
 * Created by pierre on 10/10/16.      *
 * ekylibre.database for zero-android    *
 *************************************/

public class PlantORM extends BaseORM
{
    private int     id;
    private int     activityID;
    private String  name;
    private String  variety;


    public PlantORM(Account account, Context context)
    {
        super(account, context);
    }

    @Override
    public void reset()
    {
        id = 0;
        activityID = 0;
        name = null;
        variety = null;
    }

    @Override
    public void saveInDataBase()
    {
        ContentValues cv = new ContentValues();

        cv.put(ZeroContract.Plants.EK_ID, id);
        cv.put(ZeroContract.Plants.NAME, name);
        cv.put(ZeroContract.Plants.USER, account.name);
        cv.put(ZeroContract.Plants.ACTIVITY_ID, activityID);
        contentResolver.insert(ZeroContract.Interventions.CONTENT_URI, cv);
    }

    @Override
    public void setFromBase(int id)
    {
        Cursor curs = contentResolver.query(ZeroContract.Plants.CONTENT_URI,
                ZeroContract.Plants.PROJECTION_ALL,
                id + " == " + ZeroContract.Plants._ID,
                null,
                null);
        if (curs == null)
            return;
        id = curs.getInt(0);
        name = curs.getString(1);
        variety = curs.getString(3);
        activityID = curs.getInt(5);
    }

    @Override
    public void setFromJson(JSONObject object) throws JSONException
    {
        if (BuildConfig.DEBUG) Log.d("zero", "Object Plant : " + object.toString());

        id = object.getInt("id");
        name = object.getString("name");
        variety = object.getString("variety");
        if (!object.isNull("activity_id"))
            activityID = object.getInt("activity_id");
    }



    /* ****************
    **     Getters
    ** ****************/
    public int getId()
    {
        return (id);
    }

    public int getActivityID()
    {
        return (activityID);
    }

    public String getName()
    {
        return (name);
    }

    public String getVariety()
    {
        return (variety);
    }


}
