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
import ekylibre.zero.provider.TrackingContract;
import ekylibre.zero.provider.TrackingProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    public static final String ACCOUNT_TYPE = "ekylibre.account.basic";

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    AccountManager mAccountManager;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    // Push data between zero and ekylibre
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        
        // Get crumbs from tracking (content) provider
        Cursor cursor = mContentResolver.query(TrackingContract.Crumbs.CONTENT_URI, TrackingContract.Crumbs.PROJECTION_ALL, TrackingContract.CrumbsColumns.SYNCED + " IS NULL OR " + TrackingContract.CrumbsColumns.SYNCED + " <= 0", null, TrackingContract.Crumbs.SORT_ORDER_DEFAULT);
        

        if (cursor.getCount() > 0) {
            Instance instance = null;
            try {
                instance = new Instance(account, mAccountManager);
            } catch(AccountsException e) {
                Log.e(TAG, "Account manager or user cannot help. Cannot get token.");
                return;
            } catch(IOException e) {
                Log.w(TAG, "IO problem. Cannot get token.");
                return;
            }

            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.i(TAG, "New crumb");
                    
                    // Post it to ekylibre
                    JSONObject attributes = new JSONObject();
                    attributes.put("nature", cursor.getString(1));
                    // attributes.put("latitude", cursor.getString(2));
                    // attributes.put("longitude", cursor.getString(3));
                    attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(3)) + " " + Double.toString(cursor.getDouble(2)) + ")");
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    attributes.put("read_at", parser.format(new Date(cursor.getLong(4))));
                    attributes.put("accuracy", cursor.getString(5));
                    attributes.put("device_uid", "android:" + Secure.getString(mContentResolver, Secure.ANDROID_ID));
                    JSONObject hash = new JSONObject();
                    Uri metadata = Uri.parse("http://domain.tld?" + cursor.getString(6));
                    Set<String> keys = metadata.getQueryParameterNames();
                    if (keys.size() > 0) {
                        for (String key : keys) {
                            if (!key.equals("null")) {
                                hash.put(key, metadata.getQueryParameter(key));
                            }
                        }
                        if (hash.length() > 0) {
                            attributes.put("metadata", hash);
                        }
                    }

                    long id = Crumb.create(instance, attributes);
                    // Marks them as synced
                    ContentValues values = new ContentValues();
                    values.put(TrackingContract.CrumbsColumns.SYNCED, id);
                    mContentResolver.update(Uri.withAppendedPath(TrackingContract.Crumbs.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
                    cursor.moveToNext();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Nothing to sync");
        }

        
        Log.i(TAG, "Finish network synchronization");
    }

}
