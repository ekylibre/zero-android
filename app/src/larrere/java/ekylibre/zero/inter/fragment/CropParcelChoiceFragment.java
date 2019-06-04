package ekylibre.zero.inter.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.home.Zero;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.CropParcelAdapter;
import ekylibre.zero.inter.enums.ParamType;
import ekylibre.zero.inter.model.GenericItem;

import static ekylibre.zero.inter.enums.ParamType.LAND_PARCEL;
import static ekylibre.zero.inter.enums.ParamType.PLANT;


public class CropParcelChoiceFragment extends Fragment {

    private static final String TAG = "ParcelChoiceFragment";

    @ParamType.Type
    private String paramType;
    private String filter;
    private List<GenericItem> dataset;
    private CropParcelAdapter adapter;
    private Context context;
    public String currentTab;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CropParcelChoiceFragment() {
    }

    public static CropParcelChoiceFragment newInstance(String param, String filter) {

        CropParcelChoiceFragment fragment = new CropParcelChoiceFragment();
        Bundle args = new Bundle();
        args.putString("param_type", param);
        args.putString("filter", filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {
            paramType = getArguments().getString("param_type");
            filter = getArguments().getString("filter");
            Log.i(TAG, "Parcel type = " + paramType);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @StringRes
        final int labelRes = getResources().getIdentifier(paramType + "_label", "string", Zero.getPkgName());
        InterActivity.actionBar.setTitle(labelRes);

        View view = inflater.inflate(R.layout.fragment_crop_parcel, container, false);

        dataset = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.crop_parcel_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter = new CropParcelAdapter(dataset);
        recyclerView.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_crop_parcel);

        if (paramType.equals("cultivation")) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override public void onTabUnselected(TabLayout.Tab tab) {}
                @Override public void onTabReselected(TabLayout.Tab tab) {}
                @Override public void onTabSelected(TabLayout.Tab tab) {
                    queryDatabaseForList(tab.getPosition());
                }
            });
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }

//        currentTab = paramType.equals("parcel") ? LAND_PARCEL : PLANT;
        // Query data for first tab
        queryDatabaseForList(paramType.equals("parcel") ? 1 : 0);

        // SeachField logic
        SearchView searchView = view.findViewById(R.id.search_field);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                filterList(text);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
                if (text.length() > 1)
                    filterList(text);
                else
                    filterList(null);
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            // Reset search
            filterList(null);
            searchView.clearFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            Log.e("PAramChoiceFragment", "ime ?");
            if (imm != null) {
                Log.e("PAramChoiceFragment", "ime not null");
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
            return false;
        });

        return view;
    }

    private void queryDatabaseForList(int tab) {
        dataset.clear();
        currentTab = tab == 0 ? PLANT : LAND_PARCEL;
        for (GenericItem item : InterventionFormFragment.paramsList)
            if (item.type.equals(currentTab))
                dataset.add(item);
        adapter.notifyDataSetChanged();
    }

    private void filterList(String text) {
        dataset.clear();
        for (GenericItem item : InterventionFormFragment.paramsList)
            if (item.type.equals(currentTab))
                if (text != null) {
                    String filterText = StringUtils.stripAccents(text.toLowerCase());
                    String name = StringUtils.stripAccents(item.name.toLowerCase());
                    String number = "";
                    if (item.number != null && item.number.isEmpty())
                        number = StringUtils.stripAccents(item.number.toLowerCase());
                    if (name.contains(filterText) || number.contains(filterText))
                        dataset.add(item);
                } else
                    dataset.add(item);

        adapter.notifyDataSetChanged();
    }

}
