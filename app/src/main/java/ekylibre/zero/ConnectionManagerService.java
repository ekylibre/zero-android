package ekylibre.zero;

import android.accounts.Account;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by PierreBougon on 7/6/16.
 * Connection class for Ekylibre-zero
 */

public class ConnectionManagerService extends Service
{
    private ConnectivityManager     connectivityManager;
    private NetworkInfo             networkInfo;
    private Handler                 handler;
    private Account                 mAccount;

    @Override
    public void onCreate ()
    {
        final int       hDelay;
        MenuActivity    menu = new MenuActivity();

        // Need to change this to get the actual account
        mAccount = menu.getAccount();
        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        handler = new Handler();
        hDelay = 10000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (try_connection())
                    syncAll();
                handler.postDelayed(this, hDelay);
            }
        }, hDelay);
    }

    private boolean  try_connection()
    {
        boolean     wifi;

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
        {
            wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            Log.d("NetworkState", "L'interface de connexion active est du Wifi : " + wifi);
            if (wifi)
                return (true);
            else
                return (false);
        }
        return (false);
    }

    private void syncAll()
    {
        Log.d("zero", "syncData: " + mAccount.toString() + ", " + ZeroContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccount, ZeroContract.AUTHORITY, extras);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.data_synced, Toast.LENGTH_SHORT);
        toast.show();
    }

    public IBinder onBind(Intent intent)
    {
        return (null);
    }
}
