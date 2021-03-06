package ekylibre.zero.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ekylibre.database.ZeroContract;
import ekylibre.util.UpdatableActivity;
import ekylibre.zero.AuthenticatorActivity;
import ekylibre.zero.R;
import ekylibre.APICaller.SyncAdapter;
import ekylibre.util.AccountTool;

public class AccountManagerActivity extends UpdatableActivity
{
    private ArrayList<Account> listAccount;
    private String                  TAG = "AccountManager";
    private AccountAdapter accountAdapter;
    public final static String      CURRENT_ACCOUNT_NAME = "Current account";
    public final static String      CURRENT_ACCOUNT_INSTANCE = "Current instance";

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        super.setToolBar();
        setTitle(R.string.accountManager);

        ListView accountListView = (ListView)findViewById(R.id.account_list);
        setAccountList(accountListView);
    }

    /*
    ** Set the listView with ekylibre accounts
    ** Click on account make it the current account
    */
    private void setAccountList(ListView accountListView)
    {
        Account[] arrayAccount = AccountManager.get(this).getAccountsByType(SyncAdapter
                .ACCOUNT_TYPE);

        listAccount = new ArrayList<>(Arrays.asList(arrayAccount));
        accountAdapter = new AccountAdapter(this, listAccount, this);
        accountListView.setAdapter(accountAdapter);
        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Account newCurrAccount;
                AccountManager accManager = AccountManager.get(AccountManagerActivity.this);

                newCurrAccount = (Account)parent.getItemAtPosition(position);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AccountManagerActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(CURRENT_ACCOUNT_NAME, newCurrAccount.name);
                editor.commit();
                Log.d(TAG, "New current account is ==> " + newCurrAccount.name);
                syncAll(newCurrAccount);
                finish();
            }
        });
    }

    protected void syncAll(Account account)
    {
        if (account == null)
            return ;
        Log.d("zero", "syncData: " + account.toString() + ", " + ZeroContract.AUTHORITY);
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(account, ZeroContract.AUTHORITY, extras);
        Toast toast = Toast.makeText(getApplicationContext(), R.string.data_synced, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void    addAccount(View v)
    {
        Intent intent = new Intent(this, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, SyncAdapter.ACCOUNT_TYPE);
        intent.putExtra(AuthenticatorActivity.KEY_REDIRECT, AuthenticatorActivity.CHOICE_REDIRECT_TRACKING);
        startActivity(intent);
        finish();
    }

}