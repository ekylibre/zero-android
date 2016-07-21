package ekylibre.zero;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
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
    private final String            TAG = "PlantCounting";
    private LinearLayout            mLayout;
    private EditText                mObservationEditText;
    private TextView                mAverageText;
    private TextView                mPlantName;
    private TextView                mAbaque;
    private TextView                mDensityText;
    private List<EditText>          mListValues = new ArrayList();
    private ListIterator<EditText>  mListIteratorValues;
    private AlertDialog.Builder     mPlantChooser = null;
    private AlertDialog.Builder     mVarietyChooser = null;


    private CharSequence[] mPlantID;
    private CharSequence[] mPlantNameTab;
    private CharSequence[] mAbaqueTab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_counting);

        mLayout = (LinearLayout) findViewById(R.id.Valueslayout);
        mPlantName = (Button)findViewById(R.id.plantName);
        mAbaque = (Button)findViewById(R.id.Abaque);
        mAverageText = (TextView) findViewById(R.id.textAverage);
        mAverageText.setText("");

        Cursor cursorPlantName = queryPlantName();
        Cursor cursorPlantId = queryPlantId();

        Log.d(TAG, "plant name count :" + cursorPlantName.getCount());
        if (cursorPlantName != null && cursorPlantId != null)
        {
            Log.d(TAG, "Data exists !");
            mPlantNameTab = new CharSequence[cursorPlantName.getCount()];
            mPlantID = new CharSequence[cursorPlantId.getCount()];

            fillPlantName(cursorPlantName);
            fillPlantId(cursorPlantId);

            Log.d(TAG, "end plant name");
            Log.d(TAG, "nb in cursor : " + cursorPlantName.getCount());
        }
        else
        {
            Toast.makeText(this, "Data does not exist", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Plant data does NOT exist");
        }

        createPlantChooser();

        cursorPlantId.close();
        cursorPlantName.close();
        setHandlerAverageValue();
    }

    private void setHandlerAverageValue()
    {
        final int     delay = 1000;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                getAverage();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void    createPlantChooser()
    {
        mPlantChooser = new AlertDialog.Builder(this)
                .setTitle("Choix du plant")
                .setItems(mPlantNameTab, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mPlantName.setText(mPlantNameTab[which]);
                        setAbacusList();
                    }
                });
    }

    private void    fillPlantId(Cursor cursorPlantId)
    {
        int itId    = 0;

        while (cursorPlantId.moveToNext())
        {
            Log.d(TAG, "id : " + cursorPlantId.getString(0));
            mPlantID[itId] = cursorPlantId.getString(0);
            itId++;
        }
    }

    private void    fillPlantName(Cursor cursorPlantName)
    {
        int itName  = 0;

        while (cursorPlantName.moveToNext())
        {
            Log.d(TAG, "name : " + cursorPlantName.getString(0));
            mPlantNameTab[itName] = cursorPlantName.getString(0);
            Log.d(TAG, "tablename[" + itName + "] : " + mPlantNameTab[itName]);
            itName++;
        }
    }

    private Cursor  queryPlantId()
    {
        String[] projectionPlantId = {ZeroContract.PlantsColumns._ID};

        Cursor cursorPlantId = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                projectionPlantId,
                null,
                null,
                null);
        return (cursorPlantId);
    }

    private Cursor  queryPlantName()
    {
        String[] projectionPlantName = {ZeroContract.PlantsColumns.NAME};

        Cursor cursorPlantName = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                projectionPlantName,
                null,
                null,
                null);
        return (cursorPlantName);
    }

    public void setAbacusList()
    {
        Cursor cursorVariety = queryVariety();
        Cursor cursorAbacus = queryAbacus(cursorVariety);

        Log.d(TAG, "valeur récupérée : " + mPlantName.getText().toString());
        Log.d(TAG, "Valeur du where : " + cursorVariety.getString(0));
               Log.d("zero", "beginning abaque");

        if (cursorAbacus != null)
        {
            Log.d(TAG, "data exists");
            fillAbacusTab(cursorAbacus);

            Log.d(TAG, "end Abaque");
            Log.d(TAG, "nb in cursor : " + cursorAbacus.getCount());
        }
        else
        {
            Log.d(TAG, "abaque data does NOT exist");
        }
        createVarietyChooser();
        cursorVariety.close();
    }

    private void createVarietyChooser()
    {
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

    private void fillAbacusTab(Cursor cursorAbacus)
    {
        int itName = 0;

        mAbaqueTab = new CharSequence[cursorAbacus.getCount()];
        while (cursorAbacus.moveToNext())
        {
            Log.d("zero", "name : " + cursorAbacus.getString(0));
            mAbaqueTab[itName] = cursorAbacus.getString(0);
            Log.d("zero", "tablename[" + itName + "] : " + mAbaqueTab[itName]);
            itName++;
        }
    }

    private Cursor queryAbacus(Cursor cursorVariety)
    {
        String[] projectionAbaque = {ZeroContract.PlantDensityAbaci.NAME};

        cursorVariety.moveToFirst();
        Cursor cursorAbacus = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                projectionAbaque,
                ZeroContract.PlantDensityAbaciColumns.VARIETY + " like \"" + cursorVariety.getString(0) + "\"",
                null,
                null);
        return (cursorAbacus);
    }

    private Cursor    queryVariety()
    {
        String[] mProjectionVariety = {ZeroContract.Plants.VARIETY};

        Cursor cursorVariety = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionVariety,
                ZeroContract.PlantsColumns.NAME + " like \"" + mPlantName.getText().toString() + "\"",
                null,
                null);
        return (cursorVariety);
    }


    public void chosePlant(View view)
    {
        if (mPlantChooser == null)
            return ;
        mPlantChooser.show();
    }

    public void choseAbaque(View view)
    {
        if (mVarietyChooser == null)
            return ;
        mVarietyChooser.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
        insertNewValuesPlantCounting();
        ContentValues newValuesPlantCountingItem = fillNewValuesPlantCountingItems();
        insertNewValuesPlantCountingItems(newValuesPlantCountingItem);
        this.finish();
    }

    private void insertNewValuesPlantCountingItems(ContentValues newValuesPlantCountingItem)
    {
        if (!mListValues.isEmpty())
        {
            getContentResolver().insert(
                    ZeroContract.PlantCountingItems.CONTENT_URI,
                    newValuesPlantCountingItem);
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Plant Counting saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    private ContentValues fillNewValuesPlantCountingItems()
    {
        ContentValues newValuesPlantCountingItem = new ContentValues();

        while(mListIteratorValues.hasNext())
        {
            EditText et = mListIteratorValues.next();
            if(et.getText().toString() != null)
            {
                newValuesPlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.VALUE, et.getText().toString());
            }
        }
        return (newValuesPlantCountingItem);
    }

    private void insertNewValuesPlantCounting()
    {
        ContentValues newValuesPlantCounting = new ContentValues();

        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVED_AT,
                (new java.util.Date()).getTime());
        setLocation(newValuesPlantCounting);
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVATION,
                mObservationEditText.getText().toString());
        mListIteratorValues = mListValues.listIterator();
        getContentResolver().insert(
                ZeroContract.PlantCountings.CONTENT_URI,
                newValuesPlantCounting);
    }

    private void setLocation(ContentValues newValuesPlantCounting)
    {
        LocationManager locationManager =
                (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null)
        {
            newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LATITUDE,
                    lastKnownLocation.getLatitude());
            newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LONGITUDE,
                    lastKnownLocation.getLongitude());
        }
    }

    public void addValue(View view)
    {
        EditText input = new EditText(this);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        lp.setMargins(0, 30, 0, 0);
        input.setPadding(0, 0, 0, 10);
        input.setLayoutParams(lp);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setOnEditorActionListener(
            new EditText.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    if (event == null)
                        getAverage();
                    return (false);
                }
            });
        mLayout.addView(input);
        mListValues.add(input);
    }

    public void getAverage()
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
