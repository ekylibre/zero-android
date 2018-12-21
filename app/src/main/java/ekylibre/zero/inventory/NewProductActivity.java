package ekylibre.zero.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.MainZoneAdapter;



public class NewProductActivity extends AppCompatActivity implements SelectZoneDialogFragment.OnFragmentInteractionListener, SelectCategoryDialogFragment.OnFragmentInteractionListener{

    private SelectZoneDialogFragment selectZoneDialogFragment;
    ArrayList<ItemZoneInventory> listeZone = new ArrayList<>();

    private SelectCategoryDialogFragment selectCategoryDialogFragment;
    ArrayList<ItemZoneInventory> listeCat = new ArrayList<>();

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.production_new);

       final ArrayList<ItemZoneInventory> listeAllZones = new ArrayList<ItemZoneInventory>();
       Cursor cursorAddZone = queryAllZones();
       cursorAddZone.moveToFirst();
       while(cursorAddZone.moveToNext()) {
           int indexname = cursorAddZone.getColumnIndexOrThrow("name");
           String zoneName = cursorAddZone.getString(indexname);
           listeAllZones.add(new ItemZoneInventory(zoneName));
       }
       cursorAddZone.close();

       final ArrayList<ItemCategory> listeAllCat = new ArrayList<ItemCategory>();
       Cursor cursorCatZone = queryAllCats();
       cursorCatZone.moveToFirst();
       while(cursorCatZone.moveToNext()) {
           int indexname = cursorCatZone.getColumnIndexOrThrow("category_name");
           String catName = cursorCatZone.getString(indexname);
           listeAllCat.add(new ItemCategory(catName));
       }
       cursorCatZone.close();


       TextView modifyProduct = findViewById(R.id.modifier_zone);
       modifyProduct.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View addInventoryZone) {
               selectZoneDialogFragment = SelectZoneDialogFragment.newInstance(listeAllZones);
               selectZoneDialogFragment.show(getFragmentTransaction(),"dialog");

           }
       });

       TextView modifyCat = findViewById(R.id.modifier_cat);
       modifyCat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View addcat) {
               selectCategoryDialogFragment = SelectCategoryDialogFragment.newInstance(listeAllCat);
               selectCategoryDialogFragment.show(getFragmentTransaction(),"dialog");

           }
       });


   }

    private Cursor queryAllZones() {
        String[] projectionZoneID = ZeroContract.ZoneStock.PROJECTION_ALL;

        Cursor cursorZone = getContentResolver().query(
                ZeroContract.ZoneStock.CONTENT_URI,
                projectionZoneID,
                null,
                null,
                null);

        while(cursorZone.moveToNext()) {
            int index;

            index = cursorZone.getColumnIndexOrThrow("id_zone_stock");
            String zoneId = cursorZone.getString(index);

            index = cursorZone.getColumnIndexOrThrow("name");
            String zoneName = cursorZone.getString(index);

            index = cursorZone.getColumnIndexOrThrow("shape");
            String zoneShape = cursorZone.getString(index);

            Log.i("Query", "" + zoneId + zoneName + zoneShape);
        }

        //cursorZone.close();

        return cursorZone;
    }

    private Cursor queryAllCats() {
        String[] projectionCatID = ZeroContract.Category.PROJECTION_ALL;

        Cursor cursorZone = getContentResolver().query(
                ZeroContract.Category.CONTENT_URI,
                projectionCatID,
                null,
                null,
                null);

        while(cursorZone.moveToNext()) {
            int index;

            index = cursorZone.getColumnIndexOrThrow("category_id");
            String catId = cursorZone.getString(index);

            index = cursorZone.getColumnIndexOrThrow("category_name");
            String catName = cursorZone.getString(index);

            Log.i("Query", "" + catId + catId);
        }

        return cursorZone;
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
    public void onFragmentInteraction(ItemZoneInventory zone) {
        Log.i("MyTag", "click zone"+zone.zone);
        TextView zone_choisie = findViewById(R.id.selected_zone);
        zone_choisie.setText(zone.getZone());
        selectZoneDialogFragment.dismiss();
    }

    @Override
    public void onFragmentInteraction(ItemCategory cat) {
        Log.i("MyTag", "click zone"+cat.name);
        TextView cat_choisie = findViewById(R.id.selected_cat);
        cat_choisie.setText(cat.name);
        selectCategoryDialogFragment.dismiss();
    }
}




