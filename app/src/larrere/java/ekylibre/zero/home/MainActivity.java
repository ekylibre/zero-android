package ekylibre.zero.home;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ekylibre.database.ZeroContract;
import ekylibre.service.ConnectionManagerService;
import ekylibre.service.GeneralHandler;
import ekylibre.util.AccountTool;
import ekylibre.util.CSVReader;
import ekylibre.util.Contact;
import ekylibre.util.PermissionManager;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.IssueActivity;
import ekylibre.zero.ObservationActivity;
import ekylibre.zero.PlantCountingActivity;
import ekylibre.zero.R;
import ekylibre.zero.SettingsActivity;
import ekylibre.zero.account.AccountManagerActivity;
import ekylibre.zero.intervention.InterventionActivity;

/**************************************
 * Created by pierre on 7/12/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/


/*
** The home menu for the app
** There is a slide menu if you slide left including :
** - Information activity
** - Some management activity
**
** You can access all actions activity from the floating button
**
** //!\\ WARNING //!\\ You must block all user's actions during sync by using the boolean isSync of super class
**                 __Example__ : private void foo()
**                               {
**                                    if (super.isSync)
**                                       return ;
**                                    // do what you want
**                               }
*/
public class MainActivity extends UpdatableActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private Account         mAccount;
    private TextView        mNav_account;
    private TextView        mNav_instance;
    private NavigationView  mNavigationView;
    private Toolbar         mToolbar;
    private ProgressBar     mPrgressBar;
    private boolean         firstPass = true;
    private final String    TAG = "MainActivity";
    private TodoListActivity todoListActivity;
    private Calendar        syncTime = Calendar.getInstance();
    private DrawerLayout    mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Planning");
        PermissionManager.multiplePermissions(this, this);

        if (!AccountTool.isAnyAccountExist(this))
        {
            AccountTool.askForAccount(this, this);
            return;
        }
        mAccount = AccountTool.getCurrentAccount(MainActivity.this);
        super.setToolBar();

        setFloatingActBtn();

        setDrawerLayout();
        startConnectionManager();
        startGeneralHandler();
        View headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        mNav_account = (TextView)headerLayout.findViewById(R.id.nav_accountName);
        mNav_instance = (TextView)headerLayout.findViewById(R.id.nav_farmURL);
        mPrgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        sync_data();
        firstPass = false;

        // Load CSV if not already done
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        if (!prefs.getBoolean("csv_loaded", false)) {

            Log.e(TAG, "Loading CSV ...");
//                for (int i = 0; i < rows.size(); i++) {
//                    Log.d(Constants.TAG, String.format("row %s: %s, %s", i, rows.get(i)[0], rows.get(i)[1]));
//                }
            ContentResolver cr = getContentResolver();
            CSVReader csvReader = new CSVReader(this, "incidents.csv");
            List<String[]> rows = null;
            try {
                rows = csvReader.readCSV();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (rows != null) {
                ContentValues[] contentValues = new ContentValues[rows.size()];
                for (int i = 0; i < rows.size(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(ZeroContract.IssueNatures.CATEGORY, rows.get(i)[0]);
                    cv.put(ZeroContract.IssueNatures.LABEL, rows.get(i)[1]);
                    cv.put(ZeroContract.IssueNatures.NATURE, rows.get(i)[2]);
                    contentValues[i] = cv;
                }
                cr.bulkInsert(ZeroContract.IssueNatures.CONTENT_URI, contentValues);
            }

            csvReader = new CSVReader(this, "stades-vegetatifs.csv");
            rows = null;
            try {
                rows = csvReader.readCSV();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (rows != null) {
                ContentValues[] contentValues = new ContentValues[rows.size()];
                for (int i = 0; i < rows.size(); i++) {
                    ContentValues cv = new ContentValues();
                    cv.put(ZeroContract.VegetalScale.REFERENCE, rows.get(i)[0]);
                    cv.put(ZeroContract.VegetalScale.LABEL, rows.get(i)[1]);
                    cv.put(ZeroContract.VegetalScale.VARIETY, rows.get(i)[2]);
                    cv.put(ZeroContract.VegetalScale.POSITION, rows.get(i)[3]);
                    contentValues[i] = cv;
                }
                cr.bulkInsert(ZeroContract.VegetalScale.CONTENT_URI, contentValues);
            }
        }
    }

    /*
    ** Callback call when the activity is visible / revisible
    ** Use to set / reset the header of the drawer layout
    */
    @Override
    public void onStart()
    {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
        {
            AccountTool.askForAccount(this, this);
            return;
        }
        mAccount = AccountTool.getCurrentAccount(MainActivity.this);
        setAccountName(mNavigationView);
        setTodolist();
        sync_data();
    }

    /*
    ** Start service which auto sync when internet is detected
    */
    private void    startConnectionManager()
    {
        Intent connectIntent = new Intent(MainActivity.this, ConnectionManagerService.class);
        connectIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mAccount.name);
        startService(connectIntent);
    }

    /*
    ** Start service which call function every 5 minutes to do some useful stuff
    */
    private void    startGeneralHandler()
    {
        if (PermissionManager.vibrationPermissions(this, this))
        {
            Intent connectIntent = new Intent(MainActivity.this, GeneralHandler.class);
            startService(connectIntent);
        }
    }

    /*
    ** Setting the navigation view on drawer layout
    ** This is the slide menu on the left
    */
    private void    setDrawerLayout()
    {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mNavigationView = navigationView;
    }

    /*
    ** Setting the floating button which is use to access to
    ** different type of action buttons
    */
    private void    setFloatingActBtn()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchIntervention(null);
            }
        });
    }

    /*
    ** Get and set name and instance of the actual account setting appropriate private variable
    */
    private void    setAccountName(NavigationView navigationView)
    {
        mAccount = AccountTool.getCurrentAccount(this);
        mNav_instance.setText(AccountTool.getAccountInstance(mAccount, this));
        mNav_account.setText(AccountTool.getAccountName(mAccount, this));
    }

    /*
    ** The main content of the activity which is set on TodoListActivity
    ** This list will sync your phone and Ekylibre calendar to show all
    ** tasks of the current day
    */
    private void    setTodolist()
    {
        todoListActivity = new TodoListActivity(MainActivity.this, this,
                (ListView)findViewById(R.id.listView));
    }

    /*
    ** Close slide menu on back press
    */
    @Override
    public void onBackPressed()
    {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
        {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    /*
    ** Set toolbar options
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
    ** Actions on toolbar items
    ** Items are identified by their view id
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int     id = item.getItemId();
        Intent  intent;

        if (super.isSync)
            return (true);
        if (id == R.id.action_settings)
        {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return (true);
        }
        else if (id == android.R.id.home)
        {
            mDrawer.openDrawer(GravityCompat.START);
        }
        return (super.onOptionsItemSelected(item));
    }

    /*
    ** Actions on slide menu
    ** Items are identified by their view id on menu file
    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        if (super.isSync)
            return (false);
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_tracking :
            {
                launchIntervention(null);
                break;
            }
            case R.id.nav_observation:
            {
                Intent intent = new Intent(this, ObservationActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_issue :
            {
                Intent intent = new Intent(this, IssueActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_counting :
            {
                Intent intent = new Intent(this, PlantCountingActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings :
            {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_account :
            {
                Intent intent = new Intent(this, AccountManagerActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_sync :
            {
                forceSync_data();
                break;
            }
            default:
            {
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return (false);
    }

    private void sync_contact()
    {
        if (!PermissionManager.writeContactPermissions(this, this))
            return;
        Contact contact = new Contact(this);
        contact.setAccount(AccountTool.getCurrentAccount(this));
        contact.setName("Je", "TEST");
        contact.setEmail("email@ekylibre.org");
        contact.setEmail("email@ekylibre.com");
        contact.setHomeNumber("1337");
        contact.setHomeNumber("1338");
        contact.setMobileNumber("0612345678");
        contact.setMobileNumber("0612345679");
        contact.setOrganization("Ekylibre", "Developer");
        contact.setWebsite("www.test.com");
        contact.setWebsite("www.qkzdjlkzqjd.com");
        contact.setMail("Pierre Bougon", "33600", "PESSAC", "fr");
        contact.setMail("2", "33600", "PESSAC", "fr");



        contact.commit();
        contact.clear();
    }

    public void launchIntervention(View v)
    {
        if (super.isSync)
            return ;
        Intent intent = new Intent(this, InterventionActivity.class);
        intent.putExtra(InterventionActivity.NEW, true);
        startActivity(intent);
    }

    /*
    ** This method will sync the data if it's needed
    ** You can call this every where since the maximum sync for this is
    ** 1 every TIME_TO_NEXT_SYNC
    */
    private void    sync_data()
    {
        if (mAccount == null || (syncedInLastMinutes() && !firstPass))
            return ;
        Log.d("zero", "syncData: " + mAccount.toString() + ", " + ZeroContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, ZeroContract.AUTHORITY, extras);
    }

    /*
    ** Force sync
    ** If you need to be sure that data will be sync use this method
    ** Do not abuse of this method because sync will block every actions from users
    */
    private void    forceSync_data()
    {
        if (mAccount == null)
            return ;
        Log.d("zero", "syncData: " + mAccount.toString() + ", " + ZeroContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, ZeroContract.AUTHORITY, extras);
        syncTime.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
    }

    /*
    ** Return true if there is no sync since TIME_TO_NEXT_SYNC
    */
    private boolean syncedInLastMinutes()
    {
        Calendar currentTime = Calendar.getInstance();
        if (syncTime.getTimeInMillis() + ConnectionManagerService.TIME_TO_NEXT_SYNC < currentTime.getTimeInMillis())
        {
            syncTime.setTimeInMillis(currentTime.getTimeInMillis());
            return (false);
        }
        return (true);
    }

    /*
    ** onClick method which is call when click on header of drawer layout is detected
    ** actually redirecting to the account manager
    */
    public void launchAccountManagerActivity(View v)
    {
        Intent intent = new Intent(this, AccountManagerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSyncFinish()
    {
        todoListActivity.onSyncFinish();
        setTodolist();
        mPrgressBar.setVisibility(View.GONE);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.data_synced, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onSyncStart()
    {
        todoListActivity.onSyncStart();
        mPrgressBar.setVisibility(View.VISIBLE);
        mPrgressBar.setScaleX((float) 0.15);
        mPrgressBar.setScaleY((float) 0.15);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }

}