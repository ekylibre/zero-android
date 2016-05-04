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
import android.text.TextUtils;
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
    private ListView mListVariety;
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
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException n){
            Log.d("zero","Null pointer exception action bar");
        }
        mAdvocatedDensityList = (ListView) findViewById(R.id.advocatedDensityListView);
        mPlantDensityAbaciNameList = (ListView) findViewById(R.id.abaciName);
        mListVariety = (ListView) findViewById(R.id.varietyList);
        String[] mProjectionPlantDensityAbaci = {
                ZeroContract.PlantDensityAbaciColumns.NAME
        };
        String[] mProjectionPlant = {
                ZeroContract.PlantsColumns.VARIETY
        };

        /////////////////////////////////////////////////


        List<String> listPlant = new ArrayList<>();
        Cursor mCursorPlant = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionPlant,
                null,
                null,
                null
        );

        Log.d("zero", "beginning plant");

        if (mCursorPlant.getCount() > 0) {
            Log.d("zero", "data exists");
            mCursorPlant.moveToFirst();
            do{
                listPlant.add(mCursorPlant.getString(0));
                Log.d("zero",mCursorPlant.getString(0));
            }
            while(mCursorPlant.moveToNext());
            Log.d("zero","end");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listPlant );

            mListVariety.setAdapter(arrayAdapter);

        }
        else{
            Log.d("zero", "data does NOT exist");

        }

        /////////////////////////////////////////////////
        List<String> listplantdensityabacus = new ArrayList<>();
        Cursor mCursor = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                mProjectionPlantDensityAbaci,
                null,
                null,
                null);

        Log.d("zero","beginning plant density abacus");

        if (mCursor.getCount() > 0){
            Log.d("zero", "data exists");
            mCursor.moveToFirst();

            do{
                listplantdensityabacus.add(mCursor.getString(0));
                Log.d("zero",mCursor.getString(0));
            }
            while(mCursor.moveToNext());

            Log.d("zero","end");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                     this,
                     android.R.layout.simple_list_item_1,
                     listplantdensityabacus );

             mAdvocatedDensityList.setAdapter(arrayAdapter);

            addValue(mLayout);
        }
        else{
            Log.d("zero", "data does NOT exist");

        }
        mCursor.close();
        /////////////////////////////////////////////////

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
        input.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        LinearLayout valueLayout = new LinearLayout(this);
        valueLayout.setOrientation(LinearLayout.HORIZONTAL);

        mLayout.addView(valueLayout);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        valueLayout.addView(input);


        mListValues.add(input);

    }

    public void getAverage(View view){

        float total = 0;
        int nbvalues = 0;
        float moyenne = 0;
        Iterator it = mListValues.iterator();

        while(it.hasNext()){

            EditText editText = (EditText) it.next();
            String txt = editText.getText().toString();

            if(
                    !editText.getText().toString().contains("/") &&
                    !editText.getText().toString().contains("*") &&
                    !editText.getText().toString().contains("-") &&
                    !editText.getText().toString().contains("+") &&
                    !editText.getText().toString().contains(".") &&
                    !editText.getText().toString().contains(",") &&
                    !editText.getText().toString().contains(";") &&
                    !editText.getText().toString().contains(":") &&
                    !editText.getText().toString().contains("(") &&
                    !editText.getText().toString().contains(")") &&
                    !TextUtils.isEmpty(editText.getText())
                    ){
                total += Float.parseFloat(txt);
                nbvalues++;
            }
            else{
                Toast toast = Toast.makeText(this, "wrong type at value " + editText.getText().toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        moyenne = total/nbvalues;
        mAverageText.setText(String.valueOf(moyenne));
    }

}