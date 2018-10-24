package ekylibre.zero.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ekylibre.zero.R;
import ekylibre.util.AccountTool;

import static ekylibre.zero.account.AccountManagerActivity.CURRENT_ACCOUNT_NAME;

/**************************************
 * Created by pierre on 7/18/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/
public class AccountAdapter extends ArrayAdapter<Account>
{
    Context mContext;
    AccountManagerActivity mAccountManagerActivity;

    public AccountAdapter(Context context, ArrayList<Account> accountList, AccountManagerActivity
            accountManagerActivity)
    {
        super(context, 0, accountList);
        mContext = context;
        mAccountManagerActivity = accountManagerActivity;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item, parent, false);
        }

        AccountViewHolder viewHolder = (AccountViewHolder) convertView.getTag();
        if (viewHolder == null)
        {
            viewHolder = new AccountViewHolder();
            viewHolder.accountName = (TextView)convertView.findViewById(R.id.accountName);
            viewHolder.accountInstance = (TextView)convertView.findViewById(R.id.accountInstance);
            viewHolder.delete = (ImageButton) convertView.findViewById(R.id.deleteAccount);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar_account);
            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.account_item);
            convertView.setTag(viewHolder);
        }
        final Account     item = getItem(position);
        viewHolder.accountName.setText(AccountTool.getAccountName(item, mContext));
        viewHolder.accountInstance.setText(AccountTool.getAccountInstance(item, mContext));
        viewHolder.delete.setVisibility(View.VISIBLE);
        viewHolder.avatar.setVisibility(View.VISIBLE);
        viewHolder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Account selectedAccount = getItem(position);
                if (selectedAccount != null && selectedAccount.equals(AccountTool.getCurrentAccount
                    (getContext())))
                    AccountTool.setFirstAccountPref(PreferenceManager.getDefaultSharedPreferences
                            (getContext()), getContext());
                AccountManager manager = AccountManager.get(getContext());
                remove(selectedAccount);
                manager.removeAccount(selectedAccount, null, null);
                notifyDataSetChanged();
            }
        });

        viewHolder.layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Account newCurrAccount;
                AccountManager accManager = AccountManager.get(getContext());

                newCurrAccount = item;
                if (newCurrAccount == null)
                    return;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(CURRENT_ACCOUNT_NAME, newCurrAccount.name);
                editor.commit();
                Log.d("Account manager", "New current account is ==> " + newCurrAccount.name);
                mAccountManagerActivity.syncAll(newCurrAccount);
                mAccountManagerActivity.finish();
            }
        });
        return (convertView);
    }}
