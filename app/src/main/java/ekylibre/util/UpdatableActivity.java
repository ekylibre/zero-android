package ekylibre.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import ekylibre.zero.R;

/**************************************
 * Created by pierre on 8/4/16.       *
 * ekylibre.zero.util for zero-android*
 *************************************/

/*
** Extend classic activity that listen sync events and ping event
** You can override methods needed to do what you want on event
** The generic application toolbar can be easily added by calling setTollBar
*/
public abstract class UpdatableActivity extends AppCompatActivity
{
    public static final String  ACTION_FINISHED_SYNC        = "ekylibre.zero.util.ACTION_FINISHED_SYNC";
    public static final String  ACTION_STARTED_SYNC         = "ekylibre.zero.util.ACTION_STARTED_SYNC";
    public static final String  PING                        = "ekylibre.zero.util.PONG";
    private static IntentFilter syncIntentFilterFINISHED    = new IntentFilter(ACTION_FINISHED_SYNC);
    private static IntentFilter syncIntentFilterSTART       = new IntentFilter(ACTION_STARTED_SYNC);
    private static IntentFilter pingIntentFilter            = new IntentFilter(PING);
    protected boolean           isSync                      = false;
    private final String        TAG                         = "UpdatableActivity";

    protected Toolbar mToolbar;
    private BroadcastReceiver   syncBroadcastReceiverFinish  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d(TAG, "I'm receiving finish message from syncAdapter");
            isSync = false;
            onSyncFinish();
        }
    };

    private BroadcastReceiver   syncBroadcastReceiverStart  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d(TAG, "I'm receiving start message from syncAdapter");
            onSyncStart();
            isSync = true;
        }
    };

    private BroadcastReceiver   pingBroadcastReceiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d(TAG, "I'm receiving ping message from" + context.toString());
            onPing(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /*
    ** Use this method to apply the application toolbar
    ** You MUST have called onCreate() AND setContentView()
    ** to use this method
    */
    protected void setToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (toolbar == null || getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar = toolbar;
    }

    /*
    ** Called when sync is finished
    */
    protected void onSyncFinish(){}

    /*
    ** Called when sync starts
    */
    protected void onSyncStart(){}

    /*
    ** Called when ping is sent
    */
    protected void onPing(Intent intent){}

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(syncBroadcastReceiverFinish, syncIntentFilterFINISHED);
        registerReceiver(syncBroadcastReceiverStart, syncIntentFilterSTART);
        registerReceiver(pingBroadcastReceiver, pingIntentFilter);
    }

    @Override
    protected void onPause()
    {
        unregisterReceiver(syncBroadcastReceiverFinish);
        unregisterReceiver(syncBroadcastReceiverStart);
        unregisterReceiver(pingBroadcastReceiver);
        super.onPause();
    }
}
