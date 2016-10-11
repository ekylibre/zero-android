package ekylibre.database;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.CallSuper;

/**************************************
 * Created by pierre on 10/7/16.      *
 * ekylibre.database for zero-android *
 *************************************/

public abstract class BaseORM implements BasicORM
{
    protected Account account;
    protected ContentResolver contentResolver;

    public BaseORM(Account account, Context context)
    {
        reset();
        this.account = account;
        contentResolver = context.getContentResolver();
    }
}
