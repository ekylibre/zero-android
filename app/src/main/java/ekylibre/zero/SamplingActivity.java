package ekylibre.zero;


import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class SamplingActivity extends Activity {


    private LinearLayout mLayout;
    private EditText mObservationEditText;
    private TextView mAverageText;
    private List<EditText> mListValues = new ArrayList();
    private ListIterator<EditText> mListIteratorValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorting);

        mLayout = (LinearLayout) findViewById(R.id.AllValuesLayout);
        mObservationEditText = (EditText)findViewById(R.id.observationEditText);
        mAverageText = (TextView) findViewById(R.id.textAverage);
        mAverageText.setText("");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addValue(mLayout);
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
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                saveSampling(item.getActionView());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void saveSampling(View v) {

        //http://developer.android.com/guide/topics/providers/content-provider-basics.html

        // Defines an object to contain the new values to insert
        ContentValues mNewValuesSampling = new ContentValues();
        ContentValues mNewValuesSamplingCount = new ContentValues();
        // Sets the values of each column and inserts the word. The arguments to the "put"
        // method are "column name" and "value"
        mNewValuesSampling.put(ZeroContract.SamplingColumns.OBSERVED_AT, (new java.util.Date()).getTime());
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            mNewValuesSampling.put(ZeroContract.SamplingColumns.LATITUDE, lastKnownLocation.getLatitude());
            mNewValuesSampling.put(ZeroContract.SamplingColumns.LONGITUDE, lastKnownLocation.getLongitude());
        }
        mNewValuesSampling.put(ZeroContract.SamplingColumns.OBSERVATION, mObservationEditText.getText().toString());

        mListIteratorValues = mListValues.listIterator();



        getContentResolver().insert(
                ZeroContract.Samplings.CONTENT_URI,   // the user dictionary content URI
                mNewValuesSampling                          // the values to insert
        );



        while(mListIteratorValues.hasNext()){
            EditText et = mListIteratorValues.next();
            if(et.getText().toString() != null){
                mNewValuesSamplingCount.put(ZeroContract.SamplingCountsColumns.VALUE, et.getText().toString());
            }
        }


        getContentResolver().insert(
                ZeroContract.SamplingCounts.CONTENT_URI,
                mNewValuesSamplingCount
        );

        Context context = getApplicationContext();
        CharSequence text = "Sampling saved";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //close activity
        this.finish();

    }

    public void addValue(View view){

        EditText input = new EditText(this);
        //ActionBar.LayoutParams editTextParam =new ActionBar.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        //editTextParam.weight = 0;
        //input.setLayoutParams(editTextParam);
        input.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        //ImageButton suppr = new ImageButton(this);
        //suppr.setImageResource(R.drawable.abc_ic_clear_mtrl_alpha);

        LinearLayout valueLayout = new LinearLayout(this);
        valueLayout.setOrientation(LinearLayout.HORIZONTAL);

        mLayout.addView(valueLayout);

        valueLayout.addView(input);
        //valueLayout.addView(suppr);

        mListValues.add(input);

    }

    public void getAverage(View view){
       /*
        int total = 0;
        int nbvalues = mListValues.size();
        float moyenne;

        Iterator it = mListValues.iterator();

        while(it.hasNext()){
            total = total + Integer.parseInt(it.next().toString());
        }


        //for(int i=0;i<nbvalues;i++){

            //EditText tmp = mListValues.get(i);
            //total = total + Integer.parseInt(tmp.getText().toString());

        }
        moyenne = total/nbvalues;
        //String moyenne_text = ((String) moyenne);
        mAverageText.setText(moyenne);
        */
    }
}
