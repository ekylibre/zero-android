package ekylibre.zero.reception;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import ekylibre.database.ZeroContract;
import ekylibre.database.ZeroProvider;
import ekylibre.zero.R;

/**
 * Created by Asus on 18/12/2017.
 */

public class ReceptionActivity extends AppCompatActivity {
    ArrayList<ReceptionDataModel> receptionDataModels;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mListView = (ListView) findViewById(R.id.listView);
        if (savedInstanceState == null) {
            loadData(this);
        }
    }

    public void loadData (Context context){
        receptionDataModels = new ArrayList<ReceptionDataModel>();
        ContentObserver receptionContentResolverObserver = null;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor curs;
        String[] projection = {"received_at"};
        curs = contentResolver.query(ZeroContract.InterventionParameters.CONTENT_URI, projection, null,
                null, ZeroContract.Receptions.SORT_ORDER_DEFAULT);

        while (curs.moveToNext()) {
            ReceptionDataModel receptionDataModel = new ReceptionDataModel(curs.getString
                    (curs.getColumnIndexOrThrow(ZeroContract.ReceptionsColumns.RECEIVED_AT)));
            receptionDataModels.add(receptionDataModel);
        }
        contentResolver.unregisterContentObserver(receptionContentResolverObserver);
    }
}