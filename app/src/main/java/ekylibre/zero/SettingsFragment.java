package ekylibre.zero;

import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

    }

}
