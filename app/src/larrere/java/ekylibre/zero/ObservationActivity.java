package ekylibre.zero;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ekylibre.zero.fragments.ActivityChoiceFragment;
import ekylibre.zero.fragments.model.ActivityItem;

public class ObservationActivity extends AppCompatActivity
        implements ActivityChoiceFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Choix de l'activité");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set first fragment = activity choice list
        ActivityChoiceFragment choseActivity = ActivityChoiceFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_choice_fragment, choseActivity).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.obs_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.obs_cancel:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onListFragmentInteraction(ActivityItem item) {

    }
}
