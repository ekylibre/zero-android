package ekylibre.zero;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import ekylibre.zero.provider.IssueContract;

public class IssueActivity extends Activity {

    Spinner mIssueNatureSpinner;
    NumberPicker mSeverity;
    NumberPicker mEmergency;
    EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        //récuperation of XML elements
        mIssueNatureSpinner = (Spinner) findViewById(R.id.spinner);
        mSeverity = (NumberPicker) findViewById(R.id.numberPickerSeverity);
        mEmergency = (NumberPicker) findViewById(R.id.numberPickerEmergency);
        mDescription = (EditText) findViewById(R.id.description);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.issueNatures_entries, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mIssueNatureSpinner.setAdapter(adapter);


        //setting the minimum, maximum and initial value of the number pickers
        mSeverity.setMaxValue(4);
        mSeverity.setMinValue(0);
        mSeverity.setValue(2);

        mEmergency.setMaxValue(4);
        mEmergency.setMinValue(0);
        mEmergency.setValue(2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            // case R.id.action_search:
            //     // openSearch();
            //     return true;
            case R.id.action_save:
                issueSave(item.getActionView());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }



    public void issueSave(View v){
        Context context = getApplicationContext();
        CharSequence text = "Issue saved";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //http://developer.android.com/guide/topics/providers/content-provider-basics.html

        // Defines an object to contain the new values to insert
        ContentValues mNewValues = new ContentValues();

        /*
        * Sets the values of each column and inserts the word. The arguments to the "put"
        * method are "column name" and "value"
        */
        String[] mTestArray;
        mTestArray = getResources().getStringArray(R.array.issueNatures_values);


        mNewValues.put(IssueContract.IssueColumns.NATURE, mTestArray[mIssueNatureSpinner.getSelectedItemPosition()]);
        mNewValues.put(IssueContract.IssueColumns.EMERGENCY, mEmergency.getValue());
        mNewValues.put(IssueContract.IssueColumns.SEVERITY,  mSeverity.getValue());
        mNewValues.put(IssueContract.IssueColumns.DESCRIPTION, mDescription.getText().toString());
        mNewValues.put(IssueContract.IssueColumns.PINNED, Boolean.FALSE);
//        mNewValues.put(IssueContract.IssueColumns.SYNCED_AT, rightNow.get(Calendar.DAY_OF_MONTH)+1 + "/" + rightNow.get(Calendar.MONTH)+1 + "/" + rightNow.get(Calendar.YEAR));
        mNewValues.put(IssueContract.IssueColumns.OBSERVED_AT, (new java.util.Date()).getTime() );

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null){
            mNewValues.put(IssueContract.IssueColumns.LATITUDE, lastKnownLocation.getLatitude());
            mNewValues.put(IssueContract.IssueColumns.LONGITUDE, lastKnownLocation.getLongitude());
        }


        getContentResolver().insert(
                IssueContract.Issues.CONTENT_URI,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );

        //est censé fermer l'activity
        this.finish();

    }
}
