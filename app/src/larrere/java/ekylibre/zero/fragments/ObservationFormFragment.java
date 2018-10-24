package ekylibre.zero.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import ekylibre.zero.R;
import ekylibre.zero.fragments.model.CultureItem;
import ekylibre.zero.fragments.model.IssueItem;
import ekylibre.zero.util.DateTools;

import static ekylibre.zero.ObservationActivity.BBCH_FRAGMENT;
import static ekylibre.zero.ObservationActivity.CULTURES_FRAGMENT;
import static ekylibre.zero.ObservationActivity.ISSUES_FRAGMENT;
import static ekylibre.zero.ObservationActivity.culturesList;
import static ekylibre.zero.ObservationActivity.date;
import static ekylibre.zero.ObservationActivity.issuesList;
import static ekylibre.zero.ObservationActivity.observation;
import static ekylibre.zero.ObservationActivity.selectedActivity;
import static ekylibre.zero.ObservationActivity.selectedBBCH;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ObservationFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ObservationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObservationFormFragment extends Fragment {

    private static final int CULTURE_CHOICE = 1001;

    private OnFragmentInteractionListener listener;

    // UI
    private ConstraintLayout dateLayout;
    private ConstraintLayout culturesLayout;
    private ConstraintLayout bbchLayout;
    private ConstraintLayout issuesLayout;
    private TextView dateTextView;
    private TextView bbchTextView;
    private ChipGroup culturesChipsGroup;
    private ChipGroup issuesChipsGroup;
    private TextInputLayout commentInput;
    private EditText commentText;
    private int culturesCount;
    private int issuesCount;

    public ObservationFormFragment() {
        // Required empty public constructor
    }

    public static ObservationFormFragment newInstance() {
        // Bundle args = new Bundle();
        // args.putSerializable(ARG_ACTIVITY, activityParam);
        // fragment.setArguments(args);
        return new ObservationFormFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get selected activity from previous fragment
//        if (getArguments() != null)
//            selectedActivity = (ActivityItem) getArguments().getSerializable(ARG_ACTIVITY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_observation_form, container, false);

        // UI Activity
        TextView activityNameTextView = inflatedView.findViewById(R.id.form_activity_name);
        TextView activityDetailsTextView = inflatedView.findViewById(R.id.form_activity_details);

        // UI date
        dateLayout = inflatedView.findViewById(R.id.form_date_layout);
        dateTextView = inflatedView.findViewById(R.id.form_date_text);

        // UI cultures
        culturesLayout = inflatedView.findViewById(R.id.form_cultures_layout);
        culturesChipsGroup = inflatedView.findViewById(R.id.form_cultures_chips_group);

        // UI BBCH
        bbchLayout = inflatedView.findViewById(R.id.form_bbch_layout);
        bbchTextView = inflatedView.findViewById(R.id.form_bbch_text);

        // UI issues
        issuesLayout = inflatedView.findViewById(R.id.form_issues_layout);
        issuesChipsGroup = inflatedView.findViewById(R.id.form_issues_chips_group);

        // UI Observation Comment
        commentInput = inflatedView.findViewById(R.id.form_obs_text);
        commentText = commentInput.getEditText();

        // Save current observation text
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable editable) { observation = editable.toString(); }
        });

        // Set clickListener
        dateLayout.setOnClickListener(v -> {
            // Set the datePickerDialog
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(v.getContext(), (view, year, month, day) -> {
                        // Save date in Calendar object
                        date.set(year, month, day);
                        // Display selected date
                        dateTextView.setText(DateTools.display(v.getContext(), date));
                        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)
                    );
            // Show the dialog
            datePickerDialog.show();
        });
        culturesLayout.setOnClickListener(v -> listener.onFormInteraction(CULTURES_FRAGMENT));
        bbchLayout.setOnClickListener(v -> listener.onFormInteraction(BBCH_FRAGMENT));
        issuesLayout.setOnClickListener(v -> listener.onFormInteraction(ISSUES_FRAGMENT));

        // Fill UI
        activityNameTextView.setText(selectedActivity.name);
        activityDetailsTextView.setText(selectedActivity.details);
        if (selectedBBCH != null) bbchTextView.setText(selectedBBCH.name);
        dateTextView.setText(DateTools.display(getContext(), date));
        commentText.setText(observation);

        culturesCount = 0;
        for (CultureItem culture : culturesList) {
            if (culture.is_selected) {
                culturesCount++;
                Chip chip = new Chip(container.getContext());
                chip.setText(culture.name);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    culturesChipsGroup.removeView(chip);
                    culture.is_selected = false;
                    if (--culturesCount == 0)
                        culturesChipsGroup.setVisibility(View.GONE);
                });
                culturesChipsGroup.addView(chip);
            }
        }
        culturesChipsGroup.setVisibility(culturesCount > 0 ? View.VISIBLE : View.GONE);

        issuesCount = 0;
        for (IssueItem issue : issuesList) {
            if (issue.is_selected) {
                issuesCount++;
                Chip chip = new Chip(container.getContext());
                chip.setText(issue.name);
                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(v -> {
                    issuesChipsGroup.removeView(chip);
                    issue.is_selected = false;
                    if (--issuesCount == 0)
                        issuesChipsGroup.setVisibility(View.GONE);
                });
                issuesChipsGroup.addView(chip);
            }
        }
        issuesChipsGroup.setVisibility(issuesCount > 0 ? View.VISIBLE : View.GONE);

        return inflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFormInteraction(int action);
    }
}
