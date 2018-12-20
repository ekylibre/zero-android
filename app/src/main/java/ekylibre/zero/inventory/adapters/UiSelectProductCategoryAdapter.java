package ekylibre.zero.inventory.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inventory.ItemCategoryInventory;
import ekylibre.zero.inventory.ItemZoneInventory;
import ekylibre.zero.inventory.SelectProductCategoryFragment;
import ekylibre.zero.inventory.SelectZoneDialogFragment;


public class UiSelectProductCategoryAdapter extends RecyclerView.Adapter<UiSelectProductCategoryAdapter.ViewHolder> {

    List<ItemCategoryInventory> listCategory;
    SelectProductCategoryFragment.OnFragmentInteractionListener fragmentListener;


    public UiSelectProductCategoryAdapter(List<ItemCategoryInventory> dataset, SelectProductCategoryFragment.OnFragmentInteractionListener listener) {
        this.listCategory = dataset;
        fragmentListener=listener;

    }

    /**
     * ViewHolder : défini l'affichage de l'item
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView categoryNameView;
        ItemCategoryInventory item;
        ImageView categoryImageView;
        Switch switchcategory;

        ViewHolder(View itemView) {
            super(itemView);

            categoryNameView= itemView.findViewById(R.id.select_category_item_name);
            categoryImageView=itemView.findViewById(R.id.select_category_item_image);
            Log.i("MyTag","ViewHolder");
            switchcategory.setOnClickListener(this);
        }

        void display(ItemCategoryInventory currentItem) {
            categoryImageView.setImageResource(R.drawable.ic_category_item);
            categoryNameView.setText(item.category);
            Log.i("MyTag","Display item:"+item);
            item = currentItem;
        }

        @Override
        public void onClick(View view) {
            Log.i("MyTag", "click zone");
            fragmentListener.onFragmentInteraction(this.item);
            item.is_selected = !item.is_selected;
            }
        }


    @NonNull
    @Override
    public UiSelectProductCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_category_item, viewGroup, false);
        Log.i("MyTag","onCreateViewHolder");
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UiSelectProductCategoryAdapter.ViewHolder viewHolder, int i) {
        viewHolder.display(listCategory.get(i));
        Log.i("MyTag","Bind item:"+listCategory.get(i));
    }

    @Override
    public int getItemCount() {
        Log.i("MyTag",""+listCategory.size());
        return listCategory.size();
    }
}
