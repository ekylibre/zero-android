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
    @Override
    public void onCreate()
    {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
