package ekylibre.zero.fragments.adapter;

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

public class IssuesRecyclerAdapter extends RecyclerView.Adapter<IssuesRecyclerAdapter.ViewHolder> {

    private final List<IssueItem> issuesList;

    public IssuesRecyclerAdapter(List<IssueItem> items) {
        issuesList = items;
    }

    /**
     * The item ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View view;
        final Context context;
        final TextView nameTextView;
        final TextView detailsTextView;
        IssueItem cultureItem;
        int pos;


        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            nameTextView = itemView.findViewById(R.id.item_culture_name);
            detailsTextView = itemView.findViewById(R.id.item_culture_details);
            view.setOnClickListener(this);
        }

        void display(int position, int backgroundId) {
            pos = position;
            cultureItem = issuesList.get(position);
            int textColor = R.color.primary_text;
            int detailsColor = R.color.secondary_text;
            if (cultureItem.is_selected) {
                backgroundId = R.color.basic_blue;
                textColor = R.color.white;
                detailsColor = R.color.white;
            }
            // Set colors
            nameTextView.setTextColor(ContextCompat.getColor(context, textColor));
            detailsTextView.setTextColor(ContextCompat.getColor(context, detailsColor));
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            // Set text
            nameTextView.setText(cultureItem.name);
            detailsTextView.setText(cultureItem.details);
        }

        @Override
        public void onClick(View v) {
            view.setSelected(!view.isSelected());
            cultureItem.is_selected = !cultureItem.is_selected;
            notifyItemChanged(pos);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_culture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int backgroundId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
        holder.display(position, backgroundId);
    }

    @Override
    public int getItemCount() {
        return issuesList.size();
    }
}
