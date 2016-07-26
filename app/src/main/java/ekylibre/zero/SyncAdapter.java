package ekylibre.zero;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.database.Cursor;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;

import ekylibre.api.Crumb;
import ekylibre.api.Instance;
import ekylibre.api.Issue;
import ekylibre.api.PlantCounting;
import ekylibre.api.PlantCountingItem;
import ekylibre.api.PlantDensityAbacus;
import ekylibre.api.Plant;
import ekylibre.api.ZeroContract;
import ekylibre.exceptions.HTTPException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    public static final String TAG = "SyncAdapter";
    public static final String ACCOUNT_TYPE = "ekylibre.account.basic";

    ContentResolver mContentResolver;
    AccountManager mAccountManager;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
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
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.d(TAG, "Performing Sync ! Pushing all the local data to Ekylibre instance");
        performCrumbsSync(account, extras, authority, provider, syncResult);
        performIssuesSync(account, extras, authority, provider, syncResult);
        performPlantDensityAbaciSync(account, extras, authority, provider, syncResult);
        performPlantsSync(account, extras, authority, provider, syncResult);
        performPlantCounting(account, extras, authority, provider, syncResult);
        performPlantCountingItem(account, extras, authority, provider, syncResult);
    }

    /*
    ** Following methods are used to transfer data between zero and ekylibre instance
    ** There are POST and GET call
    */

    public void performCrumbsSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.i(TAG, "Beginning network synchronization");
        
        // Get crumbs from tracking (content) provider
        Cursor cursor = mContentResolver.query(ZeroContract.Crumbs.CONTENT_URI,
                ZeroContract.Crumbs.PROJECTION_ALL,
                ZeroContract.CrumbsColumns.SYNCED + " IS NULL OR " + ZeroContract.CrumbsColumns.SYNCED + " <= 0",
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
                Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.i(TAG, "Finish network synchronization");
    }

    private void    postNewCrumb(Cursor cursor, Instance instance)
            throws JSONException, IOException, HTTPException
    {
        Log.i(TAG, "New crumb");
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

        long id = Crumb.create(instance, attributes);
        // Marks them as synced
        ContentValues values = new ContentValues();
        values.put(ZeroContract.CrumbsColumns.SYNCED, id);
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Crumbs.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
    }

    public void performIssuesSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.i(TAG, "Beginning network issues synchronization");

        // Get crumbs from Issue (content) provider
        Cursor cursor = mContentResolver.query(ZeroContract.Issues.CONTENT_URI, ZeroContract.Issues.PROJECTION_ALL, ZeroContract.IssuesColumns.SYNCED + " IS NULL OR " + ZeroContract.IssuesColumns.SYNCED + " <= 0", null, ZeroContract.Issues.SORT_ORDER_DEFAULT);


        try
        {
            if (cursor.getCount() > 0)
            {
                Instance instance = getInstance(account);
                cursor.moveToFirst();
                while (!cursor.isAfterLast())
                {
                    postNewIssue(cursor, instance);
                    cursor.moveToNext();
                }
            }
            else
            {
                Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Log.i(TAG, "Finish network synchronization");
    }

    private void    postNewIssue(Cursor cursor, Instance instance)
            throws JSONException, IOException, HTTPException
    {
        Log.i(TAG, "New issue");

        // Post it to ekylibre
        JSONObject attributes = new JSONObject();
        attributes.put("nature", cursor.getString(1));
        attributes.put("gravity", cursor.getInt(2));
        attributes.put("priority", cursor.getInt(3));
        attributes.put("description",cursor.getString(5) );
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        attributes.put("observed_at", parser.format(new Date(cursor.getLong(8))));
        if (cursor.getDouble(9) != 0 && cursor.getDouble(10) != 0)
        {
            attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(10)) + " " + Double.toString(cursor.getDouble(9)) + ")");
        }

        long id = Issue.create(instance, attributes);
        // Marks them as synced
        ContentValues values = new ContentValues();
        values.put(ZeroContract.IssuesColumns.SYNCED, id);
        mContentResolver.update(Uri.withAppendedPath(ZeroContract.Issues.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
    }

    public void performPlantDensityAbaciSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {

        Log.i(TAG, "Destruction of the PlantDensityAbacus table");
        int result = mContentResolver.delete(ZeroContract.PlantDensityAbaci.CONTENT_URI, null, null);

        Log.i(TAG, "Beginning network plant_density_abaci synchronization");
        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        try
        {
            List<PlantDensityAbacus> abacusList = PlantDensityAbacus.all(instance, new JSONObject());
            Log.d("zero", "Nombre d'abaque : " + abacusList.size() );
            Iterator<PlantDensityAbacus> abacusIterator = abacusList.iterator();

            Log.d("zero", "début parcours de liste plantDensityAbacus");

            while(abacusIterator.hasNext())
            {
                Log.d("zero", "boucle abaque");

                PlantDensityAbacus plantDensityAbacus = abacusIterator.next();
                cv.put(ZeroContract.PlantDensityAbaciColumns._ID, plantDensityAbacus.getId());
                cv.put(ZeroContract.PlantDensityAbaciColumns.NAME, plantDensityAbacus.getName());
                cv.put(ZeroContract.PlantDensityAbaciColumns.VARIETY, plantDensityAbacus.getVariety());
                cv.put(ZeroContract.PlantDensityAbaciColumns.GERMINATION_PERCENTAGE, plantDensityAbacus.getGerminationPercentage());
                cv.put(ZeroContract.PlantDensityAbaciColumns.SEEDING_DENSITY_UNIT, plantDensityAbacus.getSeedingDensityUnit());
                cv.put(ZeroContract.PlantDensityAbaciColumns.SAMPLING_LENGTH_UNIT, plantDensityAbacus.getSamplingLenghtUnit());
                cv.put(ZeroContract.PlantDensityAbaciColumns.USER, account.name);
                mContentResolver.insert(ZeroContract.PlantDensityAbaci.CONTENT_URI, cv);
            }
            Log.d("zero", "fin parcours de liste plantDensityAbacus");
        }
        catch (JSONException j)
        {
            Log.d("zero", "JSON Exception : " + j.getMessage());
            j.printStackTrace();
        }
        catch (IOException i)
        {
            Log.d("zero", "IO Exception : " + i.getMessage());
        }
        catch (HTTPException h){
            Log.d("zero", "HTTP Exception : " + h.getMessage());
        }

        Log.i(TAG, "Finish network plant_density_abaci synchronization");
    }

    public void performPlantsSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.i(TAG, "Destruction of the Plant table");
        int result = mContentResolver.delete(ZeroContract.Plants.CONTENT_URI, null, null);
        Log.i(TAG, "Beginning network plants synchronization");
        ContentValues cv = new ContentValues();
        Instance instance = getInstance(account);

        try
        {
            List<Plant> plantsList = Plant.all(instance, new JSONObject());
            Log.d(TAG, "Nombre de plants : " + plantsList.size() );
            Iterator<Plant> plantsIterator = plantsList.iterator();
            Log.d(TAG, "début parcours de liste plantDensityAbacus");
            while(plantsIterator.hasNext())
            {
                Log.d(TAG, "boucle");
                Plant plants = plantsIterator.next();
                cv.put(ZeroContract.Plants._ID, plants.getId());
                cv.put(ZeroContract.Plants.NAME, plants.getName());
                cv.put(ZeroContract.Plants.VARIETY, plants.getVariety());
                cv.put(ZeroContract.Plants.ACTIVE, true);
                cv.put(ZeroContract.Plants.USER, account.name);
                mContentResolver.insert(ZeroContract.Plants.CONTENT_URI, cv);
            }
            Log.d(TAG, "fin parcours de liste plantDensityAbacus");
        }
        catch (JSONException j)
        {
            Log.d(TAG, "JSON Exception : " + j.getMessage());
            j.printStackTrace();
        }
        catch (IOException i)
        {
            Log.d(TAG, "IO Exception : " + i.getMessage());
        }
        catch (HTTPException h)
        {
            Log.d(TAG, "HTTP Exception : " + h.getMessage());
        }
        Log.i(TAG, "Finish network plant synchronization");
    }

    public void performPlantCounting(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.i(TAG, "Beginning network synchronization");
        Cursor cursor = mContentResolver.query(ZeroContract.PlantCountings.CONTENT_URI,
                ZeroContract.PlantCountings.PROJECTION_ALL,
                null,
                null,
                ZeroContract.PlantCountings.SORT_ORDER_DEFAULT);
        try
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                Instance instance = getInstance(account);

                while (cursor.moveToNext())
                {
                    Log.i(TAG, "New plantCounting");
                    // Post it to ekylibre
                    JSONObject attributes = new JSONObject();
                    attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(3)) + " " + Double.toString(cursor.getDouble(2)) + ")");
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    attributes.put("observed_at", parser.format(new Date(cursor.getLong(1))));
                    attributes.put("observation", cursor.getString(4));
                    attributes.put("plant_density_abacus_item_id", cursor.getString(5));
                    attributes.put("plant_density_abacus_id", cursor.getString(7));
                    attributes.put("plant_id", cursor.getString(8));
                    attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));

                    long id = PlantCounting.create(instance, attributes);
                }
                cursor.close();
            }
            else
            {
                Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.i(TAG, "Finish network synchronization");
    }

    public void performPlantCountingItem(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.i(TAG, "Beginning network synchronization");
        Cursor cursor = mContentResolver.query(ZeroContract.PlantCountingItems.CONTENT_URI,
                ZeroContract.PlantCountingItems.PROJECTION_ALL,
                null,
                null,
                ZeroContract.PlantCountingItems.SORT_ORDER_DEFAULT);
        try
        {
            if (cursor != null && cursor.getCount() > 0)
            {
                Instance instance = getInstance(account);

                while (cursor.moveToNext())
                {
                    Log.i(TAG, "New plantCounting");
                    // Post it to ekylibre
                    JSONObject attributes = new JSONObject();
                    attributes.put("value", cursor.getInt(1));
                    attributes.put("plant_id", cursor.getInt(2));
                    attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));

                    long id = PlantCountingItem.create(instance, attributes);
                }
                cursor.close();
            }
            else
            {
                Log.i(TAG, "Nothing to sync");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.i(TAG, "Finish network synchronization");
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
                Log.e(TAG, "Account manager or user cannot help. Cannot get token.");
            }
            catch(IOException e)
            {
                Log.w(TAG, "IO problem. Cannot get token.");
            }
        return (instance);
    }

}
