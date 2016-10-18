package ekylibre.APICaller;

import android.accounts.Account;

import org.json.JSONObject;

/**************************************
 * Created by pierre on 10/5/16.      *
 * ekylibre.APICaller for zero-android    *
 *************************************/

public interface BasicCaller
{
    void post(JSONObject attributes);

    void postUserData(Account account);

    void get(String attributes);
}
