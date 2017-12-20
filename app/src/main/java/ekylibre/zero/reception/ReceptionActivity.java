package ekylibre.zero.reception;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ekylibre.database.ZeroContract;
import ekylibre.util.DateConstant;
import ekylibre.zero.R;

/**
 * Created by Asus on 18/12/2017.
 */

public class ReceptionActivity extends AppCompatActivity {
    ArrayList<ReceptionDataModel> receptionDataModels;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.reception_main);
        ListView mListView = (ListView) findViewById(R.id.reception_list);
        if (savedInstanceState == null) {
            try {
                loadData(this);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final ReceptionAdapter adapter = new ReceptionAdapter(this, receptionDataModels);
        mListView.setAdapter(adapter);

        //mListView.setEmptyView(findViewById(R.id.emptyListView));


    }


    public void add_supplier(Context context) {

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

    public void add_reception(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(ZeroContract.Receptions.RECEPTION_NUMBER, 6515);
        mNewValues.put(ZeroContract.Receptions.RECEIVED_AT, DateConstant.getCurrentDateFormatted());
        mNewValues.put(ZeroContract.Receptions.FK_SUPPLIER, 1);
        mNewValues.put(ZeroContract.Receptions.EK_ID, 1);
        contentResolver.insert(ZeroContract.Receptions.CONTENT_URI, mNewValues);
    }


    static public String getDateFormatted(String inputDate) throws ParseException {
//        Calendar cal = Calendar.getInstance();

        //Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(inputDate);
        //return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
        Log.i("MyTag", inputDate);
        Date date = new SimpleDateFormat(DateConstant.ISO_8601).parse(inputDate);
        String date1 = new SimpleDateFormat("dd/MM/yyyy").format(date);
        Log.i("MyTag", date1);
        return date1;
    }

    public void loadData(Context context) throws ParseException {

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
            String date = curs.getString(curs.getColumnIndexOrThrow(ZeroContract.ReceptionsColumns.RECEIVED_AT));
            Log.i("MyTag", "date après query " + date);
            String date3 = getDateFormatted(date);
            Log.i("MyTag", "date après formattage " + date3);
            //Log.i("MyTag","curs.getColumnIndexOrThrow(date3) "+curs.getColumnIndexOrThrow(date3));

            ReceptionDataModel receptionDataModel;
            receptionDataModel = new ReceptionDataModel(date3);
            Log.i("myTag", "received_at" + receptionDataModel.getReceived_at());
            receptionDataModels.add(receptionDataModel);
        }
    }
    //contentResolver.unregisterContentObserver(receptionContentResolverObserver);


}