package ekylibre.zero;

import android.app.Service;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by PierreBougon on 7/6/16.
 * Connection class for Ekylibre-zero
 */

public class ConnectionManagerService extends Service
{
    public ConnectivityManager connectivityManager;
    public NetworkInfo networkInfo;

    public ConnectionManagerService ()
    {
        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return (this);
    }

    public boolean  try_connection()
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
}
