package ekylibre.zero;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Objects;

/**************************************
 * Created by pierre on 7/18/16.      *
 * ekylibre.zero for zero-android    *
 *************************************/
public class AccountTool
{
    private Context mContext;

    public Account getCurrentAccount(Context context)
    {
        mContext = context;
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String accName = preference.getString(AccountManagerActivity.CURRENT_ACCOUNT_NAME, null);
        String accInstance = preference.getString(AccountManagerActivity.CURRENT_ACCOUNT_INSTANCE, null);
        if (accName == null)
            return (null);
        Account[] listAccount = AccountManager.get(context).getAccountsByType(SyncAdapter.ACCOUNT_TYPE);
        Account currAcc = findCurrentAccount(listAccount, accName, accInstance);
        return (currAcc);
    }

    private Account findCurrentAccount(Account[] listAccount, String accName, String accInstance)
    {
        int i = -1;
        AccountManager accManager = AccountManager.get(mContext);

        while (listAccount[++i] != null && !Objects.equals(listAccount[i].name, accName)
                && !Objects.equals(accManager.getUserData(listAccount[i], AuthenticatorActivity.KEY_INSTANCE_URL), accInstance));
        return (listAccount[i]);
    }
}
