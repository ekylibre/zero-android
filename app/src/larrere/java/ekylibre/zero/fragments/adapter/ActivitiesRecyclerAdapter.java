package ekylibre.zero.fragments.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ekylibre.zero.R;
import ekylibre.zero.fragments.ActivityChoiceFragment.OnListFragmentInteractionListener;
import ekylibre.zero.fragments.model.ActivityItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ActivityItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ActivitiesRecyclerAdapter extends RecyclerView.Adapter<ActivitiesRecyclerAdapter.ViewHolder> {

    private final List<ActivityItem> activityList;
    private final OnListFragmentInteractionListener mListener;
    int selected_position = 0;
    private static final String TAG = "ActivitiesRecycler";

    public ActivitiesRecyclerAdapter(List<ActivityItem> items, OnListFragmentInteractionListener listener) {
        activityList = items;
        mListener = listener;
    }

    /**
     * The item ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final TextView nameTextView;
        final TextView detailsTextView;
        ActivityItem mItem;


        ViewHolder(View view) {
            super(view);
            mView = view;
            nameTextView = view.findViewById(R.id.activity_name);
            detailsTextView = view.findViewById(R.id.activity_details);
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + detailsTextView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            Log.e(TAG, "Clicked");
            // Updating old as well as new positions
//            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            mItem.is_selected = !mItem.is_selected;
            notifyItemChanged(selected_position);
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
        ActivityItem item = activityList.get(position);
        holder.mItem = item;
        holder.nameTextView.setText(item.name);
        holder.detailsTextView.setText(item.details);
        if (item.is_selected)
            holder.mView.setBackgroundResource(R.color.basic_grey);
        else
            holder.mView.setBackgroundResource(R.color.background_grey);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
