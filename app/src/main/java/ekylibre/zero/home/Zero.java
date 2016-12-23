package ekylibre.zero.home;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

/**************************************
 * Created by pierre on 9/19/16.      *
 * ekylibre.zero.home for zero-android    *
 *************************************/
public class Zero extends Application
{
    /*
    ** https://developer.android.com/reference/android/support/v7/app/AppCompatDelegate.html#setCompatVectorFromResourcesEnabled%28boolean%29
    */
    @Override
    public void onCreate()
    {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
