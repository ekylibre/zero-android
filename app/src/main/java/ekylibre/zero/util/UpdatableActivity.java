package ekylibre.zero.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**************************************
 * Created by pierre on 8/4/16.       *
 * ekylibre.zero.util for zero-android*
 *************************************/

public abstract class UpdatableActivity extends AppCompatActivity
{
    public static final String  ACTION_FINISHED_SYNC        = "ekylibre.zero.util.ACTION_FINISHED_SYNC";
    public static final String  ACTION_STARTED_SYNC         = "ekylibre.zero.util.ACTION_STARTED_SYNC";
    private static IntentFilter syncIntentFilterFINISHED    = new IntentFilter(ACTION_FINISHED_SYNC);
    private static IntentFilter syncIntentFilterSTART       = new IntentFilter(ACTION_STARTED_SYNC);
    protected boolean isSync = false;
    private final String    TAG = "UpdatableActivity";

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
            isSync = true;
        }
    };

    protected abstract void onSyncFinish();

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(syncBroadcastReceiverFinish, syncIntentFilterFINISHED);
        registerReceiver(syncBroadcastReceiverStart, syncIntentFilterSTART);
    }

    @Override protected void onPause()
    {
        unregisterReceiver(syncBroadcastReceiverFinish);
        unregisterReceiver(syncBroadcastReceiverStart);
        super.onPause();
    }
}
