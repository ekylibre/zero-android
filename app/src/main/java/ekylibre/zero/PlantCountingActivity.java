package ekylibre.zero;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;


public class PlantCountingActivity extends Activity {


    private LinearLayout mLayout;
    private EditText mObservationEditText;
    private TextView mAverageText;
    private List<EditText> mListValues = new ArrayList();
    private ListIterator<EditText> mListIteratorValues;
    private ListView mAdvocatedDensityList;
    private ListView mPlantDensityAbaciNameList;
    private Pattern mIntegerPattern = Pattern.compile("^\\d+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_counting);

        mLayout = (LinearLayout) findViewById(R.id.AllValuesLayout);
        mObservationEditText = (EditText)findViewById(R.id.observationEditText);
        mAverageText = (TextView) findViewById(R.id.textAverage);
        mAverageText.setText("");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mAdvocatedDensityList = (ListView) findViewById(R.id.advocatedDensityListView);
        mPlantDensityAbaciNameList = (ListView) findViewById(R.id.abaciName);

        String[] mProjectionPlantDensityAbaci = {
                ZeroContract.PlantDensityAbaciColumns.NAME
        };

        List<String> PDA = new ArrayList<String>();
        Cursor mCursor = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                mProjectionPlantDensityAbaci,
                null,
                null,
                null);
        Log.d("zero","beginning");
        while(mCursor.moveToNext()){
            PDA.add(mCursor.getString(0));
            Log.d("zero",mCursor.getString(0));
        }
        Log.d("zero","end");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                 this,
                 android.R.layout.simple_list_item_1,
                 PDA );

         mAdvocatedDensityList.setAdapter(arrayAdapter);

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
                savePlantCounting(item.getActionView());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void savePlantCounting(View v) {

        //http://developer.android.com/guide/topics/providers/content-provider-basics.html

        // Defines an object to contain the new values to insert
        ContentValues mNewValuesPlantCounting = new ContentValues();
        ContentValues mNewValuesPlantCountingItem = new ContentValues();
        // Sets the values of each column and inserts the word. The arguments to the "put"
        // method are "column name" and "value"
        mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVED_AT, (new java.util.Date()).getTime());
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LATITUDE, lastKnownLocation.getLatitude());
            mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LONGITUDE, lastKnownLocation.getLongitude());
        }
        mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVATION, mObservationEditText.getText().toString());

        mListIteratorValues = mListValues.listIterator();



        getContentResolver().insert(
                ZeroContract.PlantCountings.CONTENT_URI,   // the user dictionary content URI
                mNewValuesPlantCounting                          // the values to insert
        );



        while(mListIteratorValues.hasNext()){
            EditText et = mListIteratorValues.next();
            if(et.getText().toString() != null){
                mNewValuesPlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.VALUE, et.getText().toString());
            }
        }


        getContentResolver().insert(
                ZeroContract.PlantCountingItems.CONTENT_URI,
                mNewValuesPlantCountingItem
        );

        Context context = getApplicationContext();
        CharSequence text = "Plant Counting saved";
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

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        valueLayout.addView(input);
        //valueLayout.addView(suppr);

        mListValues.add(input);

    }

    public void getAverage(View view){

        float total = 0;
        int nbvalues = mListValues.size();
        float moyenne = 0;

        Iterator it = mListValues.iterator();

        while(it.hasNext()){

            EditText editText = (EditText) it.next();
            String txt = editText.getText().toString();




            if(mIntegerPattern.matcher(txt).find()){
                total += Float.parseFloat(txt);
            }
            else{
                Toast toast = Toast.makeText(this, "wrong_type", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        moyenne = total/nbvalues;
        //String moyenne_text = ((String) moyenne);
        mAverageText.setText(String.valueOf(moyenne));

    }
}
