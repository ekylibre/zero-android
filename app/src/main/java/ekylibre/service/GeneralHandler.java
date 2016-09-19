package ekylibre.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import ekylibre.util.PermissionManager;
import ekylibre.zero.SettingsActivity;

/**************************************
 * Created by pierre on 9/19/16.      *
 * ekylibre.service for zero-android  *
 *************************************/
public class GeneralHandler extends Service
{
    private static final String     TAG = "GeneralHandler";
    public static final int         TIME_TO_NEXT_SYNC = 600000;
    private Handler handler;


    @Override
    public void         onCreate()
    {
        final int       hDelay = TIME_TO_NEXT_SYNC;

        handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (getPokePerm())
                {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                }
                handler.postDelayed(this, hDelay);
            }
        }, hDelay);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return (null);
    }

    public boolean getPokePerm()
    {
        SharedPreferences pref;

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        return (pref.getBoolean(SettingsActivity.PREF_POKE, false));
    }
}
