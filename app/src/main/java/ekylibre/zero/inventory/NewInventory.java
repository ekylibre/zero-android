package ekylibre.zero.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.MainZoneAdapter;
import ekylibre.zero.inventory.adapters.UiSelectProductAdapter;


public class NewInventory extends AppCompatActivity implements SelectProductCategoryFragment.OnFragmentInteractionListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SelectProductCategoryFragment selectproductcategoryfragment;
    ArrayList<ItemCategoryInventory> listeCategory=new ArrayList<>();
    ArrayList<ItemProductInventory> listProduct = new ArrayList<>();
    byte[] image;




//todo: Faire une méthode pour enregistrer automatiquement la dernière date d'inventaire dans la zone
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_new_current_inventory);

       setTitle("Nouvel Inventaire Courant");

//       fillDBtest();
//       Cursor cursorDateZone = queryDateZone();


//       mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//       mLayoutManager = new LinearLayoutManager(this);
//       mRecyclerView.setLayoutManager(mLayoutManager);



       class MyButtonClickListener implements View.OnClickListener {
           @Override
           public void onClick(View _buttonView) {
//               if (_buttonView.getId() == R.id.choose_category) {
//                   Log.i("mytag", "categorychoice");
//                   Intent intent = new Intent(this, thirdactivity.class);
//                   startActivity(intent);
//               }
               if (_buttonView.getId() == R.id.validate_button) {

               }
           }


       }
       TextView modifycategory = findViewById(R.id.choose_category);
       modifycategory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View productcategorychoice) {
               fillDBtest();
               queryAddCategory();
               selectproductcategoryfragment = SelectProductCategoryFragment.newInstance(listeCategory);
               selectproductcategoryfragment.show(getFragmentTransaction(),"dialog");
           }
       });

       for (int i=0;i<4;i++){
           listProduct.add(new ItemProductInventory("Name_"+i, "var_"+i,
                   "date_"+i, "qtt_"+i, "comm_"+i, image));
       }
       Log.i("MyTag", ""+listProduct.toString());

       mRecyclerView = (RecyclerView) findViewById(R.id.ProductsRecycler);

       mLayoutManager = new LinearLayoutManager(this);
       mRecyclerView.setLayoutManager(mLayoutManager);

       mAdapter = new UiSelectProductAdapter(listProduct);
       mRecyclerView.setAdapter(mAdapter);

       mRecyclerView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               switch (view.getId()){
                   case R.id.product_qtt_edit:
                       Log.i("MyTag", "Edit Quantity");
                       break;
                   case R.id.product_minus_qtt:
                       Log.i("MyTag", "Edit Quantity Minus");
                       break;
                   case R.id.product_plus_qtt:
                       Log.i("MyTag", "Edit Quantity Plus");
                       break;
                   case R.id.product_checkbox:
                       Log.i("MyTag", "Check");
                       break;
                   case R.id.add_image:
                       // Should open popup
                       Log.i("MyTag", "Edit Image");
                       break;
                   case R.id.add_comment:
                       // Should open popup
                       Log.i("MyTag", "Edit Comment");
                       break;
                   default:
                       Log.i("MyTag", "Default");
                       break;
               }
           }
       });

       TextView validateCategories = findViewById(R.id.validate_button);
       modifycategory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int length = listeCategory.size();
               for (int i=0; i<length; i++) {
                   if (listeCategory.get(i).is_selected){
                       //todo:Add a chip to the category chipGroup when it doesn't make the app crash anymore
                   }
               }
               //todo:Make the popup close to get back to the Second Activity
           }
       });

   }

    private void queryAddCategory() {
        String[] projectionCategoryID = ZeroContract.Category.PROJECTION_ALL;

        Cursor cursorAddCategory = getContentResolver().query(
                ZeroContract.Category.CONTENT_URI,
                projectionCategoryID,
                null,
                null,
                null);

        while(cursorAddCategory.moveToNext()) {
            int index;

            index = cursorAddCategory.getColumnIndexOrThrow("category_name");
            String categoryName = cursorAddCategory.getString(index);

            Log.i("Query", "" +categoryName);

            listeCategory.add(new ItemCategoryInventory(categoryName,index));

        }
        //cursorZone.close();
    }




    private FragmentTransaction getFragmentTransaction (){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev!= null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

    @Override
    public void onFragmentInteraction(ItemCategoryInventory categoryInventory) {
        //mAdapter.notifyDataSetChanged();
    }
    /*@Override
    public void onFragmentInteraction(ItemZoneInventory zone) {
        Log.i("MyTag", "click zone"+zone.zone);



        mAdapter.notifyDataSetChanged();
        selectCategoryDialogFragment.dismiss();
    }*/


    public void fillDBtest() {
        ContentValues mNewValues = new ContentValues();
        for (int i=0;i<7;i++){
            if (i<8) {
                mNewValues.clear();
                int zoneId = i;
                //String zoneName = "zone_" + i;
                //String zoneShape = null;
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
                //SimpleDateFormat sdf = new SimpleDateFormat("E  d MMM yyyy, HH:mm");
                //SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                //String zoneDate = sdf.format(new Date());
                String categoryName = new String("test1");
                Log.i("myatg"," test 1 ");
                //String zoneDate = sdf.format(Calendar.getInstance().getTime());
                //Date zoneDate = new Date() ;

                mNewValues.put(ZeroContract.Category.CATEGORY_NAME, categoryName);
                //mNewValues.put(ZeroContract.ZoneStock.NAME, zoneName);
                //mNewValues.put(ZeroContract.ZoneStock.DATEZONE, zoneDate);
                getContentResolver().insert(
                        ZeroContract.Category.CONTENT_URI,   // the user dictionary content URI
                        mNewValues                          // the values to insert
                );
                //this.finish();
            }


        }
    }

}





