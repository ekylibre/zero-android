package ekylibre.zero.fragments;

import android.content.Context;
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
import ekylibre.zero.fragments.adapter.ActivitiesRecyclerAdapter;
import ekylibre.zero.fragments.adapter.BBCHRecyclerAdapter;
import ekylibre.zero.fragments.model.ActivityItem;
import ekylibre.zero.fragments.model.BBCHItem;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link OnBBCHFragmentInteractionListener}
 * interface.
 */
public class BBCHChoiceFragment extends Fragment {

    private List<BBCHItem> dataset;
    private OnBBCHFragmentInteractionListener fragmentListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BBCHChoiceFragment() {
    }

    public static BBCHChoiceFragment newInstance() {
        return new BBCHChoiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create dummy data
        // TODO: Get from database
        dataset = new ArrayList<>();
        int i = 0;
        while (i < 15) {
            dataset.add(new BBCHItem(i, "Stade #" + i));
            i++;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_choice, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new BBCHRecyclerAdapter(dataset, fragmentListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBBCHFragmentInteractionListener)
            fragmentListener = (OnBBCHFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnActivityFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBBCHFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBBCHInteraction(BBCHItem item);
    }
}
