package ekylibre.zero.inter;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.database.ZeroContract.GroupZones;
import ekylibre.database.ZeroContract.DetailedInterventionAttributes;
import ekylibre.database.ZeroContract.DetailedInterventions;
import ekylibre.database.ZeroContract.WorkingPeriodAttributes;
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
import ekylibre.zero.inter.fragment.SimpleChoiceFragment;
import ekylibre.zero.inter.model.CropParcel;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.Period;
import ekylibre.zero.inter.model.Zone;


public class InterActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        ProcedureFamilyChoiceFragment.OnFragmentInteractionListener,
        ProcedureChoiceFragment.OnFragmentInteractionListener,
        InterventionFormFragment.OnFragmentInteractionListener,
        SimpleChoiceFragment.OnFragmentInteractionListener {

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
        replaceFragmentWith(PROCEDURE_FAMILY_FRAGMENT, null, null);

        // Load procedure natures, families and params from assets
        loadXMLAssets();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void replaceFragmentWith(String fragmentTag, String filter, String role) {

        if (BuildConfig.DEBUG && role != null)
            Log.i(TAG, "Role -> " + role);

        // Update current fragment reference
        currentFragment = fragmentTag;
        invalidateOptionsMenu();

//        if (BuildConfig.DEBUG)
//            Log.i(TAG, "Current Fragment = " + currentFragment);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment;

        switch (fragmentTag) {

            case PROCEDURE_FAMILY_FRAGMENT:
                fragment = ProcedureFamilyChoiceFragment.newInstance();
                break;

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

                // TODO -> manage zone group
            case "zone_plant":
            case "zone_land_parcel":
                // Role = zoneId here ton keep ref to current zone edition
                // Zone item position passed with role variable
                Log.i(TAG, "SimpleChoiceFragment.newInstance("+fragmentTag+", "+filter+", "+role+");");
                fragment = SimpleChoiceFragment.newInstance(fragmentTag, filter, role);
                break;

            default:
                // Default to param choice fragment list (inputs, equipments, workers...)
                fragment = ParamChoiceFragment.newInstance(fragmentTag, filter, role);
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

        replaceFragmentWith(PROCEDURE_CATEGORY_FRAGMENT, null, null);
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
    public void onFormFragmentInteraction(String fragmentTag, String filter, String role) {
        replaceFragmentWith(fragmentTag, filter, role);
    }

    @Override
    public void onItemChoosed(Pair<String,String> item, String fragmentRef) {

        if (PROCEDURE_CATEGORY_FRAGMENT.equals(fragmentRef)) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "Category -> " + item);
            currentCategory = item;
            replaceFragmentWith(PROCEDURE_NATURE_FRAGMENT, null, null);
        } else {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "Procedure -> " + item);
            selectedProcedure = getProcedureItem(item.first);
            replaceFragmentWith(INTERVENTION_FORM, null, null);
        }
    }

    @Override
    public void onItemChoosed() {
        previousFragmentOrQuit();
    }

    /** *****************************************************************
     *                                                                  *
     *                      NAVIGATION MANAGEMENT                       *
     *                                                                  *
     ***************************************************************** **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        if (BuildConfig.DEBUG) Log.v(TAG, "onCreateOptionMenu");
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
        Log.v(TAG, "onPrepareOptionMenu");
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

        if (f instanceof InterventionFormFragment)
            currentFragment = INTERVENTION_FORM;
        else if (f instanceof ParamChoiceFragment)
            currentFragment = PARAM_FRAGMENT;
        else if (f instanceof ProcedureChoiceFragment)
            currentFragment = PROCEDURE_NATURE_FRAGMENT;

        if (BuildConfig.DEBUG)
            Log.v(TAG, "onBackStackChanged - current fragment is -> " + currentFragment);



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

    /**
     * Method used to verify and save intervention
     */
    private void saveIntervention() {

        // test
        for(Zone zone : InterventionFormFragment.zoneList) {
            Log.i(TAG, "---");
            Log.e(TAG, "zone " + zone.landParcel + " " + zone.plant + " " + zone.newName);
        }

        for (GenericItem item : InterventionFormFragment.paramsList) {
            if (item.referenceName.containsValue("targets")) {
                Log.i(TAG, "---");
                Log.e(TAG, "item " + item);
            }
        }

        // Do some validation before saving

        boolean valid = true;
        int counter;
        BigDecimal zero = new BigDecimal("0");

        periodLoop: for (Period period1 : InterventionFormFragment.periodList)
            for (Period period2 : InterventionFormFragment.periodList)
                if (period2 != period1 && overlaps(period1, period2)) {
                    valid = false;
                    displayWarningDialog(this, "Attention, des periodes de travail se chevauchent");
                    break periodLoop;
                }

        // Check if one culture/parcel is set
        if (valid && selectedProcedure.target.size() > 0) {
            counter = 0;
            for (GenericItem input : InterventionFormFragment.paramsList) {
                if (input.referenceName.containsValue("targets")) {
                    ++counter;
                    break;
                }
            }
            // Check the is one input at least
            if (counter == 0) {
                valid = false;
                displayWarningDialog(this, "Vous devez renseigner au moins une culture/parcelle");
            }
        }

        // Check if input needed and a quantity is set
        if (valid && selectedProcedure.input.size() > 0) {
            counter = 0;
            for (GenericItem input : InterventionFormFragment.paramsList) {
                if (input.referenceName.containsValue("inputs")) {
                    ++counter;
                    if (input.quantity == null || input.quantity.compareTo(zero) <= 0) {
                        valid = false;
                        displayWarningDialog(this, "Il faut renseigner une quantité pour l'intrant");
                        break;
                    }
                }
            }
            // Check the is one input at least
            if (counter == 0) {
                valid = false;
                displayWarningDialog(this, "Vous devez renseigner au moins un intrant");
            }
        }

        if (valid) {
            // Get content resolver instance
            ContentResolver cr = getContentResolver();

            // -------------------------- //
            // Creating base intervention //
            // -------------------------- //
            ContentValues cv = new ContentValues();
            cv.put(DetailedInterventions.PROCEDURE_NAME, selectedProcedure.name);
            cv.put(DetailedInterventions.CREATED_ON, new Date().getTime());
            cv.put(DetailedInterventions.USER, account.name);

            // Insert into database & get returning id
            Uri uri = cr.insert(DetailedInterventions.CONTENT_URI, cv);
            long interId = ContentUris.parseId(uri);
//            long interId = -1;
//            if (uri != null && uri.getLastPathSegment() != null)
//                interId = Long.valueOf(uri.getLastPathSegment());
            if (BuildConfig.DEBUG)
                Log.i(TAG, "Intervention id = " + interId);


            // ------------ //
            // Working time //
            // ------------ //

            List<ContentValues> bulkPeriodCv = new ArrayList<>();

            for (Period period : InterventionFormFragment.periodList) {

                ContentValues periodCv = new ContentValues();
                periodCv.put(WorkingPeriodAttributes.DETAILED_INTERVENTION_ID, interId);
                periodCv.put(WorkingPeriodAttributes.STARTED_AT, period.startDateTime.getTime());
                periodCv.put(WorkingPeriodAttributes.STOPPED_AT, period.stopDateTime.getTime());
                bulkPeriodCv.add(periodCv);
            }
            cr.bulkInsert(WorkingPeriodAttributes.CONTENT_URI, bulkPeriodCv.toArray(new ContentValues[0]));


            // ---------------- //
            // Zones parameters //
            // ---------------- //

            for (Zone zone : InterventionFormFragment.zoneList) {

                // Save new zone
                ContentValues zoneCv = new ContentValues();
                zoneCv.put(GroupZones.DETAILED_INTERVENTION_ID, interId);
//                zoneCv.put(GroupZones.TARGET_ID, targetId);
//                zoneCv.put(GroupZones.OUTPUT_ID, outputId);
                zoneCv.put(GroupZones.NEW_NAME, zone.newName);
                zoneCv.put(GroupZones.BATCH_NUMBER, zone.batchNumber);

                // Insert into database & get returning id
                Uri zoneUri = cr.insert(GroupZones.CONTENT_URI, zoneCv);
                long zoneId = ContentUris.parseId(zoneUri);

                // Save target and get id back
                Uri targetUri = cr.insert(DetailedInterventionAttributes.CONTENT_URI,
                        getContentValue(interId, zone.landParcel, "land_parcel", "targets", zoneId));
                long targetId = ContentUris.parseId(targetUri);

                // Save output and get id back
                Uri outputUri = cr.insert(DetailedInterventionAttributes.CONTENT_URI,
                        getContentValue(interId, zone.plant, "plant", "outputs", zoneId));
                long outputId = ContentUris.parseId(outputUri);

                // Updates current zone with created ids
                zoneCv.put(GroupZones.TARGET_ID, targetId);
                zoneCv.put(GroupZones.OUTPUT_ID, outputId);

                cr.update(zoneUri, zoneCv, null, null);
            }



            // ----------------------- //
            // Intervention parameters //
            // ----------------------- //

            List<ContentValues> bulkParamCv = new ArrayList<>();

            for (GenericItem param : InterventionFormFragment.paramsList)
                for (Map.Entry<String,String> entry : param.referenceName.entrySet())
                    // Do not save zones parameters here
                    if (!entry.getKey().equals("zone"))
                        bulkParamCv.add(getContentValue(interId, param, entry.getKey(), entry.getValue(), null));

            // Insert into database & get returning id
            cr.bulkInsert(DetailedInterventionAttributes.CONTENT_URI, bulkParamCv.toArray(new ContentValues[0]));


            // ------ //
            // Finish //
            // ------ //

            Toast.makeText(this, "L'intervention est enregistrée", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private ContentValues getContentValue(long interId, GenericItem param, String refName, String role, Long groupId) {
        ContentValues paramCv = new ContentValues();
        paramCv.put(DetailedInterventionAttributes.DETAILED_INTERVENTION_ID, interId);
        paramCv.put(DetailedInterventionAttributes.REFERENCE_ID, param.id);
        paramCv.put(DetailedInterventionAttributes.REFERENCE_NAME, refName);
        paramCv.put(DetailedInterventionAttributes.ROLE, role);

        if (groupId != null)
            paramCv.put(DetailedInterventionAttributes.GROUP_ID, groupId);

        if (param.quantity != null) {
            paramCv.put(DetailedInterventionAttributes.QUANTITY_VALUE, param.quantity.toString());
            paramCv.put(DetailedInterventionAttributes.QUANTITY_UNIT_NAME, param.unit);
        }
        return paramCv;
    }

    private boolean overlaps(Period period1, Period period2){
        return period1.startDateTime.getTime() <= period2.stopDateTime.getTime()
                && period2.startDateTime.getTime() <= period1.stopDateTime.getTime();
    }

    private void displayWarningDialog(Context context, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text);
        builder.setPositiveButton("Ok", (dialog, i) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
