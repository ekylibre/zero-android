package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.SimpleSelectableItemAdapter;
import ekylibre.zero.inter.enums.ParamType.Type;
import ekylibre.zero.inter.model.SimpleSelectableItem;


public class ParamChoiceFragment extends Fragment {

    @Type
    private String paramType;
    private Context context;

    public ParamChoiceFragment() {}

    public static ParamChoiceFragment newInstance(String param) {
        ParamChoiceFragment fragment = new ParamChoiceFragment();
        Bundle args = new Bundle();
        args.putString("param_type", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null)
            paramType = getArguments().getString("param_type");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @StringRes
        int titleRes = getResources().getIdentifier(paramType, "string", context.getPackageName());
        InterActivity.actionBar.setTitle(titleRes);

        // Filter list
        List<SimpleSelectableItem> dataset = new ArrayList<>();
        for (SimpleSelectableItem item : InterventionFormFragment.paramsList)
            if (item.type.equals(paramType))
                dataset.add(item);

        View view = inflater.inflate(R.layout.fragment_single_line_item, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.simple_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        SimpleSelectableItemAdapter adapter = new SimpleSelectableItemAdapter(dataset);
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
