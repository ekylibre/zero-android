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
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import ekylibre.database.ZeroContract;
import ekylibre.exceptions.HTTPException;
import ekylibre.util.AccountTool;
import ekylibre.util.Contact;
import ekylibre.util.DateConstant;
import ekylibre.util.ImageConverter;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.SettingsActivity;
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

    /*
    ** Get mobilePerm from SharedPreference set on settings activity
    */
    public boolean getContactPref()
    {
        SharedPreferences   pref;

        pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        return (pref.getBoolean(SettingsActivity.PREF_SYNC_CONTACTS, false));
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.e(TAG, "DEBUG = " + BuildConfig.DEBUG);
        // Updates Security Provider to Protect Against SSL Exploits
        try {
            ProviderInstaller.installIfNeeded(getContext());
        } catch (GooglePlayServicesRepairableException e) {

            // Indicates that Google Play services is out of date, disabled, etc.

            // Prompt the user to install/update/enable Google Play services.
            //GooglePlayServicesUtil
            // .showErrorNotification(e.getConnectionStatusCode(), getContext());
            GoogleApiAvailability.getInstance()
                    .showErrorNotification(getContext(), e.getConnectionStatusCode());

            // Notify the SyncManager that a soft error occurred.
            syncResult.stats.numIoExceptions++;
            return;

        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates a non-recoverable error; the ProviderInstaller is not able
            // to install an up-to-date Provider.

            // Notify the SyncManager that a hard error occurred.
            syncResult.stats.numAuthExceptions++;
            return;
        }

        getContext().sendBroadcast(new Intent(UpdatableActivity.ACTION_STARTED_SYNC));

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Destruction of tables which will be resynced !");

        mContentResolver.delete(ZeroContract.Plants.CONTENT_URI, null, null);
        mContentResolver.delete(ZeroContract.PlantDensityAbacusItems.CONTENT_URI, null, null);
        mContentResolver.delete(ZeroContract.PlantDensityAbaci.CONTENT_URI, null, null);
        mContentResolver.delete(ZeroContract.InterventionParameters.CONTENT_URI, null, null);

        Account[] accountList = AccountTool.getListAccount(mContext);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Performing Sync ! Pushing all the local data to Ekylibre instance");

        int i = -1;
        while (++i < accountList.length) {

            account = accountList[i];

            if (BuildConfig.DEBUG) Log.d(TAG, "... Sync new account ...");
            if (BuildConfig.DEBUG) Log.d(TAG, "... New account is " + account.name + " ...");

            pullPlantDensityAbaci(account, extras, authority, provider, syncResult);
            pullPlants(account, extras, authority, provider, syncResult);
            pullLandParcels(account);
            pullWorkers(account);
            pullEquipments(account);

            pushIssues(account, extras, authority, provider, syncResult);
            pushPlantCounting(account, extras, authority, provider, syncResult);
            pushIntervention(account, extras, authority, provider, syncResult);
            pullIntervention(account, extras, authority, provider, syncResult);

            pushObservation(account);

            pullContacts(account, extras, authority, provider, syncResult);
            cleanLocalDb(account);
        }

        getContext().sendBroadcast(new Intent(UpdatableActivity.ACTION_FINISHED_SYNC));
    }

    private void cleanLocalDb(Account account)
    {
        mContentResolver.delete(ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.USER + " LIKE " + " \"" + account.name + "\"" + " AND " +
                ZeroContract.Interventions.STATE + " LIKE " +
                        "\"" + InterventionActivity.STATUS_FINISHED + "\"",
                null);
    }

    /*
    ** Following methods are used to transfer data between zero and ekylibre instance
    ** There are POST and GET call
    */

    public void pushIssues(Account account, Bundle extras, String authority, ContentProviderClient
            provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network issues synchronization");

        // Get crumbs from Issue (content) provider
        Cursor cursor = mContentResolver.query(
                ZeroContract.Issues.CONTENT_URI,
                ZeroContract.Issues.PROJECTION_ALL,
                ZeroContract.Issues.USER + " LIKE " + "\"" + account.name + "\""
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
        String db_id = Long.toString(cursor.getLong(0));
        // Marks them as synced
        ContentValues values = new ContentValues();
        values.put(ZeroContract.Issues.SYNCED, 1);
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Issues.CONTENT_URI, db_id), values, null, null);

        values.clear();
        values.put(ZeroContract.ObservationIssues.EKY_ID_ISSUE, id);
        mContentResolver.update(ZeroContract.ObservationIssues.CONTENT_URI,
                values, ZeroContract.ObservationIssues.FK_ISSUE + "=?", new String[]{db_id});
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
            cv.put(ZeroContract.Plants.ACTIVITY_NAME, plants.getmActivityName());
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
                ZeroContract.PlantCountingsColumns.USER + " LIKE " + "\"" + account.name + "\""
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
                        attributes.put("nature", cursor.getString(10));
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
                ZeroContract.PlantCountingItemsColumns.USER + " LIKE " + "\"" + account.name + "\""
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

        // create the lists containing items number
        List<Integer> remoteNumbers = new ArrayList<>();
        List<Integer> localNumbers = getLocalItemsNumbers(account);

        while(interventionIterator.hasNext())
        {
            Intervention intervention = interventionIterator.next();

            // adds current remote item number to the list
            remoteNumbers.add(intervention.getNumber());

            insertIntervention(intervention, account);
            insertInterventionParams(intervention, account, instance);
        }

        // check and delete local item if not exists on remote
        for (Integer i : localNumbers) {

            if (!remoteNumbers.contains(i)){
                mContentResolver.delete(ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.USER + " LIKE " + " \"" + account.name + "\"" + " AND " +
                        ZeroContract.Interventions.NUMBER + " == " + i,null);

                if (BuildConfig.DEBUG) Log.d(TAG, String.format("Item number #%s deleted", i));
            }
        }

        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network intervention synchronization");
    }

    private List<Integer> getLocalItemsNumbers(Account account) {

        Cursor cursor = mContentResolver.query(
                ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_NUMBER,
                ZeroContract.Interventions.USER + " LIKE " + "\"" + account.name + "\"",
                null,null);

        List<Integer> localItemsNumbers = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){
                localItemsNumbers.add(cursor.getInt(1));
                cursor.moveToNext();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return localItemsNumbers;
    }

    private void insertInterventionParams(Intervention intervention, Account account, Instance instance)
    {
        ContentValues cv = new ContentValues();
        int interventionID = getInterventionID(account, intervention.getId());
        int i = -1;
        int paramLength = intervention.getParamLength();

        if (interventionID == 0)
            return;
        while (++i < paramLength)
        {
            try
            {
                cv.clear();
                cv.put(ZeroContract.InterventionParameters.EK_ID, intervention.getParamID(i));
                cv.put(ZeroContract.InterventionParameters.NAME, intervention.getParamName(i));
                cv.put(ZeroContract.InterventionParameters.FK_INTERVENTION, interventionID);
                cv.put(ZeroContract.InterventionParameters.ROLE, intervention.getParamRole(i));
                cv.put(ZeroContract.InterventionParameters.LABEL, intervention.getParamLabel(i));
                cv.put(ZeroContract.InterventionParameters.PRODUCT_NAME, intervention.getProductName(i));
                cv.put(ZeroContract.InterventionParameters.PRODUCT_ID, intervention.getProductID(i));
                if (intervention.getParamRole(i).equals("target"))
                {
                    String json = Intervention.getGeoJson(instance, intervention.getParamID(i));
                    if (json != null)
                        cv.put(ZeroContract.InterventionParameters.SHAPE, json);
                }
                mContentResolver.insert(ZeroContract.InterventionParameters.CONTENT_URI, cv);
            }
            catch (JSONException jsonex)
            {
                jsonex.printStackTrace();
            }
        }
    }

    private int getInterventionID(Account account, int refID)
    {
        Cursor cursor = mContentResolver.query(ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_NONE,
                refID + " == " + ZeroContract.Interventions.EK_ID + " AND "
                        + "\"" + account.name + "\"" + " LIKE " + ZeroContract.Interventions.USER,
                null,
                null);
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
        if (idExists(intervention.getId(), account))
            mContentResolver.update(ZeroContract.Interventions.CONTENT_URI,
                    cv,
                    intervention.getId() + " == " + ZeroContract.Interventions.EK_ID,
                    null);
        else
        {
            cv.put(ZeroContract.Interventions.UUID, UUID.randomUUID().toString());
            mContentResolver.insert(ZeroContract.Interventions.CONTENT_URI, cv);
        }
    }

    private boolean idExists(int refID, Account account)
    {
        Cursor curs = mContentResolver.query(
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

    private void pushIntervention(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network intervention synchronization");

        Cursor cursorIntervention = mContentResolver.query(
                ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_POST,
                ZeroContract.Interventions.USER + " LIKE " + "\"" + account.name + "\""
                        + " AND " + "(" + ZeroContract.Interventions.STATE + " LIKE " +
                        "\"" + InterventionActivity.STATUS_FINISHED + "\"" +
                        " OR " + ZeroContract.Interventions.STATE + " LIKE " +
                        "\"" + InterventionActivity.STATUS_PAUSE + "\"" +
                        " OR " + ZeroContract.Interventions.STATE + " LIKE " +
                        "\"" + InterventionActivity.STATUS_IN_PROGRESS + "\"" + ")",
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
        if (BuildConfig.DEBUG) Log.i(TAG, "##");
        if (BuildConfig.DEBUG) Log.i(TAG, cursorIntervention.getString(4));
        if (BuildConfig.DEBUG) Log.i(TAG, "##");

        // Post it to ekylibre
        JSONObject attributes = new JSONObject();
        if (cursorIntervention.getInt(1) > 0)
        {
            attributes.put("request_intervention_id", cursorIntervention.getInt(1));
            attributes.put("request_compliant", cursorIntervention.getInt(3));
        }
        attributes.put("uuid", cursorIntervention.getString(5));
        if (cursorIntervention.getString(4).equals(InterventionActivity.STATUS_FINISHED))
            attributes.put("state", "done");
        else
            attributes.put("state", "in_progress");
        attributes.put("procedure_name", cursorIntervention.getString(2));
        attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));

        attributes.put("working_periods", createWorkingPeriods(cursorIntervention.getInt(0)));
        attributes.put("crumbs", createCrumbs(cursorIntervention.getInt(0)));

        long id = Intervention.create(instance, attributes);
        // Marks them as synced
        //ContentValues values = new ContentValues();
        //values.put(ZeroContract.Interventions.STATE, "SYNCED");

        //mContentResolver.update(Uri.withAppendedPath(ZeroContract.Interventions.CONTENT_URI, Long
        //        .toString(cursorIntervention.getLong(0))), values, null, null);
    }

    public JSONArray createCrumbs(int idIntervention)
    {
        Cursor cursor = mContentResolver.query(ZeroContract.Crumbs.CONTENT_URI,
                ZeroContract.Crumbs.PROJECTION_ALL,
                ZeroContract.Crumbs.FK_INTERVENTION + " == " +  idIntervention,
                null,
                ZeroContract.Crumbs.SORT_ORDER_DEFAULT);

        try
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                JSONArray crumbsArray = new JSONArray();
                while (cursor.moveToNext())
                {
                    crumbsArray.put(addNewCrumb(cursor));
                }
                return (crumbsArray);
            }
            else
            if (BuildConfig.DEBUG) Log.i(TAG, "No crumbs for this intervention");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    private JSONObject    addNewCrumb(Cursor cursor)
            throws JSONException, IOException, HTTPException
    {
        // Post it to ekylibre
        JSONObject attributes = new JSONObject();
        attributes.put("nature", cursor.getString(1));
        attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(3)) + " " + Double.toString(cursor.getDouble(2)) + ")");
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        attributes.put("read_at", parser.format(new Date(cursor.getLong(4))));
        attributes.put("accuracy", cursor.getString(5));
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
        return (attributes);
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

    private void pullContacts(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        if (!getContactPref())
        {
            return;
        }
        if (BuildConfig.DEBUG) Log.i(TAG, "Beginning network contact synchronization");
        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        List<ContactCaller> contactsList;
        String lastSyncContact = null;
        try
        {
            lastSyncContact = getLastSyncContact(account);
        } catch (UnsupportedEncodingException e)
        {
            lastSyncContact = "";
            e.printStackTrace();
        }
        contactsList = ContactCaller.all(instance, lastSyncContact);



        if (contactsList == null)
            return;
        Contact contactCreator = new Contact(mContext);
        for (ContactCaller contact : contactsList)
        {
            cv.clear();
            contactCreator.clear();
            if (contact.getType().equals("create"))
                insertContact(account, contact, contactCreator, instance, cv);
            else if (contact.getType().equals("update"))
            {
                destroyContact(contact, contactCreator);
                updateContact(account, contact, contactCreator, instance, cv);
            }
            else
            {
                destroyContact(contact, contactCreator);
                mContentResolver.delete(ZeroContract.Contacts.CONTENT_URI,
                        contact.getEkId() + " == " + ZeroContract.Contacts.EK_ID,
                        null);
            }
        }
        setNewSyncDate(account);
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish network contact synchronization");
    }

    private void destroyContact(ContactCaller contact, Contact contactCreator)
    {
        Log.d(TAG, "Try to destroy id => " + contact.getEkId());
        Cursor curs = mContentResolver.query(
                ZeroContract.Contacts.CONTENT_URI,
                ZeroContract.Contacts.PROJECTION_NAME,
                ZeroContract.Contacts.EK_ID + " == " + contact.getEkId(),
                null,
                null);
        if (curs == null || curs.getCount() == 0)
            return;
        curs.moveToFirst();
        contactCreator.deleteContact(curs.getString(0), curs.getString(1), mContext);
        contactCreator.commit();
        contactCreator.clear();
        curs.close();
    }

    private void insertContact(Account account, ContactCaller contact, Contact contactCreator,
                               Instance instance, ContentValues cv)
    {
        String picture;


        cv.put(ZeroContract.Contacts.LAST_NAME, contact.getLastName());
        cv.put(ZeroContract.Contacts.FIRST_NAME, contact.getFirstName());
        cv.put(ZeroContract.Contacts.PICTURE_ID, contact.getPictureId());
        cv.put(ZeroContract.Contacts.USER, account.name);
        cv.put(ZeroContract.Contacts.EK_ID, contact.getEkId());
        cv.put(ZeroContract.Contacts.TYPE, contact.getType());
        contactCreator.setAccount(account);
        if (contact.getPictureId() > 0)
        {
            picture = contact.getPicture(instance, contact.getPictureId());
            cv.put(ZeroContract.Contacts.PICTURE, picture);
            if (picture != null)
                contactCreator.setPhoto(picture);
        }
        contactCreator.setName(contact.getFirstName(), contact.getLastName());
        contactCreator.setOrganization(contact.getOrganizationName(), contact.getOrganizationPost());
        addContactParams(contact, contactCreator);
        mContentResolver.insert(ZeroContract.Contacts.CONTENT_URI, cv);
        contactCreator.commit();
    }

    private void updateContact(Account account, ContactCaller contact, Contact contactCreator,
                               Instance instance, ContentValues cv)
    {
        String picture;


        cv.put(ZeroContract.Contacts.LAST_NAME, contact.getLastName());
        cv.put(ZeroContract.Contacts.FIRST_NAME, contact.getFirstName());
        cv.put(ZeroContract.Contacts.PICTURE_ID, contact.getPictureId());
        cv.put(ZeroContract.Contacts.USER, account.name);
        cv.put(ZeroContract.Contacts.EK_ID, contact.getEkId());
        cv.put(ZeroContract.Contacts.TYPE, contact.getType());
        contactCreator.setAccount(account);
        if (contact.getPictureId() > 0)
        {
            picture = contact.getPicture(instance, contact.getPictureId());
            cv.put(ZeroContract.Contacts.PICTURE, picture);
            if (picture != null)
                contactCreator.setPhoto(picture);
        }
        contactCreator.setName(contact.getFirstName(), contact.getLastName());
        contactCreator.setOrganization(contact.getOrganizationName(), contact.getOrganizationPost());

        updateContactParams(contact, contactCreator);

        mContentResolver.update(ZeroContract.Contacts.CONTENT_URI,
                cv,
                contact.getEkId() + " == " + ZeroContract.Contacts.EK_ID,
                null);
        contactCreator.commit();
    }

    private void updateContactParams(ContactCaller contact, Contact contactCreator)
    {
        Cursor curs = mContentResolver.query(
                ZeroContract.Contacts.CONTENT_URI,
                ZeroContract.Contacts.PROJECTION_NONE,
                ZeroContract.Contacts.EK_ID + " == " + contact.getEkId(),
                null,
                null);
        if (curs != null && curs.getCount() != 0)
        {
            curs.moveToNext();
            mContentResolver.delete(ZeroContract.ContactParams.CONTENT_URI,
                    ZeroContract.ContactParams.FK_CONTACT + " == " + curs.getInt(0),
                    null);
            curs.close();
        }
        addContactParams(contact, contactCreator);
    }


    private void setNewSyncDate(Account account)
    {
        ContentValues cv = new ContentValues();
        cv.put(ZeroContract.LastSyncs.DATE, DateConstant.getCurrentDateFormatted());
        int ret = mContentResolver.update(
                ZeroContract.LastSyncs.CONTENT_URI,
                cv,
                ZeroContract.LastSyncs.USER + " LIKE " + "\"" + account.name + "\"" +
                        " AND " + ZeroContract.LastSyncs.TYPE + " LIKE " + "\"CONTACT\"",
                null);

        if (ret == 0)
        {
            cv.put(ZeroContract.LastSyncs.USER, account.name);
            cv.put(ZeroContract.LastSyncs.TYPE, "CONTACT");
            mContentResolver.insert(ZeroContract.LastSyncs.CONTENT_URI, cv);
        }
    }

    private String getLastSyncContact(Account account) throws UnsupportedEncodingException
    {
        Cursor curs = mContentResolver.query(
                ZeroContract.LastSyncs.CONTENT_URI,
                ZeroContract.LastSyncs.PROJECTION_DATE,
                ZeroContract.LastSyncs.USER + " LIKE " + "\"" + account.name + "\"",
                null,
                null);
        if (curs == null || curs.getCount() == 0)
            return ("");
        curs.moveToFirst();
        String ret = curs.getString(0);
        curs.close();
        ret = "?last_synchro=" + URLEncoder.encode(ret, StandardCharsets.UTF_8.name());
        return (ret);
    }

    private void addContactParams(ContactCaller contact, Contact contactCreator)
    {
        ContentValues cv = new ContentValues();
        int i = -1;

        while (contact.getMailLines(++i) != null)
        {
            cv.clear();
            cv.put(ZeroContract.ContactParams.MAIL_LINES, contact.getMailLines(i));
            cv.put(ZeroContract.ContactParams.POSTAL_CODE, contact.getPostalCode(i));
            cv.put(ZeroContract.ContactParams.CITY, contact.getCity(i));
            cv.put(ZeroContract.ContactParams.COUNTRY, contact.getCountry(i));
            cv.put(ZeroContract.ContactParams.TYPE, Contact.TYPE_MAIL);
            contactCreator.setMail(contact.getMailLines(i), contact.getPostalCode(i),
                    contact.getCity(i), contact.getCountry(i));
            mContentResolver.insert(ZeroContract.ContactParams.CONTENT_URI, cv);
        }
        String email;
        while ((email = contact.getNextEmail()) != null)
        {
            cv.clear();
            cv.put(ZeroContract.ContactParams.EMAIL, email);
            cv.put(ZeroContract.ContactParams.TYPE, Contact.TYPE_EMAIL);
            contactCreator.setEmail(email);
            mContentResolver.insert(ZeroContract.ContactParams.CONTENT_URI, cv);
        }
        String mobile;
        while ((mobile = contact.getNextMobile()) != null)
        {
            cv.clear();
            cv.put(ZeroContract.ContactParams.MOBILE, mobile);
            cv.put(ZeroContract.ContactParams.TYPE, Contact.TYPE_MOBILE);
            contactCreator.setMobileNumber(mobile);
            mContentResolver.insert(ZeroContract.ContactParams.CONTENT_URI, cv);
        }
        String phone;
        while ((phone = contact.getNextPhone()) != null)
        {
            cv.clear();
            cv.put(ZeroContract.ContactParams.PHONE, phone);
            cv.put(ZeroContract.ContactParams.TYPE, Contact.TYPE_PHONE);
            contactCreator.setWorkNumber(phone);
            mContentResolver.insert(ZeroContract.ContactParams.CONTENT_URI, cv);
        }
        String website;
        while ((website = contact.getNextWebsite()) != null)
        {
            cv.clear();
            cv.put(ZeroContract.ContactParams.WEBSITE, website);
            cv.put(ZeroContract.ContactParams.TYPE, Contact.TYPE_WEBSITE);
            contactCreator.setWebsite(website);
            mContentResolver.insert(ZeroContract.ContactParams.CONTENT_URI, cv);
        }
    }

    private void pushObservation(Account account) {

        Log.e(TAG, "Push new observation");
        Instance instance = getInstance(account);

        // Get all non-synced observations
        Cursor cursor = mContentResolver.query(
                ZeroContract.Observations.CONTENT_URI,
                ZeroContract.Observations.PROJECTION_ALL,
                ZeroContract.Observations.USER+ " LIKE "+"\"" +account.name+ "\"" +
                        " AND " + ZeroContract.Observations.SYNCED + " == " + 0, null,
                ZeroContract.Observations.SORT_ORDER_DEFAULT);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Cursor issuesCursor = mContentResolver.query(
                        ZeroContract.ObservationIssues.CONTENT_URI,
                        ZeroContract.ObservationIssues.PROJECTION_ALL,
                        ZeroContract.ObservationIssues.FK_OBSERVATION + " == " +
                                cursor.getInt(0), null, null);
                try {
                    postNewObservation(cursor, issuesCursor, instance);
                } catch (JSONException | IOException | HTTPException e) {
                    e.printStackTrace();
                }
            }
        } else
            if (BuildConfig.DEBUG) Log.i(TAG, "No Observation to sync");
        if (BuildConfig.DEBUG) Log.i(TAG, "Finish Observation synchronization");

    }

    private void postNewObservation(Cursor observationCursor, Cursor issueCursor, Instance instance) throws JSONException, IOException, HTTPException {

        Log.e(TAG, "postNewObservation");
        // Creates JSONObject for each observation
        JSONObject attributes = new JSONObject();
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
        attributes.put("observed_on", dateTime.format(new Date(observationCursor.getLong(2))));
        attributes.put("activity_id", observationCursor.getInt(1));
        if (!observationCursor.isNull(4))
            attributes.put("scale_id", observationCursor.getInt(4));
        if (!observationCursor.isNull(6))
            attributes.put("description", observationCursor.getString(6));
        // Parse geolocation
        if (!observationCursor.isNull(7) && !observationCursor.isNull(8))
            attributes.put("geolocation", String.format("SRID=4326; POINT(%s %s)",
                    observationCursor.getDouble(7), observationCursor.getDouble(8)));

        // Parse pictures
        if (!observationCursor.isNull(5)) {
            String[] paths = observationCursor.getString(5).split(",");
            Log.e(TAG, "there is " + paths.length + " pictures");

            JSONArray jsonArray = new JSONArray();
            for (String path : paths) {

                String filePath = null;
                Uri pictureUri = Uri.parse(path);

                if (pictureUri.getScheme().equals("content")) {

                    String wholeID = DocumentsContract.getDocumentId(pictureUri);
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String selection = MediaStore.Images.Media._ID + "=?";

                    // TODO check if MediaStore.Images.Media.INTERNAL_CONTENT_URI is needed
                    Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, selection, new String[]{id}, null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        filePath = "file://" + cursor.getString(0);
                        cursor.close();
                    }
                } else {
                    filePath = pictureUri.toString();
                }
                Log.e(TAG, "Uri = " + Uri.parse(filePath));

                InputStream is = getContext().getContentResolver().openInputStream(Uri.parse(filePath));
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                if (is != null)
                    is.close();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                String pictureJson = "data:image/jpeg;base64," + Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                jsonArray.put(pictureJson);
            }
            attributes.put("pictures", jsonArray);
        }

        // Parse plants
        if (!observationCursor.isNull(3)) {
            String[] plants = observationCursor.getString(3).split(",");
            JSONArray jsonArray = new JSONArray();
            for (String plant : plants) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", plant);
                jsonArray.put(jsonObject);
            }
            if (jsonArray.length() > 0)
                attributes.put("plants", jsonArray);
        }

        // Parse issues
        if (issueCursor != null) {
            JSONArray jsonArray = new JSONArray();
            try {
                while (issueCursor.moveToNext()) {
                    if (!issueCursor.isNull(3)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", issueCursor.getInt(3));
                        jsonArray.put(jsonObject);
                    }
                }
            } finally {
                issueCursor.close();
            }
            if (jsonArray.length() > 0)
                attributes.put("issues", jsonArray);
        }


        long resultId = Observation.create(instance, attributes);
        Log.e(TAG, "Obs #" + resultId +" created !" );
        // Marks them as synced
        ContentValues cv = new ContentValues();
        cv.put(ZeroContract.Observations.SYNCED, 1);
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Observations.CONTENT_URI, Long.toString(observationCursor.getLong(0))), cv, null, null);
        Log.i(TAG, "Observation SYNC DONE");
    }

    /**
     *   Get workers from Ekylibre instance
     */
    private void pullWorkers(Account account) {

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Beginning network workers synchronization");

        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        List<Worker> workersList = null;
        try {
            workersList = Worker.all(instance, null);
        } catch (JSONException | IOException | HTTPException e) {
            e.printStackTrace();
        }

        if (workersList == null)
            return;

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Nombre de workers : " + workersList.size() );

        for (Worker worker : workersList) {
            cv.put(ZeroContract.Workers.EK_ID, worker.id);
            cv.put(ZeroContract.Workers.NAME, worker.name);
            cv.put(ZeroContract.Workers.NUMBER, worker.number);
            cv.put(ZeroContract.Workers.QUALIFICATION, worker.qualification);
            cv.put(ZeroContract.Workers.ABILITIES, worker.abilities);
            cv.put(ZeroContract.Workers.USER, account.name);
            mContentResolver.insert(ZeroContract.Workers.CONTENT_URI, cv);
        }

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Finish network workers synchronization");
    }

    /**
     *   Get land parcels from Ekylibre instance
     */
    private void pullLandParcels(Account account) {

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Beginning network land parcels synchronization");

        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        List<LandParcel> landParcelsList = null;
        try {
            landParcelsList = LandParcel.all(instance, null);
        } catch (JSONException | IOException | HTTPException e) {
            e.printStackTrace();
        }

        if (landParcelsList == null)
            return;

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Nombre de parcelles : " + landParcelsList.size() );

        for (LandParcel landParcel : landParcelsList) {
            cv.put(ZeroContract.LandParcels.EK_ID, landParcel.id);
            cv.put(ZeroContract.LandParcels.NAME, landParcel.name);
            cv.put(ZeroContract.LandParcels.NET_SURFACE_AREA, landParcel.net_surface_area);
            cv.put(ZeroContract.LandParcels.USER, account.name);
            mContentResolver.insert(ZeroContract.LandParcels.CONTENT_URI, cv);
        }

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Finish network land parcels synchronization");
    }

    /**
     *   Get equipments from Ekylibre instance
     */
    private void pullEquipments(Account account) {

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Beginning network equipments synchronization");

        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        List<Equipment> equipmentsList = null;
        try {
            equipmentsList = Equipment.all(instance, null);
        } catch (JSONException | IOException | HTTPException e) {
            e.printStackTrace();
        }

        if (equipmentsList == null)
            return;

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Nombre d'equipments : " + equipmentsList.size() );

        for (Equipment equipment : equipmentsList) {
            cv.put(ZeroContract.Equipments.EK_ID, equipment.id);
            cv.put(ZeroContract.Equipments.NAME, equipment.name);
            cv.put(ZeroContract.Equipments.NUMBER, equipment.number);
            cv.put(ZeroContract.Equipments.VARIETY, equipment.variety);
            cv.put(ZeroContract.Equipments.ABILITIES, equipment.abilities);
            cv.put(ZeroContract.Equipments.USER, account.name);
            mContentResolver.insert(ZeroContract.Equipments.CONTENT_URI, cv);
        }

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Finish network equipments synchronization");
    }

    protected Instance getInstance(Account account)
    {
        Instance instance = null;
            try {
                instance = new Instance(account, mAccountManager);
            } catch(AccountsException e) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Account manager or user cannot help. Cannot get token.");
            } catch(IOException e) {
                if (BuildConfig.DEBUG) Log.w(TAG, "IO problem. Cannot get token.");
            }
        return (instance);
    }

}
