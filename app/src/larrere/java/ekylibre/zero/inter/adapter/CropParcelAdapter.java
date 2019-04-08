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
import ekylibre.zero.fragments.model.IssueItem;

import static ekylibre.zero.ObservationActivity.issuesList;

public class CropParcelAdapter extends RecyclerView.Adapter<CropParcelAdapter.ViewHolder> {

    private final List<IssueItem> dataset;

    public CropParcelAdapter(List<IssueItem> items) {
        dataset = items;
    }

    /**
     * The item ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View view;
        final Context context;
        final TextView nameTextView;
        IssueItem issueItem;
        int pos;


        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            nameTextView = itemView.findViewById(R.id.item_issue_name);
            view.setOnClickListener(this);
        }

        void display(int position, int backgroundId) {
            pos = position;
            issueItem = dataset.get(position);
            int textColor = R.color.primary_text;
            if (issueItem.is_selected) {
                backgroundId = R.color.basic_blue;
                textColor = R.color.white;
            }
            // Set colors
            nameTextView.setTextColor(ContextCompat.getColor(context, textColor));
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            // Set text
            nameTextView.setText(issueItem.label);
        }

        @Override
        public void onClick(View v) {
            view.setSelected(!view.isSelected());
            if (issueItem.is_selected) {
                issueItem.is_selected = false;
                for (IssueItem item : issuesList) {
                    if (item.label.equals(issueItem.label)) {
                        issuesList.remove(item);
                        break;
                    }
                }
            } else {
                issueItem.is_selected = true;
                issuesList.add(issueItem);
            }
//            issueItem.is_selected = !issueItem.is_selected;

            notifyItemChanged(pos);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_issue, parent, false);
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
