package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.util.MarginTopItemDecoration;
import ekylibre.util.Translate;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.FormPeriodAdapter;
import ekylibre.zero.inter.model.Period;

public class InterventionFormFragment  extends Fragment {

    private static final String TAG = "InterventionFormFragment";
    private OnFragmentInteractionListener listener;
    private Context context;
    private List<Period> periodList;

    public InterventionFormFragment() {}

    public static InterventionFormFragment newInstance() {
        return new InterventionFormFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Get context when fragment is attached
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set title
        InterActivity.actionBar.setTitle(Translate.getStringId(context, InterActivity.selectedProcedure.name));

        // Add one hour period as default
        periodList = new ArrayList<>();
        periodList.add(new Period());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intervention_form, container, false);

        // Period layout

        RecyclerView recyclerView = view.findViewById(R.id.form_period_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new MarginTopItemDecoration(context, 16));
        FormPeriodAdapter periodAdapter = new FormPeriodAdapter(periodList);
        recyclerView.setAdapter(periodAdapter);
        TextView addPeriod = view.findViewById(R.id.form_period_add);
        addPeriod.setOnClickListener(v -> {
            periodList.add(new Period());
            periodAdapter.notifyDataSetChanged();
        });

        // Crop layout

        TextView addCrop = view.findViewById(R.id.form_crop_add);
        addCrop.setOnClickListener(v -> {
            listener.onCropChoice();
        });

//        culturesCount = 0;
//        for (CultureItem culture : culturesList) {
//            if (culture.is_selected) {
//                culturesCount++;
//                Chip chip = new Chip(context);
//                chip.setText(culture.name);
//                chip.setCloseIconVisible(true);
//                chip.setOnCloseIconClickListener(v -> {
//                    culturesChipsGroup.removeView(chip);
//                    culture.is_selected = false;
//                    if (--culturesCount == 0)
//                        culturesChipsGroup.setVisibility(View.GONE);
//                });
//                culturesChipsGroup.addView(chip);
//            }
//        }
//        culturesChipsGroup.setVisibility(culturesCount > 0 ? View.VISIBLE : View.GONE);

        return view;
    }

    public interface OnFragmentInteractionListener {
        void onCropChoice();
    }
}
