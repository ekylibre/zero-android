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

import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.SelectableItemAdapter;
import ekylibre.zero.inter.enums.ParamType.Type;
import ekylibre.zero.inter.model.GenericItem;

import static ekylibre.zero.inter.fragment.InterventionFormFragment.paramsList;


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

        // Load required items
        dataset = new ArrayList<>();
        dataset.addAll(Grammar.getFilteredItems(filter, paramsList, null));

        View view;
        if (dataset.isEmpty()) {
            view = inflater.inflate(R.layout.empty_recycler, container, false);
        }
        else {
            view = inflater.inflate(R.layout.fragment_recycler_with_search_field, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
            adapter = new SelectableItemAdapter(dataset, paramType);
            recyclerView.setAdapter(adapter);

            // SeachField logic
            SearchView searchView = view.findViewById(R.id.search_field);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                    dataset.clear();
                    dataset.addAll(Grammar.getFilteredItems(filter, paramsList, text));
                    Log.i(TAG, "dataset = "+dataset);
                    adapter.notifyDataSetChanged();
                    Log.e(TAG, "dataset changed");
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String text) {
                    dataset.clear();
                    dataset.addAll(Grammar.getFilteredItems(filter, paramsList, text.length() > 1 ? text : null));
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });

            searchView.setOnCloseListener(() -> {
                // Reset search
                dataset = Grammar.getFilteredItems(filter, paramsList, null);
                adapter.notifyDataSetChanged();
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

    // TODO : hide keyboard on click outside
    private static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
