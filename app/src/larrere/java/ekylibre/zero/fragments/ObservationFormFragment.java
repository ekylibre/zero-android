package ekylibre.zero.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ekylibre.zero.R;
import ekylibre.zero.fragments.adapter.PicturesRecyclerAdapter;
import ekylibre.zero.fragments.model.CultureItem;
import ekylibre.zero.fragments.model.IssueItem;
import ekylibre.zero.util.DateTools;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;
import static ekylibre.zero.ObservationActivity.BBCH_FRAGMENT;
import static ekylibre.zero.ObservationActivity.CULTURES_FRAGMENT;
import static ekylibre.zero.ObservationActivity.FORM_FRAGMENT;
import static ekylibre.zero.ObservationActivity.ISSUES_FRAGMENT;
import static ekylibre.zero.ObservationActivity.culturesList;
import static ekylibre.zero.ObservationActivity.date;
import static ekylibre.zero.ObservationActivity.fragmentManager;
import static ekylibre.zero.ObservationActivity.issuesList;
import static ekylibre.zero.ObservationActivity.picturesList;
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

    private Context context;

    private OnFragmentInteractionListener listener;
    private PicturesRecyclerAdapter picturesAdapter;
    public static RecyclerView picturesRecycler;

    private TextView dateTextView;
    private ChipGroup culturesChipsGroup;
    private ChipGroup issuesChipsGroup;
    private int culturesCount;
    private int issuesCount;

    private static final int GALLERY = 1230;
    private static final int CAMERA = 1231;

    public ObservationFormFragment() {
        // Required empty public constructor
    }

    public static ObservationFormFragment newInstance() {
        return new ObservationFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_observation_form, container, false);

        // Get context
        context = getContext();

        // UI Activity
        TextView activityNameTextView = view.findViewById(R.id.form_activity_name);
        TextView activityDetailsTextView = view.findViewById(R.id.form_activity_details);

        // UI date
        ConstraintLayout dateLayout = view.findViewById(R.id.form_date_layout);
        dateTextView = view.findViewById(R.id.form_date_text);

        // UI cultures
        ConstraintLayout culturesLayout = view.findViewById(R.id.form_cultures_layout);
        culturesChipsGroup = view.findViewById(R.id.form_cultures_chips_group);

        // UI BBCH
        ConstraintLayout bbchLayout = view.findViewById(R.id.form_bbch_layout);
        TextView bbchTextView = view.findViewById(R.id.form_bbch_text);

        // UI issues
        ConstraintLayout issuesLayout = view.findViewById(R.id.form_issues_layout);
        issuesChipsGroup = view.findViewById(R.id.form_issues_chips_group);

        // UI pictures
        ConstraintLayout picturesLayout = view.findViewById(R.id.form_picture_layout);
        picturesRecycler = view.findViewById(R.id.form_picture_recycler);
//        picturesContainer = view.findViewById(R.id.form_picture_container);

        // UI Observation Comment
        EditText commentText = view.findViewById(R.id.form_obs_text);

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
                    new DatePickerDialog(context, (dialogView, year, month, day) -> {
                        // Save date in Calendar object
                        date.set(year, month, day);
                        // Display selected date
                        dateTextView.setText(DateTools.display(context, date));
                        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)
                    );
            // Show the dialog
            datePickerDialog.show();
        });
        culturesLayout.setOnClickListener(v -> listener.onFormInteraction(CULTURES_FRAGMENT));
        bbchLayout.setOnClickListener(v -> listener.onFormInteraction(BBCH_FRAGMENT));
        issuesLayout.setOnClickListener(v -> listener.onFormInteraction(ISSUES_FRAGMENT));
        picturesLayout.setOnClickListener(v -> {
            DialogFragment pictureDialog = new PictureDialogFragment();
            pictureDialog.show(fragmentManager, "pictureDialog");
        });

        // Fill UI
        activityNameTextView.setText(selectedActivity.name);
        activityDetailsTextView.setText(selectedActivity.details);
        if (selectedBBCH != null) bbchTextView.setText(selectedBBCH.name);
        dateTextView.setText(DateTools.display(context, date));
        if (observation != null)
            commentText.setText(observation);

        // Constructs cultures chips group
        culturesCount = 0;
        for (CultureItem culture : culturesList) {
            if (culture.is_selected) {
                culturesCount++;
                Chip chip = new Chip(context);
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

        // Constructs issues chips group
        issuesCount = 0;
        for (IssueItem issue : issuesList) {
            if (issue.is_selected) {
                issuesCount++;
                Chip chip = new Chip(context);
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

        picturesRecycler.setLayoutManager(new LinearLayoutManager(context, HORIZONTAL, false));
        picturesAdapter = new PicturesRecyclerAdapter(picturesList);
        picturesRecycler.setAdapter(picturesAdapter);

        if (picturesAdapter.getItemCount() == 0)
            picturesRecycler.setVisibility(View.GONE);

        return view;
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
     */
    public interface OnFragmentInteractionListener {
        void onFormInteraction(String fragmentTag);
    }

    public static class PictureDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Prendre une photo")
                    .setItems(R.array.picture_choice_values, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                break;
                            case 1:
                                Fragment fragment = fragmentManager.findFragmentByTag(FORM_FRAGMENT);
                                if (fragment != null) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    fragment.startActivityForResult(intent, GALLERY);
                                }
                                break;
                        }
                    })
            .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
            return builder.create();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY) {
            Uri pictureUri = data.getData();
            if (!picturesList.contains(pictureUri)) {
                picturesList.add(pictureUri);
                picturesAdapter.notifyItemInserted(picturesList.indexOf(pictureUri));
            }
            if (picturesAdapter.getItemCount() > 0 && picturesRecycler.getVisibility() == View.GONE)
                picturesRecycler.setVisibility(View.VISIBLE);
            else if (picturesAdapter.getItemCount() == 0 && picturesRecycler.getVisibility() == View.VISIBLE)
                picturesRecycler.setVisibility(View.GONE);
        }
    }
}
