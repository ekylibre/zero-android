package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.inter.model.SimpleSelectableItem;


public class CropParcelAdapter extends RecyclerView.Adapter<CropParcelAdapter.ViewHolder> {

    private final List<SimpleSelectableItem> dataset;

    public CropParcelAdapter(List<SimpleSelectableItem> items) {
        dataset = items;
    }

    /**
     * The item ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        Context context;
        TextView textView;
        SimpleSelectableItem item;
        int pos;


        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            textView = itemView.findViewById(R.id.item_label);
            // Set Click listener
            view.setOnClickListener(this);
        }

        void display(int position, int backgroundId) {
            pos = position;
            item = dataset.get(position);
            int textColor = R.color.primary_text;
            if (item.isSelected) {
                backgroundId = R.color.basic_blue;
                textColor = R.color.white;
            }
            // Set colors
            textView.setTextColor(ContextCompat.getColor(context, textColor));
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            // Set text
            textView.setText(item.name);
        }

        @Override
        public void onClick(View v) {

//            if (item.isSelected)
//                InterventionFormFragment.paramsList.remove(item);
//            else
//                InterActivity.selectedCropParcels.add(item);

            item.isSelected = !item.isSelected;
            view.setSelected(!view.isSelected());
            notifyItemChanged(pos);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_single_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int backgroundId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
        holder.display(position, backgroundId);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
