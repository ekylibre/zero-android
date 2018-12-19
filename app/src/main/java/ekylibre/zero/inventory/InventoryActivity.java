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

import java.util.ArrayList;
import ekylibre.database.ZeroContract;
import ekylibre.util.AccountTool;
import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.MainZoneAdapter;

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

public class InventoryActivity extends AppCompatActivity implements SelectZoneDialogFragment.OnFragmentInteractionListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    int inventory_type =1;
    private SelectZoneDialogFragment selectZoneDialogFragment;

    //String [] type_inventory = new String[3] ;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_list_zones);
       setTitle("Nouvel Inventaire");

       //fillDBtest();
       //queryZone();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);



        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        final ArrayList<ItemZoneInventory> listeZone = new ArrayList<ItemZoneInventory>();

        for (int i=0;i<20;i++){
            listeZone.add(new ItemZoneInventory("date"+i,"zone"+i,null));
            Log.i("Mytag"," "+listeZone);
        }
       // specify an adapter (see also next example)
       mAdapter = new MainZoneAdapter(listeZone);
       mRecyclerView.setAdapter(mAdapter);


       final Switch regularSwitch = (Switch) findViewById(R.id.switchInventoryRegular);
       final Switch packageSwitch = (Switch) findViewById(R.id.switchInventoryPackage);
       final Switch yearSwitch = (Switch) findViewById(R.id.switchInventoryYear);
       class MyButtonClickListener implements View.OnClickListener {
           @Override
           public void onClick(View _buttonView) {
               if (_buttonView.getId() == R.id.switchInventoryRegular) {
                   Boolean switchState = regularSwitch.isChecked();
                   inventory_type=1;
                   if (switchState == false) {
                       regularSwitch.setChecked(true);
                       packageSwitch.setChecked(false);
                       yearSwitch.setChecked(false);
                   }
                   else if (switchState == true){
                       yearSwitch.setChecked(false);
                       packageSwitch.setChecked(false);
                   }
               }
               if (_buttonView.getId() == R.id.switchInventoryPackage) {
                   Boolean switchStatepack = packageSwitch.isChecked();
                   inventory_type=2;
                   if (switchStatepack == false) {
                       regularSwitch.setChecked(false);
                       packageSwitch.setChecked(true);
                       yearSwitch.setChecked(false);
                   }
                   else if (switchStatepack == true){
                       regularSwitch.setChecked(false);
                       yearSwitch.setChecked(false);
                   }
               }
               if (_buttonView.getId() == R.id.switchInventoryYear) {
                   Boolean switchStateyear = yearSwitch.isChecked();
                   inventory_type=3;
                   if (switchStateyear == false) {
                       regularSwitch.setChecked(false);
                       packageSwitch.setChecked(false);
                       yearSwitch.setChecked(true);
                   }
                   else if (switchStateyear == true){
                       regularSwitch.setChecked(false);
                       packageSwitch.setChecked(false);
                   }
               }
               Log.i("mytag", "inv_type : "+inventory_type);
           }
       }
       regularSwitch.setOnClickListener(new MyButtonClickListener());
       yearSwitch.setOnClickListener(new MyButtonClickListener());
       packageSwitch.setOnClickListener(new MyButtonClickListener());

       TextView addInventoryZone = findViewById(R.id.AddInventoryZone);
       addInventoryZone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View addInventoryZone) {
               selectZoneDialogFragment = SelectZoneDialogFragment.newInstance(listeZone);
               selectZoneDialogFragment.show(getFragmentTransaction(),"dialog");
           }
       });

   }

   public void fillDBtest() {
       ContentValues mNewValues = new ContentValues();
       for (int i=0;i<20;i++){
           int zoneId = i;
           String zoneName = "zone_"+i;
           String zoneShape = null ;
           mNewValues.put(ZeroContract.ZoneStockColumns.ZONE_STOCK_ID, zoneId);
           mNewValues.put(ZeroContract.ZoneStockColumns.NAME, zoneName);
           getContentResolver().insert(
                   ZeroContract.ZoneStock.CONTENT_URI,   // the user dictionary content URI
                   mNewValues                          // the values to insert
           );
           this.finish();
       }
   }

    private void queryZone() {
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

        cursorZone.close();
    }



/*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ui_list_zones, container, false);
        type_inventory[0] = "Inventaire courant";
        type_inventory[1] = "Reception de colis";
        type_inventory[2] = "Inventaire annuel";
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MainZoneAdapter(type_inventory,));
        }
        return view;
    }*/


    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_list_zones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/



        /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    @Override
    public void onFragmentInteraction(Object object) { }

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



