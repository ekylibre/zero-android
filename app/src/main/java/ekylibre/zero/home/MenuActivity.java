package ekylibre.zero.home;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import ekylibre.api.ZeroContract;
import ekylibre.zero.AuthenticatorActivity;
import ekylibre.zero.CalendarActivity;
import ekylibre.zero.IssueActivity;
import ekylibre.zero.PlantCountingActivity;
import ekylibre.zero.R;
import ekylibre.zero.SettingsActivity;
import ekylibre.zero.SyncAdapter;
import ekylibre.zero.tracking.TrackingActivity;
import ekylibre.service.ConnectionManagerService;

public class MenuActivity extends ActionBarActivity
{

    private Account                 mAccount;
    private ListView                menuList;
    private DrawerLayout            drawerLayout;
    private ActionBarDrawerToggle   menuToggle;
    private String                  mActivityTitle;
    private final String            menuTitle = "MENU SLIBAR";


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        // Get simple account or ask for it if necessary
        final AccountManager manager = AccountManager.get(this);
        final Account[] accounts = manager.getAccountsByType(SyncAdapter.ACCOUNT_TYPE);
        if (accounts.length <= 0) {
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, SyncAdapter.ACCOUNT_TYPE);
            intent.putExtra(AuthenticatorActivity.KEY_REDIRECT, AuthenticatorActivity.CHOICE_REDIRECT_TRACKING);
            startActivity(intent);
            finish();
            return;
        } else if (accounts.length > 1) {
            // TODO: Propose the list of account or get the one by simple
            mAccount = accounts[0];
        } else {
            mAccount = accounts[0];
        }

        Intent connectIntent = new Intent(MenuActivity.this, ConnectionManagerService.class);
        connectIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mAccount.name);
        startService(connectIntent);



        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        menuList = (ListView)findViewById(R.id.menuList);
        String[] test = new String[]{"test1", "test2", "test3", "test4", "test5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MenuActivity.this,
                android.R.layout.simple_list_item_1, test);
        menuList.setAdapter(adapter);



        menuToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(menuToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (menuToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            // case R.id.action_search:
            //     // openSearch();
            //     return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void gotoNewIntervention(View v) {
        Intent intent = new Intent(this,TrackingActivity.class);
        startActivity(intent);

    }

    public void gotoNewIssue(View v) {
        Intent intent = new Intent(this,IssueActivity.class);
        startActivity(intent);

    }

    public void gotoSorting(View v) {
        Intent intent = new Intent(this,PlantCountingActivity.class);
        startActivity(intent);
    }

    public void gotoCalendar(View v) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void gotoTodolist(View v) {
        Intent intent = new Intent(this, TodoListActivity.class);
        startActivity(intent);
    }

    public void syncAll(View v) {
        Log.d("zero", "syncData: " + mAccount.toString() + ", " + ZeroContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccount, ZeroContract.AUTHORITY, extras);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.data_synced, Toast.LENGTH_SHORT);
        toast.show();
    }

}
