package ekylibre.zero.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**************************************
 * Created by pierre on 8/22/16.      *
 * ekylibre for zero-android          *
 *************************************/
public class PermissionManager
{
    private final static String    TAG = "Permission manag";
    private final static int REQUEST_CALENDAR = 42;

    public static boolean calendarPermissions(Context context, Activity activity)
    {
        if (Build.VERSION.SDK_INT >= 23 &&
                (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED))
        {
            Log.d(TAG, "REQUESTING PERMISSIONS");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, REQUEST_CALENDAR);
            if (Build.VERSION.SDK_INT >= 23 &&
                    (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED))
                return (false);
        }
        Log.d(TAG, "PERMISSIONS GRANTED");
        return (true);
    }
}
