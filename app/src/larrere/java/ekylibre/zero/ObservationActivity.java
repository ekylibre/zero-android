package ekylibre.zero;

import android.Manifest;
import android.accounts.Account;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ekylibre.database.ZeroContract;
import ekylibre.service.SimpleLocationService;
import ekylibre.util.AccountTool;
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

    private static final String TAG = "ObservationActivity";

    private static final String ACTIVITY_FRAGMENT = "ekylibre.zero.fragments.activity";
    public static final String FORM_FRAGMENT = "ekylibre.zero.fragments.form";
    public static final String CULTURES_FRAGMENT = "ekylibre.zero.fragments.cultures";
    public static final String ISSUES_FRAGMENT = "ekylibre.zero.fragments.issues";
    public static final String BBCH_FRAGMENT = "ekylibre.zero.fragments.bbch";

    private Account account;
    public static FragmentManager fragmentManager;
    private ActionBar actionBar;
    private String currentFragment = ACTIVITY_FRAGMENT;
    private Intent serviceIntent;

    // Shared description variables
    public static Calendar date;
    public static String description;
    public static ActivityItem selectedActivity;
    public static BBCHItem selectedBBCH;
    public static List<CultureItem> culturesList;
    public static List<IssueItem> issuesList;
    public static List<ActivityItem> activitiesList;
    public static List<Uri> picturesList;
    public static Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        account = AccountTool.getCurrentAccount(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Set Observation
        date = Calendar.getInstance();
        selectedBBCH = null;
        description = null;
        if (activitiesList == null)
            getActivities();
        if (culturesList == null)
            culturesList = new ArrayList<>();

        issuesList = new ArrayList<>();
        picturesList = new ArrayList<>();

        fragmentManager = getSupportFragmentManager();
        serviceIntent = new Intent(this, SimpleLocationService.class);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (!isServiceRunning())
                startService(serviceIntent);
            else
                Log.i(TAG, "Service is already running...");
        }

        // Set first fragment (activity choice)
        replaceFragmentWith(ACTIVITY_FRAGMENT);
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (manager != null)
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
                if (SimpleLocationService.class.getName().equals(service.service.getClassName()))
                    return true;
        return false;
    }

    private int getCulturesCount() {
        int culturesCount = 0;
        for (CultureItem culture : culturesList)
            if (culture.is_selected) culturesCount++;
        return culturesCount;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.obs_options_menu, menu);
        // Set cancel or validate button according to fragment
        switch (currentFragment) {
            case CULTURES_FRAGMENT:
            case ISSUES_FRAGMENT:
//                menu.removeItem(R.id.obs_cancel);
                menu.removeItem(R.id.obs_save);
                break;
            case ACTIVITY_FRAGMENT:
            case BBCH_FRAGMENT:
                menu.clear();
                break;
            default:
                menu.removeItem(R.id.obs_done);
//                menu.removeItem(R.id.obs_cancel);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.obs_cancel:
//                finish();
//                return true;

            case R.id.obs_done:
                onBackPressed();
                return true;

            case R.id.obs_save:
                if (getCulturesCount() > 0) {
                    saveObservation();
                    finish();
                } else {
                    Toast.makeText(this, "Vous devez choisir au moins une culture", Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        invalidateOptionsMenu();
        switch (currentFragment) {
            case FORM_FRAGMENT:
                replaceFragmentWith(ACTIVITY_FRAGMENT);
                break;

            case BBCH_FRAGMENT:
            case ISSUES_FRAGMENT:
            case CULTURES_FRAGMENT:
                replaceFragmentWith(FORM_FRAGMENT);
                break;

            case ACTIVITY_FRAGMENT:
                finish();
        }
//        super.onBackPressed();
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
    protected void onStop() {
        super.onStop();
//        cleanData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
        cleanData();
    }

    @Override
    public void onActivityInteraction(ActivityItem item) {
        if (selectedActivity != null)
            if (!selectedActivity.variety.equals(item.variety) || culturesList.isEmpty())
                selectedBBCH = null;
        selectedActivity = item;
        replaceFragmentWith(FORM_FRAGMENT);
        filterCulturesWithActivity(item);
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
                invalidateOptionsMenu();
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
                invalidateOptionsMenu();
                fragment = ObservationFormFragment.newInstance();

        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right);
        ft.replace(R.id.fragment_container, fragment, fragmentTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    void cleanData() {
//        picturesList.clear();
        culturesList.clear();
    }

    public void saveObservation() {
        ContentResolver cr = getContentResolver();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ZeroContract.Observations.USER, AccountTool.getCurrentAccount(this).name);
        contentValues.put(ZeroContract.Observations.OBSERVED_ON, date.getTimeInMillis());
        contentValues.put(ZeroContract.Observations.ACTIVITY_ID, selectedActivity.id);
        if (description != null && !description.isEmpty())
            contentValues.put(ZeroContract.Observations.DESCRIPTION, description);
        if (selectedBBCH != null)
            contentValues.put(ZeroContract.Observations.SCALE_ID, selectedBBCH.id);
        if (location != null) {
            contentValues.put(ZeroContract.Observations.LATITUDE, location.getLatitude());
            contentValues.put(ZeroContract.Observations.LONGITUDE, location.getLongitude());
        }
        if (!picturesList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Uri pictureUri : picturesList) {
                sb.append(pictureUri);
                if (picturesList.indexOf(pictureUri) + 1 < picturesList.size())
                    sb.append(",");
            }
            contentValues.put(ZeroContract.Observations.PICTURES, sb.toString());
        }

        if (!culturesList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (CultureItem culture : culturesList) {
                if (culture.is_selected) {
                    if (sb.length() > 0)
                        sb.append(",");
                    sb.append(culture.id);
                }
            }
            if (sb.length() > 0)
                contentValues.put(ZeroContract.Observations.PLANTS, sb.toString());
        }

//        for (IssueItem issue : issuesList) {
//            if (issue.is_selected) {
//                contentValues.put(ZeroContract.Observations.ISSUES, issue.id);
//                saveIssue(issue);
//            }
//        }

        // Insert and get back the row ID
        Uri uri = cr.insert(ZeroContract.Observations.CONTENT_URI, contentValues);

        long rowId = -1;
        if (uri != null && uri.getLastPathSegment() != null)
            rowId = Long.valueOf(uri.getLastPathSegment());

        if (rowId != -1) {

            // Save related Cultures
//            for (CultureItem culture : culturesList) {
//                if (culture.is_selected) {
//                    cv.put(ZeroContract.ObservationPlants.FK_OBSERVATION, rowId);
//                    cv.put(ZeroContract.ObservationPlants.FK_PLANT, culture.id);
//                    cr.insert(ZeroContract.ObservationPlants.CONTENT_URI, cv);
//                }
//            }
//            cv.clear();

            ContentValues cv = new ContentValues();

            // Save related Issues
            for (IssueItem issue : issuesList) {
                if (issue.is_selected) {
                    long createdId = saveIssue(issue);
                    cv.put(ZeroContract.ObservationIssues.FK_OBSERVATION, rowId);
                    cv.put(ZeroContract.ObservationIssues.FK_ISSUE, createdId);
                    cr.insert(ZeroContract.ObservationIssues.CONTENT_URI, cv);
                }
            }
        }
        Toast.makeText(this, "Observation enregistrée", Toast.LENGTH_LONG).show();
    }

    private long saveIssue(IssueItem issue) {
//        String[] issuesNatures = getResources().getStringArray(R.array.issueNatures_values);
        ContentValues cv = new ContentValues();
        cv.put(ZeroContract.Issues.USER, AccountTool.getCurrentAccount(this).name);
        cv.put(ZeroContract.Issues.SYNCED, 0);
        cv.put(ZeroContract.Issues.NATURE, issue.label);
        cv.put(ZeroContract.Issues.EMERGENCY, 2);
        cv.put(ZeroContract.Issues.SEVERITY, 2);
        cv.put(ZeroContract.Issues.DESCRIPTION, "Incident déclaré lors d'une observation");
        cv.put(ZeroContract.Issues.PINNED, Boolean.FALSE);
        cv.put(ZeroContract.Issues.OBSERVED_AT, date.getTimeInMillis());
        if (location != null) {
            cv.put(ZeroContract.Issues.LATITUDE, location.getLatitude());
            cv.put(ZeroContract.Issues.LONGITUDE, location.getLongitude());
        }
        Uri uri = getContentResolver().insert(ZeroContract.Issues.CONTENT_URI, cv);

        long issueId = -1;
        if (uri != null && uri.getLastPathSegment() != null)
            issueId = Long.valueOf(uri.getLastPathSegment());
        return issueId;
    }

    private void getActivities() {

        activitiesList = new ArrayList<>();
        List<ActivityItem> tempList = new ArrayList<>();
        List<Integer> iDs = new ArrayList<>();

        // Do query
        Cursor cursor = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI, ZeroContract.Plants.PROJECTION_OBS,
                ZeroContract.Plants.USER + " LIKE " + "\"" + account.name + "\""
                        + " AND " + ZeroContract.Plants.ACTIVE + " == " + 1,
                null, ZeroContract.Issues.SORT_ORDER_DEFAULT);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    tempList.add(new ActivityItem(cursor.getInt(4), cursor.getString(5), cursor.getString(3), null));
                    Log.i(TAG, String.format("Activité #%s - %s - %s", cursor.getInt(4), cursor.getString(5), cursor.getString(3)));
                }
            } finally {
                cursor.close();
            }

            for (ActivityItem tempActivity : tempList) {
                if (!iDs.contains(tempActivity.id)) {
                    activitiesList.add(tempActivity);
                    iDs.add(tempActivity.id);
                }
            }
            if (activitiesList.size() > 0) {
                Collections.sort(activitiesList, (ele1, ele2) -> {
                    Collator localeCollator = Collator.getInstance(Locale.FRANCE);
                    return localeCollator.compare(ele1.name, ele2.name);
                });
            }
        }
    }

    private void getIssues() {

        Log.e(TAG, "Get IssuesList");

        issuesList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();

        try (Cursor cursor = contentResolver.query(ZeroContract.IssueNatures.CONTENT_URI,
                ZeroContract.IssueNatures.PROJECTION_ALL,
                null, null, ZeroContract.IssueNatures.SORT_ORDER_DEFAULT)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    issuesList.add(new IssueItem(cursor.getString(1), cursor.getString(2),
                            cursor.getString(3)));
                }
            }

        }
        Log.e(TAG, issuesList.toString());
    }

    private void filterCulturesWithActivity(ActivityItem activity) {

        culturesList = new ArrayList<>();

        // Do query
        Cursor cursor = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI, ZeroContract.Plants.PROJECTION_OBS,
                ZeroContract.Plants.USER + " LIKE " + "\"" + account.name + "\""
                        + " AND " + ZeroContract.Plants.ACTIVE + " == " + 1
                        + " AND " + ZeroContract.Plants.ACTIVITY_ID + " == " + activity.id,
                null, ZeroContract.Issues.SORT_ORDER_DEFAULT);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
//                    Pattern pattern = Pattern.compile(".*(19|20\\d{2}).*");
//                    Matcher m = pattern.matcher(cursor.getString(2));
//                    m.matches() ? m.group(1) : "---";
                    culturesList.add(new CultureItem(cursor.getInt(1), cursor.getString(2)
                            .replace(selectedActivity.name + " ", ""), null));
                }
            } finally {
                cursor.close();
            }

            // sorting the List
            Collections.sort(culturesList, (ele1, ele2) -> {
                Collator localeCollator = Collator.getInstance(Locale.FRANCE);
                return localeCollator.compare(ele1.name, ele2.name);
            });
        }
    }

    public static int getActivityLogo(String activityName) {
        int resId;

        if (activityName.contains("Asperge"))
            resId = R.drawable.icon_asparagus;
        else if (activityName.contains("Blé") || activityName.contains("Orge"))
            resId = R.drawable.icon_wheat;
        else if (activityName.contains("Brocoli") || activityName.contains("Kale"))
            resId = R.drawable.icon_broccoli;
        else if (activityName.contains("Carotte"))
            resId = R.drawable.icon_carrot;
        else if (activityName.contains("Choudou"))
            resId = R.drawable.icon_cabbage;
        else if (activityName.contains("Colza"))
            resId = R.drawable.icon_canola;
        else if (activityName.contains("Épinard"))
            resId = R.drawable.icon_spinach;
        else if (activityName.contains("Haricot"))
            resId = R.drawable.icon_peas;
        else if (activityName.contains("Maïs"))
            resId = R.drawable.icon_corn;
        else if (activityName.contains("Poireau"))
            resId = R.drawable.icon_leek;
        else if (activityName.contains("Pomme"))
            resId = R.drawable.icon_potato;
        else if (activityName.contains("Patate"))
            resId = R.drawable.icon_sweet_potato;
        else if (activityName.equals("Radis"))
            resId = R.drawable.icon_radish;
        else if (activityName.contains("Radis Noir"))
            resId = R.drawable.icon_black_radish;
        else if (activityName.contains("Navet"))
            resId = R.drawable.icon_radish;
        else if (activityName.contains("Soja") || activityName.contains("Pois"))
            resId = R.drawable.icon_soybean;
        else if (activityName.contains("Stévia"))
            resId = R.drawable.icon_stevia;
        else if (activityName.contains("Tournesol"))
            resId = R.drawable.icon_sunflower;
        else if (activityName.contains("ETA"))
            resId = R.drawable.icon_harvester;
        else if (activityName.contains("Courge") || activityName.contains("Potiron"))
            resId = R.drawable.icon_pumpkin;
        else if (activityName.contains("trèfle"))
            resId = R.drawable.icon_shamrock;
        else if (activityName.contains("Forestier"))
            resId = R.drawable.icon_forest;
        else if (activityName.contains("Prairie") || activityName.contains("Jachère"))
            resId = R.drawable.icon_grass;
        else if (activityName.contains("Topinambour"))
            resId = R.drawable.icon_ginger;
        else
            resId = R.drawable.icon_plant;

        return resId;
    }
}
