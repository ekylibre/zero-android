package ekylibre.zero;

import android.app.Activity;
import android.os.Bundle;

import ekylibre.zero.util.AccountTool;

/**************************************
 * Created by pierre on 7/7/16.       *
 * ekylibre.zero for zero-android     *
 *************************************/

public class CalendarActivity extends Activity
{
    @Override
    public void onStart()
    {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
            AccountTool.askForAccount(this, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
    }
}