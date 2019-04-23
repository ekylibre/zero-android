package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ekylibre.zero.R;
import ekylibre.zero.inter.InterActivity;


public class ProcedureFamilyChoiceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ProcedureFamilyChoiceFragment() {}

    public static ProcedureFamilyChoiceFragment newInstance() {
        return new ProcedureFamilyChoiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InterActivity.actionBar.setTitle("Nouvelle intervention");

        View rootView = inflater.inflate(R.layout.fragment_procedure_family_choice, container, false);

        Button administrativeButton = rootView.findViewById(R.id.administering);
        Button animalButton = rootView.findViewById(R.id.animal_farming);
        Button vegetalButton = rootView.findViewById(R.id.plant_farming);
        Button processingButton = rootView.findViewById(R.id.processing);
        Button maintenanceButton = rootView.findViewById(R.id.tool_maintaining);

        View.OnClickListener clickListener = v -> {
            if (mListener != null)
                mListener.onProcedureFamilyInteraction(v.getId());
        };

        administrativeButton.setOnClickListener(clickListener);
        animalButton.setOnClickListener(clickListener);
        vegetalButton.setOnClickListener(clickListener);
        processingButton.setOnClickListener(clickListener);
        maintenanceButton.setOnClickListener(clickListener);

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onProcedureFamilyInteraction(Integer id);
    }
}
