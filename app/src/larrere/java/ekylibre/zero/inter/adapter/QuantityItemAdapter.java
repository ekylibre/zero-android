package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.InterventionFormFragment;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.ItemWithQuantity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class QuantityItemAdapter extends RecyclerView.Adapter<QuantityItemAdapter.ViewHolder> {

    private final List<ItemWithQuantity> dataset;
    private final RecyclerView recyclerView;
    private final String role;

    public QuantityItemAdapter(List<ItemWithQuantity> dataset, RecyclerView recyclerView, String role) {
        this.dataset = dataset;
        this.recyclerView = recyclerView;
        this.role = role;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Layout
        @BindView(R.id.item_label) TextView name;
        @BindView(R.id.quantity_value) EditText quantity;
        @BindView(R.id.quantity_unit) TextView unit;
        @BindView(R.id.item_warning_message) TextView warningMessage;
        @BindView(R.id.item_delete) ImageView deleteButton;

        // Set quantity click listeners
        @OnTextChanged(R.id.quantity_value)
        void onQuantityChanged(CharSequence value, int start, int count, int after) {
            item.quantity = Float.valueOf(value.length() > 0 ? value.toString() : "0");
        }

        // Item reference
        ItemWithQuantity item;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);

            // Bind view to ButterKnife
            ButterKnife.bind(this, itemView);

            // Get context from view
            context = itemView.getContext();

            // Set delete click listeners
            deleteButton.setOnClickListener(v -> {
                int index = dataset.indexOf(item);
                dataset.remove(index);
                notifyItemRemoved(index);
                for (GenericItem simpleItem : InterventionFormFragment.paramsList) {
                    if (simpleItem.id == item.id && simpleItem.referenceName.contains(role)) {
                        simpleItem.referenceName.remove(role);
                        break;
                    }
                }
                if (dataset.isEmpty())
                    recyclerView.setVisibility(GONE);
            });

        }
        
        /**
         * The methode in charge of displaying an item
         * @param item The current Period item
         */
        void display(ItemWithQuantity item) {

            // Save reference of the current item
            this.item = item;
            // Set fields according to current item
            name.setText(item.name != null ? item.name : "");
            quantity.setText(item.quantity != null ? item.quantity.toString() : "");
            if (item.unit != null)
                if (!item.unit.equals("null")) {
                    @StringRes final int labelRes = context.getResources().getIdentifier(
                            item.unit, "string", context.getPackageName());
                    unit.setText(labelRes);
                    unit.setVisibility(VISIBLE);
                } else {
                    unit.setVisibility(GONE);
                }
            else {
                unit.setVisibility(GONE);
            }
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_with_quantity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.display(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private void displayWarningDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("L'intervention doit comporter au moins une période. Vous ne pouvez supprimer cette dernière.");
        builder.setPositiveButton("Ok", (dialog, i) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
