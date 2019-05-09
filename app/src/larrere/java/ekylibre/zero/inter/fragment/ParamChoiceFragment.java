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
import androidx.annotation.StringRes;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ekylibre.util.query_language.DSL;
import ekylibre.util.query_language.QL.QL;
import ekylibre.util.query_language.QL.TreeNode;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.SelectableItemAdapter;
import ekylibre.zero.inter.enums.ParamType.Type;
import ekylibre.zero.inter.model.GenericItem;


public class ParamChoiceFragment extends Fragment {

    private static final String TAG = "ParamChoiceFragment";

    @Type
    private String paramType;
    private String filter;
    private Context context;
    private List<GenericItem> dataset;
    private SelectableItemAdapter adapter;

    public ParamChoiceFragment() {}

    public static ParamChoiceFragment newInstance(String param, String filter) {
        ParamChoiceFragment fragment = new ParamChoiceFragment();
        Bundle args = new Bundle();
        args.putString("param_type", param);
        args.putString("filter", filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {
            paramType = getArguments().getString("param_type");
            filter = getArguments().getString("filter");
            Log.i(TAG, "ParamType = " + paramType);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @StringRes
        int titleRes = getResources().getIdentifier(paramType, "string", context.getPackageName());
        InterActivity.actionBar.setTitle(titleRes);

        // Filter list
        dataset = new ArrayList<>();
        outer: for (GenericItem item : InterventionFormFragment.paramsList) {
            if (item.type.equals(paramType))
                dataset.add(item);

            // TODO : activate when abilities available on API
//            List<String> itemAbilities = DSL.getElements(item.abilities);
//            List<String> requiredAbilities = DSL.getElements(filter);
//            Log.i(TAG, "Item abilities = " + itemAbilities);
//            Log.i(TAG, "Required abilities = " + requiredAbilities);
//            for (String requiredAbility : requiredAbilities) {
//                if (!itemAbilities.contains(requiredAbility)) {
//                    // Missing ability
//                    continue outer;
//                }
//            }
//            dataset.add(item);
        }

        View view;
        if (dataset.isEmpty()) {
            view = inflater.inflate(R.layout.empty_recycler, container, false);
        }
        else {
            view = inflater.inflate(R.layout.fragment_recycler_with_search_field, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
            adapter = new SelectableItemAdapter(dataset);
            recyclerView.setAdapter(adapter);

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
        }

        return view;
    }

    private void filterList(String text) {

//        Uri uri;
//        String[] projection;
//        String query;
//        String sortOrder;
//        ContentResolver contentResolver = context.getContentResolver();
//                uri = ZeroContract.Workers.CONTENT_URI;
//                projection = ZeroContract.Workers.PROJECTION_ALL;
//                query = ZeroContract.Workers.QUALIFICATION + " = " + paramType;
//                sortOrder = ZeroContract.Workers.SORT_ORDER_DEFAULT;

        dataset.clear();

        for (GenericItem item : InterventionFormFragment.paramsList)
            if (item.type.equals(paramType))
                if (text != null) {
                    String name = item.name.toLowerCase();
                    if (name.contains(text.toLowerCase()))
                        dataset.add(item);
                } else
                    dataset.add(item);

        adapter.notifyDataSetChanged();


//        switch (paramType) {
//
//            case "wine_man":
//            case "operator":
//            case "responsible":
//            case "mechanic":
//            case "forager_driver":
//            case "caregiver":
//            case "inseminator":
//            case "worker":
//            case "doer":
//            case "driver":
//                for (GenericItem item : InterventionFormFragment.paramsList)
//                    if (item.type.equals(paramType))
//                        if (text != null) {
//                            String name = item.name.toLowerCase();
//                            if (name.contains(text.toLowerCase()))
//                                dataset.add(item);
//                        } else
//                            dataset.add(item);
//                break;
//        }

//        try (Cursor cursor = contentResolver.query(uri, projection, query, null, sortOrder)) {
//
//            if (cursor != null) {
//                dataset.clear();
//                while (cursor.moveToNext()) {
//                    String label = cursor.getString(2);
//                    boolean selected = false;
//                    for (IssueItem item : issuesList)
//                        if (item.label.equals(label)) {
//                            selected = true;
//                            break;
//                        }
//                    dataset.add(new IssueItem(cursor.getString(1), label,
//                            cursor.getString(3), selected));
//                }
//                adapter.notifyDataSetChanged();
//            }
//        }
    }

    private static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
