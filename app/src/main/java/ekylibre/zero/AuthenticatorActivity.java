package ekylibre.zero;

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

import ekylibre.api.Token;
import ekylibre.exceptions.UnauthorizedException;

/**
 * The Authenticator activity.
 *
 * Called by the Authenticator and in charge of identifing the user.
 *
 * It sends back to the Authenticator the result.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String KEY_ACCOUNT_PASSWORD    = "accountPassword";
    public final static String KEY_AUTH_TOKEN_TYPE     = "authTokenType";
    public final static String KEY_CONFIRM_CREDENTIALS = "confirmCredentials";
    public final static String KEY_INSTANCE_URL        = "instanceURL";
    public final static String KEY_ACCOUNT_NAME        = "accountName";
    public final static String KEY_REDIRECT            = "redirect";

    public final static String CHOICE_REDIRECT_TRACKING  = "tracking";

    private final String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private String mAccountType;
    private String mAccountName;
    private String mAccountPassword;
    private String mAccountInstance;

    private EditText mAccountNameEdit;
    private EditText mAccountPasswordEdit;
    private EditText mAccountInstanceEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAccountNameEdit     = (EditText) findViewById(R.id.accountName);
        mAccountPasswordEdit = (EditText) findViewById(R.id.accountPassword);
        mAccountInstanceEdit = (EditText) findViewById(R.id.accountInstance);

        mAccountManager = AccountManager.get(getBaseContext());

        final Intent intent = getIntent();

        mAuthTokenType = intent.getStringExtra(KEY_AUTH_TOKEN_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = Authenticator.AUTH_TOKEN_TYPE_GLOBAL;

        mAccountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        
        mAccountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (mAccountName != null) {
            mAccountNameEdit.setText(mAccountName);
        }
    }


    public void signIn(View view) {
        mAccountName     = mAccountNameEdit.getText().toString();
        mAccountPassword = mAccountPasswordEdit.getText().toString();
        mAccountInstance = mAccountInstanceEdit.getText().toString();
        new AsyncTask<String, Void, Intent>() {

            @Override protected Intent doInBackground(String... params) {
                Log.d("zero", TAG + "> Started authenticating");
                String authToken = null;
                Bundle extras = new Bundle();
                extras.putString(KEY_REDIRECT, getIntent().getStringExtra(KEY_REDIRECT));
                try {
                    authToken = Token.create(mAccountName, mAccountPassword, mAccountInstance);
                    extras.putString(AccountManager.KEY_ACCOUNT_NAME, mAccountName);
                    extras.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                    extras.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                    extras.putString(KEY_ACCOUNT_PASSWORD, mAccountPassword);
                    extras.putString(KEY_INSTANCE_URL, mAccountInstance);
                } catch (UnauthorizedException e) {
                    extras.putString(AccountManager.KEY_ERROR_MESSAGE, getString(R.string.notif_invalidPasswordOrEmail));
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
                    Toast.makeText(getBaseContext(), getString(R.string.notif_loginSucceeded), Toast.LENGTH_SHORT).show();
                    finishLogin(intent);
                }
            }

        }.execute();
    }


    public void testDemo(View view) {
        mAccountNameEdit.setText("admin@ekylibre.org");
        mAccountPasswordEdit.setText("12345678");
        mAccountInstanceEdit.setText("https://demo.ekylibre.farm");
        signIn(view);
    }


    private void finishLogin(Intent intent) {
        Log.d("zero", TAG + "> finishLogin");
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME) + " - " + intent.getStringExtra(KEY_INSTANCE_URL);
        String accountPassword = intent.getStringExtra(KEY_ACCOUNT_PASSWORD);
        Log.d("zero", TAG + "> finishLogin(" + accountName + ", " + accountPassword + ", " + intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE) + ")");
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        // if (getIntent().getBooleanExtra(KEY_IS_ADDING_NEW_ACCOUNT, false)) {
        String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        Log.d("zero", TAG + "> finishLogin > addAccountExplicitly " + authToken);
        // String authTokenType = mAuthTokenType;
        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        Bundle userdata = new Bundle();
        userdata.putString(Authenticator.KEY_INSTANCE_URL, intent.getStringExtra(KEY_INSTANCE_URL));
        userdata.putString(Authenticator.KEY_ACCOUNT_NAME, intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));

        if (mAccountManager.addAccountExplicitly(account, accountPassword, userdata)) {
            Log.d("zero", TAG + "> finishLogin > addAccountExplicitly: YES!");
        } else {
            Log.d("zero", TAG + "> finishLogin > addAccountExplicitly: NO!");
        }
        mAccountManager.setAuthToken(account, mAuthTokenType, authToken);
        // } else {
        //     Log.d("zero", TAG + "> finishLogin > setPassword");
        //     mAccountManager.setPassword(account, accountPassword);
        // }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        // Redirect to MainActivity if requested
        String redirect = intent.getStringExtra(KEY_REDIRECT);
        if (redirect != null && redirect.equals(CHOICE_REDIRECT_TRACKING)) {


            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }
        finish();
    }


}
