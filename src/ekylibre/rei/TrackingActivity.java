package ekylibre.rei;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ekylibre.rei.provider.TrackingContract;
import ekylibre.rei.provider.TrackingProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TrackingActivity extends Activity implements TrackingListenerWriter {
    
    public final static String KEY_ACCOUNT = "account";

    private long mMasterDuration;
    private long mMasterStart;
    private boolean mRunning;
    private String mLastProcedureNature, mLastProcedureNatureName;
    private Chronometer mMasterChrono;
    private Button mScanButton, mStartButton, mStopButton, mPauseButton, mResumeButton;
    private HorizontalScrollView mDetails;
    private TextView mProcedureNature, mLatitude, mLongitude, mCrumbsCount, mCoordinates, mBarcode;
    private String mLocationProvider;
    private TrackingListener mTrackingListener;
    private Account mAccount;
    private AlertDialog.Builder mProcedureChooser;
    private SharedPreferences mPreferences;
    private IntentIntegrator mScanIntegrator;

    private LocationManager mLocationManager;
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotificationBuilder;
    private int mNotificationID;
    //private Notification mNotification;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get default account or ask for it if necessary
        final AccountManager manager = AccountManager.get(this);
        final Account[] accounts = manager.getAccountsByType(SyncAdapter.ACCOUNT_TYPE);
        if (accounts.length <= 0) {
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, SyncAdapter.ACCOUNT_TYPE);
            intent.putExtra(AuthenticatorActivity.KEY_REDIRECT, AuthenticatorActivity.CHOICE_REDIRECT_TRACKING);
            startActivity(intent);
            finish();
            return;
        } else if (accounts.length > 1) {
            // TODO: Propose the list of account or get the one by default
            mAccount = accounts[0];
        } else {
            mAccount = accounts[0];
        }

        // Set content view
        setContentView(R.layout.tracking);
        
        // Find view elements
        mProcedureNature = (TextView) findViewById(R.id.procedure_nature);
        mMasterChrono = (Chronometer) findViewById(R.id.master_chrono);
        mDetails      = (HorizontalScrollView) findViewById(R.id.details);
        mLatitude     = (TextView)    findViewById(R.id.latitude);
        mLongitude    = (TextView)    findViewById(R.id.longitude);
        mCrumbsCount  = (TextView)    findViewById(R.id.crumbs_count);
        // mCoordinates  = (TextView)    findViewById(R.id.coordinates);
        // mBarcode      = (TextView)    findViewById(R.id.barcode);
        mStartButton  = (Button)      findViewById(R.id.start_intervention_button);
        mStopButton   = (Button)      findViewById(R.id.stop_intervention_button);
        mPauseButton  = (Button)      findViewById(R.id.pause_intervention_button);
        mResumeButton = (Button)      findViewById(R.id.resume_intervention_button);
        mScanButton   = (Button)      findViewById(R.id.scan_code_button);

        // Synchronize data
        this.syncData();

        // Acquire a reference to the system Location Manager
        mTrackingListener = new TrackingListener(this);
        mLocationManager  = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationProvider = LocationManager.GPS_PROVIDER;

        // Reference preferences
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Reference scan integrator
        mScanIntegrator = new IntentIntegrator(this);

        // Notification
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationID = 1;
        mNotificationBuilder = new Notification.Builder(this)
            .setOngoing(true)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("")
            .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, TrackingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
            .setSmallIcon(R.drawable.ic_stat_notify);
        

        // Procedure nature chooser for starting intervention
        mProcedureChooser = new AlertDialog.Builder(this)
            .setTitle(R.string.procedure_nature)
            .setNegativeButton(android.R.string.cancel, null)
            .setItems(R.array.procedureNatures_entries, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLastProcedureNature = getResources().getStringArray(R.array.procedureNatures_values)[which];
                    mLastProcedureNatureName = getResources().getStringArray(R.array.procedureNatures_entries)[which];
                    Log.d("rei", "Start a new " + mLastProcedureNature);

                    mStartButton.setVisibility(View.GONE);
                    mMasterChrono.setVisibility(View.VISIBLE);
                    mStopButton.setVisibility(View.VISIBLE);
                    mPauseButton.setVisibility(View.VISIBLE);
                    mScanButton.setVisibility(View.VISIBLE);
                    mProcedureNature.setVisibility(View.VISIBLE);
                    mProcedureNature.setText(mLastProcedureNatureName);

                    mMasterStart = SystemClock.elapsedRealtime();
                    mMasterDuration = 0;
                    mMasterChrono.setBase(mMasterStart);
                    mMasterChrono.start();
                    
                    startTracking();

                    final Bundle metadata = new Bundle();
                    metadata.putString("procedureNature", mLastProcedureNature);
                    addCrumb("start", metadata);

                    mNotificationBuilder
                        .setSmallIcon(R.drawable.ic_stat_notify_running)
                        .setContentTitle(mLastProcedureNatureName)
                        .setContentText(getString(R.string.running));
                    mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tracking, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            // case R.id.action_search:
            //     // openSearch();
            //     return true;
        case R.id.action_settings:
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);  
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


    public void startIntervention(View view) {
        mProcedureChooser.show();
    }


    public void stopIntervention(View view) {
        mMasterChrono.stop();
        mMasterChrono.setVisibility(View.INVISIBLE);
        mStopButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mScanButton.setVisibility(View.GONE);
        mDetails.setVisibility(View.GONE);
        mProcedureNature.setVisibility(View.INVISIBLE);
        mStartButton.setVisibility(View.VISIBLE);
        this.stopTracking();
        this.addCrumb("stop");

        mNotificationBuilder
            .setSmallIcon(R.drawable.ic_stat_notify)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("");
        mNotificationManager.cancel(mNotificationID);
    }


    public void pauseIntervention(View view) {
        mMasterDuration += SystemClock.elapsedRealtime() - mMasterStart;
        mMasterChrono.stop();
        mPauseButton.setVisibility(View.GONE);
        // mStartButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        mScanButton.setVisibility(View.GONE);
        mResumeButton.setVisibility(View.VISIBLE);
        this.stopTracking();
        this.addCrumb("pause");
        mNotificationBuilder
            .setSmallIcon(R.drawable.ic_stat_notify_paused)
            .setContentText(getString(R.string.paused));
        mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
   }

    public void resumeIntervention(View view) {
        mMasterStart = SystemClock.elapsedRealtime();
        mMasterChrono.setBase(mMasterStart - mMasterDuration);
        mMasterChrono.start();
        mResumeButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        // mStartButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
        mScanButton.setVisibility(View.VISIBLE);
        this.startTracking();
        this.addCrumb("resume");
        mNotificationBuilder
            .setSmallIcon(R.drawable.ic_stat_notify_running)
            .setContentText(getString(R.string.running));
        mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
    }


    public void scanCode(View view) {
        mScanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult aScanResult = mScanIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (aScanResult != null) {
            final String contents = aScanResult.getContents();
            if (contents != null) {
                // TODO: Ask for quantity
                // mBarcode.setText("CODE: " + contents);
                
                // handle scan result
                final Bundle metadata = new Bundle();
                metadata.putString("scannedCode", aScanResult.getFormatName() + ":" + contents);
                this.addCrumb("scan", metadata);
            }
        }
        // else continue with any other code you need in the method
    }

    private void startTracking() {
        mLocationManager.requestLocationUpdates(mLocationProvider, 1000, 0, mTrackingListener);
        mRunning = true;
    }

    private void stopTracking() {
        mLocationManager.removeUpdates(mTrackingListener);
        mRunning = false;
    }

    private void addCrumb(String type) {
        this.addCrumb(type, null);
    }

    private void addCrumb(String type, Bundle metadata) {
        Location location = mLocationManager.getLastKnownLocation(mLocationProvider);
        if (location == null) {
            TrackingListener listener = new TrackingListener(this, type, metadata);
            mLocationManager.requestSingleUpdate(mLocationProvider, listener, null);
        } else {
            writeCrumb(location, type, metadata);
        }
    }    

    public void writeCrumb(Location location, String type, Bundle metadata) {
        ContentValues values = new ContentValues();

        values.put(TrackingContract.CrumbsColumns.TYPE, type);
        values.put(TrackingContract.CrumbsColumns.LATITUDE, location.getLatitude());
        values.put(TrackingContract.CrumbsColumns.LONGITUDE, location.getLongitude());
        // values.put(TrackingContract.CrumbsColumns.READ_AT, location.getTime());
        long readAt = (new Date()).getTime();
        values.put(TrackingContract.CrumbsColumns.READ_AT, readAt);
        values.put(TrackingContract.CrumbsColumns.ACCURACY, location.getAccuracy());
        values.put(TrackingContract.CrumbsColumns.SYNCED, 0);
        if (metadata != null) {
            try {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                Object[] keys = metadata.keySet().toArray();
                String key;
                for(int i = 0; i < keys.length; i++) {
                    key = (String) keys[i];
                    metadata.getString(key);
                    pairs.add(new BasicNameValuePair(key, metadata.getString(key)));
                }
                UrlEncodedFormEntity params  = new UrlEncodedFormEntity(pairs);
                ByteArrayOutputStream stream = new ByteArrayOutputStream(4096);
                params.writeTo(stream);
                values.put(TrackingContract.CrumbsColumns.METADATA, stream.toString());
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
            // values.put(TrackingContract.CrumbsColumns.METADATA, metadata.getString("procedureNature"));
        }

        getContentResolver().insert(TrackingContract.Crumbs.CONTENT_URI, values);

        // Sync data is interesting moment
        if (type.equals("stop")) { //  || type.equals("pause")
            this.syncData();
        }

        this.refreshDetails(location);
    }

    private void refreshDetails(Location location) {
        Boolean aShow = mPreferences.getBoolean(SettingsActivity.PREF_SHOW_DETAILS, false);
        if (aShow && mRunning) {
            if (mDetails.getVisibility() != View.VISIBLE) {
                mDetails.setVisibility(View.VISIBLE);
            }
            Cursor cursor = getContentResolver().query(TrackingContract.Crumbs.CONTENT_URI, TrackingContract.Crumbs.PROJECTION_NONE, null, null, null);
            int count = cursor.getCount();
            // Called when a new location is found by the network location provider.
            mLatitude.setText(String.valueOf(location.getLatitude()));
            mLongitude.setText(String.valueOf(location.getLongitude()));
            mCrumbsCount.setText(String.valueOf(count));

        } else if (mDetails.getVisibility() == View.VISIBLE) {
            mDetails.setVisibility(View.GONE);
        }
    }


    // Call the sync service
    private void syncData() {
        Log.d("rei", "syncData: " + mAccount.toString() + ", " + TrackingContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, TrackingContract.AUTHORITY, extras);
    }

    
}
