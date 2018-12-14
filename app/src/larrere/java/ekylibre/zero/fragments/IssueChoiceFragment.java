package ekylibre.zero.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;
import ekylibre.zero.fragments.adapter.IssuesRecyclerAdapter;
import ekylibre.zero.fragments.model.IssueItem;

import static ekylibre.zero.ObservationActivity.issuesList;


public class IssueChoiceFragment extends Fragment {

    private List<IssueItem> dataset = new ArrayList<>();
    private IssuesRecyclerAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IssueChoiceFragment() {
    }

    public static IssueChoiceFragment newInstance() {
        return new IssueChoiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Create dummy data
//        int i = 0;
//        if (issuesList.isEmpty())
//            while (i < 14) {
//                issuesList.add(new IssueItem(i, "Ravageur #" + i, null));
//                i++;
//            }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issues, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.issue_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter = new IssuesRecyclerAdapter(dataset);
        recyclerView.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_issues);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                queryDatabaseForList(tab.getPosition()); }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });

        queryDatabaseForList(0);

        return view;
    }

    private void queryDatabaseForList(int tab) {
        // Field values as existing in database
        String[] categories = {"Adventices", "Ravageurs", "Maladies", "Autres"};
        Context context = getActivity();
        if (context != null) {
            ContentResolver contentResolver = context.getContentResolver();

            try (Cursor cursor = contentResolver.query(ZeroContract.IssueNatures.CONTENT_URI,
                    ZeroContract.IssueNatures.PROJECTION_ALL,
                    ZeroContract.IssueNatures.CATEGORY + " = ? ", new String[]{categories[tab]},
                    ZeroContract.IssueNatures.SORT_ORDER_DEFAULT)) {
                if (cursor != null) {
                    dataset.clear();
                    while (cursor.moveToNext()) {
                        String label = cursor.getString(2);
                        boolean selected = false;
                        for (IssueItem item : issuesList)
                            if (item.label.equals(label)) {
                                selected = true;
                                break;
                            }
                        dataset.add(new IssueItem(cursor.getString(1), label,
                                cursor.getString(3), selected));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

}
