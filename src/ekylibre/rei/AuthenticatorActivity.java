package ekylibre.rei;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ekylibre.Token;

/**
 * The Authenticator activity.
 *
 * Called by the Authenticator and in charge of identifing the user.
 *
 * It sends back to the Authenticator the result.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String KEY_CONFIRM_CREDENTIALS = "confirmCredentials";
    public final static String KEY_ACCOUNT_PASSWORD = "accountPassword";
    public final static String KEY_AUTH_TOKEN_TYPE  = "authTokenType";
    public final static String KEY_REDIRECT  = "redirect";

    public final static String CHOICE_REDIRECT_TRACKING  = "tracking";

    private final String TAG = this.getClass().getSimpleName();
    private AccountManager accountManager;
    private String authTokenType;
    private String accountType;
    private String accountName;
    private EditText accountNameEdit;
    private String accountPassword;
    private EditText accountPasswordEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accountNameEdit     = (EditText) findViewById(R.id.accountName);
        accountPasswordEdit = (EditText) findViewById(R.id.accountPassword);

        accountManager = AccountManager.get(getBaseContext());

        final Intent intent = getIntent();

        authTokenType = intent.getStringExtra(KEY_AUTH_TOKEN_TYPE);
        if (authTokenType == null)
            authTokenType = Authenticator.AUTH_TOKEN_TYPE_GLOBAL;

        accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        
        accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (accountName != null) {
            accountNameEdit.setText(accountName);
        }
    }


    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     // The sign up activity returned that the user has successfully created an account
    //     if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
    //         finishLogin(data);
    //     } else
    //         super.onActivityResult(requestCode, resultCode, data);
    // }


    public void signIn(View view) {
        accountName     = accountNameEdit.getText().toString();
        accountPassword = accountPasswordEdit.getText().toString();
        new AsyncTask<String, Void, Intent>() {

            @Override protected Intent doInBackground(String... params) {
                Log.d("rei", TAG + "> Started authenticating");
                String authToken = null;
                Bundle extras = new Bundle();
                extras.putString(KEY_REDIRECT, getIntent().getStringExtra(KEY_REDIRECT));
                try {
                    authToken = Token.create(accountName, accountPassword, "url");
                    extras.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
                    extras.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    extras.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                    extras.putString(KEY_ACCOUNT_PASSWORD, accountPassword);
                } catch (Exception e) {
                    extras.putString(AccountManager.KEY_ERROR_MESSAGE, e.getMessage());
                }
                final Intent result = new Intent();
                result.putExtras(extras);
                return result;
            }

            @Override protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(AccountManager.KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(AccountManager.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Pas trop mal", Toast.LENGTH_SHORT).show();
                    finishLogin(intent);
                }
            }

        }.execute();
    }


    private void finishLogin(Intent intent) {
        Log.d("rei", TAG + "> finishLogin");
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(KEY_ACCOUNT_PASSWORD);
        Log.d("rei", TAG + "> finishLogin("+ accountName +", "+ accountPassword +", "+ intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)+")");
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        // if (getIntent().getBooleanExtra(KEY_IS_ADDING_NEW_ACCOUNT, false)) {
        String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        Log.d("rei", TAG + "> finishLogin > addAccountExplicitly " + authToken);
        // String authTokenType = this.authTokenType;
        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        if (this.accountManager.addAccountExplicitly(account, accountPassword, null)) {
            Log.d("rei", TAG + "> finishLogin > addAccountExplicitly: YES!");
        } else {
            Log.d("rei", TAG + "> finishLogin > addAccountExplicitly: NO!");
        }
        this.accountManager.setAuthToken(account, authTokenType, authToken);
        // } else {
        //     Log.d("rei", TAG + "> finishLogin > setPassword");
        //     this.accountManager.setPassword(account, accountPassword);
        // }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        if (intent.getStringExtra(KEY_REDIRECT).equals(CHOICE_REDIRECT_TRACKING)) {
            Intent trackingIntent = new Intent(this, TrackingActivity.class);
            startActivity(trackingIntent);
        }
        finish();
    }


}
