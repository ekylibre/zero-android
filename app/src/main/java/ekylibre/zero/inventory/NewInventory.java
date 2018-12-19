package ekylibre.zero.inventory;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import java.util.ArrayList;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.MainZoneAdapter;


public class NewInventory extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int inventory_type =1;



   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_new_current_inventory);
       setTitle("Nouvel Inventaire");
   }


    public void fillDBtest() {
        ContentValues mNewValues = new ContentValues();
        for (int i=0;i<20;i++){ //Modifier les valeurs de i par rapport au nombre de lignes de la table
            int CategoryId = i;
            String CategoryName = "Category_"+i;
            mNewValues.put(ZeroContract.CategoryColumns.CATEGORY_ID, CategoryId);
            mNewValues.put(ZeroContract.CategoryColumns.CATEGORY_NAME, CategoryName);
            getContentResolver().insert(
                    ZeroContract.Category.CONTENT_URI,   // the user dictionary content URI
                    mNewValues                          // the values to insert
            );
            this.finish();
        }
    }
}




