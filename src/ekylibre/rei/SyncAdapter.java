package ekylibre.rei;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

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
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    // Push data between rei and ekylibre
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        
        // Get crumbs from provider

        // Post it to ekylibre

        // Marks them as treated
        
        Log.i(TAG, "Finish network synchronization");
    }


      
        // // Create a new HttpClient and Post Header
        // HttpClient httpClient = new DefaultHttpClient();
        // HttpPost httpPost = new HttpPost("https://demo.ergolis.com/api/v1/crumbs");
        
        // try {
        //     // Add your data
        //     List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        //     nameValuePairs.add(new BasicNameValuePair("id", "12345"));
        //     nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
        //     httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
        //     // Execute HTTP Post Request
        //     HttpResponse response = httpClient.execute(httpPost);
            
        // } catch (ClientProtocolException e) {
        //     // TODO Auto-generated catch block
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        // }


}
