package ekylibre.zero.inventory;

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

import java.util.ArrayList;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.MainZoneAdapter;


public class NewInventory extends AppCompatActivity implements SelectProductCategoryFragment.OnFragmentInteractionListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SelectProductCategoryFragment selectproductcategoryfragment;
    ArrayList<ItemCategoryInventory> listeCategory = new ArrayList<>();



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
               queryAddCategory();
               selectproductcategoryfragment = SelectProductCategoryFragment.newInstance(listeCategory);
               selectproductcategoryfragment.show(getFragmentTransaction(),"dialog");
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

            index = cursorAddCategory.getColumnIndexOrThrow("name");
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
    public void onFragmentInteraction(ItemCategoryInventory zone) {

    }
    /*@Override
    public void onFragmentInteraction(ItemZoneInventory zone) {
        Log.i("MyTag", "click zone"+zone.zone);



        mAdapter.notifyDataSetChanged();
        selectCategoryDialogFragment.dismiss();
    }*/

}





