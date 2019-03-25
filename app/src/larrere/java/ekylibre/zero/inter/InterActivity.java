package ekylibre.zero.inter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ekylibre.util.AccountTool;
import ekylibre.util.ProceduresXMLReader;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.ProcedureChoice1Fragment;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class InterActivity extends AppCompatActivity
        implements ProcedureChoice1Fragment.OnFragmentInteractionListener {

    public static final String CHOICE_ONE_FRAGMENT = "ekylibre.zero.fragments.choice_one";
    public static final String CHOICE_TWO_FRAGMENT = "ekylibre.zero.fragments.choice_two";

    private Account account;
    private ActionBar actionBar;
    private String currentFragment;

    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);

        account = AccountTool.getCurrentAccount(this);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.close);
        }

        // Get fragment manager
        fragmentManager = getSupportFragmentManager();

        // Set first fragment
        currentFragment = CHOICE_ONE_FRAGMENT;
        replaceFragmentWith(currentFragment);

    }

    void replaceFragmentWith(String fragmentTag) {

        // Update current fragment reference
        currentFragment = fragmentTag;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment;

        switch (fragmentTag) {

            case CHOICE_ONE_FRAGMENT:
                actionBar.setTitle("Nouvelle Intervention");
                fragment = ProcedureChoice1Fragment.newInstance();
                break;

            default:
                actionBar.setTitle("Nouvelle Intervention");
                fragment = ProcedureChoice1Fragment.newInstance();
                break;

        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.setCustomAnimations(R.anim.exit_to_left, R.anim.enter_from_right);
        ft.replace(R.id.fragment_container, fragment, fragmentTag);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onFragmentInteraction(int id) {

        switch (id) {
            case R.id.administering:
                ProceduresXMLReader proceduresXmlReader = new ProceduresXMLReader();
                try {
                    InputStream is = getAssets().open("procedures/all_in_one_sowing.xml");
                    ProcedureEntity procedureEntity = proceduresXmlReader.parse(is);
                    Log.e("InterActivity", procedureEntity.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

        }

    }
}