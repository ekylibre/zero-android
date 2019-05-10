package ekylibre.zero.inter;

import android.accounts.Account;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.util.AccountTool;
import ekylibre.util.ProcedureFamiliesXMLReader;
import ekylibre.util.ProceduresXMLReader;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.util.query_language.QL.ParseError;
import ekylibre.util.query_language.QL.QL;
import ekylibre.util.query_language.QL.TreeNode;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
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
    public static final String CROP_CHOICE_FRAGMENT = "ekylibre.zero.fragments.crop.choice";
    public static final String INPUT_CHOICE_FRAGMENT = "ekylibre.zero.fragments.input.choice";

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

        // Ekylibre DSL testing (canopy)
//        try {
//            TreeNode tree = QL.parse("can tow(equipment) and can move");
//            for (TreeNode node : tree.elements)
//                Log.e(TAG, node.text);
//        } catch (ParseError parseError) {
//            parseError.printStackTrace();
//        }
    }

    void replaceFragmentWith(String fragmentTag, String filter) {

        // Update current fragment reference
        currentFragment = fragmentTag;
        invalidateOptionsMenu();

        if (BuildConfig.DEBUG)
            Log.i(TAG, "Current Fragment = " + currentFragment);

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

            case CROP_CHOICE_FRAGMENT:
                fragment = CropParcelChoiceFragment.newInstance();
                break;

//            case INPUT_CHOICE_FRAGMENT:
//                fragment = InputChoiceFragment.newInstance();
//                break;

            case PROCEDURE_FAMILY_FRAGMENT:
                fragment = ProcedureFamilyChoiceFragment.newInstance();
                break;

            default:
                // Default to Procedure Family fragment
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

                String category = procedureEntity.categories;
                String name = procedureEntity.name;
                int resId = getResources().getIdentifier(name, "string", getPackageName());
                Pair<String,String> procedureNaturePair = Pair.create(name, getString(resId));

                if (natures.containsKey(category)) {
                    List<Pair<String,String>> proceduresList = natures.get(category);
                    if (proceduresList == null)
                        proceduresList = new ArrayList<>();
                    proceduresList.add(procedureNaturePair);
                    natures.put(category, proceduresList);
                } else {
                    natures.put(category, new ArrayList<>(Collections.singletonList(procedureNaturePair)));
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
            currentCategory = item;
            replaceFragmentWith(PROCEDURE_NATURE_FRAGMENT, null);
        } else {
            // case PROCEDURE_NATURE_FRAGMENT:
            if (BuildConfig.DEBUG)
                Log.e(TAG, "Procedure = " + item.second);
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
        Log.i(TAG, "onCreateOptionMenu");
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
        Log.i(TAG, "onPrepareOptionMenu");
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
        Log.i(TAG, "onBackStackChanged" + f.toString());

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
                break;
            case R.id.action_inter_done:
                previousFragmentOrQuit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
