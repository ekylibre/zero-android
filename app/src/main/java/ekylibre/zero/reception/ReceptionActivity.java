package ekylibre.zero.reception;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ekylibre.database.DatabaseHelper;
import ekylibre.database.ZeroContract;
import ekylibre.database.ZeroProvider;
import ekylibre.util.DateConstant;
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
    public void add_supplier(Context context, String name_supplier){

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(ZeroContract.Suppliers.NAME, name_supplier);
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
    public void add_reception(Context context, String reception_number, int supplierId){

        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(ZeroContract.Receptions.RECEPTION_NUMBER,reception_number);
        mNewValues.put(ZeroContract.Receptions.RECEIVED_AT, DateConstant.getCurrentDateFormatted());
        mNewValues.put(ZeroContract.Receptions.FK_SUPPLIER,supplierId );
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
        add_supplier(context,"Terrena");
        add_supplier(context,"Cap Seine");
        add_reception(context, "6565",1);
        add_reception(context,"6767", 2);

        //ContentValues mNewValues = new ContentValues();

//        mNewValues.put(ZeroContract.Suppliers.NAME, "patate");
//        mNewValues.put(ZeroContract.Suppliers.EK_ID, 1);

//        mNewValues.put(ZeroContract.Receptions.RECEPTION_NUMBER, 6515);
//        mNewValues.put(ZeroContract.Receptions.RECEIVED_AT, DateConstant.getCurrentDateFormatted());
//        mNewValues.put(ZeroContract.Receptions.FK_SUPPLIER, getIdZeroContract.Suppliers.);
//        mNewValues.put(ZeroContract.Receptions.EK_ID, 1);
        //contentResolver.insert(ZeroContract.Receptions.CONTENT_URI, mNewValues);


        String rawQuery = "SELECT " + ZeroContract.Receptions.RECEIVED_AT + ", " +
                ZeroContract.Suppliers.NAME + ", " + ZeroContract.Receptions.RECEPTION_NUMBER
                + " FROM " + ZeroContract.Receptions.TABLE_NAME + " INNER JOIN " + ZeroContract.Suppliers.TABLE_NAME
            + " ON " + ZeroContract.Receptions._ID + " = " + ZeroContract.Suppliers._ID
                + " WHERE " + ZeroContract.Receptions.FK_SUPPLIER + " = ?" ;
        String rawQuery1 = "SELECT received_at, suppliers._ID, name, receptions_number FROM receptions" +
                " INNER JOIN suppliers ON receptions._id=suppliers._id " +
                "WHERE fk_supplier= suppliers._id" ;


        DatabaseHelper db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor curs = database.rawQuery(rawQuery1,null,null);
        //new String[]{String.valueOf(ZeroContract.Suppliers._ID)}
//        String [] projection_date = {ZeroContract.Receptions.RECEIVED_AT};
//        String[] projection = {ZeroContract.Receptions.RECEIVED_AT,ZeroContract.Suppliers.NAME,ZeroContract.Receptions.RECEPTION_NUMBER};
//        String selection =  ZeroContract.Receptions.FK_SUPPLIER + " == " + ZeroContract.Suppliers._ID;
//
//
//        Cursor curs = contentResolver.query(ZeroContract.Receptions.CONTENT_URI,projection,
//                selection,
//                null, ZeroContract.Receptions.SORT_ORDER_DEFAULT);

//        String TAG = "MyTag";
//        int it;
//        Log.d(TAG, "oy = " + rawQuery1);
//
//        it=0;
//
//        if (curs != null && curs.moveToFirst())
//        {
//            do
//            {
//                ++it;
//                Log.d(TAG, "==========");
//                Log.d(TAG, "Item NUM " + it);
//                Log.d(TAG, "DATE = " + curs.getString(curs.getColumnIndexOrThrow(ZeroContract.ReceptionsColumns.RECEIVED_AT)));
//                Log.d(TAG, "NAME = " + curs.getString(curs.getColumnIndexOrThrow(ZeroContract.SuppliersColumns.NAME)));
//                Log.d(TAG, "BL = " + curs.getString(curs.getColumnIndexOrThrow(ZeroContract.ReceptionsColumns.RECEPTION_NUMBER)));
//                Log.d(TAG, "==========");
//            } while (curs.moveToNext());
//        }

        while (curs.moveToNext()) {
            String date = curs.getString(curs.getColumnIndexOrThrow(ZeroContract.ReceptionsColumns.RECEIVED_AT));
            Log.i("MyTag", "date après query " + date);
            String date3 = getDateFormatted(date);
            Log.i("MyTag", "date après formattage " + date3);
            String date = curs.getString(curs.getColumnIndexOrThrow("received_at")) ;
            String reception_number = curs.getString(curs.getColumnIndexOrThrow("receptions_number")) ;
            String supplier_name = curs.getString(curs.getColumnIndexOrThrow("name")) ;
            String supplierID = curs.getString(curs.getColumnIndexOrThrow("_id")) ;
            Log.i("MyTag","date après query "+date);
            String date3=getDateFormatted(date);
            Log.i("MyTag","date après formattage "+date3);
            //Log.i("MyTag","curs.getColumnIndexOrThrow(date3) "+curs.getColumnIndexOrThrow(date3));

            ReceptionDataModel receptionDataModel;
            receptionDataModel = new ReceptionDataModel(date3, reception_number, Integer.parseInt(supplierID));
            Log.i("myTag", "received_at" + receptionDataModel.getReceived_at());
            receptionDataModels.add(receptionDataModel);
        }
    }
    //contentResolver.unregisterContentObserver(receptionContentResolverObserver);


}