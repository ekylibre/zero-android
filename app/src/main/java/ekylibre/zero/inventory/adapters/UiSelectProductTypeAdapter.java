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
import ekylibre.zero.inventory.ItemTypeInventory;
import ekylibre.zero.inventory.SelectProductCategoryFragment;
import ekylibre.zero.inventory.SelectProductTypeFragment;


public class UiSelectProductTypeAdapter extends RecyclerView.Adapter<UiSelectProductTypeAdapter.ViewHolder> {

    List<ItemTypeInventory> listType;
    SelectProductTypeFragment.OnTypeFragmentInteractionListener fragmentListener;


    public UiSelectProductTypeAdapter(List<ItemTypeInventory> dataset, SelectProductTypeFragment.OnTypeFragmentInteractionListener listener) {
        this.listType = dataset;
        fragmentListener=listener;

    }

    /**
     * ViewHolder : défini l'affichage de l'item
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView typeNameView;
        ItemTypeInventory item;
        ImageView typeImageView;
        Switch switchtype;

        ViewHolder(View itemView) {
            super(itemView);

            typeNameView= itemView.findViewById(R.id.select_type_item_name);
            typeImageView=itemView.findViewById(R.id.select_type_item_image);
            switchtype=itemView.findViewById(R.id.select_type_item_switch);
            Log.i("MyTag","ViewHolder");
            switchtype.setOnClickListener(this);
        }

        void display(ItemTypeInventory currentItem) {
            //typeImageView.setImageResource(R.drawable.ic_type_item);
            item = currentItem;
            typeNameView.setText(item.type);
            Log.i("MyTag","Display item:"+item);
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
    public UiSelectProductTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_type_item, viewGroup, false);
        Log.i("MyTag","onCreateViewHolder");
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UiSelectProductTypeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.display(listType.get(i));
        Log.i("MyTag","Bind item:"+listType.get(i));
    }

    @Override
    public int getItemCount() {
        Log.i("MyTag",""+listType.size());
        return listType.size();
    }


}
