package ekylibre.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**************************************
 * Created by pierre on 9/7/16.      *
 * ekylibre.util for zero-android    *
 *************************************/
public abstract class UpdatableClass
{
    public static final String  ACTION_FINISHED_SYNC        = "ekylibre.zero.util.ACTION_FINISHED_SYNC";
    public static final String  ACTION_STARTED_SYNC         = "ekylibre.zero.util.ACTION_STARTED_SYNC";
    public static final String  PING                        = "ekylibre.zero.util.PONG";
    private static IntentFilter syncIntentFilterFINISHED    = new IntentFilter(ACTION_FINISHED_SYNC);
    private static IntentFilter syncIntentFilterSTART       = new IntentFilter(ACTION_STARTED_SYNC);
    private static IntentFilter pingIntentFilter            = new IntentFilter(PING);
    protected boolean           isSync                      = false;
    private final String        TAG                         = "UpdatableClass";

    private BroadcastReceiver syncBroadcastReceiverFinish  = new BroadcastReceiver()
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

    protected void onSyncFinish(){}

    protected void onSyncStart(){}

    protected void onPing(Intent intent){}

    protected void startListening(Context context)
    {
        context.registerReceiver(syncBroadcastReceiverFinish, syncIntentFilterFINISHED);
        context.registerReceiver(syncBroadcastReceiverStart, syncIntentFilterSTART);
        context.registerReceiver(pingBroadcastReceiver, pingIntentFilter);
    }

    /*
    ** This method must be call if you want to use this abstract class
    ** Call this when the activity related is at the onPause method
    ** It will unregister Receiver
    */
    public void onDestroy(Context context)
    {
        context.unregisterReceiver(syncBroadcastReceiverFinish);
        context.unregisterReceiver(syncBroadcastReceiverStart);
        context.unregisterReceiver(pingBroadcastReceiver);
    }
}
