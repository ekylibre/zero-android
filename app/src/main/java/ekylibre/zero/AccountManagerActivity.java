package ekylibre.zero;

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
    private String[]              listAccount = new String[]{"Admin", "Acc 1", "Account2", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3", "Account3"};
    public ArrayAdapter<String> adapter;
    private String TAG = "AccountManager";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        ListView accountList = (ListView)findViewById(R.id.account_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listAccount);
        accountList.setAdapter(adapter);
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