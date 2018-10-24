package ekylibre.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import ekylibre.zero.SettingsActivity;

/**************************************
 * Created by pierre on 8/22/16.      *
 * ekylibre for zero-android          *
 *************************************/

/*
** Use this class to request and check permissions for Android SDK >= 23
** Each methods will check permissions,
** if you don't have them the request will be activated
** and finally the check is done again to give the final answer
*/
public class PermissionManager
{
    private final static String		TAG = "Permission manag";
    private final static int		REQUEST_CALENDAR    = 1;
    private final static int		REQUEST_GPS         = 2;
    private final static int		REQUEST_STORAGE     = 3;
    private final static int		REQUEST_INTERNET    = 4;
    private final static int		REQUEST_VIBRATION   = 5;
    private final static int		REQUEST_CONTACT     = 6;
    private final static int		REQUEST_MULTIPLE    = 7;

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
                    REQUEST_INTERNET);

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

    public static boolean vibrationPermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE)
                        != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.VIBRATE},
                    REQUEST_VIBRATION);

            if (Build.VERSION.SDK_INT >= 23 &&
                    (ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE)
                            != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
    }

    public static boolean writeContactPermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_CONTACTS},
                    REQUEST_CONTACT);

            if (Build.VERSION.SDK_INT >= 23 &&
                    (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                            != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
    }

    public static void multiplePermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.VIBRATE,
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR},
                    REQUEST_MULTIPLE);
        }
    }
}
