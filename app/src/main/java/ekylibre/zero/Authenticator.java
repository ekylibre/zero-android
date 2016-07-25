package ekylibre.zero;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import ekylibre.api.Token;
import ekylibre.api.User;


public class Authenticator extends AbstractAccountAuthenticator {

    public static final String AUTH_TOKEN_TYPE_GLOBAL = "global";
    public final static String KEY_INSTANCE_URL       = "instanceURL";
    public final static String KEY_ACCOUNT_NAME        = "accountName";


    private final String TAG = "Authenticator";
    private Context mContext;
    // Simple constructor
    public Authenticator(Context context)
    {
        super(context);
        mContext = context;
    }

    // Don't add additional accounts
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d("zero", "> addAccount " + accountType);
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.KEY_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    // Getting an authentication token is not supported
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d("zero", "> getAuthToken");
        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(AUTH_TOKEN_TYPE_GLOBAL)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager accountManager = AccountManager.get(mContext);
        String authToken = accountManager.peekAuthToken(account, authTokenType);
        Log.d("zero", "> getAuthToken > peekAuthToken returned - " + authToken);
        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            Log.d("zero", "> getAuthToken > empty token");
            final String password = accountManager.getPassword(account);
            if (password != null) {
                try {
                    Log.d("zero", TAG + "> re-authenticating with the existing password");
                    // authToken = sServerAuthenticate.userSignIn(account.name, password, authTokenType);
                    final String instanceUrl = accountManager.getUserData(account, KEY_INSTANCE_URL);
                    authToken = Token.create(account.name, password, instanceUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            Log.d("zero", "> getAuthToken > empty token!");
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }
        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        Log.d("zero", "> getAuthToken > no password");
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra(AuthenticatorActivity.KEY_AUTH_TOKEN_TYPE, authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    // Getting a label for the auth token is not supported
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (authTokenType.equals(AUTH_TOKEN_TYPE_GLOBAL)) {
            return mContext.getString(R.string.global_auth_token);
        }
        return null;
    }

    // Checking features for the account is not supported
    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    // Editing properties is not supported
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException();
    }

    // Ignore attempts to confirm credentials
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        Log.d("zero", "> confirmCredentials");
        if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
            final AccountManager accountManager = AccountManager.get(mContext);
            final String password  = options.getString(AccountManager.KEY_PASSWORD);
            final String instanceUrl = accountManager.getUserData(account, KEY_INSTANCE_URL);
            final boolean verified = User.authenticate(account.name, password, "url");
            final Bundle result = new Bundle();
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, verified);
            return result;
        }

        // Launch AuthenticatorActivity to confirm credentials
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra(AuthenticatorActivity.KEY_CONFIRM_CREDENTIALS, true);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }


    // Updating user credentials 
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle loginOptions) {
        Log.d("zero", "> updateCredentials");
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra(AuthenticatorActivity.KEY_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(AuthenticatorActivity.KEY_CONFIRM_CREDENTIALS, false);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }
}
