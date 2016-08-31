package ekylibre.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import ekylibre.zero.SettingsActivity;

/**************************************
 * Created by pierre on 8/22/16.      *
 * ekylibre for zero-android          *
 *************************************/
public class PermissionManager
{
    private final static String		TAG = "Permission manag";
    private final static int		REQUEST_CALENDAR = 1;
    private final static int		REQUEST_GPS = 2;
    private final static int		REQUEST_STORAGE = 3;

    public static boolean calendarPermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                    REQUEST_CALENDAR);

            if (Build.VERSION.SDK_INT >= 23 &&
                    (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
    }

    public static boolean GPSPermissions(Context context, Activity activity)
    {
        if (!localGPSPerm(context))
            return (false);
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_GPS);

            if (Build.VERSION.SDK_INT >= 23 &&
		(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
    }

    private static boolean localGPSPerm(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean prefGPS = pref.getBoolean(SettingsActivity.PREF_GPS, false);

        return (prefGPS);
    }

    public static boolean storagePermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE);

            if (Build.VERSION.SDK_INT >= 23 &&
                    (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
    }

    public static boolean internetPermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},
                    REQUEST_STORAGE);

            if (Build.VERSION.SDK_INT >= 23 &&
                    (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
                            != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                            != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
}
}
