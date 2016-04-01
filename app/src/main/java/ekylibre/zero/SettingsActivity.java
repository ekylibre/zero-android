package ekylibre.zero;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity {

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
