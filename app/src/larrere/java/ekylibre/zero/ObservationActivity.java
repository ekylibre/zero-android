package ekylibre.zero;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ekylibre.zero.fragments.ActivityChoiceFragment;
import ekylibre.zero.fragments.BBCHChoiceFragment;
import ekylibre.zero.fragments.CultureChoiceFragment;
import ekylibre.zero.fragments.IssueChoiceFragment;
import ekylibre.zero.fragments.ObservationFormFragment;
import ekylibre.zero.fragments.model.ActivityItem;
import ekylibre.zero.fragments.model.BBCHItem;
import ekylibre.zero.fragments.model.CultureItem;
import ekylibre.zero.fragments.model.IssueItem;

public class ObservationActivity extends AppCompatActivity implements
        ActivityChoiceFragment.OnActivityFragmentInteractionListener,
        ObservationFormFragment.OnFragmentInteractionListener,
        CultureChoiceFragment.OnListFragmentInteractionListener,
        BBCHChoiceFragment.OnBBCHFragmentInteractionListener {

    private static final String ACTIVITY_FRAGMENT = "ekylibre.zero.fragments.activity";
    public static final String FORM_FRAGMENT = "ekylibre.zero.fragments.form";
    public static final String CULTURES_FRAGMENT = "ekylibre.zero.fragments.cultures";
    public static final String ISSUES_FRAGMENT = "ekylibre.zero.fragments.issues";
    public static final String BBCH_FRAGMENT = "ekylibre.zero.fragments.bbch";

    public static FragmentManager fragmentManager;
    private ActionBar actionBar;
    private String currentFragment = ACTIVITY_FRAGMENT;

    // Shared observation variables
    public static Calendar date;
    public static String observation;
    public static ActivityItem selectedActivity;
    public static BBCHItem selectedBBCH;
    public static List<CultureItem> culturesList;
    public static List<IssueItem> issuesList;
    public static List<Uri> picturesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Set Observation
        date = Calendar.getInstance();
        selectedBBCH = null;
        observation = null;
        culturesList = new ArrayList<>();
        issuesList = new ArrayList<>();
        picturesList = new ArrayList<>();

        fragmentManager = getSupportFragmentManager();

        // Set first fragment (activity choice)
        replaceFragmentWith(ACTIVITY_FRAGMENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.obs_options_menu, menu);
        // Set cancel or validate button according to fragment
        if (currentFragment.equals(CULTURES_FRAGMENT) ||
                currentFragment.equals(ISSUES_FRAGMENT) ||
                currentFragment.equals(BBCH_FRAGMENT))
            menu.removeItem(R.id.obs_cancel);
        else
            menu.removeItem(R.id.obs_done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.obs_cancel:
                finish();
                return true;
            case R.id.obs_done:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        switch (currentFragment) {
            case FORM_FRAGMENT:
                currentFragment = ACTIVITY_FRAGMENT;
                break;

            case BBCH_FRAGMENT:
            case ISSUES_FRAGMENT:
            case CULTURES_FRAGMENT:
                currentFragment = FORM_FRAGMENT;
                break;
            case ACTIVITY_FRAGMENT:
                finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Same as onBackPressed() except super()
        switch (currentFragment) {
            case FORM_FRAGMENT:
                replaceFragmentWith(ACTIVITY_FRAGMENT);
                break;

            case BBCH_FRAGMENT:
            case ISSUES_FRAGMENT:
            case CULTURES_FRAGMENT:
                replaceFragmentWith(FORM_FRAGMENT);
                break;

            default:
                finish();
//                super.onSupportNavigateUp();
        }
        return true;
    }

    @Override
    public void onActivityInteraction(ActivityItem item) {
        selectedActivity = item;
        replaceFragmentWith(FORM_FRAGMENT);
    }

    @Override
    public void onBBCHInteraction(BBCHItem item) {
        selectedBBCH = item;
        replaceFragmentWith(FORM_FRAGMENT);

    }

    @Override
    public void onFormInteraction(String fragmentTag) {
        replaceFragmentWith(fragmentTag);
    }

    @Override
    public void onCultureFragmentInteraction(ActivityItem item) {

    }

    void replaceFragmentWith(String fragmentTag) {

        currentFragment = fragmentTag;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment;

        switch (fragmentTag) {

            case ACTIVITY_FRAGMENT:
                actionBar.setTitle("Choix de l'activité");
                fragment = ActivityChoiceFragment.newInstance();
                break;

            case CULTURES_FRAGMENT:
                actionBar.setTitle("Choix des cultures");
                invalidateOptionsMenu();
                fragment = CultureChoiceFragment.newInstance();
                break;

            case BBCH_FRAGMENT:
                actionBar.setTitle("Stade végétatif");
                invalidateOptionsMenu();
                fragment = BBCHChoiceFragment.newInstance();
                break;

            case ISSUES_FRAGMENT:
                actionBar.setTitle("Incidents");
                invalidateOptionsMenu();
                fragment = IssueChoiceFragment.newInstance();
                break;

            default:
                actionBar.setTitle(R.string.observation);
                fragment = ObservationFormFragment.newInstance();

        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right);
        ft.replace(R.id.fragment_container, fragment, fragmentTag);
        ft.addToBackStack(null);
        ft.commit();
    }
}
