package ekylibre.zero.inter.adapter;

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
import butterknife.OnClick;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.SimpleChoiceFragment.OnFragmentInteractionListener;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.Zone;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    private final List<GenericItem> dataset;
    private final OnFragmentInteractionListener listener;
    private final Zone zone;
    private final String filter;

    public SimpleAdapter(List<GenericItem> dataset, OnFragmentInteractionListener listener,
                         Zone zone, String filter) {
        this.dataset = dataset;
        this.listener = listener;
        this.zone = zone;
        this.filter = filter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        GenericItem item;

        @BindView(R.id.item_first_line)
        TextView firstLine;
        @BindView(R.id.item_second_line)
        TextView secondLine;
        @BindView(R.id.item_third_line)
        TextView thirdLine;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void display(int position) {

            // Save item reference
            item = dataset.get(position);

            // Set background odd and even
            @ColorRes int colorId = position %2 == 1 ? R.color.another_light_grey : R.color.white;
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), colorId));

            // Set text
            firstLine.setText(item.name);

            // Compute second line
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

            // Set third line if needed
            if (item.population != null) {
                thirdLine.setText(item.population);
                thirdLine.setVisibility(View.VISIBLE);
            } else
                thirdLine.setVisibility(View.GONE);
        }

        @OnClick
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;

            item.referenceName.put("zone", "targets");
            if (filter.equals("is plant"))
                zone.plant = item;
            else
                zone.landParcel = item;

            // Send back item to activity
//            listener.onItemChoosed(item, fragmentTag);
            view.postDelayed(listener::onItemChoosed, 200);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_generic, parent, false);
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
