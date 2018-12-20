package ekylibre.zero.inventory.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inventory.InventoryActivity;
import ekylibre.zero.inventory.ItemZoneInventory;
import ekylibre.zero.inventory.NewInventory;

public class MainZoneAdapter extends RecyclerView.Adapter<MainZoneAdapter.ViewHolder> {
    private List<ItemZoneInventory> listeZone;
    public int inventory_type;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder  {

        View view;
        Context context;
        TextView nameZoneTextView;
        TextView dateTextView;
        ImageView iconImageView;


        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            nameZoneTextView = view.findViewById(R.id.zone_inventory_name);
            dateTextView = view.findViewById(R.id.inventory_date);
            iconImageView = view.findViewById(R.id.zone_inventory_item_image);
            // Attach onClickListener
         //   view.setOnClickListener(this);
        }

        void display(ItemZoneInventory item) {
            nameZoneTextView.setText(item.zone);
            dateTextView.setText(item.dateInventory);
           // iconImageView.setImageResource(item.image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainZoneAdapter(List<ItemZoneInventory> listez, int inventoryType) {
        listeZone = listez;
        inventory_type = inventoryType;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainZoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
               .inflate(R.layout.zone_inventory_item, parent, false);
       // RecyclerView v = (RecyclerView) LayoutInflater.from(parent.getContext())
              //  .inflate(R.layout.zone_inventory_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        //onBindViewHolder(vh,20);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.display(listeZone.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                 long itemId = position;
                 Intent intent = new Intent(v.getContext(),NewInventory.class);
                 intent.putExtra("itemId",itemId);
                 intent.putExtra("inventoryType", inventory_type);
                 Log.i("mytag"," id : "+itemId);
                 v.getContext().startActivity(intent);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listeZone.size();
    }
}