package ekylibre.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Objects;

import ekylibre.zero.account.AccountManagerActivity;
import ekylibre.zero.Authenticator;
import ekylibre.zero.AuthenticatorActivity;
import ekylibre.APICaller.SyncAdapter;

/**************************************
 * Created by pierre on 7/18/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/
public class AccountTool
{
    private Context mContext;
    private final static String TAG = "AccountTool";

    public AccountTool(Context context)
    {
        mContext = context;
    }

    /*
    ** @return an account list of users from the current phone
    */
    public static Account[] getListAccount(Context context)
    {
        Account[] listAccount = AccountManager.get(context).getAccountsByType(SyncAdapter.ACCOUNT_TYPE);
        return (listAccount);
    }

    /*
    ** @return TRUE if any account exists, else this return false
    */
    public static boolean isAnyAccountExist(Context context)
    {
        AccountManager accMan = AccountManager.get(context);
        final Account[] accList = accMan.getAccountsByType(SyncAdapter.ACCOUNT_TYPE);
        if (accList.length == 0)
            return (false);
        else
            return (true);
    }

    /*
    ** This function start the login activity and finish activity passed in parameters
    ** You can ask this function when you do not have any account  with isAnyAccountExist
    */
    public static void  askForAccount(Context context, Activity activity)
    {
        Intent intent = new Intent(context, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, SyncAdapter.ACCOUNT_TYPE);
        intent.putExtra(AuthenticatorActivity.KEY_REDIRECT, AuthenticatorActivity.CHOICE_REDIRECT_TRACKING);
        context.startActivity(intent);
        activity.finish();
    }

    /*
    ** @return accountName (which is different from account.name) for account passed in parameters
    ** Account name is the email of the user
    */
    public static String getAccountName(Account account, Context context)
    {
        AccountManager accountManager = AccountManager.get(context);
        String accName = accountManager.getUserData(account, Authenticator.KEY_ACCOUNT_NAME);
        return (accName);
    }

    /*
    ** @return accountInstance for account passed in parameters
    ** Account instance is the farm URL of the user
    */
    public static String getAccountInstance(Account account, Context context)
    {
        AccountManager accountManager = AccountManager.get(context);
        String accInstance = accountManager.getUserData(account, Authenticator.KEY_INSTANCE_URL);
        return (accInstance);
    }

    /*
    ** @return instance of current account which is set in preferences
    */
    public static Account getCurrentAccount(Context context)
    {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String accName = preference.getString(AccountManagerActivity.CURRENT_ACCOUNT_NAME, null);
        if (accName == null)
        {
            setFirstAccountPref(preference, context);
            preference = PreferenceManager.getDefaultSharedPreferences(context);
            accName = preference.getString(AccountManagerActivity.CURRENT_ACCOUNT_NAME, null);
        }
        Account[] listAccount = AccountManager.get(context).getAccountsByType(SyncAdapter.ACCOUNT_TYPE);
        Account currAcc = findCurrentAccount(listAccount, accName, context);
        Log.d(TAG, "Current account is ==> " + currAcc.name);
        return (currAcc);
    }

    /*
    ** Method call first time you use the app to set the first account you signed in as current account
    */
    public static void    setFirstAccountPref(SharedPreferences preferences, Context context)
    {
        Account newCurrAccount = AccountManager.get(context).getAccountsByType(SyncAdapter.ACCOUNT_TYPE)[0];

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AccountManagerActivity.CURRENT_ACCOUNT_NAME, newCurrAccount.name);
        editor.commit();
    }

    /*
    ** Bruteforce list account to find an account by his unique account.name
    ** This method is viable because the list account should not be > 1xx accounts
    */
    private static Account findCurrentAccount(Account[] listAccount, String accName, Context context)
    {
        int i = -1;

        while (++i < listAccount.length && !Objects.equals(listAccount[i].name, accName));
        if (i == listAccount.length)
            return (listAccount[0]);
        return (listAccount[i]);
    }

    public static String getEmail(Account account)
    {
        String email;

        email = account.name;
        email = email.split(" - ")[0];
        return (email);
    }

    public static String getInstance(Account account)
    {
        String instance;

        instance = account.name;
        instance = instance.split(" - ")[1];
        return (instance);
    }
}
