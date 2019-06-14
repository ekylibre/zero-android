package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.ProcedureChoiceFragment.OnFragmentInteractionListener;

public class ProcedureChoiceAdapter extends RecyclerView.Adapter<ProcedureChoiceAdapter.ViewHolder> {

    private final List<Pair<String,String>> dataset;
    private final OnFragmentInteractionListener listener;
    private final String fragmentTag;

    public ProcedureChoiceAdapter(List<Pair<String,String>> items,
                                  OnFragmentInteractionListener fraglistener,
                                  String tag) {
        dataset = items;
        listener = fraglistener;
        fragmentTag = tag;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        Context context;
        TextView textView;
        Pair<String,String> item;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            context = view.getContext();
            textView = view.findViewById(R.id.category_name);
            view.setOnClickListener(this);
        }

        void display(Pair<String,String> currentItem, int backgroundId) {
            item = currentItem;
            view.setBackgroundColor(ContextCompat.getColor(context, backgroundId));
            textView.setText(currentItem.second);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            // Send back item to activity
//            listener.onItemChoosed(item, fragmentTag);
            view.postDelayed(() -> listener.onItemChoosed(item, fragmentTag), 200);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        int backgroundId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
        holder.display(dataset.get(position), backgroundId);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
