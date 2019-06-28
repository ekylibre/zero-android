package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.util.pojo.HandlerEntity;
import ekylibre.util.pojo.SpinnerItem;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.InterventionFormFragment;
import ekylibre.zero.inter.model.GenericItem;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class QuantityItemAdapter extends RecyclerView.Adapter<QuantityItemAdapter.ViewHolder> {

    private final List<GenericItem> dataset;
    private RecyclerView recyclerView;
    private final String paramType;
    private final ArrayList<SpinnerItem> spinnerItems;

    public QuantityItemAdapter(List<GenericItem> dataset, GenericEntity inputType) {
        this.dataset = dataset;
        this.paramType = inputType.name;
        this.spinnerItems = computeSpinnerItems(inputType.handler);
    }

    private ArrayList<SpinnerItem> computeSpinnerItems(List<HandlerEntity> handlers) {
        ArrayList<SpinnerItem> spinnerList = new ArrayList<>();
        for (HandlerEntity handler : handlers) {
            if (handler.unit != null)
                spinnerList.add(new SpinnerItem(handler.unit, handler.indicator));
        }
        return spinnerList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Layout
        @BindView(R.id.item_label) TextView name;
        @BindView(R.id.quantity_value) EditText quantity;
//        @BindView(R.id.quantity_unit) TextView unit;
        @BindView(R.id.quantity_unit_spinner) Spinner unitSpinner;
        @BindView(R.id.item_warning_message) TextView warningMessage;
        @BindView(R.id.item_delete) ImageView deleteButton;

        // Set quantity click listeners
        @OnTextChanged(R.id.quantity_value)
        void onQuantityChanged(CharSequence value, int start, int count, int after) {
            item.quantity = new BigDecimal(value.length() > 0 ? value.toString() : "0");
        }

        // Item reference
        GenericItem item;
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
                    if (simpleItem.id == item.id && simpleItem.referenceName.containsKey(paramType)) {
                        simpleItem.referenceName.remove(paramType);
                        break;
                    }
                }
                if (dataset.isEmpty())
                    recyclerView.setVisibility(GONE);
            });

            unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onNothingSelected(AdapterView<?> parent) { }
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SpinnerItem selectedUnit = (SpinnerItem) parent.getSelectedItem();
                    item.unit = selectedUnit.getIndicator();
                }
            });

        }
        
        /**
         * The methode in charge of displaying an item
         * @param item The current Period item
         */
        void display(GenericItem item) {

            // Save reference of the current item
            this.item = item;
            // Set fields according to current item
            name.setText(item.name != null ? item.name : "");
            quantity.setText(item.quantity != null ? item.quantity.toString() : String.valueOf(0f));

            if (spinnerItems.size() > 0) {
                ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                unitSpinner.setAdapter(adapter);
                unitSpinner.setVisibility(VISIBLE);
                if (item.unit != null)
                    unitSpinner.setSelection(SpinnerItem.getIndex(spinnerItems, item.unit));
                else {
                    item.unit = spinnerItems.get(0).getValue();
                    unitSpinner.setSelection(0);
                }
            } else
                unitSpinner.setVisibility(GONE);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
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
