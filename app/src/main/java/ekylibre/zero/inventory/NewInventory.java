package ekylibre.zero.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import java.util.ArrayList;

import ekylibre.database.ZeroContract;
import ekylibre.database.ZeroProvider;
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
    Bundle extras;
    String zoneId = "1";
    int inventoryType;



//todo: Faire une méthode pour enregistrer automatiquement la dernière date d'inventaire dans la zone
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_new_current_inventory);
       Intent intent = getIntent();
       extras = intent.getExtras();
       inventoryType = extras.getInt("inventoryType");
       zoneId = extras.getString("itemId");
       setTitle("Nouvel Inventaire Courant");

//       fillDBtest();
//       Cursor cursorDateZone = queryDateZone();


//       mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//       mLayoutManager = new LinearLayoutManager(this);
//       mRecyclerView.setLayoutManager(mLayoutManager);

       /*
       Uri uri = ZeroContract.Product.CONTENT_URI;
       String selection = ZeroContract.ZoneStockColumns.ZONE_STOCK_ID+" = "+zoneId;
       String[] projection = {ZeroContract.ProductColumns.PRODUCT_ID,
               ZeroContract.ProductColumns.NAME,
               ZeroContract.ProductColumns.FK_VARIANT_ID,
               ZeroContract.InventoryProductColumns.DATE,
               ZeroContract.InventoryProductColumns.QUANTITY,
               ZeroContract.InventoryProductColumns.COMMENT,
               ZeroContract.ProductColumns.PHOTO};
       Cursor cursor = getContentResolver().query(uri,projection,selection,null,null);
       while (cursor.moveToNext()){
           listProduct.add(new ItemProductInventory(cursor.getString(1),
                   cursor.getString(2),
                   cursor.getString(3),
                   cursor.getString(4),
                   cursor.getString(5),
                   image));

       }
       Log.i("MyTag","QueryListProduct"+listProduct.toString());
        */


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
    public void onFragmentInteraction(ItemCategoryInventory zone) {

    }
    /*@Override
    public void onFragmentInteraction(ItemZoneInventory zone) {
        Log.i("MyTag", "click zone"+zone.zone);



        mAdapter.notifyDataSetChanged();
        selectCategoryDialogFragment.dismiss();
    }*/

}





