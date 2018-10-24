package ekylibre.zero.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.fragments.adapter.ActivitiesRecyclerAdapter;
import ekylibre.zero.fragments.model.ActivityItem;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link OnActivityFragmentInteractionListener}
 * interface.
 */
public class ActivityChoiceFragment extends Fragment {

    private List<ActivityItem> dataset;
    private OnActivityFragmentInteractionListener fragmentListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActivityChoiceFragment() {
    }

    public static ActivityChoiceFragment newInstance() {
        return new ActivityChoiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create dummy data
        // TODO: Get from database
        dataset = new ArrayList<>();
        int i = 0;
        while (i < 5) {
            dataset.add(new ActivityItem(i, "Carotte #" + i, "2019"));
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
            recyclerView.setAdapter(new ActivitiesRecyclerAdapter(dataset, fragmentListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActivityFragmentInteractionListener)
            fragmentListener = (OnActivityFragmentInteractionListener) context;
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
    public interface OnActivityFragmentInteractionListener {
        // TODO: Update argument type and name
        void onActivityInteraction(ActivityItem item);
    }
}
