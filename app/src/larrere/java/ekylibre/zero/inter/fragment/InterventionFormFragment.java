package ekylibre.zero.inter.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ekylibre.database.ZeroContract.Products;
import ekylibre.util.Helper;
import ekylibre.util.MarginTopItemDecoration;
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


public class InterventionFormFragment extends Fragment {

    private static final String TAG = "InterventionFormFragmen";

    private Context context;
    private OnFragmentInteractionListener listener;
    private static int scrollPosition = 0;

    public static List<Period> periodList;
    public static List<GenericItem> paramsList;
    public static List<Zone> zoneList;

    // LAYOUT BINDINGS
    @BindView(R.id.widgets_container) LinearLayoutCompat widgetContainer;
    @BindView(R.id.form_period_recycler) RecyclerView periodRecycler;
    @BindView(R.id.form_period_add) TextView addPeriod;
    @BindView(R.id.scroll_view) ScrollView scrollView;


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

        if (BuildConfig.DEBUG)
            Log.v(TAG, "onCreate");

        ContentResolver cr = context.getContentResolver();

        // Add one hour period as default
        periodList = new ArrayList<>();
        periodList.add(new Period());

        zoneList = new ArrayList<>();
        zoneList.add(new Zone());

        paramsList = new ArrayList<>();
        paramsList.addAll(getProducts(cr));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (BuildConfig.DEBUG)
            Log.v(TAG, "onCreateView");

        // Set title
        InterActivity.actionBar.setTitle(Helper.getStringId(selectedProcedure.name));

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

            if (BuildConfig.DEBUG)
                Log.i(TAG, "group --> zone");

            View zoneView = inflater.inflate(R.layout.widget_with_icon_and_recycler, container, false);

            ImageView icon = zoneView.findViewById(R.id.icon);
            icon.setImageResource(getWidgetIcon("land_parcel"));

            // Set param label
            TextView label = zoneView.findViewById(R.id.widget_label);
            label.setText(Helper.getTranslation("zone"));

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

        // -------------- //
        // Targets layout //
        // -------------- //

        for (GenericEntity entity : selectedProcedure.target) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "target --> " + entity.name);
            if (entity.group == null)
                widgetContainer.addView(new WidgetParamView(context, listener, entity, paramsList, "targets"));
        }

        // ------------- //
        // Inputs layout //
        // ------------- //

        for (GenericEntity inputType : selectedProcedure.input) {

            if (inputType.group == null) {
                if (BuildConfig.DEBUG) Log.i(TAG, "input --> " + inputType.name);

                // Get layout
                View inputView = inflater.inflate(R.layout.widget_with_icon_and_recycler, container, false);

                ImageView icon = inputView.findViewById(R.id.icon);
                icon.setImageResource(getWidgetIcon(inputType.name));

                // Set param label
                TextView label = inputView.findViewById(R.id.widget_label);
                label.setText(Helper.getStringId(inputType.name));

                // Add onClick listener
                TextView addButton = inputView.findViewById(R.id.widget_add);
                addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(inputType.name, inputType.filter, "inputs"));

                // Initialize Recycler
                RecyclerView inputRecycler = inputView.findViewById(R.id.widget_recycler);
                inputRecycler.setLayoutManager(new LinearLayoutManager(context));
                inputRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
                QuantityItemAdapter quantityItemAdapter = new QuantityItemAdapter(
                        getCurrentDataset(inputType.name), inputType);
                inputRecycler.setAdapter(quantityItemAdapter);
                inputRecycler.requestLayout();

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
                View inputView = inflater.inflate(R.layout.widget_with_icon_and_recycler, container, false);

                ImageView icon = inputView.findViewById(R.id.icon);
                icon.setImageResource(getWidgetIcon(outputType.name));

                // Set param label
                TextView label = inputView.findViewById(R.id.widget_label);
                label.setText(Helper.getStringId(outputType.name));

                // Add onClick listener
                TextView addButton = inputView.findViewById(R.id.widget_add);
                addButton.setOnClickListener(v -> listener.onFormFragmentInteraction(outputType.name, outputType.filter, "outputs"));

                // Initialize Recycler
                RecyclerView inputRecycler = inputView.findViewById(R.id.widget_recycler);
                inputRecycler.setLayoutManager(new LinearLayoutManager(context));
                inputRecycler.addItemDecoration(new MarginTopItemDecoration(context, 16));
                QuantityItemAdapter quantityItemAdapter = new QuantityItemAdapter(
                        getCurrentDataset(outputType.name), outputType);
                inputRecycler.setAdapter(quantityItemAdapter);
                inputRecycler.requestLayout();

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
            widgetContainer.addView(new WidgetParamView(context, listener, entity, paramsList, "workers"));
        }

        // ----------------- //
        // Equipments layout //
        // ----------------- //

        for (GenericEntity entity : selectedProcedure.tool) {
            if (BuildConfig.DEBUG) Log.i(TAG, "tool --> " + entity.name);
            widgetContainer.addView(new WidgetParamView(context, listener, entity, paramsList, "equipments"));
        }

        // Set ScrollView to previous position
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> scrollPosition = scrollY);
        scrollView.scrollTo(0, scrollPosition);

        return view;
    }

    private List<GenericItem> getCurrentDataset(String role) {

        List<GenericItem> dataset = new ArrayList<>();

        // Loop over all items availables for this role
        for (GenericItem item : paramsList)

            if (item.referenceName.containsKey(role))
                dataset.add(item);

        return dataset;
    }

//    private void chipGroupDisplay(ChipGroup group) {
//        boolean itemCounter = false;
//        for (GenericItem item : paramsList)
//            if ((item.variety.equals(PLANT) || item.variety.equals(LAND_PARCEL)) && item.isSelected) {
//                itemCounter = true;
//                break;
//            }
//        group.setVisibility(itemCounter ? View.VISIBLE : View.GONE);
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private List<GenericItem> getProducts(ContentResolver cr) {
        List<GenericItem> list = new ArrayList<>();

        final String whereClause = "user LIKE ? AND (dead_at IS NULL OR dead_at > CAST(? AS INTEGER))";
        final String[] args = new String[] {account.name, String.valueOf(new Date().getTime())};

        // Load Inputs
        try (Cursor cursor = cr.query(Products.CONTENT_URI, Products.PROJECTION,
                whereClause, args, Products.ORDER_BY_NAME)) {

            // Projection --> EK_ID, NAME, NUMBER, WORK_NUMBER, VARIETY, ABILITIES, POPULATION,
            // POPULATION_UNIT, CONTAINER_NAME, NET_SURFACE_AREA

            while (cursor != null && cursor.moveToNext()) {

                GenericItem item = new GenericItem();
                item.id = cursor.getInt(0);
                item.name = cursor.getString(1);
                item.number = cursor.getString(2);
                item.workNumber = cursor.getString(3);
                item.variety = cursor.getString(4);
                item.abilities = cursor.getString(5).split(",");
                item.population = cursor.getString(6);
//                item.unit = cursor.getString(7);
                item.containerName = cursor.getString(8);
                item.netSurfaceArea = cursor.getString(9);
                list.add(item);
            }
        }

        return list;
    }

    public static int getWidgetIcon(String refName) {

        List<String> targets = Arrays.asList("plant", "land_parcel", "cultivation");
        List<String> drivers = Arrays.asList("driver", "forager_driver");
        List<String> doers =  Arrays.asList("caregiver", "doer", "worker", "operator", "inseminator", "responsible", "wine_man", "mechanic");
        List<String> inputs =  Arrays.asList("animal_food", "animal_medicine", "bottles",
                "cleaner_product", "construction_material", "consumable_part", "consumable_part",
                "corks", "disinfectant", "energy", "fertilizer", "food", "fuel", "herbicide",
                "item", "litter", "mulching_material", "oenological_intrant", "oil",
                "packaging_material", "plant_medicine", "plants", "pollinating_insects",
                "product_to_prepare", "product_to_sort", "protective_canvas", "protective_net",
                "replacement_part", "seedlings", "seeds", "stakes", "straw_to_bunch", "tunnel",
                "vial", "water", "wine", "wire_fence");



        if (refName.equals("tractor"))
            return R.drawable.icon_tractor;
        if (targets.contains(refName))
            return R.drawable.icon_field;
        if (drivers.contains(refName))
            return R.drawable.icon_driver;
        if (doers.contains(refName))
            return R.drawable.icon_doer;
        if (inputs.contains(refName))
            return R.drawable.icon_sack;

        return R.drawable.icon_trailer;
    }

    private void sortAlphabetically(List<GenericItem> list) {
        Collections.sort(list, (ele1, ele2) -> {
            Collator localeCollator = Collator.getInstance(Locale.FRANCE);
            return localeCollator.compare(ele1.name, ele2.name);
        });
    }

    public interface OnFragmentInteractionListener {
        void onFormFragmentInteraction(String fragmentTag, String filter, String role);
    }

}


