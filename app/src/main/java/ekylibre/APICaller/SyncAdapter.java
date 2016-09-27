package ekylibre.APICaller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ekylibre.database.ZeroContract;
import ekylibre.exceptions.HTTPException;
import ekylibre.util.DateConstant;
import ekylibre.zero.BuildConfig;
import ekylibre.util.AccountTool;
import ekylibre.util.ImageConverter;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.intervention.InterventionActivity;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    public static final String TAG = "SyncAdapter";
    public static final String ACCOUNT_TYPE = "ekylibre.account.basic";
    public static final String SYNC_STARTED = "sync_started";
    public static final String SYNC_FINISHED = "sync_finished";

    private ContentResolver mContentResolver;
    private AccountManager  mAccountManager;
    private Context         mContext;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        mContext = context;

    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        getContext().sendBroadcast(new Intent(UpdatableActivity.ACTION_STARTED_SYNC));

        if (BuildConfig.DEBUG) Log.i(TAG, "Destruction of tables which will be resynced !");
        mContentResolver.delete(ZeroContract.Plants.CONTENT_URI,
                null,
                null);
        mContentResolver.delete(ZeroContract.PlantDensityAbacusItems.CONTENT_URI,
                null,
                null);
        mContentResolver.delete(ZeroContract.PlantDensityAbaci.CONTENT_URI,
                null,
                null);
        mContentResolver.delete(ZeroContract.Interventions.CONTENT_URI,
                null,
                null);
        mContentResolver.delete(ZeroContract.InterventionParameters.CONTENT_URI,
                null,
                null);

        Account[] accountList = AccountTool.getListAccount(mContext);
        if (BuildConfig.DEBUG) Log.d(TAG, "Performing Sync ! Pushing all the local data to Ekylibre instance");
        int i = -1;
        while (++i < accountList.length)
        {
            account = accountList[i];
            if (BuildConfig.DEBUG) Log.d(TAG, "... Sync new account ...");
            if (BuildConfig.DEBUG) Log.d(TAG, "... New account is " + account.name + " ...");
            pullPlantDensityAbaci(account,
                extras, authority, provider, syncResult);
            pullPlants(account, extras, authority, provider, syncResult);
            pushCrumbs(account, extras, authority, provider, syncResult);
            pushIssues(account, extras, authority, provider, syncResult);
            pushPlantCounting(account, extras, authority, provider, syncResult);
            pullIntervention(account, extras, authority, provider, syncResult);

            //pushIntervention(account, extras, authority, provider, syncResult);
        }
        getContext().sendBroadcast(new Intent(UpdatableActivity.ACTION_FINISHED_SYNC));
    }

    /*
    ** Following methods are used to transfer data between zero and ekylibre instance
    ** There are POST and GET call
    */
    public void pushCrumbs(Account account, Bundle extras, String authority,
                            ContentProviderClient provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network synchronization");
        
        // Get crumbs from intervention (content) provider
        Cursor cursor = mContentResolver.query(ZeroContract.Crumbs.CONTENT_URI,
                ZeroContract.Crumbs.PROJECTION_ALL,
                "\"" + ZeroContract.CrumbsColumns.USER + "\" " + "LIKE" + " \"" + account.name + "\""
                        + " AND " + ZeroContract.CrumbsColumns.SYNCED + " == " + 0,
                null,
                ZeroContract.Crumbs.SORT_ORDER_DEFAULT);

        try
        {
            if (cursor.getCount() > 0)
            {
                Instance instance = getInstance(account);
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    postNewCrumb(cursor, instance);
                    cursor.moveToNext();
                }
            }
            else
            {
                if (BuildConfig.DEBUG) Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network synchronization");
    }

    private void    postNewCrumb(Cursor cursor, Instance instance)
            throws JSONException, IOException, HTTPException
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "New crumb");
        // Post it to ekylibre
        JSONObject attributes = new JSONObject();
        attributes.put("nature", cursor.getString(1));
        attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(3)) + " " + Double.toString(cursor.getDouble(2)) + ")");
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        attributes.put("read_at", parser.format(new Date(cursor.getLong(4))));
        attributes.put("accuracy", cursor.getString(5));
        attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));
        JSONObject hash = new JSONObject();
        Uri metadata = Uri.parse("http://domain.tld?" + cursor.getString(6));
        Set<String> keys = metadata.getQueryParameterNames();
        if (keys.size() > 0)
        {
            for (String key : keys)
            {
                if (!key.equals("null"))
                {
                    hash.put(key, metadata.getQueryParameter(key));
                }
            }
            if (hash.length() > 0)
            {
                attributes.put("metadata", hash);
            }
        }

        long id = CrumbPoster.create(instance, attributes);
        // Marks them as synced
        ContentValues values = new ContentValues();
        values.put(ZeroContract.CrumbsColumns.SYNCED, 1);
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Crumbs.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
    }

    public void pushIssues(Account account, Bundle extras, String authority, ContentProviderClient
            provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network issues synchronization");

        // Get crumbs from Issue (content) provider
        Cursor cursor = mContentResolver.query(
                ZeroContract.Issues.CONTENT_URI,
                ZeroContract.Issues.PROJECTION_ALL,
                "\"" + ZeroContract.Issues.USER + "\"" + " LIKE " + "\"" + account.name + "\""
                        + " AND " + ZeroContract.IssuesColumns.SYNCED + " == " + 0,
                null,
                ZeroContract.Issues.SORT_ORDER_DEFAULT);


            if (cursor != null && cursor.getCount() > 0)
            {
                Instance instance = getInstance(account);
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    try {
                        postNewIssue(cursor, instance);
                    } catch (JSONException | IOException | HTTPException e) {
                        e.printStackTrace();
                    }
                    cursor.moveToNext();
                }
            }
            else
            {
                if (BuildConfig.DEBUG) Log.i(TAG, "Nothing to sync");
            }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network synchronization");
    }

    private void    postNewIssue(Cursor cursor, Instance instance)
            throws JSONException, IOException, HTTPException
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "New issue");

        // Post it to ekylibre
        JSONObject attributes = new JSONObject();
        attributes.put("nature", cursor.getString(1));
        attributes.put("gravity", cursor.getInt(2));
        attributes.put("priority", cursor.getInt(3));
        attributes.put("description", cursor.getString(5));
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        attributes.put("observed_at", parser.format(new Date(cursor.getLong(8))));
        //TODO : send images to ekylibre api
        //attributes.put("images", createImageJSONArray(cursor));
        if (cursor.getDouble(9) != 0 && cursor.getDouble(10) != 0)
        {
            attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(10)) + " " + Double.toString(cursor.getDouble(9)) + ")");
        }

        long id = Issue.create(instance, attributes);
        // Marks them as synced
        ContentValues values = new ContentValues();
        values.put(ZeroContract.IssuesColumns.SYNCED, 1);
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Issues.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
    }

    private JSONArray createImageJSONArray(Cursor cursor) throws JSONException
    {
        JSONArray imageJSON = new JSONArray();
        int id = cursor.getInt(0);
        ArrayList<String> imageBlock = ImageConverter.getImagesFromIssue(id);
        int i = -1;

        while (++i < imageBlock.size())
        {
            JSONObject jsonObject = new JSONObject();
            String image = imageBlock.get(i);
            jsonObject.put("value", image);
            imageJSON.put(jsonObject);
        }
        return (imageJSON);
    }

    public void pullPlantDensityAbaci(Account account, Bundle extras, String authority,
                                       ContentProviderClient provider, SyncResult syncResult)
    {



        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network plant_density_abaci synchronization");
        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        List<PlantDensityAbacus> abacusList = null;
        try {
            abacusList = PlantDensityAbacus.all(instance, null);
        } catch (JSONException | IOException | HTTPException e) {
            e.printStackTrace();
        }
        if (abacusList == null)
            return;

        if (BuildConfig.DEBUG) Log.d("zero", "Nombre d'abaque : " + abacusList.size() );
            Iterator<PlantDensityAbacus> abacusIterator = abacusList.iterator();

        if (BuildConfig.DEBUG) Log.d("zero", "début parcours de liste plantDensityAbacus");

            while(abacusIterator.hasNext())
            {
                PlantDensityAbacus plantDensityAbacus = abacusIterator.next();
                cv.put(ZeroContract.PlantDensityAbaciColumns.EK_ID, plantDensityAbacus.getId());
                cv.put(ZeroContract.PlantDensityAbaciColumns.NAME, plantDensityAbacus.getName());
                cv.put(ZeroContract.PlantDensityAbaciColumns.VARIETY, plantDensityAbacus.getVariety());
                cv.put(ZeroContract.PlantDensityAbaciColumns.ACTIVITY_ID, plantDensityAbacus.getActivityID());
                cv.put(ZeroContract.PlantDensityAbaciColumns.GERMINATION_PERCENTAGE, plantDensityAbacus.getGerminationPercentage());
                cv.put(ZeroContract.PlantDensityAbaciColumns.SEEDING_DENSITY_UNIT, plantDensityAbacus.getSeedingDensityUnit());
                cv.put(ZeroContract.PlantDensityAbaciColumns.SAMPLING_LENGTH_UNIT, plantDensityAbacus.getSamplingLenghtUnit());
                cv.put(ZeroContract.PlantDensityAbaciColumns.USER, account.name);
                mContentResolver.insert(ZeroContract.PlantDensityAbaci.CONTENT_URI, cv);
            }
        if (BuildConfig.DEBUG) Log.d("zero", "fin parcours de liste plantDensityAbacus");
        try {
            pullPlantDensityAbacusItem(account, extras, authority, provider, syncResult, abacusList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network plant_density_abaci synchronization");
    }

    public void pullPlantDensityAbacusItem(Account account, Bundle extras, String authority,
                                         ContentProviderClient provider, SyncResult syncResult, List<PlantDensityAbacus> abacusList) throws JSONException
    {


        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network plant_density_abacus_items synchronization");
        ContentValues cv = new ContentValues();

        if (BuildConfig.DEBUG) Log.d("zero", "Nombre d'abaque : " + abacusList.size() );
        Iterator<PlantDensityAbacus> abacusIterator = abacusList.iterator();

        if (BuildConfig.DEBUG) Log.d("zero", "début parcours de liste plantDensityAbacus");

        while(abacusIterator.hasNext())
        {
            PlantDensityAbacus plantDensityAbacus = abacusIterator.next();
            int i = 0;
            while (i < plantDensityAbacus.mItems.length())
            {
                cv.put(ZeroContract.PlantDensityAbacusItemsColumns.EK_ID, plantDensityAbacus.getItemID(i));
                cv.put(ZeroContract.PlantDensityAbacusItemsColumns.FK_ID, plantDensityAbacus.getId());
                cv.put(ZeroContract.PlantDensityAbacusItemsColumns.PLANTS_COUNT, plantDensityAbacus.getItemPlantCount(i));
                cv.put(ZeroContract.PlantDensityAbacusItemsColumns.SEEDING_DENSITY_VALUE, plantDensityAbacus.getItemDensityValue(i));
                cv.put(ZeroContract.PlantDensityAbacusItemsColumns.USER, account.name);
                mContentResolver.insert(ZeroContract.PlantDensityAbacusItems.CONTENT_URI, cv);
                i++;
            }
        }
        if (BuildConfig.DEBUG) Log.d("zero", "fin parcours de liste plantDensityAbacus");
    }



    public void pullPlants(Account account, Bundle extras, String authority, ContentProviderClient
            provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network plants synchronization");
        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        List<Plant> plantsList = null;
        try {
            plantsList = Plant.all(instance, null);
        } catch (JSONException | IOException | HTTPException e) {
            e.printStackTrace();
        }

        if (plantsList == null)
            return;

        if (BuildConfig.DEBUG) Log.d(TAG, "Nombre de plants : " + plantsList.size() );
        Iterator<Plant> plantsIterator = plantsList.iterator();
        while(plantsIterator.hasNext())
        {
            Plant plants = plantsIterator.next();
            cv.put(ZeroContract.Plants.EK_ID, plants.getId());
            cv.put(ZeroContract.Plants.NAME, plants.getName());
            cv.put(ZeroContract.Plants.VARIETY, plants.getVariety());
            cv.put(ZeroContract.Plants.ACTIVITY_ID, plants.getActivityID());
            cv.put(ZeroContract.Plants.ACTIVE, true);
            cv.put(ZeroContract.Plants.USER, account.name);
            mContentResolver.insert(ZeroContract.Plants.CONTENT_URI, cv);
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network plant synchronization");
    }

    public void pushPlantCounting(Account account, Bundle extras, String authority,
                               ContentProviderClient provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network plant counting synchronization");
        Cursor cursor = mContentResolver.query(ZeroContract.PlantCountings.CONTENT_URI,
                ZeroContract.PlantCountings.PROJECTION_ALL,
                "\"" + ZeroContract.PlantCountingsColumns.USER + "\"" + " LIKE " + "\"" + account.name + "\""
                        + " AND " + ZeroContract.PlantCountingsColumns.SYNCED + " == " + 0,
                null,
                ZeroContract.PlantCountings.SORT_ORDER_DEFAULT);

            if (cursor != null && cursor.getCount() > 0)
            {
                Instance instance = getInstance(account);
                while (cursor.moveToNext())
                {
                    try
                    {
                        if (BuildConfig.DEBUG) Log.i(TAG, "New plantCounting");
                        // Post it to ekylibre
                        JSONObject attributes = new JSONObject();
                        //attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(3)) + " " + Double.toString(cursor.getDouble(2)) + ")");
                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        attributes.put("read_at", parser.format(new Date(cursor.getLong(1))));
                        attributes.put("comment", cursor.getString(4));
                        attributes.put("plant_density_abacus_item_id", cursor.getInt(5));
                        //attributes.put("plant_density_abacus_id", cursor.getString(7));
                        attributes.put("plant_id", cursor.getInt(8));
                        attributes.put("average_value", cursor.getFloat(9));
                        attributes.put("items_attributes", createPlantCountingItemJSON(account, extras, authority, provider, syncResult, cursor.getInt(0)));
                        //attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));

                        long id = PlantCounting.create(instance, attributes);
                        ContentValues values = new ContentValues();
                        values.put(ZeroContract.IssuesColumns.SYNCED, 1);
                        mContentResolver.update(Uri.withAppendedPath(ZeroContract.PlantCountings.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }
            else
            {
                if (BuildConfig.DEBUG) Log.i(TAG, "Nothing to sync");
            }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network synchronization");
    }

    public JSONArray createPlantCountingItemJSON(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult, int ID)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network synchronization");
        Cursor cursor = mContentResolver.query(ZeroContract.PlantCountingItems.CONTENT_URI,
                ZeroContract.PlantCountingItems.PROJECTION_ALL,
                "\"" + ZeroContract.PlantCountingItemsColumns.USER + "\"" + " LIKE " + "\"" + account.name + "\""
                + " AND " + ID + " == " + ZeroContract.PlantCountingItems.PLANT_COUNTING_ID,
                null,
                ZeroContract.PlantCountingItems.SORT_ORDER_DEFAULT);
        try
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                JSONArray arrayJSON = new JSONArray();
                int i = 0;
                while (cursor.moveToNext())
                {
                    if (BuildConfig.DEBUG) Log.i(TAG, "New plantCounting");
                    // Post it to ekylibre
                    JSONObject attributes = new JSONObject();
                    attributes.put("value", cursor.getInt(1));
                    /*attributes.put("plant_id", cursor.getInt(2));
                    attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));*/
                    arrayJSON.put(attributes);
                }
                cursor.close();
                return (arrayJSON);
            }
            else
            {
                if (BuildConfig.DEBUG) Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network synchronization");
        return (null);
    }

    public void pullIntervention(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network intervention synchronization");
        Instance instance = getInstance(account);

        List<Intervention> interventionList = null;
        try {
            interventionList = Intervention.all(instance, "?nature=request&user_email=" +
                    AccountTool.getEmail(account) + "&without_interventions=true");
        } catch (JSONException | IOException | HTTPException e) {
            e.printStackTrace();
        }

        if (interventionList == null)
            return;

        if (BuildConfig.DEBUG) Log.d(TAG, "Number of interventions : " + interventionList.size() );
        Iterator<Intervention> interventionIterator = interventionList.iterator();
        while(interventionIterator.hasNext())
        {
            Intervention intervention = interventionIterator.next();
            insertIntervention(intervention, account);
            insertInterventionParams(intervention, account);
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network intervention synchronization");
    }

    private void insertInterventionParams(Intervention intervention, Account account)
    {
        ContentValues cv = new ContentValues();
        int interventionID = getInterventionID();
        int i = -1;
        int paramLength = intervention.getParamLength();

        if (interventionID == 0)
            return;
        while (++i < paramLength)
        {
            try
            {
                cv.put(ZeroContract.InterventionParameters.EK_ID, intervention.getParamID(i));
                cv.put(ZeroContract.InterventionParameters.NAME, intervention.getParamName(i));
                cv.put(ZeroContract.InterventionParameters.FK_INTERVENTION, interventionID);
                cv.put(ZeroContract.InterventionParameters.ROLE, intervention.getParamRole(i));
                cv.put(ZeroContract.InterventionParameters.LABEL, intervention.getParamLabel(i));
                cv.put(ZeroContract.InterventionParameters.PRODUCT_NAME, intervention.getProductName(i));
                cv.put(ZeroContract.InterventionParameters.PRODUCT_ID, intervention.getProductID(i));
                mContentResolver.insert(ZeroContract.InterventionParameters.CONTENT_URI, cv);
            }
            catch (JSONException jsonex)
            {
                jsonex.printStackTrace();
            }
        }
    }

    private int getInterventionID()
    {
        Cursor cursor = mContentResolver.query(ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_NONE,
                null,
                null,
                ZeroContract.Interventions.SORT_ORDER_LAST);
        if (cursor == null || cursor.getCount() == 0)
            return (0);
        cursor.moveToFirst();
        int ret = cursor.getInt(0);
        cursor.close();
        return (ret);
    }

    private void insertIntervention(Intervention intervention, Account account)
    {
        ContentValues cv = new ContentValues();

        cv.put(ZeroContract.Interventions.EK_ID, intervention.getId());
        cv.put(ZeroContract.Interventions.NAME, intervention.getName());
        cv.put(ZeroContract.Interventions.TYPE, intervention.getType());
        cv.put(ZeroContract.Interventions.PROCEDURE_NAME, intervention.getProcedureName());
        cv.put(ZeroContract.Interventions.NUMBER, intervention.getNumber());
        cv.put(ZeroContract.Interventions.STARTED_AT, intervention.getStartedAt());
        cv.put(ZeroContract.Interventions.STOPPED_AT, intervention.getStoppedAt());
        cv.put(ZeroContract.Interventions.DESCRIPTION, intervention.getDescription());
        cv.put(ZeroContract.Interventions.USER, account.name);
        mContentResolver.insert(ZeroContract.Interventions.CONTENT_URI, cv);
    }

    private void pushIntervention(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network intervention synchronization");

        Cursor cursorIntervention = mContentResolver.query(
                ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_POST,
                "\"" + ZeroContract.Interventions.USER + "\"" + " LIKE " + "\"" + account.name + "\""
                        + " AND " + ZeroContract.Interventions.STATE + " LIKE " +
                        InterventionActivity.STATUS_FINISHED,
                null,
                ZeroContract.Interventions.SORT_ORDER_DEFAULT);


        if (cursorIntervention != null && cursorIntervention.getCount() > 0)
        {
            Instance instance = getInstance(account);
            while (cursorIntervention.moveToNext())
            {
                try
                {
                    Cursor cursorWorkingPeriods = mContentResolver.query(
                            ZeroContract.WorkingPeriods.CONTENT_URI,
                            ZeroContract.WorkingPeriods.PROJECTION_ALL,
                            ZeroContract.WorkingPeriods.FK_INTERVENTION + " == " +
                                    cursorIntervention.getInt(0),
                            null,
                            null);

                    postNewIntervention(cursorIntervention, cursorWorkingPeriods, instance);
                }
                catch (JSONException | IOException | HTTPException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            if (BuildConfig.DEBUG) Log.i(TAG, "Nothing to sync");
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network synchronization");
    }

    private void    postNewIntervention(Cursor cursorIntervention, Cursor cursorWorkingPeriods,
                                        Instance instance)
            throws JSONException, IOException, HTTPException
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "New intervention");

        // Post it to ekylibre
        JSONObject attributes = new JSONObject();
        if (cursorIntervention.getInt(1) > 0)
        {
            attributes.put("request_intervention_id", cursorIntervention.getInt(1));
            attributes.put("request_compliant", cursorIntervention.getInt(3));
        }
        attributes.put("procedure_name", cursorIntervention.getString(2));
        attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));

        attributes.put("working_periods", createWorkingPeriods(cursorIntervention.getInt(0)));

        // TODO :: put crumbs here


        long id = Intervention.create(instance, attributes);
        // Marks them as synced
        ContentValues values = new ContentValues();
        values.put(ZeroContract.Interventions.STATE, "SYNCED");
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Issues.CONTENT_URI, Long
                .toString(cursorIntervention.getLong(0))), values, null, null);
    }

    private JSONArray createWorkingPeriods(int interventionID)
    {
        Cursor cursor = mContentResolver.query(ZeroContract.WorkingPeriods.CONTENT_URI,
                ZeroContract.WorkingPeriods.PROJECTION_POST,
                interventionID + " == " + ZeroContract.WorkingPeriods.FK_INTERVENTION,
                null,
                ZeroContract.PlantCountingItems.SORT_ORDER_DEFAULT);
        try
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                JSONArray arrayJSON = new JSONArray();
                while (cursor.moveToNext())
                {
                    JSONObject attributes = new JSONObject();
                    attributes.put("started_at", cursor.getString(1));
                    attributes.put("stopped_at", cursor.getString(2));
                    attributes.put("nature", cursor.getString(3));
                    arrayJSON.put(attributes);
                }
                cursor.close();
                return (arrayJSON);
            }
            else
            {
                if (BuildConfig.DEBUG) Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    protected Instance getInstance(Account account)
    {
        Instance instance = null;
            try
            {
                instance = new Instance(account, mAccountManager);
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
