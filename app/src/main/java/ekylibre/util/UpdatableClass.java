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
    protected boolean           isSync                      = false;
    private final String        TAG                         = "UpdatableClass";

    public void onSyncFinish()
    {
        isSync = false;
    }

    public void onSyncStart()
    {
        isSync = true;
    }
}
