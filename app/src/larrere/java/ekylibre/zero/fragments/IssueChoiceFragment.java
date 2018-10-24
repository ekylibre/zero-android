package ekylibre.zero.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ekylibre.zero.R;
import ekylibre.zero.fragments.adapter.IssuesRecyclerAdapter;
import ekylibre.zero.fragments.model.IssueItem;

import static ekylibre.zero.ObservationActivity.issuesList;

public class IssueChoiceFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IssueChoiceFragment() {
    }

    public static IssueChoiceFragment newInstance() {
        return new IssueChoiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create dummy data
        // TODO: Get from database
        int i = 0;
        if (issuesList.isEmpty())
            while (i < 14) {
                issuesList.add(new IssueItem(i, "Ravageur #" + i, null));
                i++;
            }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issues, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.issue_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        IssuesRecyclerAdapter adapter = new IssuesRecyclerAdapter(issuesList);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
