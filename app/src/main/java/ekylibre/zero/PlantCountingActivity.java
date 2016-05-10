package ekylibre.zero;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
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

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import ekylibre.api.PlantDensityAbacusItem;


public class PlantCountingActivity extends Activity {


    private LinearLayout mLayout;
    private EditText mObservationEditText;
    private TextView mAverageText;
    private ListView mListVariety;
    private List<EditText> mListValues = new ArrayList();
    private ListIterator<EditText> mListIteratorValues;
    private ListView mAdvocatedDensityList;
    private ListView mPlantDensityAbaciNameList;
    private AlertDialog.Builder mPlantChooser;
    private Pattern mIntegerPattern = Pattern.compile("^\\d+$");

    private CharSequence[] mPlantID ;
    private CharSequence[] mPlantName;

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
        String[] mProjectionAdvocatedDensity = {
                ZeroContract.PlantDensityAbacusItemsColumns.SEEDING_DENSITY_VALUE
        };
        String[] mProjectionPlantId = {
                ZeroContract.PlantsColumns._ID
        };
        String[] mProjectionPlantName = {
                ZeroContract.PlantsColumns.NAME
        };
        String[] mProjectionPlantVariety = {
                ZeroContract.PlantsColumns.VARIETY
        };

        /////////////////////////////////////////////////
        List<String> listPlantName = new ArrayList<>();
        Cursor mCursorPlantName = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionPlantName,
                null,
                null,
                null
        );
        Cursor mCursorPlantId = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionPlantId,
                null,
                null,
                null
        );



        Log.d("zero", "beginning plant name");
        if (mCursorPlantName.getCount() > 0 && mCursorPlantId.getCount()>0) {
            Log.d("zero", "data exists");
            mCursorPlantName.moveToFirst();
            mCursorPlantId.moveToFirst();
            int itName = 0;
            int itId = 0;
            do{
                Log.d("zero","name : " + mCursorPlantName.getString(0));
                mPlantName[itName] =mCursorPlantName.getString(0);
                itName++;
            }
            while(mCursorPlantName.moveToNext());

            do{
                Log.d("zero","id : " + mCursorPlantId.getString(0));
                mPlantID[itId] =mCursorPlantId.getString(0);
                itId++;
            }
            while(mCursorPlantName.moveToNext());

            /*do{
                listPlantName.add(mCursorPlant.getString(0));
                Log.d("zero","name : " + mCursorPlant.getString(0));
            }
            while(mCursorPlant.moveToNext());
            */

            Log.d("zero","end plant name");
            Log.d("zero","nb in cursor : " + mCursorPlantName.getCount());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listPlantName );
            mPlantDensityAbaciNameList.setAdapter(arrayAdapter);
        }
        else{
            Log.d("zero", "data does NOT exist");
        }

        /*mPlantChooser = new AlertDialog.Builder(this)
                .setTitle("Choix du plant")
                .setNegativeButton(android.R.string.cancel, null)
                .setItems(, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mLastProcedureNature = getResources().getStringArray(R.array.procedures_values)[which];


                    }
                });

*/
        mCursorPlant.close();







        /////////////////////////////////////////////////
        List<String> listPlantVariety = new ArrayList<>();
        Cursor mCursorPlantVariety = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionPlantVariety,
                null,
                null,
                null
        );
        Log.d("zero", "beginning plant");
        if (mCursorPlantVariety.getCount() > 0) {
            Log.d("zero", "data exists");
            mCursorPlantVariety.moveToFirst();
            do{
                listPlantVariety.add(mCursorPlantVariety.getString(0));
                Log.d("zero","variety : " + mCursorPlantVariety.getString(0));
            }
            while(mCursorPlantVariety.moveToNext());
            Log.d("zero","end");
            Log.d("zero","nb in cursor : " + mCursorPlantVariety.getCount());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listPlantVariety );
            mListVariety.setAdapter(arrayAdapter);
        }
        else{
            Log.d("zero", "data does NOT exist");
        }
        mCursorPlantVariety.close();
        /////////////////////////////////////////////////
        List<String> listPlantDensityAbacus = new ArrayList<>();
        Cursor mCursorDensity = getContentResolver().query(
                ZeroContract.PlantDensityAbacusItems.CONTENT_URI,
                mProjectionAdvocatedDensity,
                ZeroContract.PlantDensityAbacusItemsColumns._ID + " = " + ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ID,
                null,
                null);
        Log.d("zero","beginning plant density abacus");
        if (mCursorDensity.getCount() > 0){
            Log.d("zero", "data exists");
            mCursorDensity.moveToFirst();
            do{
                listPlantDensityAbacus.add(mCursorDensity.getString(0));
                Log.d("zero", "Density : " + mCursorDensity.getString(0));
            }
            while(mCursorDensity.moveToNext());
            Log.d("zero","end");
            Log.d("zero","nb in cursor : " + mCursorDensity.getCount());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                     this,
                     android.R.layout.simple_list_item_1,
                     listPlantDensityAbacus );
             mAdvocatedDensityList.setAdapter(arrayAdapter);
            addValue(mLayout);
        }
        else{
            Log.d("zero", "data does NOT exist");
        }
        mCursorDensity.close();
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
