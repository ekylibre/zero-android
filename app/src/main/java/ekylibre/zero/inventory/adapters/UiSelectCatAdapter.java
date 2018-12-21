package ekylibre.zero.inventory.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inventory.ItemCategory;
import ekylibre.zero.inventory.ItemZoneInventory;
import ekylibre.zero.inventory.SelectCategoryDialogFragment;
import ekylibre.zero.inventory.SelectZoneDialogFragment;

public class UiSelectCatAdapter extends RecyclerView.Adapter<UiSelectCatAdapter.ViewHolder> {

    List<ItemCategory> listCat;
    SelectCategoryDialogFragment.OnFragmentInteractionListener fragmentListener;

    public UiSelectCatAdapter(List<ItemCategory> dataset, SelectCategoryDialogFragment.OnFragmentInteractionListener listener) {
        this.listCat = dataset;
        fragmentListener=listener;

    }

    /**
     * ViewHolder : défini l'affichage de l'item
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView catImageView;
        TextView catNameView;
        ItemCategory item;


        ViewHolder(View itemView) {
            super(itemView);

            catImageView = itemView.findViewById(R.id.select_zone_item_image);
            catNameView = itemView.findViewById(R.id.select_zone_item_name);
            Log.i("MyTag","ViewHolder");
            itemView.setOnClickListener(this);

        }


        void display(ItemCategory item) {
            catImageView.setImageResource(R.drawable.action_travel);
            catNameView.setText(item.name);
            Log.i("MyTag","Display item:"+item);
            this.item=item;

        }


        @Override
        public void onClick(View view) {
            Log.i("MyTag", "click zone");
            fragmentListener.onFragmentInteraction(this.item);
        }
    }

    @NonNull
    @Override
    public UiSelectCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_zone_item, viewGroup, false);
        Log.i("MyTag","onCreateViewHolder");
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UiSelectCatAdapter.ViewHolder viewHolder, int i) {
        viewHolder.display(listCat.get(i));
        Log.i("MyTag","Bind item:"+listCat.get(i));
    }

    @Override
    public int getItemCount() {
        Log.i("MyTag",""+listCat.size());
        return listCat.size();
    }
}
