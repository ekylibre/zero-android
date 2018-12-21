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
import ekylibre.zero.inventory.ItemType;
import ekylibre.zero.inventory.SelectCategoryDialogFragment;
import ekylibre.zero.inventory.SelectTypeDialogFragment;

public class UiSelectTypeAdapter extends RecyclerView.Adapter<UiSelectTypeAdapter.ViewHolder> {

    List<ItemType> listType;
    SelectTypeDialogFragment.OnFragmentInteractionListener fragmentListener;

    public UiSelectTypeAdapter(List<ItemType> dataset, SelectTypeDialogFragment.OnFragmentInteractionListener listener) {
        this.listType = dataset;
        fragmentListener=listener;

    }

    /**
     * ViewHolder : défini l'affichage de l'item
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView typeImageView;
        TextView typeNameView;
        ItemType item;


        ViewHolder(View itemView) {
            super(itemView);

            typeImageView = itemView.findViewById(R.id.select_zone_item_image);
            typeNameView = itemView.findViewById(R.id.select_zone_item_name);
            Log.i("MyTag","ViewHolder");
            itemView.setOnClickListener(this);

        }


        void display(ItemType item) {
            typeImageView.setImageResource(R.drawable.action_travel);
            typeNameView.setText(item.name);
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
    public UiSelectTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_zone_item, viewGroup, false);
        Log.i("MyTag","onCreateViewHolder");
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UiSelectTypeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.display(listType.get(i));
        Log.i("MyTag","Bind item:"+listType.get(i));
    }

    @Override
    public int getItemCount() {
        Log.i("MyTag",""+listType.size());
        return listType.size();
    }
}
