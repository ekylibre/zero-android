package ekylibre.zero.intervention;

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
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ekylibre.database.ZeroContract;
import ekylibre.util.AccountTool;
import ekylibre.util.DateConstant;
import ekylibre.util.ExtendedChronometer;
import ekylibre.util.PermissionManager;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.R;


public class InterventionActivity extends UpdatableActivity
        implements TrackingListenerWriter
{
    /* ****************************
    **      CONSTANT VALUES
    ** ***************************/
    protected final int PRODUCT_NAME = 1;
    protected final int LABEL = 2;
    protected final int NAME = 3;
    protected final int PREPARATION = 0;
    protected final int TRAVEL = 1;
    protected final int INTERVENTION = 2;
    protected final int PAUSE = 3;
    public final static String NEW = "new_intervention";
    public static final String   _interventionID = "intervention_id";
    private final String TAG = "InterventionActivity";
    public final static String STATUS_PAUSE = "PAUSE";
    public final static String STATUS_FINISHED = "FINISHED";

    /* ****************************
    ** Notification builder and variables
    ** ***************************/
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotificationBuilder;
    private int mNotificationID;

    /* ****************************
    ** Interface elements variables
    ** ***************************/
    private ScrollView infoScroll;
    private RelativeLayout layoutActivePreparation, layoutActiveTravel, layoutActiveIntervention, layoutPreparation, layoutTravel, layoutIntervention;
    private View preparation, travel, intervention;
    private ExtendedChronometer chronoGeneral, chronoPreparation, chronoTravel, chronoIntervention, chronoActivePreparation, chronoActiveTravel, chronoActiveIntervention;
    private CheckBox diffStuff;
    private Button mStartButton, mMapButton;
    private TextView mProcedureNature;
    private CardView cardView;

    /* ****************************
    **      Class variables
    ** ***************************/
    private int    mInterventionID;
    protected int       state = -1;
    private String mLastProcedureNature, mLastProcedureNatureName;
    private String mLocationProvider;
    private TrackingListener mTrackingListener;
    private Account mAccount;
    private AlertDialog.Builder mProcedureChooser;
    private SharedPreferences mPreferences;
    private IntentIntegrator mScanIntegrator;
    private RelativeLayout infoLayout;
    private LocationManager mLocationManager;
    private CrumbsCalculator crumbsCalculator;
    private boolean mNewIntervention;
    private String started_at = null;
    private String stopped_at = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat(DateConstant.ISO_8601);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    /*
    ** Actions on toolbar items
    ** Items are identified by their view id
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int     id = item.getItemId();

        if (id == R.id.action_save)
        {
            phasePause(null);
            switchStateToFinished();
            InterventionActivity.super.onBackPressed();
        }
        else if (id == android.R.id.home)
        {
            exitInterface();
            return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAccount = AccountTool.getCurrentAccount(this);


        // Set content view
        setContentView(R.layout.intervention);
        super.setToolBar();
        mNewIntervention = getIntent().getBooleanExtra(InterventionActivity.NEW, false);

        layoutPreparation = (RelativeLayout) findViewById(R.id.layout_preparation);
        layoutTravel = (RelativeLayout) findViewById(R.id.layout_travel);
        layoutIntervention = (RelativeLayout) findViewById(R.id.layout_intervention);
        layoutActivePreparation = (RelativeLayout) findViewById(R.id.layout_active_preparation);
        layoutActiveTravel = (RelativeLayout) findViewById(R.id.layout_active_travel);
        layoutActiveIntervention = (RelativeLayout) findViewById(R.id.layout_active_intervention);
        preparation =  findViewById(R.id.preparation);
        travel =  findViewById(R.id.travel);
        intervention =  findViewById(R.id.intervention);
        chronoPreparation = (ExtendedChronometer) findViewById(R.id.chrono_preparation);
        chronoTravel = (ExtendedChronometer) findViewById(R.id.chrono_travel);
        chronoIntervention = (ExtendedChronometer) findViewById(R.id.chrono_intervention);
        chronoActivePreparation = (ExtendedChronometer) findViewById(R.id.chrono_active_preparation);
        chronoActiveTravel = (ExtendedChronometer) findViewById(R.id.chrono_active_travel);
        chronoActiveIntervention = (ExtendedChronometer) findViewById(R.id.chrono_active_intervention);
        chronoGeneral = (ExtendedChronometer) findViewById(R.id.chrono_general);
        mProcedureNature          = (TextView)   findViewById(R.id.procedure_nature);
        mStartButton              = (Button)     findViewById(R.id.start_intervention_button);
        mMapButton                = (Button)     findViewById(R.id.map_button);
        infoLayout                = (RelativeLayout) findViewById(R.id.infoLayout);
        infoScroll                = (ScrollView) findViewById(R.id.interventionInfo);
        cardView                  = (CardView) findViewById(R.id.card_view);
        diffStuff                 = (CheckBox) findViewById(R.id.diff_stuff);
        diffStuff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CheckBox box =  (CheckBox) view;
                Log.d(TAG, "CheckBox state => " + box.isChecked());
            }
        });

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
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, InterventionActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_stat_notify);

        if (!mNewIntervention)
            prepareRequest();
        else
        {
            createProcedureChooser();
            createIntervention();
            prepareSimpleIntervention();
        }
    }

    private void prepareSimpleIntervention()
    {
        cardView.setVisibility(View.INVISIBLE);
        diffStuff.setVisibility(View.GONE);
        chronoGeneral.setVisibility(View.GONE);
        layoutPreparation.setVisibility(View.GONE);
        layoutTravel.setVisibility(View.GONE);
        layoutIntervention.setVisibility(View.GONE);
        mStartButton.setVisibility(View.VISIBLE);
    }


    private void prepareRequest()
    {
        mStartButton.setVisibility(View.GONE);
        Intent intent = getIntent();
        mInterventionID = intent.getIntExtra(ZeroContract.Interventions._ID, 0);
        writeInterventionInfo();
        setPausedValues();
    }

    private void setPausedValues()
    {
        Cursor curs = getContentResolver().query(
                ZeroContract.Interventions.CONTENT_URI,
                ZeroContract.Interventions.PROJECTION_PAUSED,
                ZeroContract.Interventions._ID + " == " + mInterventionID,
                null,
                null);
        if (curs == null || curs.getCount() == 0)
            return;
        curs.moveToFirst();
        setTitle(curs.getString(6));
        if (curs.getString(0) != null && !curs.getString(0).equals("PAUSE"))
            return;
        diffStuff.setChecked(curs.getInt(1) == 0 ? false : true);
        chronoGeneral.setTime(curs.getInt(2));
        chronoPreparation.setTime(curs.getInt(3));
        chronoActivePreparation.setTime(chronoPreparation.getTime());
        chronoTravel.setTime(curs.getInt(4));
        chronoActiveTravel.setTime(chronoTravel.getTime());
        chronoIntervention.setTime(curs.getInt(5));
        chronoActiveIntervention.setTime(chronoIntervention.getTime());
        curs.close();
    }

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
            return (botId);
        botId = writeCursor(cursTarget, botId);
        cursTarget.close();
        return (botId);
    }

    private int writeInput(int botId)
    {
        Cursor cursInput = queryInput();

        if (cursInput == null || cursInput.getCount() == 0)
            return (botId);
        botId = writeCursor(cursInput, botId);
        cursInput.close();
        return (botId);
    }

    private int writeTool(int botId)
    {
        Cursor cursTool = queryTool();

        if (cursTool == null || cursTool.getCount() == 0)
            return (botId);
        botId = writeCursor(cursTool, botId);
        cursTool.close();
        return (botId);
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

    private void updateWorkingPeriods(int currentState)
    {
        if (started_at == null)
        {
            Calendar cal = Calendar.getInstance();
            started_at = dateFormatter.format(cal.getTime());
        }
        else if (currentState == PAUSE)
        {
            Calendar cal = Calendar.getInstance();
            stopped_at = dateFormatter.format(cal.getTime());
            addWorkingPeriodsToLocalDatabase();
            started_at = null;
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            stopped_at = dateFormatter.format(cal.getTime());
            addWorkingPeriodsToLocalDatabase();
            started_at = stopped_at;
        }
    }

    private void addWorkingPeriodsToLocalDatabase()
    {
        ContentValues values = new ContentValues();

        values.put(ZeroContract.WorkingPeriodsColumns.FK_INTERVENTION, mInterventionID);
        if (state == PREPARATION)
            values.put(ZeroContract.WorkingPeriodsColumns.NATURE, "preparation");
        else if (state == TRAVEL)
            values.put(ZeroContract.WorkingPeriodsColumns.NATURE, "travel");
        else
            values.put(ZeroContract.WorkingPeriodsColumns.NATURE, "intervention");

        values.put(ZeroContract.WorkingPeriodsColumns.STARTED_AT, started_at);
        values.put(ZeroContract.WorkingPeriodsColumns.STOPPED_AT, stopped_at);

        getContentResolver().insert(ZeroContract.WorkingPeriods.CONTENT_URI, values);
    }

    public void phasePreparation(View view)
    {
        updateWorkingPeriods(PREPARATION);

        chronoGeneral.startTimer();

        state = PREPARATION;
        Log.d(TAG, "Activating preparation !  State => " + state);
        layoutPreparation.setVisibility(View.GONE);
        layoutActivePreparation.setVisibility(View.VISIBLE);
        layoutActiveTravel.setVisibility(View.GONE);
        layoutActiveIntervention.setVisibility(View.GONE);
        layoutTravel.setVisibility(View.VISIBLE);
        layoutIntervention.setVisibility(View.VISIBLE);

        chronoActivePreparation.startTimer();
        chronoActiveTravel.stopTimer();
        chronoActiveIntervention.stopTimer();
        chronoTravel.setTime(chronoActiveTravel.getTime());
        chronoIntervention.setTime(chronoActiveIntervention.getTime());

    }


    public void phaseTravel(View view)
    {
        updateWorkingPeriods(TRAVEL);

        chronoGeneral.startTimer();

        state = TRAVEL;
        Log.d(TAG, "Activating travel !  State => " + state);
        layoutTravel.setVisibility(View.GONE);
        layoutActiveTravel.setVisibility(View.VISIBLE);
        layoutActivePreparation.setVisibility(View.GONE);
        layoutActiveIntervention.setVisibility(View.GONE);
        layoutPreparation.setVisibility(View.VISIBLE);
        layoutIntervention.setVisibility(View.VISIBLE);

        chronoActiveTravel.startTimer();
        chronoActivePreparation.stopTimer();
        chronoActiveIntervention.stopTimer();
        chronoPreparation.setTime(chronoActivePreparation.getTime());
        chronoIntervention.setTime(chronoActiveIntervention.getTime());


    }

    public void phaseIntervention(View view)
    {
        updateWorkingPeriods(INTERVENTION);

        chronoGeneral.startTimer();

        state = INTERVENTION;
        Log.d(TAG, "Activating intervention !  State => " + state);
        layoutIntervention.setVisibility(View.GONE);
        layoutActiveIntervention.setVisibility(View.VISIBLE);
        layoutActivePreparation.setVisibility(View.GONE);
        layoutActiveTravel.setVisibility(View.GONE);
        layoutPreparation.setVisibility(View.VISIBLE);
        layoutTravel.setVisibility(View.VISIBLE);

        chronoActiveIntervention.startTimer();
        chronoActiveTravel.stopTimer();
        chronoActivePreparation.stopTimer();
        chronoTravel.setTime(chronoActiveTravel.getTime());
        chronoPreparation.setTime(chronoActivePreparation.getTime());


    }

    public void phasePause(View view)
    {
        updateWorkingPeriods(PAUSE);

        state = PAUSE;
        Log.d(TAG, "PAUSE !!  State => " + state);
        layoutActivePreparation.setVisibility(View.GONE);
        layoutActiveTravel.setVisibility(View.GONE);
        layoutActiveIntervention.setVisibility(View.GONE);
        layoutPreparation.setVisibility(View.VISIBLE);
        layoutTravel.setVisibility(View.VISIBLE);
        layoutIntervention.setVisibility(View.VISIBLE);

        chronoGeneral.stopTimer();
        chronoActivePreparation.stopTimer();
        chronoActiveTravel.stopTimer();
        chronoActiveIntervention.stopTimer();
        chronoPreparation.setTime(chronoActivePreparation.getTime());
        chronoTravel.setTime(chronoActiveTravel.getTime());
        chronoIntervention.setTime(chronoActiveIntervention.getTime());


    }

    @Override
    public void onBackPressed()
    {
        exitInterface();
    }

    private void exitInterface()
    {
        phasePause(null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Que voulez vous faire ?");
        dialog.setPositiveButton("Terminer", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                switchStateToFinished();
                InterventionActivity.super.onBackPressed();
            }
        });
        dialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //cleanCurrentSave();
                InterventionActivity.super.onBackPressed();
            }
        });
        dialog.setNeutralButton("Pause", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                saveCurrentStateOfIntervention();
                InterventionActivity.super.onBackPressed();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void switchStateToFinished()
    {
        ContentValues values = new ContentValues();

        values.put(ZeroContract.Interventions.STATE, STATUS_FINISHED);
        values.put(ZeroContract.Interventions.REQUEST_COMPLIANT, diffStuff.isChecked());

        getContentResolver().update(ZeroContract.Interventions.CONTENT_URI,
                values,
                ZeroContract.Interventions._ID + " == " + mInterventionID,
                null);
    }

    private void saveCurrentStateOfIntervention()
    {
        ContentValues values = new ContentValues();

        values.put(ZeroContract.Interventions.GENERAL_CHRONO, chronoGeneral.getTime());
        values.put(ZeroContract.Interventions.PREPARATION_CHRONO, chronoPreparation.getTime());
        values.put(ZeroContract.Interventions.TRAVEL_CHRONO, chronoTravel.getTime());
        values.put(ZeroContract.Interventions.INTERVENTION_CHRONO, chronoIntervention.getTime());
        values.put(ZeroContract.Interventions.STATE, STATUS_PAUSE);
        values.put(ZeroContract.Interventions.REQUEST_COMPLIANT, diffStuff.isChecked());

        getContentResolver().update(ZeroContract.Interventions.CONTENT_URI,
                values,
                ZeroContract.Interventions._ID + " == " + mInterventionID,
                null);
    }

    @Override
    public void onDestroy()
    {
        stopIntervention(null);
        super.onDestroy();
    }

    private void createProcedureChooser()
    {
        infoScroll.setVisibility(View.GONE);
        mProcedureChooser = new AlertDialog.Builder(this)
                .setTitle(R.string.procedure_nature)
                .setNegativeButton(android.R.string.cancel, null)
                .setItems(R.array.procedures_entries, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mLastProcedureNature = getResources().getStringArray(R.array.procedures_values)[which];
                        mLastProcedureNatureName = getResources().getStringArray(R.array.procedures_entries)[which];
                        Log.d("zero", "Start a new " + mLastProcedureNature);
                        crumbsCalculator = new CrumbsCalculator(mLastProcedureNature);

                        mStartButton.setVisibility(View.GONE);
                        mMapButton.setVisibility(View.GONE);

                        chronoGeneral.setVisibility(View.VISIBLE);
                        layoutPreparation.setVisibility(View.VISIBLE);
                        layoutTravel.setVisibility(View.VISIBLE);
                        layoutIntervention.setVisibility(View.VISIBLE);
                        mProcedureNature.setText(mLastProcedureNatureName);

                        setTitle(mLastProcedureNatureName);
                        startTracking();
                        mNotificationBuilder
                                .setSmallIcon(R.mipmap.ic_stat_notify_running)
                                .setContentTitle(mLastProcedureNatureName)
                                .setContentText(getString(R.string.running));
                        mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
                    }
                });
    }


    private void createIntervention()
    {
        ContentValues values = new ContentValues();

        values.put(ZeroContract.InterventionsColumns.USER, AccountTool.getCurrentAccount(this).name);
        values.put(ZeroContract.InterventionsColumns.EK_ID, -1);
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
        mMapButton.setVisibility(View.GONE);
        mProcedureNature.setVisibility(View.INVISIBLE);
        mStartButton.setVisibility(View.VISIBLE);
        this.stopTracking();

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

        getContentResolver().insert(ZeroContract.Crumbs.CONTENT_URI, values);
    }
}
