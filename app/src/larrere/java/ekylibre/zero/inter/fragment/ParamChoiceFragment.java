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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ekylibre.util.antlr4.Grammar;
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

        // Add each item matching required abilities, else pass
//        List<String> requiredAbilities = DSL.parse(filter);
        List<String> requiredAbilities = Grammar.parse(filter);
        outer: for (GenericItem item : InterventionFormFragment.paramsList) {
            // Check all abilities are satified
            if (item.abilities != null) {
                for (String requiredAbility : requiredAbilities) {
                    int count = 0;
                    for (String ability : item.abilities)
                        if (!ability.contains(requiredAbility))
                            ++count;
                    if (count == item.abilities.length) {
                        Log.e(TAG, "- - - Item ability -> " + Arrays.toString(item.abilities));
                        Log.e(TAG, "- - - Required ability -> " + requiredAbilities);
                        continue outer;
                    }
                }
                Log.i(TAG, "Item ability -> " + Arrays.toString(item.abilities));
                Log.i(TAG, "Required ability -> " + requiredAbilities);
                // Do things with matching item
                dataset.add(item);
            }
        }

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
        dataset.clear();
//        List<String> requiredAbilities = DSL.parse(filter);
        List<String> requiredAbilities = Grammar.parse(filter);
        outer: for (GenericItem item : InterventionFormFragment.paramsList) {
            // Check all abilities are satified
            if (item.abilities != null) {
                for (String requiredAbility : requiredAbilities) {
                    if (!Arrays.asList(item.abilities).contains(requiredAbility)) {
                        Log.e(TAG, "Filtering (skip) " + Arrays.asList(item.abilities) + "/" + requiredAbility);
                        continue outer;
                    }
                }
                // Do things with matching item
                if (text != null) {
                    String filterText = StringUtils.stripAccents(text.toLowerCase());
                    String name = StringUtils.stripAccents(item.name.toLowerCase());
                    String number = StringUtils.stripAccents(item.number.toLowerCase());
                    if (name.contains(filterText) || number.contains(filterText))
                        dataset.add(item);
                } else
                    dataset.add(item);
            }
        }
        adapter.notifyDataSetChanged();
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
