package ekylibre.zero;


import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ekylibre.api.ZeroContract;
import ekylibre.zero.util.AccountTool;


public class PlantCountingActivity extends AppCompatActivity {
    private final String TAG = "PlantCounting";
    private Account mAccount;
    private LinearLayout mLayout;
    private EditText mObservationEditText;
    private TextView mAverageText;
    private TextView mPlantName;
    private TextView mAbaque;
    private TextView mDensityText;
    private ImageView mIndicator;
    private List<EditText> mListValues = new ArrayList();
    private ListIterator<EditText> mListIteratorValues;
    private AlertDialog.Builder mPlantChooser = null;
    private AlertDialog.Builder mAbacusChooser = null;
    private AlertDialog.Builder mDensityChooser = null;
    private Drawable mGreen;
    private Drawable mOrange;
    private Drawable mRed;

    private int testColor = 0;


    private CharSequence[] mPlantID;
    private CharSequence[] mPlantNameTab;
    private CharSequence[] mAbaqueTab;
    private CharSequence[] mDensityTab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                savePlantCounting(item.getActionView());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
            AccountTool.askForAccount(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_counting);

        mLayout = (LinearLayout) findViewById(R.id.Valueslayout);
        mPlantName = (Button) findViewById(R.id.plantName);
        mAbaque = (Button) findViewById(R.id.Abaque);
        mDensityText = (Button) findViewById(R.id.densityValue);
        mAverageText = (TextView) findViewById(R.id.textAverage);
        mAverageText.setText("");
        mIndicator = (ImageView) findViewById(R.id.indicator);
        mGreen = getResources().getDrawable(R.color.basic_green);
        mOrange = getResources().getDrawable(R.color.basic_orange);
        mRed = getResources().getDrawable(R.color.basic_red);
        mObservationEditText = (EditText) findViewById(R.id.observationEditText);
        mAccount = AccountTool.getCurrentAccount(this);

        Cursor cursorPlantName = queryPlantName();
        Cursor cursorPlantId = queryPlantId();

        Log.d(TAG, "plant name count :" + cursorPlantName.getCount());
        if (cursorPlantName != null && cursorPlantId != null) {
            Log.d(TAG, "Data exists !");
            mPlantNameTab = new CharSequence[cursorPlantName.getCount()];
            mPlantID = new CharSequence[cursorPlantId.getCount()];

            fillPlantName(cursorPlantName);
            fillPlantId(cursorPlantId);

            Log.d(TAG, "end plant name");
            Log.d(TAG, "nb in cursor : " + cursorPlantName.getCount());
        } else {
            Toast.makeText(this, "Data does not exist", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Plant data does NOT exist");
        }

        createPlantChooser();

        cursorPlantId.close();
        cursorPlantName.close();
        setHandlerAverageValue();
    }

    private void fillAbacusTab(Cursor cursorAbacus) {
        int itName = 0;

        mAbaqueTab = new CharSequence[cursorAbacus.getCount()];
        while (cursorAbacus.moveToNext()) {
            Log.d("zero", "name : " + cursorAbacus.getString(0));
            mAbaqueTab[itName] = cursorAbacus.getString(0);
            Log.d("zero", "tablename[" + itName + "] : " + mAbaqueTab[itName]);
            itName++;
        }
    }

    private void fillDensityTab(Cursor cursorDensity) {
        int itName = 0;

        mDensityTab = new CharSequence[cursorDensity.getCount()];
        while (cursorDensity.moveToNext())
        {
            Log.d("zero", "name : " + cursorDensity.getString(0));
            mDensityTab[itName] = cursorDensity.getString(0);
            Log.d("zero", "tablename[" + itName + "] : " + mAbaqueTab[itName]);
            itName++;
        }
    }

    private void fillPlantId(Cursor cursorPlantId) {
        int itId = 0;

        while (cursorPlantId.moveToNext()) {
            Log.d(TAG, "id : " + cursorPlantId.getString(0));
            mPlantID[itId] = cursorPlantId.getString(0);
            itId++;
        }
    }

    private void fillPlantName(Cursor cursorPlantName) {
        int itName = 0;

        while (cursorPlantName.moveToNext()) {
            Log.d(TAG, "name : " + cursorPlantName.getString(0));
            mPlantNameTab[itName] = cursorPlantName.getString(0);
            Log.d(TAG, "tablename[" + itName + "] : " + mPlantNameTab[itName]);
            itName++;
        }
    }

    public void setAbacusList() {
        Cursor cursorVariety = queryVariety();
        Cursor cursorAbacus = queryAbacus(cursorVariety);

        Log.d(TAG, "valeur récupérée : " + mPlantName.getText());
        Log.d(TAG, "Valeur du where : " + cursorVariety.getString(0));
        Log.d("zero", "beginning abaque");

        if (cursorAbacus != null) {
            Log.d(TAG, "data exists");
            fillAbacusTab(cursorAbacus);
            Log.d(TAG, "end Abaque");
            Log.d(TAG, "nb in cursor : " + cursorAbacus.getCount());
        } else {
            Log.d(TAG, "abaque data does NOT exist");
        }
        createAbacusChooser();
        cursorVariety.close();
    }

    public void setDensityList(CharSequence abacusSelected)
    {
        Cursor cursorDensity = queryDensityValue(abacusSelected);

        Log.d(TAG, "valeur récupérée : " + mPlantName.getText());
        Log.d("zero", "beginning abaque");

        if (cursorDensity != null) {
            Log.d(TAG, "data exists");
            fillDensityTab(cursorDensity);
            Log.d(TAG, "end Abaque");
            Log.d(TAG, "nb in cursor : " + cursorDensity.getCount());
        } else {
            Log.d(TAG, "abaque data does NOT exist");
        }
        createDensityChooser();
        cursorDensity.close();
    }

    private boolean averageIsOkay(float averageValue) {
        //TODO here check if the average value is in the right interval from abacus
        return (true);
    }

    private void setHandlerAverageValue() {
/*
        final int     delay = 500;
*/
        //test value for color iteration
        final int delay = 3000;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float average = getAverage();
                if (averageIsOkay(average)) {
                    if (testColor % 3 == 0)
                        mIndicator.setBackground(mGreen);
                    else if (testColor % 3 == 1)
                        mIndicator.setBackground(mOrange);
                    else
                        mIndicator.setBackground(mRed);
                    testColor++;
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void createPlantChooser() {
        mPlantChooser = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.chose_plant))
                .setItems(mPlantNameTab, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlantName.setText(mPlantNameTab[which]);
                        setAbacusList();
                    }
                });
    }

    private void createAbacusChooser() {
        mAbacusChooser = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.chose_abacus))
                .setNegativeButton(android.R.string.cancel, null)
                .setItems(mAbaqueTab, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAbaque.setText(mAbaqueTab[which]);
                        setDensityList(mAbaqueTab[which]);
                    }
                });
    }

    private void createDensityChooser() {
        mDensityChooser = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.chose_density))
                .setNegativeButton(android.R.string.cancel, null)
                .setItems(mDensityTab, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDensityText.setText(mDensityTab[which]);
                    }
                });
    }

    private Cursor queryPlantId() {
        String[] projectionPlantId = {ZeroContract.PlantsColumns._ID};

        Cursor cursorPlantId = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                projectionPlantId,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\"",
                null,
                null);
        return (cursorPlantId);
    }

    private Cursor queryPlantName() {
        String[] projectionPlantName = {ZeroContract.PlantsColumns.NAME};

        Cursor cursorPlantName = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                projectionPlantName,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\"",
                null,
                null);
        return (cursorPlantName);
    }

    private Cursor queryAbacus(Cursor cursorVariety) {
        String[] projectionAbaque = {ZeroContract.PlantDensityAbaci.NAME};

        if (cursorVariety.moveToFirst()) {
            Cursor cursorAbacus = getContentResolver().query(
                    ZeroContract.PlantDensityAbaci.CONTENT_URI,
                    projectionAbaque,
                    "\"" + ZeroContract.PlantDensityAbaciColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                            + " AND " + ZeroContract.PlantDensityAbaciColumns.VARIETY + " IS NOT NULL"
                            + " AND " + ZeroContract.PlantDensityAbaciColumns.VARIETY + " like \"" + cursorVariety.getString(0) + "\"",
                    null,
                    null);
            return (cursorAbacus);
        }
        return (null);
    }

    private Cursor queryDensityValue(CharSequence abacusSelected) {
        String[] projectionAbaque = {ZeroContract.PlantDensityAbaci._ID};

        Cursor cursorAbacus = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                projectionAbaque,
                "\"" + ZeroContract.PlantDensityAbaciColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                + " AND " + "\"" + ZeroContract.PlantDensityAbaciColumns.NAME + "\"" + " LIKE " + "\"" + abacusSelected + "\"",
                null,
                null);

        if (cursorAbacus == null)
            return (null);

        String[] projectionDensity = {ZeroContract.PlantDensityAbacusItems.SEEDING_DENSITY_VALUE};

        cursorAbacus.moveToFirst();
        Cursor cursorDensityUnit = getContentResolver().query(
                    ZeroContract.PlantDensityAbacusItems.CONTENT_URI,
                    projectionDensity,
                    "\"" + ZeroContract.PlantDensityAbacusItemsColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                            + " AND " + ZeroContract.PlantDensityAbacusItemsColumns.FK_ID + " IS NOT NULL"
                            + " AND " + ZeroContract.PlantDensityAbacusItemsColumns.FK_ID + " like \"" + cursorAbacus.getInt(0) + "\"",
                    null,
                    null);
        cursorAbacus.close();
        return (cursorDensityUnit);
    }

    private Cursor queryVariety() {
        String[] mProjectionVariety = {ZeroContract.Plants.VARIETY};

        Cursor cursorVariety = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionVariety,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                        + " AND " + ZeroContract.PlantsColumns.NAME + " like \"" + mPlantName.getText().toString() + "\"",
                null,
                null);
        return (cursorVariety);
    }

    public void chosePlant(View view) {
        if (mPlantChooser == null)
            return;
        mPlantChooser.show();
    }

    public void choseAbaque(View view) {
        if (mAbacusChooser == null)
            return;
        mAbacusChooser.show();
    }

    public void choseDensity(View view) {
        if (mDensityChooser == null)
            return;
        mDensityChooser.show();
    }

    public void savePlantCounting(View v) {
        insertNewValuesPlantCounting();
        pushNewValue();
        Toast toast = Toast.makeText(getApplicationContext(), "Plant Counting saved", Toast.LENGTH_SHORT);
        toast.show();
        this.finish();
    }

    private void pushNewValue()
    {
        ContentValues newValuePlantCountingItem = new ContentValues();

        mListIteratorValues = mListValues.listIterator();
        while (mListIteratorValues.hasNext())
        {
            EditText et = mListIteratorValues.next();
            if (et.getText().toString() != null)
            {
                newValuePlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.VALUE, et.getText().toString());
                newValuePlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.USER, mAccount.name);
                insertNewValuePlantCountingItems(newValuePlantCountingItem);

            }
        }
        return ;
    }

    private void insertNewValuePlantCountingItems(ContentValues newValuePlantCountingItem)
    {
        getContentResolver().insert(
                ZeroContract.PlantCountingItems.CONTENT_URI,
                newValuePlantCountingItem);
    }

    private void insertNewValuesPlantCounting()
    {
        ContentValues newValuesPlantCounting = new ContentValues();

        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVED_AT,
                (new java.util.Date()).getTime());
        setLocation(newValuesPlantCounting);
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVATION,
                mObservationEditText.getText().toString());
        getContentResolver().insert(
                ZeroContract.PlantCountings.CONTENT_URI,
                newValuesPlantCounting);
    }

    private void setLocation(ContentValues newValuesPlantCounting)
    {
        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        try
        {
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null)
            {
                newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LATITUDE,
                        lastKnownLocation.getLatitude());
                newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.LONGITUDE,
                        lastKnownLocation.getLongitude());
            }
        }
        catch(SecurityException e)
        {
            Toast.makeText(this, getResources().getString(R.string.GPSissue), Toast.LENGTH_SHORT).show();
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

    public float getAverage()
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
        return (averageScore);
    }
}
