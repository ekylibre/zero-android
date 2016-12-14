package ekylibre.zero;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import ekylibre.util.AccountTool;

public class SettingsActivity extends PreferenceActivity
{
    final private String TAG = "SETTINGS";
    public final static String PREF_SHOW_DETAILS = "pref_showDetails";
    public final static String PREF_MOBILE_NETWORK = "pref_MobileNetwork";
    public final static String PREF_AUTO_SYNC = "pref_AutoSync";
    public final static String PREF_SYNC_CALENDAR = "pref_syncCalendar";
    public final static String PREF_GPS = "pref_GPS";
    public final static String PREF_POKE = "pref_poke";
    public final static String PREF_SYNC_CONTACTS = "pref_syncContact";

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

}
