package ekylibre.zero.inter;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.database.ZeroContract.DetailedInterventionAttributes;
import ekylibre.database.ZeroContract.DetailedInterventions;
import ekylibre.util.AccountTool;
import ekylibre.util.ProcedureFamiliesXMLReader;
import ekylibre.util.ProceduresXMLReader;
import ekylibre.util.ontology.Node;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.home.Zero;
import ekylibre.zero.inter.fragment.CropParcelChoiceFragment;
import ekylibre.zero.inter.fragment.InterventionFormFragment;
import ekylibre.zero.inter.fragment.ParamChoiceFragment;
import ekylibre.zero.inter.fragment.ProcedureChoiceFragment;
import ekylibre.zero.inter.fragment.ProcedureFamilyChoiceFragment;
import ekylibre.zero.inter.model.CropParcel;
import ekylibre.zero.inter.model.GenericItem;

public class InterActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        ProcedureFamilyChoiceFragment.OnFragmentInteractionListener,
        ProcedureChoiceFragment.OnFragmentInteractionListener,
        InterventionFormFragment.OnFragmentInteractionListener {

    private static final String TAG = "NewInterventionActivity";

    public static final String PROCEDURE_FAMILY_FRAGMENT = "ekylibre.zero.fragments.procedure.family";
    public static final String PROCEDURE_CATEGORY_FRAGMENT = "ekylibre.zero.fragments.procedure.category";
    public static final String PROCEDURE_NATURE_FRAGMENT = "ekylibre.zero.fragments.procedure.nature";
    public static final String INTERVENTION_FORM = "ekylibre.zero.fragments.intervention.form";
    public static final String PARAM_FRAGMENT = "ekylibre.zero.fragments.intervention.param";

    private static final Pair<Integer,String> ADMINISTERING = Pair.create(R.id.administering, "administering");
    private static final Pair<Integer,String> ANIMAL_FARMING = Pair.create(R.id.animal_farming, "animal_farming");
    private static final Pair<Integer,String> PLANT_FARMING = Pair.create(R.id.plant_farming, "plant_farming");
    private static final Pair<Integer,String> PROCESSING = Pair.create(R.id.processing, "processing");
    private static final Pair<Integer,String> TOOL_MAINTAINING = Pair.create(R.id.tool_maintaining, "tool_maintaining");

    public static Account account;
    public static ActionBar actionBar;
    private String currentFragment;
    private static FragmentManager fragmentManager;

    public static Pair<Integer,String> currentFamily;
    public static Pair<String,String> currentCategory;
    public static List<ProcedureEntity> procedures;
    public static ProcedureEntity selectedProcedure;
    public static List<CropParcel> selectedCropParcels;
    public static List<GenericItem> selectedDrivers;

    public static Node<String> ontology;
    public static Map<String,List<Pair<String,String>>> families;
    public static Map<String,List<Pair<String,String>>> natures;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);
        ButterKnife.bind(this);

        // Get current account
        account = AccountTool.getCurrentAccount(this);

        // Set toolbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.close);
        }

        // Init interventions parameters
        selectedCropParcels = new ArrayList<>();
        selectedDrivers = new ArrayList<>();

        // Get fragment manager
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);

        // Set first fragment to procedure family choice
        replaceFragmentWith(PROCEDURE_FAMILY_FRAGMENT, null);

        // Load procedure natures, families and params from assets
        loadXMLAssets();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void replaceFragmentWith(String fragmentTag, String filter) {

        // Update current fragment reference
        currentFragment = fragmentTag;
        invalidateOptionsMenu();

//        if (BuildConfig.DEBUG)
//            Log.i(TAG, "Current Fragment = " + currentFragment);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment;

        switch (fragmentTag) {

            case PROCEDURE_CATEGORY_FRAGMENT:
            case PROCEDURE_NATURE_FRAGMENT:
                fragment = ProcedureChoiceFragment.newInstance(fragmentTag);
                break;

            case INTERVENTION_FORM:
                fragment = InterventionFormFragment.newInstance();
                break;

            case "plant":
            case "land_parcel":
            case "cultivation":
                fragment = CropParcelChoiceFragment.newInstance(fragmentTag, filter);
                break;

//            case INPUT_CHOICE_FRAGMENT:
//                fragment = InputChoiceFragment.newInstance();
//                break;

            case PROCEDURE_FAMILY_FRAGMENT:
                fragment = ProcedureFamilyChoiceFragment.newInstance();
                break;

            default:
                // Default to param choice fragment list (inputs, equipments, workers...)
                fragment = ParamChoiceFragment.newInstance(fragmentTag, filter);
                break;
        }

//        ft.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment_container, fragment, fragmentTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onProcedureFamilyInteraction(Integer id) {

        if (id.equals(ADMINISTERING.first))
            currentFamily = ADMINISTERING;
        else if (id.equals(ANIMAL_FARMING.first))
            currentFamily = ANIMAL_FARMING;
        else if (id.equals(PLANT_FARMING.first))
            currentFamily = PLANT_FARMING;
        else if (id.equals(PROCESSING.first))
            currentFamily = PROCESSING;
        else if (id.equals(TOOL_MAINTAINING.first))
            currentFamily = TOOL_MAINTAINING;

        replaceFragmentWith(PROCEDURE_CATEGORY_FRAGMENT, null);
    }

    public ProcedureEntity getProcedureItem(String name) {
        for (ProcedureEntity procedure : procedures)
            if (procedure.name.equals(name))
                return procedure;
        return null;
    }

    private void loadXMLAssets() {
        try {
            procedures = loadProcedures();
            families = loadFamilies();

            // Removes childless categories
            for (Map.Entry<String, List<Pair<String, String>>> family : families.entrySet()) {
                List<Pair<String, String>> categories = family.getValue();
                ListIterator<Pair<String, String>> it = categories.listIterator();
                while (it.hasNext()) {
                    if (!natures.keySet().contains(it.next().first))
                        it.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private List<ProcedureEntity> loadProcedures() throws IOException, XmlPullParserException {

        final List<ProcedureEntity> procedureList = new ArrayList<>();
        String[] files = getAssets().list("procedures");
        natures = new ArrayMap<>();

        if (files != null) {
            for (String asset : files) {
                ProceduresXMLReader proceduresXmlReader = new ProceduresXMLReader();
                InputStream is = getAssets().open("procedures/" + asset);
                ProcedureEntity procedureEntity = proceduresXmlReader.parse(is);
                procedureList.add(procedureEntity);

                // Fill procedure natures map
                String name = procedureEntity.name;
                int resId = getResources().getIdentifier(name, "string", Zero.getPkgName());
                Pair<String,String> procedureNaturePair = Pair.create(name, getString(resId));

                for (String category : procedureEntity.categories) {
                    if (natures.containsKey(category)) {
                        List<Pair<String, String>> proceduresList = natures.get(category);
                        if (proceduresList == null)
                            proceduresList = new ArrayList<>();
                        proceduresList.add(procedureNaturePair);
                        natures.put(category, proceduresList);
                    } else {
                        natures.put(category, new ArrayList<>(Collections.singletonList(procedureNaturePair)));
                    }
                }
            }
        }
        return procedureList;
    }

    private Map<String,List<Pair<String,String>>> loadFamilies() throws IOException, XmlPullParserException {
        InputStream is = getAssets().open("db.xml");
        ProcedureFamiliesXMLReader xmlReader = new ProcedureFamiliesXMLReader(getBaseContext());
        return xmlReader.parse(is);
    }

    @Override
    public void onFormFragmentInteraction(String fragmentTag, String filter) {
        replaceFragmentWith(fragmentTag, filter);
    }

    @Override
    public void onItemChoosed(Pair<String,String> item, String fragmentRef) {

        if (PROCEDURE_CATEGORY_FRAGMENT.equals(fragmentRef)) {
            if (BuildConfig.DEBUG) Log.e(TAG, "category = " + item);
            currentCategory = item;
            replaceFragmentWith(PROCEDURE_NATURE_FRAGMENT, null);
        } else {
            if (BuildConfig.DEBUG) Log.e(TAG, "procedure = " + item);
            selectedProcedure = getProcedureItem(item.first);
            replaceFragmentWith(INTERVENTION_FORM, null);
        }
    }

    /** *****************************************************************
     *                                                                  *
     *                      NAVIGATION MANAGEMENT                       *
     *                                                                  *
     ***************************************************************** **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateOptionMenu");
//        if (currentFragment.equals(INTERVENTION_FORM)) {
//            menu.clear();
//            menu.add()
////            MenuInflater inflater = getMenuInflater();
////            inflater.inflate(R.menu.intervention_options_menu, menu);
//        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.intervention_options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionMenu");
        switch (currentFragment) {
            case INTERVENTION_FORM:
                menu.findItem(R.id.action_inter_save).setVisible(true);
                break;
            case PARAM_FRAGMENT:
                menu.findItem(R.id.action_inter_done).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void previousFragmentOrQuit() {
        // Finish the activity if on first fragment else popBackStack()
        Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);
        if (f instanceof ProcedureFamilyChoiceFragment)
            finish();
        fragmentManager.popBackStack();
    }

    @Override
    public void onBackStackChanged() {
        Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onBackStackChanged");

        if (f instanceof InterventionFormFragment)
            currentFragment = INTERVENTION_FORM;
        else if (f instanceof ParamChoiceFragment)
            currentFragment = PARAM_FRAGMENT;

        invalidateOptionsMenu();

        // Change icon to close if on first fragment
        int backStack = fragmentManager.getBackStackEntryCount();
        if (backStack > 1 )
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        else
            actionBar.setHomeAsUpIndicator(R.drawable.close);

//        Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);

        // Force reset the option menu
//        invalidateOptionsMenu();
    }

    /**
     *      Back and Up navigation will do the same thing
     */
    @Override
    public boolean onSupportNavigateUp() {
        previousFragmentOrQuit();
        return true;
    }

    @Override
    public void onBackPressed() {
        previousFragmentOrQuit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_inter_save:
                if (currentFragment.equals(INTERVENTION_FORM)) {
                    saveIntervention();
                }
                break;

            case R.id.action_inter_done:
                previousFragmentOrQuit();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveAttribute(ContentResolver cr, long id, String role, GenericItem item) {
        ContentValues cv = new ContentValues();
        cv.put(DetailedInterventionAttributes.DETAILED_INTERVENTION_ID, id);
        cv.put(DetailedInterventionAttributes.REFERENCE_NAME, role);
        cv.put(DetailedInterventionAttributes.REFERENCE_ID, item.id);
        Uri plantUri = cr.insert(DetailedInterventionAttributes.CONTENT_URI, cv);
        if (BuildConfig.DEBUG && plantUri != null && plantUri.getLastPathSegment() != null)
            Log.i(TAG, role + " -> " + item.name + " (id=" + plantUri.getLastPathSegment() + ")");
    }

    private void saveIntervention() {

        ContentResolver cr = getContentResolver();

        // Create base intervention
        ContentValues cv = new ContentValues();
        cv.put(DetailedInterventions.PROCEDURE_NAME, selectedProcedure.name);
        cv.put(DetailedInterventions.CREATED_ON, new Date().getTime());
        cv.put(DetailedInterventions.USER, account.name);

        // Insert into database & get returning id
        Uri uri = cr.insert(DetailedInterventions.CONTENT_URI, cv);
        long interId = -1;
        if (uri != null && uri.getLastPathSegment() != null)
            interId = Long.valueOf(uri.getLastPathSegment());
        if (BuildConfig.DEBUG)
            Log.i(TAG, "Intervention id = " + interId);


        // Storing working time

        List<ContentValues> allParamsCv = new ArrayList<>();

        // Store parameters by reference_name
        for (GenericItem param : InterventionFormFragment.paramsList) {
            for (String refName : param.referenceName) {

                ContentValues paramCv = new ContentValues();
                paramCv.put(DetailedInterventionAttributes.DETAILED_INTERVENTION_ID, interId);
                paramCv.put(DetailedInterventionAttributes.REFERENCE_ID, param.id);
                paramCv.put(DetailedInterventionAttributes.REFERENCE_NAME, refName);

                if (param.type.equals("input")) {
                    paramCv.put(DetailedInterventionAttributes.QUANTITY_VALUE, param.quantity.toString());
                    paramCv.put(DetailedInterventionAttributes.QUANTITY_UNIT_NAME, param.unit);
                }

                allParamsCv.add(paramCv);
            }
        }

        // Insert into database & get returning id
        cr.bulkInsert(DetailedInterventionAttributes.CONTENT_URI, allParamsCv.toArray(new ContentValues[0]));

        // Close the activity
        finish();
//
//        String whereClause = DetailedInterventions._ID + "=?";
//        String[] args = new String[] {String.valueOf(interId)};
//
//        try (Cursor cursor = cr.query(
//                DetailedInterventions.CONTENT_URI,
//                DetailedInterventions.PROJECTION_ALL,
//                whereClause, args, null)) {
//
//            while (cursor != null && cursor.moveToNext())
//                Log.i(TAG, "L'enregistrement est bien en base");
//        }
//


//        // Trying sync query
//        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
//
//        // Get reference_name and reference_id of current parameter
//        String query = "SELECT "+DetailedInterventionAttributes.REFERENCE_ID+", "+DetailedInterventionAttributes.REFERENCE_NAME
//                +" FROM "+DetailedInterventionAttributes.TABLE_NAME+", "+ DetailedInterventions.TABLE_NAME
//                +" WHERE "+DetailedInterventionAttributes.DETAILED_INTERVENTION_ID+"=?";
//
//        try(Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(interId)})) {
//            while (cursor != null && cursor.moveToNext())
//                Log.e(TAG, "Raw query #" + cursor.getInt(0) + " " + cursor.getString(1));
//        }
    }
}
