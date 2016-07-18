package ekylibre.zero;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**************************************
 * Created by pierre on 7/18/16.      *
 * ekylibre.zero for zero-android     *
 *************************************/
public class AccountAdapter  extends ArrayAdapter<Account>
{
    public AccountAdapter(Context context, Account[] accountList)
    {
        super(context, 0, accountList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
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
            convertView.setTag(viewHolder);
        }
        Account     item = getItem(position);
        viewHolder.accountName.setText(item.name);


        return (convertView);
    }
}
