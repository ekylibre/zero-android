package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.zero.R;
import ekylibre.zero.inter.model.SimpleSelectableItem;


public class SimpleSelectableItemAdapter extends RecyclerView.Adapter<SimpleSelectableItemAdapter.ViewHolder> {

    private final List<SimpleSelectableItem> dataset;

    // CONTRUCTOR
    public SimpleSelectableItemAdapter(List<SimpleSelectableItem> dataset) {
        this.dataset = dataset;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_label) TextView textView;
        SimpleSelectableItem item;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                item.isSelected = !item.isSelected;
                v.setSelected(!v.isSelected());
                notifyItemChanged(dataset.indexOf(item));
            });
        }

        void display(int position) {

            Context context = itemView.getContext();
            item = dataset.get(position);

            @ColorRes int colorId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
            @ColorRes int textColor = R.color.primary_text;
            if (item.isSelected) {
                colorId = R.color.basic_blue;
                textColor = R.color.white;
            }
            textView.setText(item.name);
            textView.setTextColor(ContextCompat.getColor(context, textColor));
            itemView.setBackgroundColor(ContextCompat.getColor(context, colorId));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_single_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
