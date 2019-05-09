package ekylibre.zero.inter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.CropParcelAdapter;
import ekylibre.zero.inter.model.GenericItem;

import static ekylibre.zero.inter.enums.ParamType.LAND_PARCEL;
import static ekylibre.zero.inter.enums.ParamType.PLANT;


public class CropParcelChoiceFragment extends Fragment {

    List<GenericItem> dataset;
    CropParcelAdapter adapter;
    public int currentTab = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CropParcelChoiceFragment() {
    }

    public static CropParcelChoiceFragment newInstance() {
        return new CropParcelChoiceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InterActivity.actionBar.setTitle("Choix des cultures/parcelles");
        View view = inflater.inflate(R.layout.fragment_crop_parcel, container, false);

        dataset = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.crop_parcel_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter = new CropParcelAdapter(dataset);
        recyclerView.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_crop_parcel);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                queryDatabaseForList(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Query data for first tab
        queryDatabaseForList(0);

        return view;
    }

    private void queryDatabaseForList(int tab) {
        dataset.clear();
        String filter = tab == 0 ? PLANT : LAND_PARCEL;
        for (GenericItem item : InterventionFormFragment.paramsList)
            if (item.type.equals(filter))
                dataset.add(item);
        adapter.notifyDataSetChanged();
    }

}
