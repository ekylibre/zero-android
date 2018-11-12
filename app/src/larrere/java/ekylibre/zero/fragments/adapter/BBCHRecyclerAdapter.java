package ekylibre.zero.fragments.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.fragments.BBCHChoiceFragment.OnBBCHFragmentInteractionListener;
import ekylibre.zero.fragments.model.BBCHItem;

import static ekylibre.zero.ObservationActivity.selectedActivity;
import static ekylibre.zero.ObservationActivity.selectedBBCH;


public class BBCHRecyclerAdapter extends RecyclerView.Adapter<BBCHRecyclerAdapter.ViewHolder> {

    private final List<BBCHItem> bbchList;
    private final OnBBCHFragmentInteractionListener fragmentListener;

    public BBCHRecyclerAdapter(List<BBCHItem> items, OnBBCHFragmentInteractionListener listener) {
        bbchList = items;
        fragmentListener = listener;
    }

    /**
     * The item ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View view;
        final Context context;
        final TextView nameTextView;
        BBCHItem bbchItem;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            nameTextView = view.findViewById(R.id.item_issue_name);
            // Attach onClickListener
            view.setOnClickListener(this);
        }

        void display(int position) {
            bbchItem = bbchList.get(position);
            int backgroundId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
            int textColor = R.color.primary_text;
            if (selectedBBCH != null && bbchItem.id == selectedBBCH.id) {
                backgroundId = R.color.basic_blue;
                textColor = R.color.white;
            }
            nameTextView.setTextColor(ContextCompat.getColor(context, textColor));
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            nameTextView.setText(bbchItem.name);
        }

        @Override
        public void onClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            // Send back item to activity
            view.postDelayed(() -> fragmentListener.onBBCHInteraction(bbchItem), 200);
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
        holder.display(position);
    }

    @Override
    public int getItemCount() {
        return bbchList.size();
    }
}