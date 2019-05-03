package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.zero.R;
import ekylibre.zero.inter.model.ItemWithQuantity;


public class QuantityItemAdapter extends RecyclerView.Adapter<QuantityItemAdapter.ViewHolder> {

    private final List<ItemWithQuantity> dataset;

    public QuantityItemAdapter(List<ItemWithQuantity> dataset) {
        this.dataset = dataset;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Layout
        @BindView(R.id.item_label) TextView name;
        @BindView(R.id.quantity_value) TextView quatity;
        @BindView(R.id.quantity_unit) TextView unit;
        @BindView(R.id.item_warning_message) TextView warningMessage;
        @BindView(R.id.item_delete) TextView deleteButton;

        // Item reference
        ItemWithQuantity item;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);

            // Bind view to ButterKnife
            ButterKnife.bind(this, itemView);

            // Get context from view
            context = itemView.getContext();

            // Set click listeners
            deleteButton.setOnClickListener(v -> {
                if (getItemCount() > 1) {
                    int index = dataset.indexOf(item);
                    dataset.remove(index);
                    notifyItemRemoved(index);
                } else {
                    displayWarningDialog(context);
                }
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
            quatity.setText(item.quantity != null ? item.quantity.toString() : "");
            unit.setText(item.unit);
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
