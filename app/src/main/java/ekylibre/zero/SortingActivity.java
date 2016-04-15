package ekylibre.zero;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SortingActivity extends Activity {


    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorting);

        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEditText = (EditText) findViewById(R.id.editText);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = new TextView(this);
        textView.setText("New text");
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
                saveSampling(item.getActionView());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void saveSampling(View v) {
/*        Context context = getApplicationContext();
        CharSequence text = "Issue saved";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //http://developer.android.com/guide/topics/providers/content-provider-basics.html

        // Defines an object to contain the new values to insert
        ContentValues mNewValues = new ContentValues();


        // Sets the values of each column and inserts the word. The arguments to the "put"
        // method are "column name" and "value"

        String[] mTestArray;
        mTestArray = getResources().getStringArray(R.array.issueNatures_values);


        mNewValues.put(ZeroContract.IssuesColumns.NATURE, mTestArray[mIssueNatureSpinner.getSelectedItemPosition()]);
        mNewValues.put(ZeroContract.IssuesColumns.EMERGENCY, mEmergency.getValue());
        mNewValues.put(ZeroContract.IssuesColumns.SEVERITY, mSeverity.getValue());
        mNewValues.put(ZeroContract.IssuesColumns.DESCRIPTION, mDescription.getText().toString());
        mNewValues.put(ZeroContract.IssuesColumns.PINNED, Boolean.FALSE);
//        mNewValues.put(ZeroContract.IssuesColumns.SYNCED_AT, rightNow.get(Calendar.DAY_OF_MONTH)+1 + "/" + rightNow.get(Calendar.MONTH)+1 + "/" + rightNow.get(Calendar.YEAR));
        mNewValues.put(ZeroContract.IssuesColumns.OBSERVED_AT, (new java.util.Date()).getTime());

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null){
            mNewValues.put(ZeroContract.IssuesColumns.LATITUDE, lastKnownLocation.getLatitude());
            mNewValues.put(ZeroContract.IssuesColumns.LONGITUDE, lastKnownLocation.getLongitude());
        }


        getContentResolver().insert(
                ZeroContract.Issues.CONTENT_URI,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );
*/
        //est censé fermer l'activity
        this.finish();

    }

    public void addValue(View view){

        final EditText input = new EditText(this);
        TextView t = createTextview(mEditText.getText().toString());
        mLayout.addView(t);

    }


    public TextView createTextview(String text){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }


}
