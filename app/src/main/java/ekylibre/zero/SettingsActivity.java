package ekylibre.zero;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import ekylibre.zero.util.AccountTool;

public class SettingsActivity extends PreferenceActivity
{
    final private String TAG = "SETTINGS";
    public final static String PREF_SHOW_DETAILS = "pref_showDetails";
    public final static String PREF_MOBILE_NETWORK = "pref_MobileNetwork";
    public final static String PREF_AUTO_SYNC = "pref_AutoSync";
    public final static String PREF_SYNC_CALENDAR = "pref_syncCalendar";

    @Override
    public void onStart()
    {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
        {
            AccountTool.askForAccount(this, this);
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
						.replace(android.R.id.content, new SettingsFragment())
						.commit();
    }


    /*@Override
    protected void onStop(){
        super.onStop();
        Intent intent = new Intent(this,TrackingActivity.class);
        super.startActivity(intent);
    }*/
}
