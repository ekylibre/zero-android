package ekylibre.zero;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AccountManagerActivity extends AppCompatActivity
{
    private Account[]               listAccount;
    private String                  TAG = "AccountManager";
    private AccountAdapter          accountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        ListView accountListView = (ListView)findViewById(R.id.account_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listAccount = AccountManager.get(this).getAccountsByType(SyncAdapter.ACCOUNT_TYPE);

        accountAdapter = new AccountAdapter(this, listAccount);
        accountListView.setAdapter(accountAdapter);
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