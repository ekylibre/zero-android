package ekylibre.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import ekylibre.util.AccountTool;
import ekylibre.zero.SettingsActivity;
import ekylibre.database.ZeroContract;

/*************************************
 * Created by PierreBougon on 7/6/16.*
 * Connection class for Ekylibre-zero*
 ************************************/

public class ConnectionManagerService extends Service
{
    private static final String     TAG = "ConnectionManagerS";
    public static final int         TIME_TO_NEXT_SYNC_AUTO = 300000;
    public static final int         TIME_TO_NEXT_SYNC = 600000;

    private Handler                 handler;
    private Account                 mAccount = null;
    public boolean                  mobile_permission = false;
    public boolean                  auto_permission = false;


    /*
    **  Get the account which is currently used
    **  Set handler to try sync every hDelay ms
    **  Each time the handler is up we try to connect : Wifi | 3G | 4G
    **  If a connection is up then we sync all the data
    */
    @Override
    public void         onCreate()
    {
        // Final value is 300000 others values are just here to test sync
        final int       hDelay = TIME_TO_NEXT_SYNC_AUTO;

        handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (try_connection())
                    syncAll();
                handler.postDelayed(this, hDelay);
            }
        }, hDelay);
    }

    /*
    ** Get the current account to sync the data
    */
    @Override
    public int  onStartCommand(Intent intent, int flags, int startId)
    {
        String  accountName;

        accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        Log.d(TAG, "accountName = " + accountName);
        mAccount = AccountTool.getCurrentAccount(ConnectionManagerService.this);
        return (START_NOT_STICKY);
    }

    /*
    ** Set the private boolean mobile_permission
    */
    public void     set_MobilePerm(boolean pref)
    {
        this.mobile_permission = pref;
    }
    public void     set_AutoPerm(boolean pref)
    {
        this.auto_permission = pref;
    }

    /*
    ** Get mobilePerm from SharedPreference set on settings activity
    */
    public boolean          get_MobilePerm()
    {
        SharedPreferences   pref;
        boolean             userPrefMobile;

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userPrefMobile = pref.getBoolean(SettingsActivity.PREF_MOBILE_NETWORK, false);
        set_MobilePerm(userPrefMobile);
        Log.d(TAG, "Mobile network is allowed : " + mobile_permission);
        return (mobile_permission);
    }

    public boolean          get_AutoPerm()
    {
        SharedPreferences   pref;
        boolean             userPrefAuto;

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userPrefAuto = pref.getBoolean(SettingsActivity.PREF_AUTO_SYNC, false);
        set_AutoPerm(userPrefAuto);
        Log.d(TAG, "Auto sync is allowed : " + auto_permission);
        return (auto_permission);
    }

    /*
    ** Method to verify internet connection
    */
    public boolean              try_connection()
    {
        ConnectivityManager     connectivityManager;
        NetworkInfo             networkInfo;
        boolean                 wifi;
        boolean                 mobile;

        if (!get_AutoPerm() || !AccountTool.isAnyAccountExist(this))
            return (false);
        mAccount = AccountTool.getCurrentAccount(ConnectionManagerService.this);
        get_MobilePerm();
        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
        {
            wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobile = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            Log.d(TAG, "Wifi is active : " + wifi);
            Log.d(TAG, "Mobile is active : " + mobile);
            if (wifi || (mobile && mobile_permission))
                return (true);
            else
                return (false);
        }
        return (false);
    }

    /*
    **  Sync all the data called automatically on handler
    */
    private void syncAll()
    {
        if (mAccount == null)
            return ;
        Log.d(TAG, "syncAll: " + mAccount.toString() + ", " + ZeroContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccount, ZeroContract.AUTHORITY, extras);
/*        Toast toast = Toast.makeText(getApplicationContext(), R.string.data_synced, Toast.LENGTH_SHORT);
        toast.show();*/
    }

    public IBinder onBind(Intent intent)
    {
        return (null);
    }
}