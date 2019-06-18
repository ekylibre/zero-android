package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import ekylibre.zero.R;
import ekylibre.zero.inter.fragment.InterventionFormFragment.OnFragmentInteractionListener;
import ekylibre.zero.inter.model.Zone;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {

    private final List<Zone> dataset;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener listener;


    public ZoneAdapter(List<Zone> dataset, OnFragmentInteractionListener listener) {
        this.dataset = dataset;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // Layout
        @BindView(R.id.parcel_add) MaterialButton parcelButton;
        @BindView(R.id.crop_add) MaterialButton cropButton;
        @BindView(R.id.variety_value) EditText varietyEditText;
        @BindView(R.id.batch_number_value) EditText batchNumberEditText;
        @BindView(R.id.parcel_chip) Chip parcelChip;
        @BindView(R.id.crop_chip) Chip cropChip;
        @BindView(R.id.zone_delete) ImageView deleteButton;
        @BindView(R.id.group) Group group;


        @OnTextChanged(R.id.variety_value)
        void onVarietyChanged(CharSequence value) {
            zone.newName = value.toString();
        }

        @OnTextChanged(R.id.batch_number_value)
        void onBatchNumberChanged(CharSequence value) {
            zone.batchNumber = value.toString();
        }

        // Reference to the current zone
        Zone zone;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);

            // Bind view to ButterKnife
            ButterKnife.bind(this, itemView);

            // Get context from view
            context = itemView.getContext();

            if (getItemCount() > 0) {
                // Set delete click listeners
                deleteButton.setVisibility(VISIBLE);
                deleteButton.setOnClickListener(v -> {
                    int index = dataset.indexOf(zone);
                    dataset.remove(index);
                    notifyItemRemoved(index);
                });
            } else
                deleteButton.setVisibility(GONE);

            parcelButton.setOnClickListener(v -> {
                Log.e("Zone", "adapter position="+getAdapterPosition());
                listener.onFormFragmentInteraction("zone_land_parcel",
                        "is land_parcel", String.valueOf(getAdapterPosition()));
            });

            cropButton.setOnClickListener(v -> {
                Log.e("Zone", "adapter position="+getAdapterPosition());
                listener.onFormFragmentInteraction("zone_plant",
                        "is plant", String.valueOf(getAdapterPosition()));
            });
        }
        
        /**
         * The methode in charge of displaying an item
         * @param currentZone The current Zone item
         */
        void display(Zone currentZone) {

            // Save reference of the current item
            this.zone = currentZone;

            // Set Chips and fields visibility
            if (zone.landParcel != null) {
                parcelChip.setText(zone.landParcel.name);
                parcelChip.setVisibility(VISIBLE);
                parcelButton.setText("modifier");
            } else {
                parcelChip.setVisibility(GONE);
                parcelButton.setText(context.getString(R.string.add));
            }

            int visibility = zone.plant != null ? VISIBLE : GONE;
            if (visibility == VISIBLE) {
                cropChip.setText(zone.plant.name);
                cropButton.setText("modifier");
            } else {
                cropButton.setText(context.getString(R.string.add));
            }
            cropChip.setVisibility(visibility);
            group.setVisibility(visibility);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_zone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.e("Adapter", "dataset count = " + getItemCount());
        holder.display(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
