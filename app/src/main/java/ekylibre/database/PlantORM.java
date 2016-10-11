package ekylibre.database;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ekylibre.zero.BuildConfig;

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
        activityID
    }

    @Override
    public void saveInDataBase()
    {

    }

    @Override
    public void setFromBase()
    {

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

    public int getId() {
        return id;
    }

    public int getActivityID() {
        return activityID;
    }

    public String getName() {
        return name;
    }

    public String getVariety() {
        return variety;
    }


}
