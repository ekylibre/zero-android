package ekylibre.zero.tracking;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.util.AccountTool;
import ekylibre.util.PermissionManager;
import ekylibre.util.UpdatableActivity;


public class TrackingActivity extends UpdatableActivity implements TrackingListenerWriter
{
    protected final int PRODUCT_NAME = 1;
    protected final int LABEL = 2;
    protected final int NAME = 3;
    public final static String NEW = "new_intervention";

    private long mMasterDuration, mMasterStart;
    private String mLastProcedureNature, mLastProcedureNatureName;
    private Chronometer mMasterChrono, mPrecisionModeChrono;
    private Button mScanButton, mStartButton, mStopButton, mPauseButton, mResumeButton, mPrecisionModeStartButton, mPrecisionModeStopButton, mSyncButton;
    private HorizontalScrollView mDetails;
    private TextView mProcedureNature, mAccuracy, mLatitude, mLongitude, mCrumbsCount;
    private String mLocationProvider;
    private TrackingListener mTrackingListener;
    private Account mAccount;
    private AlertDialog.Builder mProcedureChooser;
    private SharedPreferences mPreferences;
    private IntentIntegrator mScanIntegrator;
    public static final String   _interventionID = "intervention_id";
    private RelativeLayout infoLayout;

    private LocationManager mLocationManager;
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotificationBuilder;
    private int mNotificationID;
    //private Notification mNotification;

    private Button mMapButton;
    private int    mInterventionID;

    private CrumbsCalculator crumbsCalculator;
    private boolean mNewIntervention;

    private final String TAG = "TrackingActivity";

    @Override
    public void onStart()
    {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
        {
            AccountTool.askForAccount(this, this);
            return;
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAccount = AccountTool.getCurrentAccount(this);


        // Set content view
        setContentView(R.layout.tracking);

        mNewIntervention = getIntent().getBooleanExtra(TrackingActivity.NEW, false);

        // Find view elements
        mDetails                  = (HorizontalScrollView) findViewById(R.id.details);
        mProcedureNature          = (TextView)   findViewById(R.id.procedure_nature);
        mMasterChrono             = (Chronometer)findViewById(R.id.master_chrono);
        mPrecisionModeChrono      = (Chronometer)findViewById(R.id.precision_mode_chrono);
        mAccuracy                 = (TextView)   findViewById(R.id.accuracy);
        mLatitude                 = (TextView)   findViewById(R.id.latitude);
        mLongitude                = (TextView)   findViewById(R.id.longitude);
        mCrumbsCount              = (TextView)   findViewById(R.id.crumbs_count);
        mStartButton              = (Button)     findViewById(R.id.start_intervention_button);
        mStopButton               = (Button)     findViewById(R.id.stop_intervention_button);
        mPauseButton              = (Button)     findViewById(R.id.pause_intervention_button);
        mResumeButton             = (Button)     findViewById(R.id.resume_intervention_button);
        mScanButton               = (Button)     findViewById(R.id.scan_code_button);
        mSyncButton               = (Button)     findViewById(R.id.sync_button);
        mMapButton                = (Button)     findViewById(R.id.map_button);
        mPrecisionModeStartButton = (Button)     findViewById(R.id.start_precision_mode_button);
        mPrecisionModeStopButton  = (Button)     findViewById(R.id.stop_precision_mode_button);
        infoLayout                = (RelativeLayout) findViewById(R.id.infoLayout);

        // Acquire a reference to the system Location Manager
        mTrackingListener = new TrackingListener(this);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationProvider = LocationManager.GPS_PROVIDER;

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mScanIntegrator = new IntentIntegrator(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationID = 1;
        mNotificationBuilder = new Notification.Builder(this)
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("")
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, TrackingActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_stat_notify);

        if (!mNewIntervention)
            disableInterface();
        else
            createProcedureChooser();

    }

    private void disableInterface()
    {
        mStartButton.setVisibility(View.GONE);
        Intent intent = getIntent();
        mInterventionID = intent.getIntExtra(ZeroContract.Interventions._ID, 0);
        writeInterventionInfo();
    }

    // TODO => Il faut trier par name dans la requete puis des qu'on a un nouveau name on
    // TODO => affiche le label en tant que titre et on enregistre le titre ref
    private void writeInterventionInfo()
    {
        int botId;

        botId = writeTarget(View.NO_ID);
        botId = writeInput(botId);
        botId = writeTool(botId);



    }



    private int writeTarget(int botId)
    {
        Cursor cursTarget = queryTarget();

        if (cursTarget == null || cursTarget.getCount() == 0)
            return (0);
        return (writeCursor(cursTarget, botId));
    }

    private int writeInput(int botId)
    {
        Cursor cursInput = queryInput();

        if (cursInput == null || cursInput.getCount() == 0)
            return (0);
        return (writeCursor(cursInput, botId));
    }

    private int writeTool(int botId)
    {
        Cursor cursTool = queryTool();

        if (cursTool == null || cursTool.getCount() == 0)
            return (0);
        return (writeCursor(cursTool, botId));
    }

    private int writeCursor(Cursor curs, int botId)
    {
        String str = "";
        String titleRef = "";

        while (curs.moveToNext())
        {
            if (!curs.getString(NAME).equals(titleRef))
            {
                if (!str.equals(""))
                {
                    botId = flushBlock(str, botId);
                    str = "";
                }
                botId = createTitle(curs, botId);
                titleRef = curs.getString(NAME);
            }
            str += curs.getString(PRODUCT_NAME) + "\n";
        }
        botId = flushBlock(str, botId);
        return (botId);
    }

    private int createTitle(Cursor curs, int id)
    {
        TextView title = new TextView(this);

        title.setText("• " + curs.getString(LABEL));
        title.setTextColor(Color.BLACK);
        title.setTypeface(null, Typeface.BOLD);
        title.setId(View.generateViewId());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, id);

        title.setLayoutParams(lp);
        title.setTextSize(18);
        infoLayout.addView(title);
        return (title.getId());
    }

    private int flushBlock(String str, int id)
    {
        TextView text = new TextView(this);

        text.setText(str);
        text.setId(View.generateViewId());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginStart(40);
        lp.addRule(RelativeLayout.BELOW, id);

        text.setLayoutParams(lp);
        text.setGravity(Gravity.LEFT);
        text.setTextColor(Color.parseColor("#454545"));
        text.setTextSize(15);

        infoLayout.addView(text);
        return (text.getId());
    }

    private Cursor queryTarget()
    {
        String[] projectionTarget = ZeroContract.InterventionParameters.PROJECTION_TARGET_FULL;

        Cursor curs = getContentResolver().query(
                ZeroContract.InterventionParameters.CONTENT_URI,
                projectionTarget,
                ZeroContract.InterventionParameters.ROLE + " LIKE " + "\"target\"" + " AND "
                        + mInterventionID + " == " + ZeroContract.InterventionParameters.FK_INTERVENTION,
                null,
                ZeroContract.InterventionParameters.NAME);
        return (curs);
    }

    private Cursor queryInput()
    {
        String[] projectionInput = ZeroContract.InterventionParameters.PROJECTION_INPUT_FULL;

        Cursor curs = getContentResolver().query(
                ZeroContract.InterventionParameters.CONTENT_URI,
                projectionInput,
                ZeroContract.InterventionParameters.ROLE + " LIKE " + "\"input\"" + " AND "
                        + mInterventionID + " == " + ZeroContract.InterventionParameters.FK_INTERVENTION,
                null,
                ZeroContract.InterventionParameters.NAME);
        return (curs);
    }

    private Cursor queryTool()
    {
        String[] projectionTool = ZeroContract.InterventionParameters.PROJECTION_TOOL_FULL;

        Cursor curs = getContentResolver().query(
                ZeroContract.InterventionParameters.CONTENT_URI,
                projectionTool,
                ZeroContract.InterventionParameters.ROLE + " LIKE " + "\"tool\"" + " AND "
                        + mInterventionID + " == " + ZeroContract.InterventionParameters.FK_INTERVENTION,
                null,
                ZeroContract.InterventionParameters.NAME);
        return (curs);
    }












    // |========================================|
    // |============                ============|
    // |=========== OLD WAY TRACKING ===========|
    // |============                ============|
    // |========================================|


    private void createProcedureChooser()
    {
        mProcedureChooser = new AlertDialog.Builder(this)
                .setTitle(R.string.procedure_nature)
                .setNegativeButton(android.R.string.cancel, null)
                .setItems(R.array.procedures_entries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLastProcedureNature = getResources().getStringArray(R.array.procedures_values)[which];
                        mLastProcedureNatureName = getResources().getStringArray(R.array.procedures_entries)[which];
                        Log.d("zero", "Start a new " + mLastProcedureNature);
                        crumbsCalculator = new CrumbsCalculator(mLastProcedureNature);

                        mStartButton.setVisibility(View.GONE);
                        mMasterChrono.setVisibility(View.VISIBLE);
                        mMapButton.setVisibility(View.VISIBLE);
                        mStopButton.setVisibility(View.VISIBLE);
                        mPauseButton.setVisibility(View.VISIBLE);
                        //mScanButton.setVisibility(View.VISIBLE);
                        mSyncButton.setVisibility(View.GONE);
                        //mPrecisionModeStartButton.setVisibility(View.VISIBLE);
                        //mProcedureNature.setVisibility(View.VISIBLE);
                        mProcedureNature.setText(mLastProcedureNatureName);

                        setTitle(mLastProcedureNatureName);

                        mMasterStart = SystemClock.elapsedRealtime();
                        mMasterDuration = 0;
                        mMasterChrono.setBase(mMasterStart);
                        mMasterChrono.start();

                        startTracking();

/*
                        final Bundle metadata = new Bundle();
                        metadata.putString("procedure_nature", mLastProcedureNature);
                        addCrumb("start", metadata);
*/

                        mNotificationBuilder
                                .setSmallIcon(R.mipmap.ic_stat_notify_running)
                                .setContentTitle(mLastProcedureNatureName)
                                .setContentText(getString(R.string.running));
                        mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
                        createIntervention();
                    }
                });

        //  if (!mRunning) {
        //      mProcedureChooser.show();
        //  }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tracking, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/

    private void createIntervention()
    {
        ContentValues values = new ContentValues();

        values.put(ZeroContract.InterventionsColumns.USER, AccountTool.getCurrentAccount(this).name);
        getContentResolver().insert(ZeroContract.Interventions.CONTENT_URI, values);
        Cursor cursor = getContentResolver().query(ZeroContract.Interventions.CONTENT_URI, new String[]{ZeroContract.Interventions._ID}, null, null, null);
        if (cursor == null || !cursor.moveToLast())
            return;
        mInterventionID = cursor.getInt(0);
        cursor.close();
    }

    public void startIntervention(View view) {
        mProcedureChooser.show();
    }

    public void openMap(View view)
    {
        if (!PermissionManager.internetPermissions(this, this)
                || !PermissionManager.storagePermissions(this, this)
                || !PermissionManager.GPSPermissions(this, this))
            return;
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(this._interventionID, mInterventionID);
        startActivity(intent);
    }

    public void stopIntervention(View view) {
/*        if (mPrecisionMode) {
            stopPrecisionMode(view);
        }*/
        mMasterChrono.stop();
        mMasterChrono.setVisibility(View.INVISIBLE);
        mStopButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.GONE);
        mMapButton.setVisibility(View.GONE);
        mScanButton.setVisibility(View.GONE);
        //mPrecisionModeStartButton.setVisibility(View.GONE);
        mDetails.setVisibility(View.GONE);
        mProcedureNature.setVisibility(View.INVISIBLE);
        mStartButton.setVisibility(View.VISIBLE);
        this.stopTracking();
        //this.addCrumb("stop");


        int lastCrumbID = query_last_crumb_id();
        if (PermissionManager.GPSPermissions(this, this) && lastCrumbID != 0)
        {
            ContentValues values = new ContentValues();
            values.put(ZeroContract.Crumbs.TYPE, "stop");
            getContentResolver().update(ZeroContract.Crumbs.CONTENT_URI,
                    values,
                    ZeroContract.CrumbsColumns._ID + " == " + lastCrumbID,
                    null);
        }
        setTitle(R.string.new_intervention);
        mNotificationBuilder
                .setSmallIcon(R.mipmap.ic_stat_notify)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("");
        mNotificationManager.cancel(mNotificationID);
    }

    private int query_last_crumb_id()
    {
        String[] projectionCrumbID = {ZeroContract.CrumbsColumns._ID};

        Cursor cursorCrumb = getContentResolver().query(
                ZeroContract.Crumbs.CONTENT_URI,
                projectionCrumbID,
                null,
                null,
                ZeroContract.CrumbsColumns._ID + " DESC LIMIT 1");
        if (cursorCrumb == null || cursorCrumb.getCount() == 0)
            return (0);
        cursorCrumb.moveToFirst();
        int ret = cursorCrumb.getInt(0);
        cursorCrumb.close();
        return (ret);
    }

    public void pauseIntervention(View view) {
        mMasterDuration += SystemClock.elapsedRealtime() - mMasterStart;
        mMasterChrono.stop();
        mPauseButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        mScanButton.setVisibility(View.GONE);

        mResumeButton.setVisibility(View.VISIBLE);

        this.stopTracking();
        this.addCrumb("pause");
        mNotificationBuilder
                .setSmallIcon(R.mipmap.ic_stat_notify_paused)
                .setContentText(getString(R.string.paused));
        mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
    }

    public void resumeIntervention(View view) {
        mMasterStart = SystemClock.elapsedRealtime();
        mMasterChrono.setBase(mMasterStart - mMasterDuration);
        mMasterChrono.start();

        mResumeButton.setVisibility(View.GONE);
        mPauseButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);

        this.startTracking();
        this.addCrumb("resume");

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
                metadata.putString("scanned_code", aScanResult.getFormatName() + ":" + contents);
                this.addCrumb("scan", metadata);
            }
        }
    }

    private void startTracking() {
        startTracking(0);
    }

    private void startTracking(long interval)
    {
        if (!PermissionManager.GPSPermissions(this, this))
            return;
        mLocationManager.requestLocationUpdates(mLocationProvider, interval, 0, mTrackingListener);
    }

    private void stopTracking()
    {
        if (!PermissionManager.GPSPermissions(this, this))
            return;
        mLocationManager.removeUpdates(mTrackingListener);
    }

    private void addCrumb(String type) {
        this.addCrumb(type, null);
    }

    private void addCrumb(String type, Bundle metadata)
    {
        if (!PermissionManager.GPSPermissions(this, this))
            return;
        TrackingListener listener = new TrackingListener(this, type, metadata);
        mLocationManager.requestSingleUpdate(mLocationProvider, listener, null);
    }

    public void writeCrumb(Location location, String type, Bundle metadata)
    {


        if (!crumbsCalculator.isCrumbAccurate(location, type, metadata))
            return;

        Crumb crumb = crumbsCalculator.getFinalCrumb();


        putCrumbOnLocalDatabase(crumb);

        sendBroadcastNewCrumb(location);

    }

    private void sendBroadcastNewCrumb(Location location)
    {
        Intent intent = new Intent(UpdatableActivity.PING);
        intent.putExtra(TrackingListenerWriter.LATITUDE, location.getLatitude());
        intent.putExtra(TrackingListenerWriter.LONGITUDE, location.getLongitude());
        intent.putExtra(this._interventionID, mInterventionID);
        sendBroadcast(intent);
    }

    private void putCrumbOnLocalDatabase(Crumb crumb)
    {
        ContentValues values = new ContentValues();

        values.put(ZeroContract.CrumbsColumns.USER, AccountTool.getCurrentAccount(this).name);
        values.put(ZeroContract.CrumbsColumns.TYPE, crumb.getType());
        values.put(ZeroContract.CrumbsColumns.LATITUDE, crumb.getLatitude());
        values.put(ZeroContract.CrumbsColumns.LONGITUDE, crumb.getLongitude());
        values.put(ZeroContract.CrumbsColumns.READ_AT, crumb.getDate());
        values.put(ZeroContract.CrumbsColumns.ACCURACY, 0);
        values.put(ZeroContract.CrumbsColumns.SYNCED, 0);
        values.put(ZeroContract.CrumbsColumns.FK_INTERVENTION, mInterventionID);
        putMetadata(crumb.getMetadata(), values);

        getContentResolver().insert(ZeroContract.Crumbs.CONTENT_URI, values);
    }

    private void putMetadata(Bundle metadata, ContentValues values)
    {
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
                values.put(ZeroContract.CrumbsColumns.METADATA, stream.toString());
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        stopIntervention(null);
        super.onDestroy();
    }
}
