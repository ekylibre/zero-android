package ekylibre.zero.inter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ekylibre.util.AccountTool;
import ekylibre.util.ProcedureFamiliesXMLReader;
import ekylibre.util.ProceduresXMLReader;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.CropParcelChoiceFragment;
import ekylibre.zero.inter.fragment.InterventionFormFragment;
import ekylibre.zero.inter.fragment.ProcedureChoiceFragment;
import ekylibre.zero.inter.fragment.ProcedureFamilyChoiceFragment;

import android.accounts.Account;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InterActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
        ProcedureFamilyChoiceFragment.OnFragmentInteractionListener,
        ProcedureChoiceFragment.OnFragmentInteractionListener,
                    InterventionFormFragment.OnFragmentInteractionListener {

    private static final String TAG = "NewInterventionActivity";

    public static final String PROCEDURE_FAMILY_FRAGMENT = "ekylibre.zero.fragments.procedure.family";
    public static final String PROCEDURE_CATEGORY_FRAGMENT = "ekylibre.zero.fragments.procedure.category";
    public static final String PROCEDURE_NATURE_FRAGMENT = "ekylibre.zero.fragments.procedure.nature";
    public static final String INTERVENTION_FORM = "ekylibre.zero.fragments.intervention.form";
    public static final String CROP_CHOICE_FRAGMENT = "ekylibre.zero.fragments.crop.choice";

    private static final Pair<Integer,String> ADMINISTERING = Pair.create(R.id.administering, "administering");
    private static final Pair<Integer,String> ANIMAL_FARMING = Pair.create(R.id.animal_farming, "animal_farming");
    private static final Pair<Integer,String> PLANT_FARMING = Pair.create(R.id.plant_farming, "plant_farming");
    private static final Pair<Integer,String> PROCESSING = Pair.create(R.id.processing, "processing");
    private static final Pair<Integer,String> TOOL_MAINTAINING = Pair.create(R.id.tool_maintaining, "tool_maintaining");

    private Account account;
    public static ActionBar actionBar;
    private String currentFragment;
    private static FragmentManager fragmentManager;

    public static Pair<Integer,String> currentFamily;
    public static Pair<String,String> currentCategory;
    public static List<ProcedureEntity> procedures;
    public static ProcedureEntity selectedProcedure;
    public static Map<String,List<Pair<String,String>>> families;
    public static Map<String,List<Pair<String,String>>> natures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);

        // Get current account
        account = AccountTool.getCurrentAccount(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
//        currentFragment = PROCEDURE_FAMILY_FRAGMENT;
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.close);
        }

        // Get fragment manager
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);


        // Set first fragment
        replaceFragmentWith(PROCEDURE_FAMILY_FRAGMENT);

        // Load procedure logic from XML assets
        try {
            procedures = loadProcedures();
            families = loadFamilies();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    void replaceFragmentWith(String fragmentTag) {

        // Update current fragment reference
        currentFragment = fragmentTag;

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

            // Default to Procedure Family fragment
            default:
                fragment = ProcedureFamilyChoiceFragment.newInstance();
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

        replaceFragmentWith(PROCEDURE_CATEGORY_FRAGMENT);

//            ProceduresXMLReader proceduresXmlReader = new ProceduresXMLReader();
//            try {
//                InputStream is = getAssets().open("procedures/all_in_one_sowing.xml");
//                ProcedureEntity procedureEntity = proceduresXmlReader.parse(is);
//                Log.e("InterActivity", procedureEntity.toString());
//
////                is = getAssets().open("db.xml");
////                ProcedureFamiliesXMLReader famReader = new ProcedureFamiliesXMLReader();
////                String bla = famReader.parse(is);
////                Log.e("Families", bla);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (XmlPullParserException e) {
//                e.printStackTrace();
//            }

    }

    @Override
    public void onItemChoosed(Pair<String,String> item, String fragmentRef) {

        if (BuildConfig.DEBUG)
            Log.e(TAG, "ItemChosed and current fragment is = " + fragmentRef);

        switch (fragmentRef) {
            case PROCEDURE_CATEGORY_FRAGMENT:
                currentCategory = item;
                replaceFragmentWith(PROCEDURE_NATURE_FRAGMENT);
                break;
            case PROCEDURE_NATURE_FRAGMENT:
                Log.e(TAG, item.second);
                selectedProcedure = getProcedureItem(item.first);
                replaceFragmentWith(INTERVENTION_FORM);
                break;
        }

    }

    public ProcedureEntity getProcedureItem(String name) {
        for (ProcedureEntity procedure : procedures)
            if (procedure.name.equals(name))
                return procedure;
        return null;
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
    public void onBackStackChanged() {

        int backStack = fragmentManager.getBackStackEntryCount();

        if (backStack > 1 ) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.close);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        previousFragmentOrQuit();
        return true;
    }

    @Override
    public void onBackPressed() {
        previousFragmentOrQuit();
    }

    private void previousFragmentOrQuit() {
        Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);
        if (f instanceof ProcedureFamilyChoiceFragment)
            finish();
        else
            fragmentManager.popBackStack();
    }

    @Override
    public void onCropChoice() {
        replaceFragmentWith(CROP_CHOICE_FRAGMENT);
    }
}
