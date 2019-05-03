package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.database.ZeroContract;
import ekylibre.util.MarginTopItemDecoration;
import ekylibre.util.Translate;
import ekylibre.util.layout.component.WidgetParamView;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.FormPeriodAdapter;
import ekylibre.zero.inter.model.Period;
import ekylibre.zero.inter.model.SimpleSelectableItem;

import static ekylibre.zero.inter.InterActivity.CROP_CHOICE_FRAGMENT;
import static ekylibre.zero.inter.InterActivity.selectedProcedure;
import static ekylibre.zero.inter.enums.ParamType.DRIVER;
import static ekylibre.zero.inter.enums.ParamType.LAND_PARCEL;
import static ekylibre.zero.inter.enums.ParamType.PLANT;
import static ekylibre.zero.inter.enums.ParamType.SOWER;
import static ekylibre.zero.inter.enums.ParamType.TRACTOR;


public class InterventionFormFragment extends Fragment {

    private static final String TAG = "FormFragment";
    private OnFragmentInteractionListener listener;
    private Context context;
    private List<Period> periodList;
//    static List<SimpleSelectableItem> cropParcelList;
    public static List<SimpleSelectableItem> paramsList;

    // LAYOUT BINDINGS
    @BindView(R.id.widgets_container)
    LinearLayoutCompat widgetContainer;

    @BindView(R.id.form_period_recycler) RecyclerView periodRecycler;
    @BindView(R.id.form_period_add) TextView addPeriod;

//    @BindView(R.id.include_widget_crop) View cropWidget;
//    @BindView(R.id.form_crop_chips_group) ChipGroup cropChipGroup;
//    @BindView(R.id.form_crop_add) TextView addCrop;

    public static InterventionFormFragment newInstance() {
        return new InterventionFormFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Get context when fragment is attached
        this.context = context;

        if (context instanceof OnFragmentInteractionListener)
            listener = (OnFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");



        // Add one hour period as default
        periodList = new ArrayList<>();
        periodList.add(new Period());

//        // Init cropParcelList
//        cropParcelList = new ArrayList<>();
//        cropParcelList.addAll(getPlants());

        paramsList = new ArrayList<>();
        paramsList.addAll(getUsers());
        paramsList.addAll(getTools());
        paramsList.addAll(getPlants());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set title
        InterActivity.actionBar.setTitle(Translate.getStringId(context, InterActivity.selectedProcedure.name));

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_intervention_form, container, false);
        ButterKnife.bind(this, view);

        // ------------- //
        // Period layout //
        // ------------- //

        // Set RecyclerView and associated Adapter
        periodRecycler.setLayoutManager(new LinearLayoutManager(context));
        periodRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
        FormPeriodAdapter periodAdapter = new FormPeriodAdapter(periodList);
        periodRecycler.setAdapter(periodAdapter);

        addPeriod.setOnClickListener(v -> {
            periodList.add(new Period());
            periodAdapter.notifyDataSetChanged();
        });

        // ----------- //
        // Crop layout //
        // ----------- //

        // Check wether to display crop, parcel or both selector
        for (GenericEntity target : selectedProcedure.target) {
            switch (target.name) {

                case "plant":
                    widgetContainer.addView(new WidgetParamView(context, listener, PLANT, paramsList));
                    break;

                case "land_parcel":
                    widgetContainer.addView(new WidgetParamView(context, listener, LAND_PARCEL, paramsList));
                    break;

                case "cultivation":
                    if (target.filter.contains("is plant") && target.filter.contains("is land_parcel")) {
                        View cropParcelView = inflater.inflate(R.layout.widget_param_layout, container, false);
                        TextView label = cropParcelView.findViewById(R.id.widget_label);
                        label.setText(R.string.crop_parcel);
                        TextView addButton = cropParcelView.findViewById(R.id.widget_add);
                        ChipGroup cropChipGroup = cropParcelView.findViewById(R.id.widget_chips_group);
                        addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(CROP_CHOICE_FRAGMENT));

                        // ChipGroup Logic
                        for (SimpleSelectableItem item : paramsList) {
                            if ((item.type.equals(PLANT) || item.type.equals(LAND_PARCEL)) && item.isSelected) {
                                Chip chip = new Chip(context);
                                chip.setText(item.name);
                                chip.setCloseIconVisible(true);
                                chip.setOnCloseIconClickListener(v -> {
                                    cropChipGroup.removeView(chip);
                                    paramsList.get(paramsList.indexOf(item)).isSelected = false;
                                  chipGroupDisplay(cropChipGroup);
                                });
                                cropChipGroup.addView(chip);
                            }
                        }
                        chipGroupDisplay(cropChipGroup);
                        widgetContainer.addView(cropParcelView);
                    }
                    break;
            }
        }

        // ------------- //
        // Params layout //
        // ------------- //

        for (GenericEntity entity : selectedProcedure.input) {
            Log.i(TAG, "input --> " + entity.name);

            // Get layout
            View inputView = inflater.inflate(R.layout.widget_input, container, false);

            // Set param label
            TextView label = inputView.findViewById(R.id.widget_label);
            @StringRes final int labelRes = getResources().getIdentifier(entity.name, "string", context.getPackageName());
            label.setText(labelRes);

            // Add onClick listener
            TextView addButton = inputView.findViewById(R.id.widget_add);
            addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(entity.name));

            // Add view
            widgetContainer.addView(inputView);
        }

        for (GenericEntity entity : selectedProcedure.doer) {
            Log.i(TAG, "doer --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity.name, paramsList));
        }

        for (GenericEntity entity : selectedProcedure.tool) {
            Log.i(TAG, "tool --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity.name, paramsList));
        }

//        //        @Type
////        List<String> interventionParams = Arrays.asList(DRIVER, TRACTOR);
//
//        for (@Type String type : interventionParams)
//            widgetContainer.addView(new WidgetParamView(context, listener, type, paramsList));

        return view;
    }

    private void chipGroupDisplay(ChipGroup group) {
        boolean itemCounter = false;
        for (SimpleSelectableItem item : paramsList)
            if ((item.type.equals(PLANT) || item.type.equals(LAND_PARCEL)) && item.isSelected) {
                itemCounter = true;
                break;
            }
        group.setVisibility(itemCounter ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private List<SimpleSelectableItem> getPlants() {

        List<SimpleSelectableItem> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI, ZeroContract.Plants.PROJECTION_OBS,
                ZeroContract.Plants.USER + " LIKE " + "\"" + InterActivity.account.name + "\""
                        + " AND " + ZeroContract.Plants.ACTIVE + " == " + 1,
                null, ZeroContract.Issues.SORT_ORDER_DEFAULT);

        if (cursor != null) {
            try {
                while (cursor.moveToNext())
                    list.add(new SimpleSelectableItem(cursor.getInt(1), cursor.getString(2), PLANT, false));
            } finally {
                cursor.close();
            }

            // sorting the List
            Collections.sort(list, (ele1, ele2) -> {
                Collator localeCollator = Collator.getInstance(Locale.FRANCE);
                return localeCollator.compare(ele1.name, ele2.name);
            });
        }
        return list;
    }

    private List<SimpleSelectableItem> getUsers() {
        List<SimpleSelectableItem> list = new ArrayList<>();
        list.add(new SimpleSelectableItem(1, "Michel", DRIVER, true));
        list.add(new SimpleSelectableItem(2, "Jean", DRIVER));
        list.add(new SimpleSelectableItem(3, "Jacques", DRIVER));
        list.add(new SimpleSelectableItem(4, "Bob", DRIVER));

        Collections.sort(list, (ele1, ele2) -> {
            Collator localeCollator = Collator.getInstance(Locale.FRANCE);
            return localeCollator.compare(ele1.name, ele2.name);
        });

        return list;
    }

    private List<SimpleSelectableItem> getTools() {
        List<SimpleSelectableItem> list = new ArrayList<>();
        list.add(new SimpleSelectableItem(1, "Massey 6700 S", TRACTOR));
        list.add(new SimpleSelectableItem(2, "Sem 360 +", SOWER));
        list.add(new SimpleSelectableItem(3, "New FR250", TRACTOR));
        list.add(new SimpleSelectableItem(4, "Multigrains 500L", TRACTOR));

        Collections.sort(list, (ele1, ele2) -> {
            Collator localeCollator = Collator.getInstance(Locale.FRANCE);
            return localeCollator.compare(ele1.name, ele2.name);
        });

        return list;
    }

    public interface OnFragmentInteractionListener {
        void onFormFragmentInteraction(String fragmentTag);
    }

}
