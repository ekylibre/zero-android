package ekylibre.zero;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Set;

import ekylibre.api.Issue;
import ekylibre.api.Instance;
import ekylibre.zero.provider.IssueContract;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class IssueSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "IssueSyncAdapter";
    public static final String ACCOUNT_TYPE = "ekylibre.account.basic";

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    AccountManager mAccountManager;

    /**
     * Set up the sync adapter
     */
    public IssueSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public IssueSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
    }

    // Push data between zero and ekylibre
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.i(TAG, "Beginning network issues synchronization");

        // Get crumbs from Issue (content) provider
        Cursor cursor = mContentResolver.query(IssueContract.Issues.CONTENT_URI, IssueContract.Issues.PROJECTION_ALL, IssueContract.IssueColumns.SYNCED + " IS NULL OR " + IssueContract.IssueColumns.SYNCED + " <= 0", null, IssueContract.Issues.SORT_ORDER_DEFAULT);


        if (cursor.getCount() > 0) {
            Instance instance = getInstance(account);

            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.i(TAG, "New issue");

                    // Post it to ekylibre
                    JSONObject attributes = new JSONObject();
                    attributes.put("nature", cursor.getString(1));
                    attributes.put("gravity", cursor.getInt(2));
                    attributes.put("priority", cursor.getInt(3));
                    attributes.put("description", cursor.getString(4));
                    //attributes.put("target_type", "Product");
                    //attributes.put("target_id", "1");
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    attributes.put("observed_at", parser.format(new Date(cursor.getLong(8))));
                    if(cursor.getDouble(9) != 0 && cursor.getDouble(10) != 0){
                       attributes.put("geolocation", "SRID=4326; POINT(" + Double.toString(cursor.getDouble(10)) + " " + Double.toString(cursor.getDouble(9)) + ")");
                    }

                    long id = Issue.create(instance, attributes);
                    // Marks them as synced
                    ContentValues values = new ContentValues();
                    values.put(IssueContract.IssueColumns.SYNCED, id);
                    mContentResolver.update(Uri.withAppendedPath(IssueContract.Issues.CONTENT_URI, Long.toString(cursor.getLong(0))), values, null, null);
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

    protected Instance getInstance(Account account){
        Instance instance = null;
            try {
                instance = new Instance(account, mAccountManager);
            } catch(AccountsException e) {
                Log.e(TAG, "Account manager or user cannot help. Cannot get token.");
            } catch(IOException e) {
                Log.w(TAG, "IO problem. Cannot get token.");
            }
        return instance;
    }

}
