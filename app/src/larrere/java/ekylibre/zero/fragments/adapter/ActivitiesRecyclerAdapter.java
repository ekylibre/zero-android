package ekylibre.zero.fragments.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ekylibre.zero.R;
import ekylibre.zero.fragments.ActivityChoiceFragment;
import ekylibre.zero.fragments.model.ActivityItem;

import java.util.List;

import static ekylibre.zero.ObservationActivity.getActivityLogo;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ActivityItem} and makes a call to the
 * specified {@link ActivityChoiceFragment.OnActivityFragmentInteractionListener}.
 */
public class ActivitiesRecyclerAdapter extends RecyclerView.Adapter<ActivitiesRecyclerAdapter.ViewHolder> {

    private final List<ActivityItem> activityList;
    private final ActivityChoiceFragment.OnActivityFragmentInteractionListener fragmentListener;

    public ActivitiesRecyclerAdapter(List<ActivityItem> items, ActivityChoiceFragment.OnActivityFragmentInteractionListener listener) {
        activityList = items;
        fragmentListener = listener;
    }

    /**
     * The item ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        Context context;
        TextView nameTextView;
        TextView detailsTextView;
        ImageView iconImageView;
        ActivityItem activity;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            nameTextView = view.findViewById(R.id.activity_name);
            detailsTextView = view.findViewById(R.id.activity_details);
            iconImageView = view.findViewById(R.id.activity_icon);
            // Attach onClickListener
            view.setOnClickListener(this);
        }

        void display(ActivityItem item, int backgroundId) {
            activity = item;
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            nameTextView.setText(item.name);
            detailsTextView.setText(item.details);
            iconImageView.setImageResource(getActivityLogo(item.name));
        }

        @Override
        public void onClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            // Send back item to activity
            view.postDelayed(() -> fragmentListener.onActivityInteraction(activity), 200);
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int backgroundId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
        holder.display(activityList.get(position), backgroundId);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
