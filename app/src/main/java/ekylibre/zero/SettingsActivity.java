package ekylibre.zero;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
    public final static String PREF_SHOW_DETAILS = "pref_showDetails";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
