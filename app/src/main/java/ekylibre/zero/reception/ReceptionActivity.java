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
import ekylibre.util.DateConstant;
import ekylibre.zero.R;
import ekylibre.zero.home.Zero;

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
        final ReceptionAdapter adapter = new ReceptionAdapter(this, receptionDataModels);
        mListView.setAdapter(adapter);

        //mListView.setEmptyView(findViewById(R.id.emptyListView));

    }


    public void add_supplier(Context context){

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(ZeroContract.Suppliers.NAME, "Terrena");
        mNewValues.put(ZeroContract.Suppliers.EK_ID, 1);
        contentResolver.insert(ZeroContract.Suppliers.CONTENT_URI, mNewValues);
    }

//    public SupplierDataModel getSupplierById(int id, Context context){
//        ContentResolver contentResolver = context.getContentResolver();
//        Cursor curs;
//        String[] projection = {"_ID"};
//        curs = contentResolver.query(ZeroContract.Suppliers.CONTENT_URI, projection, ZeroContract.,
//                null,null);
//    }

    public void add_reception(Context context){

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(ZeroContract.Receptions.RECEPTION_NUMBER, 6515);
        mNewValues.put(ZeroContract.Receptions.RECEIVED_AT, DateConstant.getCurrentDateFormatted());
        mNewValues.put(ZeroContract.Receptions.FK_SUPPLIER, 1);
        mNewValues.put(ZeroContract.Receptions.EK_ID, 1);
        contentResolver.insert(ZeroContract.Receptions.CONTENT_URI, mNewValues);
    }

    public void loadData (Context context){

        receptionDataModels = new ArrayList<ReceptionDataModel>();
        //ContentObserver receptionContentResolverObserver = null;
        //ContentObserver contentObserver = null;
        ContentResolver contentResolver = context.getContentResolver();
        add_supplier(context);
        add_reception(context);
        //ContentValues mNewValues = new ContentValues();

//        mNewValues.put(ZeroContract.Suppliers.NAME, "patate");
//        mNewValues.put(ZeroContract.Suppliers.EK_ID, 1);

//        mNewValues.put(ZeroContract.Receptions.RECEPTION_NUMBER, 6515);
//        mNewValues.put(ZeroContract.Receptions.RECEIVED_AT, DateConstant.getCurrentDateFormatted());
//        mNewValues.put(ZeroContract.Receptions.FK_SUPPLIER, getIdZeroContract.Suppliers.);
//        mNewValues.put(ZeroContract.Receptions.EK_ID, 1);
        //contentResolver.insert(ZeroContract.Receptions.CONTENT_URI, mNewValues);

        Cursor curs;
        String[] projection = new String[]{ZeroContract.Receptions.RECEIVED_AT};
        curs = contentResolver.query(ZeroContract.Receptions.CONTENT_URI, projection, null,
                null, ZeroContract.Receptions.SORT_ORDER_DEFAULT);

        while (curs.moveToNext()) {
            ReceptionDataModel receptionDataModel = new ReceptionDataModel(curs.getString
                    (curs.getColumnIndexOrThrow(ZeroContract.ReceptionsColumns.RECEIVED_AT)));
            Log.i("myTag", "received_at" + receptionDataModel.getReceived_at());
            receptionDataModels.add(receptionDataModel);
        }
        //contentResolver.unregisterContentObserver(receptionContentResolverObserver);


    }
}