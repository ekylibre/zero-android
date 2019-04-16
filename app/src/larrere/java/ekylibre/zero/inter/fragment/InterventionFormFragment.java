package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import ekylibre.zero.inter.enums.ParamType.Type;
import ekylibre.zero.inter.model.CropParcel;
import ekylibre.zero.inter.model.SimpleSelectableItem;
import ekylibre.zero.inter.model.Period;

import static ekylibre.zero.inter.InterActivity.CROP_CHOICE_FRAGMENT;
import static ekylibre.zero.inter.InterActivity.selectedProcedure;
import static ekylibre.zero.inter.enums.ParamType.DRIVER;
import static ekylibre.zero.inter.enums.ParamType.SOWER;
import static ekylibre.zero.inter.enums.ParamType.TRACTOR;


public class InterventionFormFragment extends Fragment {

    private static final String TAG = "FormFragment";
    private OnFragmentInteractionListener listener;
    private Context context;
    private List<Period> periodList;
    static List<CropParcel> cropParcelList;
    static List<SimpleSelectableItem> paramsList;

    // LAYOUT BINDINGS
    @BindView(R.id.widgets_container)
    LinearLayoutCompat widgetContainer;

    @BindView(R.id.form_period_recycler) RecyclerView periodRecycler;
    @BindView(R.id.form_period_add) TextView addPeriod;

    @BindView(R.id.form_crop_chips_group) ChipGroup cropChipGroup;
    @BindView(R.id.form_crop_add) TextView addCrop;

    public static InterventionFormFragment newInstance() {
        return new InterventionFormFragment();
    }

    @Override
    public void onAttach(Context context) {
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

        // Init cropParcelList
        cropParcelList = new ArrayList<>();
        cropParcelList.addAll(getPlants());

        paramsList = new ArrayList<>();
        paramsList.addAll(getUsers());
        paramsList.addAll(getTools());
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

        addCrop.setOnClickListener(v -> listener.onFormFragmentInteraction(CROP_CHOICE_FRAGMENT));

        for (CropParcel crop : InterActivity.selectedCropParcels) {
            Chip chip = new Chip(context);
            chip.setText(crop.name);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                cropChipGroup.removeView(chip);
                InterActivity.selectedCropParcels.remove(crop);
                cropParcelList.get(cropParcelList.indexOf(crop)).isSelected = false;
                if (InterActivity.selectedCropParcels.isEmpty())
                    cropChipGroup.setVisibility(View.GONE);
            });
            cropChipGroup.addView(chip);
        }
        cropChipGroup.setVisibility(InterActivity.selectedCropParcels.isEmpty() ? View.GONE : View.VISIBLE);

        // ------------- //
        // Params layout //
        // ------------- //

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

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private List<CropParcel> getPlants() {

        List<CropParcel> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                ZeroContract.Plants.CONTENT_URI, ZeroContract.Plants.PROJECTION_OBS,
                ZeroContract.Plants.USER + " LIKE " + "\"" + InterActivity.account.name + "\""
                        + " AND " + ZeroContract.Plants.ACTIVE + " == " + 1,
                null, ZeroContract.Issues.SORT_ORDER_DEFAULT);

        if (cursor != null) {
            try {
                while (cursor.moveToNext())
                    list.add(new CropParcel(cursor.getString(2), false));
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
        list.add(new SimpleSelectableItem("1", "Michel", DRIVER, true));
        list.add(new SimpleSelectableItem("2", "Jean", DRIVER));
        list.add(new SimpleSelectableItem("3", "Jacques", DRIVER));
        list.add(new SimpleSelectableItem("4", "Bob", DRIVER));
        return list;
    }

    private List<SimpleSelectableItem> getTools() {
        List<SimpleSelectableItem> list = new ArrayList<>();
        list.add(new SimpleSelectableItem("1", "Massey 6700 S", TRACTOR));
        list.add(new SimpleSelectableItem("2", "Sem 360 +", SOWER));
        list.add(new SimpleSelectableItem("3", "New FR250", TRACTOR));
        list.add(new SimpleSelectableItem("4", "Multigrains 500L", TRACTOR));
        return list;
    }

    public interface OnFragmentInteractionListener {
        void onFormFragmentInteraction(String fragmentTag);
    }

}
