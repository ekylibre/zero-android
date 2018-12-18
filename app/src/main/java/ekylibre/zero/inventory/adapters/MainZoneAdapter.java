package ekylibre.zero.inventory.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ekylibre.zero.R;
import ekylibre.zero.inventory.ItemZoneInventory;

public class MainZoneAdapter extends RecyclerView.Adapter<MainZoneAdapter.ViewHolder> {
    private ArrayList<ItemZoneInventory> listeZone;

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
    public MainZoneAdapter(ArrayList<ItemZoneInventory> listez) {
        listeZone = listez;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.display(listeZone.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listeZone.size();
    }
}