package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;
import ekylibre.zero.inter.adapter.ProcedureChoiceAdapter;


public class ProcedureChoiceFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private List<Pair<String,String>> dataset;
    private String fragmentTag;
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

        String titleRes = "";

        // Compute the dataset, filter by current family
        if (fragmentTag != null) {
            if (fragmentTag.equals(InterActivity.PROCEDURE_CATEGORY_FRAGMENT)) {
                dataset = InterActivity.families.get(InterActivity.currentFamily.second);
                titleRes = InterActivity.currentFamily.second;
            } else {
                Log.e("tag", fragmentTag);
                dataset = InterActivity.natures.get(InterActivity.currentCategory.first);
                titleRes = InterActivity.currentCategory.first;
            }
        }

        Log.e("ChoiceFragment", dataset.toString());

        // Set fragment title to procedure family
        int resId = getResources().getIdentifier(titleRes, "string",
                Objects.requireNonNull(getActivity()).getPackageName());
        InterActivity.actionBar.setTitle(resId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyvalueitem_list, container, false);

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
    public void onAttach(Context context) {
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
