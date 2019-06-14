package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ekylibre.util.Helper;
import ekylibre.util.antlr4.Grammar;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.SimpleAdapter;
import ekylibre.zero.inter.model.GenericItem;
import ekylibre.zero.inter.model.Zone;

import static ekylibre.zero.inter.fragment.InterventionFormFragment.paramsList;


public class SimpleChoiceFragment extends Fragment {

    private static final String TAG = "SimpleChoiceFragment";

    private OnFragmentInteractionListener listener;
    private List<GenericItem> dataset;
    private String referenceName;
    private String filter;
    private int zoneId;

    public SimpleChoiceFragment() {}

    public static SimpleChoiceFragment newInstance(String refName, String filter, String zoneId) {
        SimpleChoiceFragment fragment = new SimpleChoiceFragment();
        Bundle args = new Bundle();
        args.putString("reference_name", refName);
        args.putString("filter", filter);
        args.putInt("zone_id", Integer.valueOf(zoneId));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            referenceName = getArguments().getString("reference_name");
            filter = getArguments().getString("filter");
            zoneId = getArguments().getInt("zone_id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set fragment title to procedure family
        InterActivity.actionBar.setTitle(Helper.getTranslation(referenceName.replace("zone_", "")));

        // Load required items
        dataset = new ArrayList<>();
        dataset.addAll(Grammar.getFilteredItems(filter, paramsList, null));

        View view;

        // Display empty view with message if no items
        if (dataset.isEmpty())
            view = inflater.inflate(R.layout.empty_recycler, container, false);

        else {

            view = inflater.inflate(R.layout.fragment_recycler_with_search_field, container, false);

            // Get the current zone from his ID
            Log.e("Zone ID -> ", String.valueOf(zoneId));
            Zone currentZone = InterventionFormFragment.zoneList.get(zoneId);


            // Set the adapter
            RecyclerView recyclerView = view.findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            SimpleAdapter adapter = new SimpleAdapter(dataset, listener, currentZone, filter);
            recyclerView.setAdapter(adapter);

            // SeachField logic
            SearchView searchView = view.findViewById(R.id.search_field);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String text) {
                    dataset.clear();
                    dataset.addAll(Grammar.getFilteredItems(filter, paramsList, text));
                    adapter.notifyDataSetChanged();
                    return false;
                }
                @Override public boolean onQueryTextChange(String text) {
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
                // TODO -> hide keyboard
                return false;
            });
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            listener = (OnFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        void onItemChoosed();
    }
}
