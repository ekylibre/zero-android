package ekylibre.zero.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ekylibre.zero.R;
import ekylibre.util.AccountTool;

/**************************************
 * Created by pierre on 7/18/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/
public class AccountAdapter extends ArrayAdapter<Account>
{
    Context mContext;

    public AccountAdapter(Context context, ArrayList<Account> accountList)
    {
        super(context, 0, accountList);
        mContext = context;
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
            convertView.setTag(viewHolder);
        }
        Account     item = getItem(position);
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
                AccountManager manager = AccountManager.get(getContext());
                remove(selectedAccount);
                manager.removeAccount(selectedAccount, null, null);
                notifyDataSetChanged();
            }
        });

        return (convertView);
    }}
