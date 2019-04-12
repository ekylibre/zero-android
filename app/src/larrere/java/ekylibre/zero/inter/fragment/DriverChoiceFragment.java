package ekylibre.zero.inter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.SimpleSelectableItemAdapter;


public class DriverChoiceFragment extends Fragment {

    private String actionBarTitle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DriverChoiceFragment() {
    }

    public static DriverChoiceFragment newInstance(String title) {
        DriverChoiceFragment fragment = new DriverChoiceFragment();
        Bundle args = new Bundle();
        args.putString("appbar_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get title from params
        if (getArguments() != null)
            actionBarTitle = getArguments().getString("appbar_title");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InterActivity.actionBar.setTitle(actionBarTitle);

        View view = inflater.inflate(R.layout.fragment_driver, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.driver_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SimpleSelectableItemAdapter adapter = new SimpleSelectableItemAdapter(InterventionFormFragment.driverList);
        recyclerView.setAdapter(adapter);

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
