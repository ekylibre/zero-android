package ekylibre.zero.inter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.CropParcelAdapter;
import ekylibre.zero.inter.model.CropParcel;


public class CropParcelChoiceFragment extends Fragment {

    private List<CropParcel> dataset = new ArrayList<>();
    private CropParcelAdapter adapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataset = InterventionFormFragment.cropParcelList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InterActivity.actionBar.setTitle("Choix des cultures/parcelles");

        View view = inflater.inflate(R.layout.fragment_crop_parcel, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.crop_parcel_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter = new CropParcelAdapter(dataset);
        recyclerView.setAdapter(adapter);
//
//        TabLayout tabLayout = view.findViewById(R.id.tab_issues);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override public void onTabSelected(TabLayout.Tab tab) {
////                queryDatabaseForList(tab.getPosition());
//            }
//            @Override public void onTabUnselected(TabLayout.Tab tab) { }
//            @Override public void onTabReselected(TabLayout.Tab tab) { }
//        });

//        queryDatabaseForList(0);

        return view;
    }

//    private void queryDatabaseForList(int tab) {
//        // Field values as existing in database
//        String[] categories = {"Adventices", "Ravageurs", "Maladies", "Autres"};
//        Context context = getActivity();
//        if (context != null) {
//            ContentResolver contentResolver = context.getContentResolver();
//
//            try (Cursor cursor = contentResolver.query(ZeroContract.IssueNatures.CONTENT_URI,
//                    ZeroContract.IssueNatures.PROJECTION_ALL,
//                    ZeroContract.IssueNatures.CATEGORY + " = ? ", new String[]{categories[tab]},
//                    ZeroContract.IssueNatures.SORT_ORDER_DEFAULT)) {
//                if (cursor != null) {
//                    dataset.clear();
//                    while (cursor.moveToNext()) {
//                        String label = cursor.getString(2);
//                        boolean selected = false;
//                        for (IssueItem item : issuesList)
//                            if (item.label.equals(label)) {
//                                selected = true;
//                                break;
//                            }
//                        dataset.add(new IssueItem(cursor.getString(1), label,
//                                cursor.getString(3), selected));
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }
//    }

}
