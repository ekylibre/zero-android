package ekylibre.zero.inter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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

        @OnTextChanged(R.id.variety_value)
        void onVarietyChanged(CharSequence value) {
            zone.newName = value.toString();
        }

        @OnTextChanged(R.id.batch_number_value)
        void onBatchNumberChanged(CharSequence value) {
            zone.batchNumber = value.toString();
        }

        @OnClick(R.id.parcel_add)
        void onParcelAdd() {
            listener.onFormFragmentInteraction("land_parcel", "is land_parcel", "targets");
        }

        @OnClick(R.id.crop_add)
        void onCropAdd() {
            // TODO -> bind to selector fragment
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

            // Set delete click listeners TODO -> remove delete button in only one zone
            deleteButton.setOnClickListener(v -> {
                int index = dataset.indexOf(zone);
                dataset.remove(index);
                notifyItemRemoved(index);
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
            parcelChip.setVisibility(zone.landParcel == null ? GONE : VISIBLE);
            int cropVisibility = zone.plant == null ? GONE : VISIBLE;
            cropChip.setVisibility(cropVisibility);
            varietyEditText.setVisibility(cropVisibility);
            batchNumberEditText.setVisibility(cropVisibility);

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
        holder.display(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
