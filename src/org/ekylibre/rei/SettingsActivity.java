package org.ekylibre.rei;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

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
		
}
