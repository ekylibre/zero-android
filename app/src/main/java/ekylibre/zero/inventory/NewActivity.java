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
import ekylibre.zero.inventory.adapters.UiSelectProductAdapter;

/*@Override
public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_choice, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new ActivitiesRecyclerAdapter(activitiesList, fragmentListener));
        }
        return view;
        }*/

public class NewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int inventory_type =1;
    ArrayList<ItemProductInventory> listProduct = new ArrayList<>();
    byte[] image;

    //private List<ItemZoneInventory> listeZone = new ArrayList<>();

    //String [] type_inventory = new String[3] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_list_products);

        // specify an adapter (see also next example)

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
}