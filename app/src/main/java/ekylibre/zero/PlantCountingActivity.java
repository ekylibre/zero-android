package ekylibre.zero;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;



public class PlantCountingActivity extends AppCompatActivity
{
    private LinearLayout mLayout;
    private EditText mObservationEditText;
    private TextView mAverageText;
    private TextView mPlantName;
    private TextView mAbaque;
    private TextView mDensityText;
    private List<EditText> mListValues = new ArrayList();
    private ListIterator<EditText> mListIteratorValues;
    private AlertDialog.Builder mPlantChooser = null;
    private AlertDialog.Builder mVarietyChooser = null;


    private CharSequence[] mPlantID;
    private CharSequence[] mPlantNameTab;
    private CharSequence[] mAbaqueTab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_counting);

        mLayout = (LinearLayout) findViewById(R.id.Valueslayout);
        mObservationEditText = (EditText)findViewById(R.id.observationEditText);
        mPlantName = (Button)findViewById(R.id.plantName);
        mAbaque = (Button)findViewById(R.id.Abaque);
        mDensityText = (Button)findViewById(R.id.densityValue);
        mAverageText = (TextView) findViewById(R.id.textAverage);
        mAverageText.setText("");


/*        String[] mProjectionDensity = {ZeroContract.PlantDensityAbacusItemsColumns.SEEDING_DENSITY_VALUE};
        String[] mProjectionPlantId = {ZeroContract.PlantsColumns._ID};
        String[] mProjectionPlantName = {ZeroContract.PlantsColumns.NAME};

        //String[] mProjectionAbacus = {
        //        ZeroContract.Plants
        //};


        Cursor mCursorPlantName = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionPlantName,
                null,
                null,
                null);

        Cursor mCursorPlantId = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionPlantId,
                null,
                null,
                null);


        Log.d("zero", "plant name count :" + mCursorPlantName.getCount());
        if (mCursorPlantName.getCount() > 0 && mCursorPlantId.getCount() > 0)
        {
            Log.d("zero", "data exists");
            int itName = 0;
            int itId = 0;
            mPlantNameTab = new CharSequence[mCursorPlantName.getCount()];
            if (mCursorPlantName.moveToFirst())
            {
                do
                {
                    Log.d("zero", "name : " + mCursorPlantName.getString(0));
                    mPlantNameTab[itName] = mCursorPlantName.getString(0);
                    Log.d("zero", "tablename[" + itName + "] : " + mPlantNameTab[itName]);
                    itName++;
                } while (mCursorPlantName.moveToNext());
            }
            if (mCursorPlantId.moveToFirst())
            {
                do
                {
                    Log.d("zero", "id : " + mCursorPlantId.getString(0));
                    mPlantID[itId] = mCursorPlantId.getString(0);
                    itId++;
                } while (mCursorPlantId.moveToNext());
            }
            Log.d("zero", "end plant name");
            Log.d("zero", "nb in cursor : " + mCursorPlantName.getCount());

        }
        else
        {
            Toast.makeText(this, "Data does not exist", Toast.LENGTH_SHORT).show();
            Log.d("zero", "Plant data does NOT exist");
        }


        mPlantChooser = new AlertDialog.Builder(this)
                .setTitle("Choix du plant")
                .setItems(mPlantNameTab, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mPlantName.setText(mPlantNameTab[which]);
                        //Mise a jour de la seconde liste

                    }
                });

        mCursorPlantId.close();
        mCursorPlantName.close();
        setAbacusList();*/
    }

    //Once the item from the plant is chosen. The abacus list must be updated
    public void setAbacusList()
    {

/*        String[] mProjectionAbaque = {ZeroContract.PlantDensityAbaci.NAME};

        String[] mProjectionVariety = {ZeroContract.Plants.VARIETY};



        Cursor mCursorVariety = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionVariety,
                ZeroContract.PlantsColumns.NAME + " like \"" + mPlantName.getText().toString() + "\"",
                null,
                null);
        Log.d("zero", "valeur récupérée : " + mPlantName.getText().toString());

        if (mCursorVariety.moveToFirst())
        {

            Log.d("zero", "Valeur du where : " + mCursorVariety.getString(0));
            Cursor mCursorAbaque = getContentResolver().query(
                    ZeroContract.PlantDensityAbaci.CONTENT_URI,
                    mProjectionAbaque,
                    ZeroContract.PlantDensityAbaciColumns.VARIETY + " like \"" + mCursorVariety.getString(0).toString() + "\"",
                    null,
                    null);

            Log.d("zero", "beginning abaque");
            if (mCursorAbaque.getCount() > 0)
            {
                Log.d("zero", "data exists");
                mCursorAbaque.moveToFirst();
                int itName = 0;
                mAbaqueTab = new CharSequence[mCursorAbaque.getCount()];
                do
                {
                    Log.d("zero", "name : " + mCursorAbaque.getString(0));
                    mAbaqueTab[itName] = mCursorAbaque.getString(0);
                    Log.d("zero", "tablename[" + itName + "] : " + mAbaqueTab[itName]);
                    itName++;
                } while (mCursorAbaque.moveToNext());

                Log.d("zero", "end Abaque");
                Log.d("zero", "nb in cursor : " + mCursorAbaque.getCount());
            }
            else
            {
                Log.d("zero", "abaque data does NOT exist");
            }
            mVarietyChooser = new AlertDialog.Builder(this)
                    .setTitle("Choix de l'abaque")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setItems(mAbaqueTab, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            mAbaque.setText(mAbaqueTab[which]);
                        }
                    });
        }
        mCursorVariety.close();*/
    }


    public void chosePlant(View view)
    {
/*        if (mPlantChooser == null)
            return ;
        mPlantChooser.show();*/
    }

    public void choseAbaque(View view)
    {
/*        if (mVarietyChooser == null)
            return ;
        mVarietyChooser.show();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_save:
                savePlantCounting(item.getActionView());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void savePlantCounting(View v)
    {
/*
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
        if (lastKnownLocation != null)
        {
            mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LATITUDE, lastKnownLocation.getLatitude());
            mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LONGITUDE, lastKnownLocation.getLongitude());
        }
        mNewValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVATION, mObservationEditText.getText().toString());
        mListIteratorValues = mListValues.listIterator();

        getContentResolver().insert(
                ZeroContract.PlantCountings.CONTENT_URI,
                mNewValuesPlantCounting);

        while(mListIteratorValues.hasNext())
        {
            EditText et = mListIteratorValues.next();
            if(et.getText().toString() != null)
            {
                mNewValuesPlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.VALUE, et.getText().toString());
            }
        }

        getContentResolver().insert(
                ZeroContract.PlantCountingItems.CONTENT_URI,
                mNewValuesPlantCountingItem);

        Toast toast = Toast.makeText(getApplicationContext(), "Plant Counting saved", Toast.LENGTH_SHORT);
        toast.show();

        //close activity
        this.finish();*/

    }

    public void addValue(View view)
    {
        EditText input = new EditText(this);
        input.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        input.setPadding(0, 50, 0, 0);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        mLayout.addView(input);
        mListValues.add(input);
    }

    public void getAverage(View view)
    {
        float   total = 0;
        int     nbvalues = 0;
        float   averageScore = 0;

        Iterator it = mListValues.iterator();

        while(it.hasNext())
        {
            EditText editText = (EditText)it.next();
            String txt = editText.getText().toString();

            if (!TextUtils.isEmpty(editText.getText()))
            {
                total += Float.parseFloat(txt);
                nbvalues++;
            }
        }
        if (nbvalues == 0)
            averageScore = 0;
        else
            averageScore = total / nbvalues;
        mAverageText.setText(String.valueOf(averageScore));
    }

}
