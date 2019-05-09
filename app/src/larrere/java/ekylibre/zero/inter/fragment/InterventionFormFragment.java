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
import ekylibre.APICaller.Worker;
import ekylibre.database.ZeroContract;
import ekylibre.database.ZeroContract.Plants;
import ekylibre.database.ZeroContract.LandParcels;
import ekylibre.database.ZeroContract.Equipments;
import ekylibre.database.ZeroContract.Workers;
import ekylibre.util.MarginTopItemDecoration;
import ekylibre.util.Translate;
import ekylibre.util.layout.component.WidgetParamView;
import ekylibre.util.pojo.GenericEntity;
import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.FormPeriodAdapter;
import ekylibre.zero.inter.adapter.QuantityItemAdapter;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.ItemWithQuantity;
import ekylibre.zero.inter.model.Period;

import static ekylibre.zero.inter.InterActivity.CROP_CHOICE_FRAGMENT;
import static ekylibre.zero.inter.InterActivity.selectedProcedure;
import static ekylibre.zero.inter.enums.ParamType.LAND_PARCEL;
import static ekylibre.zero.inter.enums.ParamType.PLANT;
import static ekylibre.zero.inter.enums.ParamType.PLANT_MEDICINE;


public class InterventionFormFragment extends Fragment {

    private static final String TAG = "FormFragment";
    private OnFragmentInteractionListener listener;
    private Context context;
    private List<Period> periodList;
    private static List<ItemWithQuantity> inputList;
//    static List<GenericItem> cropParcelList;
    public static List<GenericItem> paramsList;

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

        ContentResolver cr = context.getContentResolver();

        // Add one hour period as default
        periodList = new ArrayList<>();
        periodList.add(new Period());

        inputList = new ArrayList<>();

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

        if (BuildConfig.DEBUG) Log.e(TAG, "onCreateView");

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
                    widgetContainer.addView(new WidgetParamView(context, listener, PLANT, target.filter, paramsList));
                    break;

                case "land_parcel":
                    widgetContainer.addView(new WidgetParamView(context, listener, LAND_PARCEL, target.filter, paramsList));
                    break;

                case "cultivation":
                    if (target.filter.contains("is plant") && target.filter.contains("is land_parcel")) {
                        View cropParcelView = inflater.inflate(R.layout.widget_param_layout, container, false);
                        TextView label = cropParcelView.findViewById(R.id.widget_label);
                        label.setText(R.string.crop_parcel);
                        TextView addButton = cropParcelView.findViewById(R.id.widget_add);
                        ChipGroup cropChipGroup = cropParcelView.findViewById(R.id.widget_chips_group);
                        addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(CROP_CHOICE_FRAGMENT, target.filter));

                        // ChipGroup Logic
                        for (GenericItem item : paramsList) {
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
        // Inputs layout //
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
            addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(entity.name, entity.filter));

            // Initialize Recycler
            RecyclerView inputRecycler = inputView.findViewById(R.id.widget_recycler);
            inputRecycler.setLayoutManager(new LinearLayoutManager(context));
            inputRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
            QuantityItemAdapter quantityItemAdapter = new QuantityItemAdapter(inputList, inputRecycler);
            inputRecycler.setAdapter(quantityItemAdapter);

            // Updates iputList for recycler display
            for (GenericItem item : paramsList) {
                // Check we have an input
                if (entity.name.equals(item.type)) {
                    // Get item if present in inputList
                    ItemWithQuantity currentItem = null;
                    for (ItemWithQuantity quantityItem : inputList) {
                        if (item.id == quantityItem.id) {
                            currentItem = quantityItem;
                            break;
                        }
                    }

                    // If selected but not present, add it else, remove it
                    if (item.isSelected && currentItem == null)
                        inputList.add(new ItemWithQuantity(item.id, item.name, item.type, 0f, "kg"));
                    else if (currentItem != null && !item.isSelected)
                        inputList.remove(currentItem);

                }
            }
            if (inputList.isEmpty())
                inputRecycler.setVisibility(View.GONE);
            else
                inputRecycler.setVisibility(View.VISIBLE);

            // Add view
            widgetContainer.addView(inputView);
        }

        // -------------- //
        // Workers layout //
        // -------------- //

        for (GenericEntity entity : selectedProcedure.doer) {
            Log.i(TAG, "doer --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity.name, entity.filter, paramsList));
        }

        // ----------------- //
        // Equipments layout //
        // ----------------- //

        for (GenericEntity entity : selectedProcedure.tool) {
            Log.i(TAG, "tool --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity.name, entity.filter, paramsList));
        }

        return view;
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
        String likeAccountName = " LIKE " + "\"" + InterActivity.account.name + "\"";

        // Load Plants
        try (Cursor cursor = cr.query(Plants.CONTENT_URI, Plants.PROJECTION_OBS,
                Plants.USER + likeAccountName + " AND " + Plants.ACTIVE + " == " + 1,
                null, Plants.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(1),       // id
                        cursor.getString(2),    // name
                        cursor.getString(3),    // variety
                        PLANT,                  // type
                        null));
        }

//        // Load Land Parcels
        try (Cursor cursor = cr.query(LandParcels.CONTENT_URI, LandParcels.PROJECTION_ALL,
                LandParcels.USER + likeAccountName, null, LandParcels.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(0),       // id
                        cursor.getString(1),    // name
                        cursor.getString(2),    // number = surface
                        LAND_PARCEL,            // type
                        null                    // abilities
                ));
        }

        sortAlphabetically(list);
        return list;
    }

    private List<GenericItem> getUsers(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();
        String likeAccountName = " LIKE " + "\"" + InterActivity.account.name + "\"";

        // Load Workers
        try (Cursor cursor = cr.query(Workers.CONTENT_URI, Workers.PROJECTION_ALL,
                Workers.USER + likeAccountName, null, Workers.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(0),       // ek_id
                        cursor.getString(1),    // name
                        cursor.getString(2),    // number
                        cursor.getString(3),    // type
                        cursor.getString(4)     // abilities
                ));
        }

        sortAlphabetically(list);
        return list;
    }

    private List<GenericItem> getTools(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();
        String likeAccountName = " LIKE " + "\"" + InterActivity.account.name + "\"";

        // Load Plants
        try (Cursor cursor = cr.query(Equipments.CONTENT_URI, Equipments.PROJECTION_ALL,
                Equipments.USER + likeAccountName, null, Equipments.SORT_ORDER_DEFAULT)) {

            while (cursor != null && cursor.moveToNext())
                list.add(new GenericItem(
                        cursor.getInt(0),       // ek_id
                        cursor.getString(1),    // name
                        cursor.getString(2),    // number
                        cursor.getString(3),    // type
                        cursor.getString(4)     // abilities
                ));
        }

        sortAlphabetically(list);
        return list;
    }

    private List<GenericItem> getInputs(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();
//        list.add(new GenericItem(1, "Adelia EC", PLANT_MEDICINE));
//        list.add(new GenericItem(2, "PRIORI GOLD", PLANT_MEDICINE));
//        list.add(new GenericItem(3, "Licorne Flex", PLANT_MEDICINE));
//        list.add(new GenericItem(4, "SILICOSEC", PLANT_MEDICINE));
//        list.add(new GenericItem(5, "Azural xpress", PLANT_MEDICINE));
//        list.add(new GenericItem(6, "OPTION FLASH", PLANT_MEDICINE));
//        list.add(new GenericItem(7, "OXANA", PLANT_MEDICINE));
//        list.add(new GenericItem(8, "DYNAMIZ", PLANT_MEDICINE));
//        list.add(new GenericItem(9, "ORENGO", PLANT_MEDICINE));
//        list.add(new GenericItem(10, "HM BASA", PLANT_MEDICINE));
//        list.add(new GenericItem(11, "CLAYTON EL NINO", PLANT_MEDICINE));
//        list.add(new GenericItem(12, "MILLENIUM OPTI", PLANT_MEDICINE));
//
//        sortAlphabetically(list);
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
