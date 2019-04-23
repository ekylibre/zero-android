package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ekylibre.zero.BuildConfig;
import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.ProcedureChoiceAdapter;


public class ProcedureChoiceFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private List<Pair<String,String>> dataset;
    private String fragmentTag;
    private String titleRes;
    private static final String FRAGMENT_TAG = "tag";

    public ProcedureChoiceFragment() {}

    public static ProcedureChoiceFragment newInstance(String fragmentTag) {
        ProcedureChoiceFragment fragment = new ProcedureChoiceFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TAG, fragmentTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            fragmentTag = getArguments().getString(FRAGMENT_TAG);

        // Compute the dataset, filter by current family
        if (fragmentTag != null) {
            if (fragmentTag.equals(InterActivity.PROCEDURE_CATEGORY_FRAGMENT)) {
                dataset = InterActivity.families.get(InterActivity.currentFamily.second);
                titleRes = InterActivity.currentFamily.second;
            } else {
                dataset = InterActivity.natures.get(InterActivity.currentCategory.first);
                titleRes = InterActivity.currentCategory.first;
            }
        }

        if (BuildConfig.DEBUG)
            Log.d("ChoiceFragment", dataset.toString());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set fragment title to procedure family
        int resId = getResources().getIdentifier(titleRes, "string",
                Objects.requireNonNull(getActivity()).getPackageName());
        InterActivity.actionBar.setTitle(resId);

        View view = inflater.inflate(R.layout.fragment_procedure_category_choice, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ProcedureChoiceAdapter(dataset, listener, fragmentTag));
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        void onItemChoosed(Pair<String,String> item, String fragmentTag);
    }
}
