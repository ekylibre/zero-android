package ekylibre.zero.inventory.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inventory.ItemProductInventory;
import ekylibre.zero.inventory.ItemZoneInventory;
import ekylibre.zero.inventory.SelectZoneDialogFragment;

public class UiSelectProductAdapter extends RecyclerView.Adapter<UiSelectProductAdapter.ViewHolder> {

    List<ItemProductInventory> listProduct;

    public UiSelectProductAdapter(List<ItemProductInventory> dataset) {
        this.listProduct = dataset;

    }

    /**
     * ViewHolder : défini l'affichage de l'item
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productNameView;
        TextView productVariantView;
        TextView productDateView;
        TextView productQFixView;
        EditText productQEditView;
        ImageView productMinusView;
        ImageView productPlusView;
        ImageView productCommentView;
        ImageView productImageView;
        CheckBox productCheckView;
        ItemProductInventory item;


        ViewHolder(View itemView) {
            super(itemView);

            productNameView = itemView.findViewById(R.id.product_name);
            productVariantView = itemView.findViewById(R.id.product_variant);
            productDateView = itemView.findViewById(R.id.product_date);
            productQFixView = itemView.findViewById(R.id.product_qtt_fix);
            productQEditView = itemView.findViewById(R.id.product_qtt_edit);
            productMinusView = itemView.findViewById(R.id.product_minus_qtt);
            productPlusView = itemView.findViewById(R.id.product_plus_qtt);
            productCommentView = itemView.findViewById(R.id.add_comment);
            productImageView = itemView.findViewById(R.id.add_image);
            productCheckView = itemView.findViewById(R.id.product_checkbox);
            Log.i("MyTag","ViewHolder");
            productQEditView.setOnClickListener(this);
            productMinusView.setOnClickListener(this);
            productPlusView.setOnClickListener(this);
            productCommentView.setOnClickListener(this);
            productImageView.setOnClickListener(this);
            productCheckView.setOnClickListener(this);

        }


        void display(ItemProductInventory item) {

            this.item=item;
            productNameView.setText(item.productName);
            productVariantView.setText(item.productVariant);
            productDateView.setText(item.productDate);
            productQFixView.setText(item.productQuantity);
            productMinusView.setImageResource(R.drawable.ic_minus);
            productPlusView.setImageResource(R.drawable.ic_add_plus);
            productCommentView.setImageResource(R.drawable.ic_comment);
            productImageView.setImageResource(R.drawable.ic_image);
            productCheckView.setChecked(false);
            Log.i("MyTag","Display item:"+item);

        }



        @Override
        public void onClick(View view) {
            Log.i("MyTag", "click zone");
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
                    productCheckView.setChecked(true);
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
                        break;
            }
        }

    }

    @NonNull
    @Override
    public UiSelectProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item, viewGroup, false);
        Log.i("MyTag","onCreateViewHolder");
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UiSelectProductAdapter.ViewHolder viewHolder, int i) {
        viewHolder.display(listProduct.get(i));
        Log.i("MyTag","Bind item:"+listProduct.get(i));
    }

    @Override
    public int getItemCount() {
        Log.i("MyTag",""+listProduct.size());
        return listProduct.size();
    }
}
