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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ekylibre.database.DatabaseHelper;
import ekylibre.database.ZeroContract;
import ekylibre.database.ZeroProvider;
import ekylibre.util.DateConstant;
import ekylibre.zero.R;

/**
 * Created by Asus on 18/12/2017.
 */

public class ReceptionActivity extends AppCompatActivity {
    ArrayList<List> resultset=new ArrayList<List>();


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        add_supplier(this,"Terrena");
        Log.i("MyTag","OK");
        add_supplier(this,"Cap Seine");
        setContentView(R.layout.reception_main);
        ListView mListView = (ListView) findViewById(R.id.reception_list);
        if (savedInstanceState == null) {
            try {
                //dbTest();
                loadData(this);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final ReceptionAdapter adapter = new ReceptionAdapter(this, resultset);
        mListView.setAdapter(adapter);

        //mListView.setEmptyView(findViewById(R.id.emptyListView));


    }

    public void dbTest (){
        add_supplier(this,"Terrena");
        add_supplier(this,"Cap Seine");
        add_reception(this, "6565",1);
        add_reception(this,"6767", 2);
    }


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

        //ArrayList<List> resultset = new ArrayList<List>();
        //ContentObserver receptionContentResolverObserver = null;
        //ContentObserver contentObserver = null;
        ContentResolver contentResolver = context.getContentResolver();

        //ContentValues mNewValues = new ContentValues();

//        mNewValues.put(ZeroContract.Suppliers.NAME, "patate");
//        mNewValues.put(ZeroContract.Suppliers.EK_ID, 1);

//        mNewValues.put(ZeroContract.Receptions.RECEPTION_NUMBER, 6515);
//        mNewValues.put(ZeroContract.Receptions.RECEIVED_AT, DateConstant.getCurrentDateFormatted());
//        mNewValues.put(ZeroContract.Receptions.FK_SUPPLIER, getIdZeroContract.Suppliers.);
//        mNewValues.put(ZeroContract.Receptions.EK_ID, 1);
        //contentResolver.insert(ZeroContract.Receptions.CONTENT_URI, mNewValues);


//        String rawQuery = "SELECT " + ZeroContract.Receptions.RECEIVED_AT + ", " +
//                ZeroContract.Suppliers.NAME + ", " + ZeroContract.Receptions.RECEPTION_NUMBER
//                + " FROM " + ZeroContract.Receptions.TABLE_NAME + " INNER JOIN " + ZeroContract.Suppliers.TABLE_NAME
//            + " ON " + ZeroContract.Receptions._ID + " = " + ZeroContract.Suppliers._ID
//                + " WHERE " + ZeroContract.Receptions.FK_SUPPLIER + " = ?" ;
        String rawQuery1 = "SELECT received_at, suppliers._id, suppliers.name, receptions_number FROM receptions " +
                " INNER JOIN suppliers ON receptions.fk_supplier=suppliers._id ";


        DatabaseHelper db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor curs = database.rawQuery(rawQuery1,null,null);


        Log.i("MyTag","cursor length : "+ curs.getCount());

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
            String date = curs.getString(curs.getColumnIndexOrThrow("received_at")) ;
            String reception_number = curs.getString(curs.getColumnIndexOrThrow("receptions_number")) ;
            String supplier_name = curs.getString(curs.getColumnIndexOrThrow("name")) ;
            String supplierID = curs.getString(curs.getColumnIndexOrThrow("_id")) ;
            SupplierDataModel supplier = new SupplierDataModel(Integer.parseInt(supplierID), supplier_name, 1);
            Log.i("MyTag","date après query "+date);
            String date3=getDateFormatted(date);
            Log.i("MyTag","date après formattage "+date3);
            //Log.i("MyTag","curs.getColumnIndexOrThrow(date3) "+curs.getColumnIndexOrThrow(date3));

            List<String> list = new CopyOnWriteArrayList<String>();
            list.add(date3);
            list.add(reception_number);
            list.add(supplier_name);
            Log.i("MyTag","list : "+list);


            resultset.add(list);
            Log.i("MyTag","resultset : "+resultset);

        }
    }
    //contentResolver.unregisterContentObserver(receptionContentResolverObserver);


}