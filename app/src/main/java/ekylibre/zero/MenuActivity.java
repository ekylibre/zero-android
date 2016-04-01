package ekylibre.zero;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ekylibre.zero.provider.IssueContract;
import ekylibre.zero.provider.TrackingContract;

public class MenuActivity extends Activity {

    private Account mAccount;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        // Get simple account or ask for it if necessary
        final AccountManager manager = AccountManager.get(this);
        final Account[] accounts = manager.getAccountsByType(IssueSyncAdapter.ACCOUNT_TYPE);
        if (accounts.length <= 0) {
            Intent intent = new Intent(this, AuthenticatorActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, IssueSyncAdapter.ACCOUNT_TYPE);
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


        setContentView(R.layout.activity_menu);
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


    public void GotoTracking(View v){
        Intent intent = new Intent(this,TrackingActivity.class);
        startActivity(intent);

    }public void GotoNewIssue(View v){
        Intent intent = new Intent(this,IssueActivity.class);
        startActivity(intent);

    }public void GotoConsultIssue(View v){
        Intent intent = new Intent(this,IssuesActivity.class);
        startActivity(intent);
    }

    public void synchAll(View v){
        Log.d("zero", "syncData: " + mAccount.toString() + ", " + IssueContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(mAccount, IssueContract.AUTHORITY, extras);
        ContentResolver.requestSync(mAccount, TrackingContract.AUTHORITY, extras);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.data_synced, Toast.LENGTH_SHORT);
        toast.show();
    }

}
