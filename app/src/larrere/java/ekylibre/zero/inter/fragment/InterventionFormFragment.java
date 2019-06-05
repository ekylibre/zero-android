package ekylibre.zero.inter.fragment;

import android.content.ContentResolver;
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
import ekylibre.database.ZeroContract.Equipments;
import ekylibre.database.ZeroContract.Inputs;
import ekylibre.database.ZeroContract.LandParcels;
import ekylibre.database.ZeroContract.Plants;
import ekylibre.database.ZeroContract.Workers;
import ekylibre.util.MarginTopItemDecoration;
import ekylibre.util.Translate;
import ekylibre.util.layout.component.WidgetParamView;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.home.Zero;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.FormPeriodAdapter;
import ekylibre.zero.inter.adapter.QuantityItemAdapter;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.Period;

import static ekylibre.zero.inter.InterActivity.selectedProcedure;
import static ekylibre.zero.inter.enums.ParamType.LAND_PARCEL;
import static ekylibre.zero.inter.enums.ParamType.PLANT;


public class InterventionFormFragment extends Fragment {

    private static final String TAG = "InterventionFormFragmen";
    private static final String likeAccountName = String.format("user LIKE \"%s\"", InterActivity.account.name);

    private Context context;
    private OnFragmentInteractionListener listener;

    private List<Period> periodList;
    public static List<GenericItem> paramsList;

    // LAYOUT BINDINGS
    @BindView(R.id.widgets_container) LinearLayoutCompat widgetContainer;
    @BindView(R.id.form_period_recycler) RecyclerView periodRecycler;
    @BindView(R.id.form_period_add) TextView addPeriod;


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

        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");

        ContentResolver cr = context.getContentResolver();

        // Add one hour period as default
        periodList = new ArrayList<>();
        periodList.add(new Period());

//        // Init cropParcelList
//        cropParcelList = new ArrayList<>();
//        cropParcelList.addAll(getPlants());

        paramsList = new ArrayList<>();
        paramsList.addAll(getUsers(cr));
        paramsList.addAll(getTools(cr));
        paramsList.addAll(getPlantsAndLandParcels(cr));
        paramsList.addAll(getInputs(cr));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateView");

        // Set title
        InterActivity.actionBar.setTitle(Translate.getStringId(context, InterActivity.selectedProcedure.name));

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_intervention_form, container, false);
        ButterKnife.bind(this, view);

        // ------------------ //
        // Time Period layout //
        // ------------------ //

        // Set RecyclerView and associated Adapter
        periodRecycler.setLayoutManager(new LinearLayoutManager(context));
        periodRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
        FormPeriodAdapter periodAdapter = new FormPeriodAdapter(periodList);
        periodRecycler.setAdapter(periodAdapter);

        addPeriod.setOnClickListener(v -> {
            periodList.add(new Period());
            periodAdapter.notifyDataSetChanged();
        });

        // ------------ //
        // Group layout //
        // ------------ //




        // ----------- //
        // Crop layout //
        // ----------- //

        // Check wether to display crop, parcel or both selector
        for (GenericEntity target : selectedProcedure.target) {
//            switch (target.name) {

//                case "land_parcel":
//                case "plant":
//                    widgetContainer.addView(new WidgetParamView(context, listener, target, paramsList));
//                    break;

//                case "cultivation":

            // Inflate the layout
            View cropParcelView = inflater.inflate(R.layout.widget_param_layout, container, false);
            TextView label = cropParcelView.findViewById(R.id.widget_label);

            // Set the title
            if (target.name.equals("cultivation"))
                label.setText(R.string.crop_parcel);
            else {
                @StringRes final int labelRes = getResources().getIdentifier(target.name, "string", Zero.getPkgName());
                label.setText(labelRes);
            }
            TextView addButton = cropParcelView.findViewById(R.id.widget_add);
            ChipGroup cropChipGroup = cropParcelView.findViewById(R.id.widget_chips_group);
            addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(target.name, target.filter));  // CROP_CHOICE_FRAGMENT

            // ChipGroup Logic
            for (GenericItem item : paramsList) {
                if ((item.type.equals(PLANT) || item.type.equals(LAND_PARCEL)) && item.isSelected) {
                    Chip chip = new Chip(context);
                    chip.setText(item.name);
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(v -> {
                        cropChipGroup.removeView(chip);
                        item.isSelected = false;
                        item.referenceName.remove(target.name);
                      chipGroupDisplay(cropChipGroup);
                    });
                    cropChipGroup.addView(chip);
                }
            }
            chipGroupDisplay(cropChipGroup);
            widgetContainer.addView(cropParcelView);

//                    break;
//            }
        }

        // ------------- //
        // Inputs layout //
        // ------------- //

        for (GenericEntity inputType : selectedProcedure.input) {
            if (BuildConfig.DEBUG) Log.i(TAG, "Build layout for input --> " + inputType.name);

            // Get layout
            View inputView = inflater.inflate(R.layout.widget_input, container, false);

            // Set param label
            TextView label = inputView.findViewById(R.id.widget_label);
            @StringRes final int labelRes = getResources().getIdentifier(inputType.name, "string", Zero.getPkgName());
            label.setText(labelRes);

            // Add onClick listener
            TextView addButton = inputView.findViewById(R.id.widget_add);
            addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(inputType.name, inputType.filter));

            // Initialize Recycler
            RecyclerView inputRecycler = inputView.findViewById(R.id.widget_recycler);
            inputRecycler.setLayoutManager(new LinearLayoutManager(context));
            inputRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
            QuantityItemAdapter quantityItemAdapter = new QuantityItemAdapter(
                    getCurrentDataset(inputType.name), inputType.name);
            inputRecycler.setAdapter(quantityItemAdapter);
            inputRecycler.requestLayout();

            Log.e(TAG, "Adapter size = " + quantityItemAdapter.getItemCount());

            // Set visibility if one item is corresponding current input type
            int visibility = quantityItemAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
            inputRecycler.setVisibility(visibility);

//            for (GenericItem item : inputList)
//                if (item.referenceName.contains(inputType.name)) {
//                    visibility = View.VISIBLE;
//                    break;
//                }

            // Add view
            widgetContainer.addView(inputView);
        }

        // -------------- //
        // Workers layout //
        // -------------- //

        for (GenericEntity entity : selectedProcedure.doer) {
            if (BuildConfig.DEBUG) Log.i(TAG, "doer --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity, paramsList));
        }

        // ----------------- //
        // Equipments layout //
        // ----------------- //

        for (GenericEntity entity : selectedProcedure.tool) {
            if (BuildConfig.DEBUG) Log.i(TAG, "tool --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity, paramsList));
        }

        return view;
    }

    private List<GenericItem> getCurrentDataset(String role) {

        List<GenericItem> dataset = new ArrayList<>();

        // Loop over all items availables for this role
        for (GenericItem item : paramsList) {

            if (item.referenceName.contains(role))
                dataset.add(item);


//            if (inputList.contains(item)) {
//                if (!item.referenceName.contains(role))
//                    inputList.remove(item);
//            } else {
//                if (item.referenceName.contains(role))
//                    inputList.add(item);
//            }
        }

        return dataset;
    }

    private void chipGroupDisplay(ChipGroup group) {
        boolean itemCounter = false;
        for (GenericItem item : paramsList)
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

    private List<GenericItem> getPlantsAndLandParcels(ContentResolver cr) {

        List<GenericItem> list = new ArrayList<>();

        // Load Plants
        try (Cursor cursor = cr.query(Plants.CONTENT_URI, Plants.PROJECTION_INTER,
                likeAccountName + " AND " + Plants.ACTIVE + " == " + 1
                        + " AND " + Plants.DEAD_AT + " IS NULL",
                null, Plants.SORT_ORDER_NAME)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(1),       // id
                        cursor.getString(2),    // name
                        cursor.getString(4),    // surface
                        PLANT,                  // type     TODO -> change to "target"
                        null,                   // abilities
                        null                    // unit
                ));
        }

//        // Load Land Parcels
        try (Cursor cursor = cr.query(LandParcels.CONTENT_URI, LandParcels.PROJECTION_ALL,
                likeAccountName + " AND " + LandParcels.DEAD_AT + " IS NULL",
                null, LandParcels.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(0),       // id
                        cursor.getString(1),    // name
                        cursor.getString(2),    // number = surface
                        LAND_PARCEL,            // type     TODO -> change to "target"
                        null,                   // abilities
                        null                    // unit
                ));
        }

        sortAlphabetically(list);
        return list;
    }

    private List<GenericItem> getUsers(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        // Load Workers
        try (Cursor cursor = cr.query(Workers.CONTENT_URI, Workers.PROJECTION_ALL,
                likeAccountName + " AND " + Workers.DEAD_AT + " IS NULL",
                null, Workers.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(0),       // ek_id
                        cursor.getString(1),    // name
                        cursor.getString(2),    // number
//                        cursor.getString(3),    // type
                        "doer",
                        cursor.getString(4).split(","),    // abilities
                        null    // no unit here
                ));
        }

        sortAlphabetically(list);
        return list;
    }

    private List<GenericItem> getTools(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        // Load Plants
        try (Cursor cursor = cr.query(Equipments.CONTENT_URI, Equipments.PROJECTION_ALL,
                likeAccountName + " AND " + Equipments.DEAD_AT + " IS NULL",
                null, Equipments.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(0),       // ek_id
                        cursor.getString(1),    // name
                        cursor.getString(2),    // number
//                        cursor.getString(3),    // type
                        "tool",
                        cursor.getString(4).split(","),    // abilities
                        null    // no unit here
                ));
        }

        sortAlphabetically(list);
        return list;
    }

    private List<GenericItem> getInputs(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        // Load Inputs
        try (Cursor cursor = cr.query(Inputs.CONTENT_URI, Inputs.PROJECTION_ALL,
                likeAccountName + " AND " + Inputs.DEAD_AT + " IS NULL",
                null, Inputs.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext()) {
                list.add(new GenericItem(
                        cursor.getInt(0),       // ek_id
                        cursor.getString(1),    // name
                        cursor.getString(4),    // number
//                        cursor.getString(2),    // type
                        "input",
                        cursor.getString(5).split(","),    // abilities
                        cursor.getString(3)     // unit
                ));
            }
        }
        sortAlphabetically(list);
        return list;
    }

    private void sortAlphabetically(List<GenericItem> list) {
        Collections.sort(list, (ele1, ele2) -> {
            Collator localeCollator = Collator.getInstance(Locale.FRANCE);
            return localeCollator.compare(ele1.name, ele2.name);
        });
    }

    public interface OnFragmentInteractionListener {
        void onFormFragmentInteraction(String fragmentTag, String filter);
    }

}
