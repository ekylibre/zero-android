package ekylibre.zero.inter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ekylibre.util.AccountTool;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.ProcedureChoice1Fragment;

import android.accounts.Account;
import android.net.Uri;
import android.os.Bundle;

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
    public void onFragmentInteraction(Uri uri) {

    }
}
