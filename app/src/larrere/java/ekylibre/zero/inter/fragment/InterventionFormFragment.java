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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.database.ZeroContract.Equipments;
import ekylibre.database.ZeroContract.Inputs;
import ekylibre.database.ZeroContract.Outputs;
import ekylibre.database.ZeroContract.LandParcels;
import ekylibre.database.ZeroContract.Plants;
import ekylibre.database.ZeroContract.Workers;
import ekylibre.util.MarginTopItemDecoration;
import ekylibre.util.Helper;
import ekylibre.util.layout.component.WidgetParamView;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.FormPeriodAdapter;
import ekylibre.zero.inter.adapter.QuantityItemAdapter;
import ekylibre.zero.inter.adapter.ZoneAdapter;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.Period;
import ekylibre.zero.inter.model.Zone;

import static ekylibre.zero.inter.InterActivity.account;
import static ekylibre.zero.inter.InterActivity.selectedProcedure;
import static ekylibre.zero.inter.enums.ParamType.LAND_PARCEL;
import static ekylibre.zero.inter.enums.ParamType.PLANT;


public class InterventionFormFragment extends Fragment {

    private static final String TAG = "InterventionFormFragmen";

    private Context context;
    private OnFragmentInteractionListener listener;

    public static List<Period> periodList;
    public static List<GenericItem> paramsList;
    public static List<Zone> zoneList;

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

        zoneList = new ArrayList<>();
        zoneList.add(new Zone());

        paramsList = new ArrayList<>();
        paramsList.addAll(getUsers(cr));
        paramsList.addAll(getTools(cr));
        paramsList.addAll(getPlantsAndLandParcels(cr));
        paramsList.addAll(getBuildingDivisions(cr));
        paramsList.addAll(getInputs(cr));
        paramsList.addAll(getOutputs(cr));

        if (BuildConfig.DEBUG)
            Log.i(TAG, paramsList.size() + " items in paramsList");

        for (GenericItem item : paramsList) {
            if (item.id == 9604) {
                Log.e(TAG, "Item truc est présent");
                break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateView");

        // Set title
        InterActivity.actionBar.setTitle(Helper.getStringId(InterActivity.selectedProcedure.name));

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

        // Group attributes can only be [target], [input] or [output]

        if (selectedProcedure.group != null && selectedProcedure.group.equals("zone")) {

            // Check if there is target in this group
//            for (GenericEntity target : selectedProcedure.target) {
//                if (target.group.equals(selectedProcedure.group)) {

            View zoneView = inflater.inflate(R.layout.widget_input, container, false);

            // Set param label
            TextView label = zoneView.findViewById(R.id.widget_label);
            label.setText(Helper.getStringId("zone"));

            // Initialize Recycler
            RecyclerView zoneRecycler = zoneView.findViewById(R.id.widget_recycler);
            zoneRecycler.setLayoutManager(new LinearLayoutManager(context));
            zoneRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
            ZoneAdapter zoneAdapter = new ZoneAdapter(zoneList, listener);
            zoneRecycler.setAdapter(zoneAdapter);
            zoneRecycler.requestLayout();

            // Add onClick listener
            TextView addButton = zoneView.findViewById(R.id.widget_add);
            addButton.setOnClickListener(v -> {
                zoneList.add(new Zone());
                zoneAdapter.notifyDataSetChanged();
            });

            // Add view
            widgetContainer.addView(zoneView);
        }



        // ----------- //
        // Crop layout //
        // ----------- //

        // Check wether to display crop, parcel or both selector
        for (GenericEntity target : selectedProcedure.target) {

            if (target.group == null) {

                // Inflate the layout
                View cropParcelView = inflater.inflate(R.layout.widget_param_layout, container, false);
                TextView label = cropParcelView.findViewById(R.id.widget_label);

                // Set the title
                if (target.name.equals("cultivation"))
                    label.setText(R.string.crop_parcel);
                else {
                    label.setText(Helper.getStringId(target.name));
                }
                TextView addButton = cropParcelView.findViewById(R.id.widget_add);
                ChipGroup cropChipGroup = cropParcelView.findViewById(R.id.widget_chips_group);
                addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(target.name, target.filter));  // CROP_CHOICE_FRAGMENT

                // ChipGroup Logic
                for (GenericItem item : paramsList) {
                    if ((item.variety.equals(PLANT) || item.variety.equals(LAND_PARCEL)) && item.isSelected) {
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
            }
        }

        // ------------- //
        // Inputs layout //
        // ------------- //

        for (GenericEntity inputType : selectedProcedure.input) {
            if (BuildConfig.DEBUG) Log.i(TAG, "Build layout for input --> " + inputType.name);


            if (inputType.group == null) {
                if (BuildConfig.DEBUG) Log.i(TAG, "Build layout for input --> " + inputType.name);

                // Get layout
                View inputView = inflater.inflate(R.layout.widget_input, container, false);

                // Set param label
                TextView label = inputView.findViewById(R.id.widget_label);
                label.setText(Helper.getStringId(inputType.name));

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

                // Set visibility if one item is corresponding current input variety
                int visibility = quantityItemAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
                inputRecycler.setVisibility(visibility);

                // Add view
                widgetContainer.addView(inputView);
            }
        }

        // ------------- //
        // Output layout //
        // ------------- //

        for (GenericEntity outputType : selectedProcedure.output) {

            if (outputType.group == null) {
                if (BuildConfig.DEBUG) Log.i(TAG, "Build layout for output --> " + outputType.name);

                // Get layout (same as input)
                View inputView = inflater.inflate(R.layout.widget_input, container, false);

                // Set param label
                TextView label = inputView.findViewById(R.id.widget_label);
                label.setText(Helper.getStringId(outputType.name));

                // Add onClick listener
                TextView addButton = inputView.findViewById(R.id.widget_add);
                addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(outputType.name, outputType.filter));

                // Initialize Recycler
                RecyclerView inputRecycler = inputView.findViewById(R.id.widget_recycler);
                inputRecycler.setLayoutManager(new LinearLayoutManager(context));
                inputRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
                QuantityItemAdapter quantityItemAdapter = new QuantityItemAdapter(
                        getCurrentDataset(outputType.name), outputType.name);
                inputRecycler.setAdapter(quantityItemAdapter);
                inputRecycler.requestLayout();

                Log.e(TAG, "Adapter size = " + quantityItemAdapter.getItemCount());

                // Set visibility if one item is corresponding current input variety
                int visibility = quantityItemAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE;
                inputRecycler.setVisibility(visibility);

                // Add view
                widgetContainer.addView(inputView);
            }
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
        for (GenericItem item : paramsList)
            if (item.referenceName.contains(role))
                dataset.add(item);

        return dataset;
    }

    private void chipGroupDisplay(ChipGroup group) {
        boolean itemCounter = false;
        for (GenericItem item : paramsList)
            if ((item.variety.equals(PLANT) || item.variety.equals(LAND_PARCEL)) && item.isSelected) {
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

        final String whereClause = String.format("user LIKE \"%s\" AND dead_at IS NULL OR dead_at < datetime('now')", account.name);

        // Load Plants
        try (Cursor cursor = cr.query(Plants.CONTENT_URI, Plants.PROJECTION_INTER,
                whereClause, null, Plants.SORT_ORDER_NAME)) {

            while (cursor != null && cursor.moveToNext()) {
                GenericItem item = new GenericItem(PLANT);
                item.id = cursor.getInt(1);
                item.name = cursor.getString(2);
                item.number = cursor.getString(4);  // Surface in this case
                list.add(item);
            }
        }

        // Load Land Parcels
        try (Cursor cursor = cr.query(LandParcels.CONTENT_URI, LandParcels.PROJECTION_ALL,
                whereClause, null, LandParcels.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext()) {
                GenericItem item = new GenericItem(LAND_PARCEL);
                item.id = cursor.getInt(0);
                item.name = cursor.getString(1);
                item.number = cursor.getString(2);  // Surface in this case
                list.add(item);
            }
        }

        return list;
    }

    private List<GenericItem> getBuildingDivisions(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        final String whereClause = String.format("user LIKE \"%s\" AND dead_at IS NULL OR dead_at < datetime('now')", account.name);

        // Load Plants
        try (Cursor cursor = cr.query(Plants.CONTENT_URI, Plants.PROJECTION_INTER,
                whereClause, null, Plants.SORT_ORDER_NAME)) {

            while (cursor != null && cursor.moveToNext()) {
                GenericItem item = new GenericItem("building_division");
                item.id = cursor.getInt(1);
                item.name = cursor.getString(2);
                item.number = cursor.getString(4);  // Surface in this case
                list.add(item);
            }
        }

        return list;
    }

    private List<GenericItem> getUsers(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        final String whereClause = String.format("user LIKE \"%s\" AND (dead_at IS NULL OR dead_at > %d)", account.name, new Date().getTime());

        try (Cursor cursor = cr.query(Workers.CONTENT_URI, Workers.PROJECTION_ALL,
                whereClause, null, Workers.SORT_ORDER_DEFAULT)) {

            // Projection = {EK_ID, NAME, NUMBER, WORK_NUMBER, ABILITIES}

            while (cursor != null && cursor.moveToNext()) {
                GenericItem item = new GenericItem("doer");
                item.id = cursor.getInt(0);
                item.name = cursor.getString(1);
                item.number = cursor.getString(2);
                item.workNumber = cursor.getString(3);
                item.abilities = cursor.getString(4).split(",");
                list.add(item);
            }
        }

        return list;
    }

    private List<GenericItem> getTools(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        try (Cursor cursor = cr.query(Equipments.CONTENT_URI, Equipments.PROJECTION_ALL,
                "user = \"" + "123456"+ "\" AND (dead_at IS NULL OR CAST(dead_at AS INTEGER) > CAST(" + new Date().getTime() + " AS INTEGER))",
                null, Equipments.SORT_ORDER_DEFAULT)) {

            // Projection = {EK_ID, NAME, NUMBER, WORK_NUMBER, VARIETY, ABILITIES}

//            while (cursor != null && cursor.moveToNext()) {
//                GenericItem item = new GenericItem("tool");
//                item.id = cursor.getInt(0);
//                item.name = cursor.getString(1);
//                item.number = cursor.getString(2);
//                item.workNumber = cursor.getString(3);
//                item.variety = cursor.getString(4);
//                item.abilities = cursor.getString(5).split(",");
//                list.add(item);
//            }
        }

        return list;
    }

    private List<GenericItem> getInputs(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        final String whereClause = String.format("user LIKE \"%s\"", account.name);

        try (Cursor cursor = cr.query(Inputs.CONTENT_URI, Inputs.PROJECTION_ALL,
                whereClause, null, Inputs.SORT_ORDER_DEFAULT)) {

            // Projection = {EK_ID, NAME, NUMBER, VARIETY, ABILITIES, POPULATION, CONTAINER_NAME}

            while (cursor != null && cursor.moveToNext()) {
                GenericItem item = new GenericItem("input");
                item.id = cursor.getInt(0);
                item.name = cursor.getString(1);
                item.number = cursor.getString(2);
                item.variety = cursor.getString(3);
                item.abilities = cursor.getString(4).split(",");
                item.population = new BigDecimal(cursor.getString(5));
                item.workNumber = cursor.getString(6);  // container_name in this case
                list.add(item);
            }
        }

        return list;
    }

    private List<GenericItem> getOutputs(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        final String whereClause = String.format("user LIKE \"%s\"", account.name);

        // Load Inputs
        try (Cursor cursor = cr.query(Outputs.CONTENT_URI, Outputs.PROJECTION_ALL,
                whereClause, null, Outputs.SORT_ORDER_DEFAULT)) {

            // {EK_ID, NAME, VARIETY, NUMBER, ABILITIES}

            while (cursor != null && cursor.moveToNext()) {

                GenericItem item = new GenericItem("output");
                item.id = cursor.getInt(0);
                item.name = cursor.getString(1);
                item.variety = cursor.getString(2);
                item.number = cursor.getString(3);
                item.abilities = cursor.getString(4).split(",");
                list.add(item);
            }
        }

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
