package ekylibre.zero.inventory;

import android.content.Intent;
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

import java.util.ArrayList;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.MainZoneAdapter;


public class NewInventory extends AppCompatActivity implements SelectProductCategoryFragment.OnFragmentInteractionListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SelectProductCategoryFragment selectproductcategoryfragment;
    ArrayList<ItemCategoryInventory> listeCategory=new ArrayList<>();



//todo: Faire une méthode pour enregistrer automatiquement la denrière date d'inventaire dans la zone
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_new_current_inventory);

       class MyButtonClickListener implements View.OnClickListener {
           @Override
           public void onClick(View _buttonView) {
               if (_buttonView.getId() == R.id.addproduct) {
                   Log.i("mytag", "categorychoice");
                   Intent intent=new Intent(this,thirdactivity.class);
                   startActivity(intent);


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

}





