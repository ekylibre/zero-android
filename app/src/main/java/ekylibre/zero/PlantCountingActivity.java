package ekylibre.zero;


import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import ekylibre.database.ZeroContract;
import ekylibre.util.AccountTool;
import ekylibre.util.UpdatableActivity;


public class PlantCountingActivity extends UpdatableActivity  {
    private final String TAG = "PlantCounting";
    private Account mAccount;
    private LinearLayout mLayout;
    private EditText mObservationEditText;
    private TextView mAverageText;
    private Spinner plantChooser;
    private Spinner abacusChooser;
    private Spinner densityChooser;
    private boolean abacusActivated = false;
    private boolean densityActivated = false;
    private ImageView mIndicator;
    private List<EditText> mListValues = new ArrayList();
    private ListIterator<EditText> mListIteratorValues;

    private Drawable mGreen;
    private Drawable mOrange;
    private Drawable mRed;
    private Drawable mGrey;
    private int mPlantsCount = 0;
    private int[]   mDensityTabID;
    private int mGerminationPercentage = 0;
    private TextView    mPlantCountValue;
    private int testColor = 0;

    public final String  THOUSAND_PER_HECTARE = "thousand_per_hectare";
    public final String  MILLION_PER_HECTARE = "million_per_hectare";
    public final String  UNITY_PER_HECTARE = "unity_per_hectare";
    public final String  UNITY_PER_SQUARE_METER = "unity_per_square_meter";

    private final boolean seeding = false;
    private final boolean germination = true;
    private boolean       currentContext = false;

    private int[] mPlantID;
    private int[]          mPlantEK_IDTab;
    private CharSequence[] mPlantNameTab;
    private CharSequence[] mAbaqueTab;
    private CharSequence[] mDensityTab;

    private int     mPlantEK_ID;
    private int     selectedPlantID;
    private int     selectedPlantDensityAbacusItemID;


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
            case android.R.id.home:
            {
                onBackPressed();
                return (true);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
        {
            AccountTool.askForAccount(this, this);
            return;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_counting);
        super.setToolBar();

        mLayout = (LinearLayout) findViewById(R.id.Valueslayout);
        plantChooser = (Spinner) findViewById(R.id.plantName);
        abacusChooser = (Spinner) findViewById(R.id.Abaque);
        densityChooser = (Spinner) findViewById(R.id.densityValue);
        mAverageText = (TextView) findViewById(R.id.textAverage);
        mPlantCountValue = (TextView) findViewById(R.id.plant_count_value);
        mAverageText.setText("");
        mIndicator = (ImageView) findViewById(R.id.indicator);
        mGreen = getResources().getDrawable(R.color.basic_green);
        mOrange = getResources().getDrawable(R.color.basic_orange);
        mRed = getResources().getDrawable(R.color.basic_red);
        mGrey = getResources().getDrawable(R.color.dark_grey);
        mObservationEditText = (EditText) findViewById(R.id.observationEditText);
        mAccount = AccountTool.getCurrentAccount(this);

        setPlantData();

        setHandlerAverageValue();
        mPlantCountValue.setText("0");
        addValue(null);
    }

    @Override
    protected void onSyncFinish()
    {
        setPlantData();
    }

    @Override
    protected void onSyncStart()
    {}

    private void setPlantData()
    {
        Cursor cursorPlantName = queryPlantName();
        Cursor cursorPlantId = queryPlantId();

        Log.d(TAG, "plant name count :" + cursorPlantName.getCount());
        if (cursorPlantName != null && cursorPlantId != null) {
            Log.d(TAG, "Data exists !");
            mPlantNameTab = new CharSequence[cursorPlantName.getCount() + 1];
            mPlantEK_IDTab = new int[cursorPlantName.getCount() + 1];
            mPlantID = new int[cursorPlantId.getCount() + 1];

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
    }

    public void newContextIsGermination(View v)
    {
        Log.d(TAG, "Switching context ! ==>>> Context = Germination");
        currentContext = germination;
    }

    public void newContextIsSeeding(View v)
    {
        Log.d(TAG, "Switching context ! ==>>> Context = Seeding");
        currentContext = seeding;
    }

    private void fillAbacusTab(Cursor cursorAbacus) {
        int itName = 1;

        mAbaqueTab = new CharSequence[cursorAbacus.getCount() + 1];
        mAbaqueTab[0] = getResources().getString(R.string.select_abacus);
        while (cursorAbacus.moveToNext()) {
            Log.d("zero", "name : " + cursorAbacus.getString(0));
            mAbaqueTab[itName] = cursorAbacus.getString(0);
            itName++;
        }
    }

    private String convertedUnit(String srcUnit)
    {
        if (Objects.equals(srcUnit, THOUSAND_PER_HECTARE))
            srcUnit = getResources().getString(R.string.thousand_per_hectare);
        else if (Objects.equals(srcUnit, MILLION_PER_HECTARE))
            srcUnit = getResources().getString(R.string.million_per_hectare);
        else if (Objects.equals(srcUnit, UNITY_PER_HECTARE))
            srcUnit = getResources().getString(R.string.unity_per_hectare);
        else if (Objects.equals(srcUnit, UNITY_PER_SQUARE_METER))
            srcUnit = getResources().getString(R.string.unity_per_square_meter);
        return (srcUnit);
    }

    private void fillDensityTab(Cursor cursorDensity, String unit) {
        int itName = 1;

        mDensityTab = new CharSequence[cursorDensity.getCount() + 1];
        mDensityTabID = new int[cursorDensity.getCount() + 1];
        mDensityTab[0] = getResources().getString(R.string.select_density);
        mDensityTabID[0] = 0;
        while (cursorDensity.moveToNext())
        {
            Log.d("zero", "name : " + cursorDensity.getString(0));
            mDensityTab[itName] = cursorDensity.getString(0) + " " + convertedUnit(unit);
            mDensityTabID[itName] = cursorDensity.getInt(1);
            itName++;
        }
    }

    private void fillPlantId(Cursor cursorPlantId) {
        int itId = 1;

        mPlantID[0] = 0;
        while (cursorPlantId.moveToNext()) {
            Log.d(TAG, "id : " + cursorPlantId.getString(0));
            mPlantID[itId] = cursorPlantId.getInt(0);
            itId++;
        }
    }

    private void fillPlantName(Cursor cursorPlantName) {
        int itName = 1;

        mPlantNameTab[0] = getResources().getString(R.string.select_plant);
        mPlantEK_IDTab[0] = 0;
        while (cursorPlantName.moveToNext()) {
            Log.d(TAG, "name : " + cursorPlantName.getString(0));
            mPlantNameTab[itName] = cursorPlantName.getString(0);
            mPlantEK_IDTab[itName] = cursorPlantName.getInt(1);
            Log.d(TAG, "tablename[" + itName + "] : " + mPlantNameTab[itName]);
            itName++;
        }
    }

    private void setAbacusList() {
        Cursor cursorActivity = queryActivity();
        Cursor cursorAbacus = queryAbacus(cursorActivity);

        //Log.d(TAG, "valeur récupérée : " + mPlantName.getText());
        Log.d(TAG, "Valeur du activityID : " + cursorActivity.getString(0));
        Log.d("zero", "beginning abaque");

        if (cursorAbacus != null) {
            Log.d(TAG, "Plant ID => " + cursorActivity.getInt(1));
            selectedPlantID = cursorActivity.getInt(1);
            Log.d(TAG, "data exists");
            fillAbacusTab(cursorAbacus);
            Log.d(TAG, "end Abaque");
            Log.d(TAG, "nb in cursor : " + cursorAbacus.getCount());
        } else {
            Log.d(TAG, "abaque data does NOT exist");
        }
        createAbacusChooser();
        cursorActivity.close();
    }

    private void setDensityList(CharSequence abacusSelected)
    {
        String  unit;
        Cursor cursorDensity = queryDensityValue(abacusSelected);
        Cursor cursorDensityUnit = queryDensityUnit(abacusSelected);
        cursorDensityUnit.moveToFirst();
        unit = cursorDensityUnit.getString(0);

        //Log.d(TAG, "valeur récupérée : " + mPlantName.getText());
        Log.d("zero", "beginning abaque");

        if (cursorDensity != null) {
            Log.d(TAG, "data exists");
            fillDensityTab(cursorDensity, unit);
            Log.d(TAG, "end Abaque");
            Log.d(TAG, "nb in cursor : " + cursorDensity.getCount());
        } else {
            Log.d(TAG, "abaque data does NOT exist");
        }
        createDensityChooser();
        if (cursorDensity != null)
            cursorDensity.close();
    }

    private void setGerminationPercentage(CharSequence abacusSelected)
    {
        Cursor cursorGermPercentage = queryGerminationPercentage(abacusSelected);
        cursorGermPercentage.moveToFirst();
        mGerminationPercentage = cursorGermPercentage.getInt(0);
    }

    private void    setPlantsCount(int densityID)
    {
        Cursor cursorPlantsCount = queryPlantsCount(densityID);
        cursorPlantsCount.moveToFirst();
        selectedPlantDensityAbacusItemID = cursorPlantsCount.getInt(1);
        mPlantsCount = cursorPlantsCount.getInt(0);
    }

    private void setIndicatorColor(float averageValue)
    {
        double referenceValue;

        if (mPlantsCount == 0 || mGerminationPercentage == 0)
        {
            mPlantCountValue.setText("0");
            mIndicator.setBackground(mGrey);
            return;
        }
        if (currentContext == germination)
            referenceValue = mPlantsCount * (mGerminationPercentage / 100.0);
        else
            referenceValue = mPlantsCount;
        mPlantCountValue.setText(String.format("%d", (int)referenceValue));

        if (averageValue == 0)
        {
            mIndicator.setBackground(mGrey);
            return;
        }
        if (averageValue <= referenceValue + ((23.0 / 100.0) * referenceValue) && averageValue >= referenceValue - ((23.0 / 100.0) * referenceValue))
            mIndicator.setBackground(mGreen);
        else
            mIndicator.setBackground(mRed);
    }

    private void setHandlerAverageValue() {
        final int     delay = 500;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                setIndicatorColor(getAverage());
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void resetSelector(String baseMessage, Spinner chooser)
    {
        CharSequence[] tab = new CharSequence[1];
        tab[0] = baseMessage;
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tab);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooser.setAdapter(adapter);
    }

    private void createPlantChooser() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mPlantNameTab);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantChooser.setAdapter(adapter);
        plantChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                resetSelector(getResources().getString(R.string.select_abacus), abacusChooser);
                resetSelector(getResources().getString(R.string.select_density), densityChooser);
                if (id == 0)
                    return;
                mPlantEK_ID = mPlantEK_IDTab[(int)id];
                setAbacusList();

                mPlantsCount = 0;
                mGerminationPercentage = 0;
                selectedPlantDensityAbacusItemID = 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });
    }

    private void createAbacusChooser()
    {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mAbaqueTab);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        abacusChooser.setAdapter(adapter);
        abacusChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                resetSelector(getResources().getString(R.string.select_density), densityChooser);
                if (id == 0)
                    return;
                setDensityList(mAbaqueTab[(int)id]);
                setGerminationPercentage(mAbaqueTab[(int)id]);
                mPlantsCount = 0;
                selectedPlantDensityAbacusItemID = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });
    }

    private void createDensityChooser()
    {

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mDensityTab);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        densityChooser.setAdapter(adapter);
        densityChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                if (id == 0)
                {
                    return;
                }
                setPlantsCount(mDensityTabID[(int)id]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
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
        String[] projectionPlantName = {ZeroContract.PlantsColumns.NAME, ZeroContract.Plants.EK_ID};

        Cursor cursorPlantName = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                projectionPlantName,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\"",
                null,
                null);
        return (cursorPlantName);
    }

    private Cursor queryAbacus(Cursor cursorActivity) {
        String[] projectionAbaque = {ZeroContract.PlantDensityAbaci.NAME};

        if (cursorActivity.moveToFirst()) {
            Cursor cursorAbacus = getContentResolver().query(
                    ZeroContract.PlantDensityAbaci.CONTENT_URI,
                    projectionAbaque,
                    "\"" + ZeroContract.PlantDensityAbaciColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                            + " AND " + ZeroContract.PlantDensityAbaciColumns.ACTIVITY_ID + " IS NOT NULL"
                            + " AND " + ZeroContract.PlantDensityAbaciColumns.ACTIVITY_ID + " == " + cursorActivity.getInt(0),
                    null,
                    null);
            return (cursorAbacus);
        }
        return (null);
    }

    private Cursor queryDensityValue(CharSequence abacusSelected) {
        String[] projectionAbaque = {ZeroContract.PlantDensityAbaci.EK_ID};

        Cursor cursorAbacus = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                projectionAbaque,
                "\"" + ZeroContract.PlantDensityAbaciColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                        + " AND " + "\"" + ZeroContract.PlantDensityAbaciColumns.NAME + "\"" + " LIKE " + "\"" + abacusSelected + "\"",
                null,
                null);

        if (cursorAbacus == null)
            return (null);

        String[] projectionDensity = {ZeroContract.PlantDensityAbacusItems.SEEDING_DENSITY_VALUE, ZeroContract.PlantDensityAbacusItems._ID};

        if (!cursorAbacus.moveToFirst())
            return (null);
        Cursor cursorDensityValue = getContentResolver().query(
                ZeroContract.PlantDensityAbacusItems.CONTENT_URI,
                projectionDensity,
                "\"" + ZeroContract.PlantDensityAbacusItemsColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                        + " AND " + ZeroContract.PlantDensityAbacusItemsColumns.FK_ID + " IS NOT NULL"
                        + " AND " + ZeroContract.PlantDensityAbacusItemsColumns.FK_ID + " like \"" + cursorAbacus.getInt(0) + "\"",
                null,
                null);
        cursorAbacus.close();
        return (cursorDensityValue);
    }

    private Cursor queryDensityUnit(CharSequence abacusSelected) {
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

        String[] projectionDensity = {ZeroContract.PlantDensityAbaci.SEEDING_DENSITY_UNIT};

        cursorAbacus.moveToFirst();
        Cursor cursorDensityUnit = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                projectionDensity,
                "\"" + ZeroContract.PlantDensityAbaciColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\"" + " AND " +
                        "\"" + ZeroContract.PlantDensityAbaciColumns._ID + "\"" + " == " + "\"" + cursorAbacus.getInt(0) + "\"",
                null,
                null);
        cursorAbacus.close();
        return (cursorDensityUnit);
    }

    private Cursor queryGerminationPercentage(CharSequence abacusSelected) {
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

        String[] projectionDensity = {ZeroContract.PlantDensityAbaci.GERMINATION_PERCENTAGE};

        cursorAbacus.moveToFirst();
        Cursor cursorGermPerc = getContentResolver().query(
                ZeroContract.PlantDensityAbaci.CONTENT_URI,
                projectionDensity,
                "\"" + ZeroContract.PlantDensityAbaciColumns.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\"" + " AND " +
                        "\"" + ZeroContract.PlantDensityAbaciColumns._ID + "\"" + " == " + "\"" + cursorAbacus.getInt(0) + "\"",
                null,
                null);
        cursorAbacus.close();
        return (cursorGermPerc);
    }

    private Cursor queryPlantsCount(int densityID) {
        String[] mProjectionPlantsCount = {ZeroContract.PlantDensityAbacusItems.PLANTS_COUNT, ZeroContract.PlantDensityAbacusItems.EK_ID};

        Cursor cursorPlantsCount = getContentResolver().query(
                ZeroContract.PlantDensityAbacusItems.CONTENT_URI,
                mProjectionPlantsCount,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                        + " AND " + densityID + " == " + ZeroContract.PlantDensityAbacusItems._ID,
                null,
                null);
        return (cursorPlantsCount);
    }

    private Cursor queryActivity() {
        String[] mProjectionActivity = {ZeroContract.Plants.ACTIVITY_ID, ZeroContract.Plants.EK_ID};

        Cursor cursorActivity = getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI,
                mProjectionActivity,
                "\"" + ZeroContract.Plants.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\""
                        + " AND " + ZeroContract.PlantsColumns.EK_ID + " == " + mPlantEK_ID,
                null,
                null);
        return (cursorActivity);
    }

    private Cursor queryPlantCountingID() {
        String[] mProjectionID = {ZeroContract.PlantCountings._ID};

        Cursor cursorID = getContentResolver().query(
                ZeroContract.PlantCountings.CONTENT_URI,
                mProjectionID,
                "\"" + ZeroContract.PlantCountings.USER + "\"" + " LIKE " + "\"" + mAccount.name + "\"",
                null,
                null);
        if (cursorID == null)
            return (null);
        cursorID.moveToLast();
        return (cursorID);
    }

    public void savePlantCounting(View v)
    {
        if (super.isSync)
            return ;
        if (mPlantsCount != 0 && mGerminationPercentage != 0 && getAverage() != 0.0)
        {
            int id = insertNewValuesPlantCounting();
            pushNewValue(id);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.plant_counting_saved), Toast.LENGTH_SHORT);
            toast.show();
            this.finish();
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.plant_counting_cannot_save), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void pushNewValue(int plantCountingID)
    {
        ContentValues newValuePlantCountingItem = new ContentValues();

        mListIteratorValues = mListValues.listIterator();
        while (mListIteratorValues.hasNext())
        {
            EditText et = mListIteratorValues.next();
            if (et.getText().toString() != null)
            {
                newValuePlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.VALUE, Integer.parseInt(et.getText().toString()));
                newValuePlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.USER, mAccount.name);
                newValuePlantCountingItem.put(ZeroContract.PlantCountingItemsColumns.PLANT_COUNTING_ID, plantCountingID);
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
        Log.d(TAG, "NewValuesPlantCountingItem is now on the local data !");
    }
    
    private int insertNewValuesPlantCounting()
    {
        ContentValues newValuesPlantCounting = new ContentValues();
	
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVED_AT,
				   (new java.util.Date()).getTime());
        setLocation(newValuesPlantCounting);
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.OBSERVATION,
				   mObservationEditText.getText().toString());
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.AVERAGE_VALUE, getAverage());
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.USER, mAccount.name);
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.PLANT_ID, selectedPlantID);
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.PLANT_DENSITY_ABACUS_ITEM_ID, selectedPlantDensityAbacusItemID);
        newValuesPlantCounting.put(ZeroContract.PlantCountingsColumns.SYNCED, 0);
	
        getContentResolver().insert(
				    ZeroContract.PlantCountings.CONTENT_URI,
				    newValuesPlantCounting);
        Cursor curs = queryPlantCountingID();
        Log.d(TAG, "NewValuesPlantCounting is now on the local data !");
        if (curs == null)
            return (-1);
        else
	    {
		Log.d(TAG, "Plant counting ID is => " + curs.getInt(0));
		return (curs.getInt(0));
	    }
    }
    
    @TargetApi(23)
    private void setLocation(ContentValues newValuesPlantCounting)
    {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
	
        if ( Build.VERSION.SDK_INT >= 23 &&
	     ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
	     ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
	    {
		return  ;
	    }
	
	
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
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setBackground(getResources().getDrawable(R.drawable.editbar));
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});
        if (view != null)
            input.requestFocus();
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
    
    private float getAverage()
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
