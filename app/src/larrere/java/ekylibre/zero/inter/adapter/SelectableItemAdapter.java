package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.inter.model.GenericItem;


public class SelectableItemAdapter extends RecyclerView.Adapter<SelectableItemAdapter.ViewHolder> {

    private final List<GenericItem> dataset;
    private final String paramType;
    private final String role;

    // CONTRUCTOR
    public SelectableItemAdapter(List<GenericItem> dataset, String paramType, String role) {
        this.dataset = dataset;
        this.paramType = paramType;
        this.role = role;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_first_line) TextView firstLine;
        @BindView(R.id.item_second_line) TextView secondLine;
        @BindView(R.id.item_third_line) TextView thirdLine;
        GenericItem item;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (item.referenceName.containsKey(paramType)) {
//                    item.isSelected = false;
                    v.setSelected(false);
                    item.referenceName.remove(paramType);
                } else {
//                    item.isSelected = true;
                    v.setSelected(true);
                    item.referenceName.put(paramType, role);
                    if (BuildConfig.DEBUG)
                        Log.i("Adapter", "Item role ("+role+") and type ("+ paramType+")");
                }
                notifyItemChanged(dataset.indexOf(item));
            });
        }

        void display(int position) {

            Context context = itemView.getContext();
            item = dataset.get(position);

            @ColorRes int colorId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
            @ColorRes int textColor = R.color.primary_text;
            if (item.referenceName.containsKey(paramType)) {
                colorId = R.color.basic_blue;
                textColor = R.color.white;
            }

            firstLine.setText(item.name);
            firstLine.setTextColor(ContextCompat.getColor(context, textColor));

            StringBuilder sb = new StringBuilder();

            if (item.netSurfaceArea != null)
                sb.append(item.netSurfaceArea);
            else if (item.workNumber != null)
                sb.append(item.workNumber);

            if (item.number != null) {
                if (sb.length() > 0)
                    sb.append(" - ");
                sb.append(item.number);
            }

            if (item.containerName != null) {
                if (sb.length() > 0)
                    sb.append(" - ");
                sb.append(item.containerName);
            }

            secondLine.setText(sb.toString());
            secondLine.setTextColor(ContextCompat.getColor(context, textColor));
            itemView.setBackgroundColor(ContextCompat.getColor(context, colorId));

            if (item.population != null) {
                thirdLine.setText(item.population);
                thirdLine.setVisibility(View.VISIBLE);
            } else
                thirdLine.setVisibility(View.GONE);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_two_lines, parent, false);
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
