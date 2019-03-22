package ekylibre.zero.inter.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ekylibre.zero.R;


public class ProcedureChoice1Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ProcedureChoice1Fragment() {}

    public static ProcedureChoice1Fragment newInstance() {
        return new ProcedureChoice1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        List<Pair<String,String>> dataset = Arrays.asList(
//                Pair.create("administrative", "Administratif"),
//                Pair.create("animal_production", "Production animale"),
//                Pair.create("vegetal_production", "Production végétale"),
//                Pair.create("processing", "Transformation"),
//                Pair.create("maintenance", "Maintenance")
//        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_procedure_choice1, container, false);

        Button administrativeButton = rootView.findViewById(R.id.administering);
        Button animalButton = rootView.findViewById(R.id.animal_farming);
        Button vegetalButton = rootView.findViewById(R.id.plant_farming);
        Button processingButton = rootView.findViewById(R.id.processing);
        Button maintenanceButton = rootView.findViewById(R.id.maintenance);

        View.OnClickListener clickListener = v -> {
            if (mListener != null)
                mListener.onFragmentInteraction(v.getId());
        };

        administrativeButton.setOnClickListener(clickListener);
        animalButton.setOnClickListener(clickListener);
        vegetalButton.setOnClickListener(clickListener);
        processingButton.setOnClickListener(clickListener);
        maintenanceButton.setOnClickListener(clickListener);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
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
        // TODO: Update argument type and name
        void onFragmentInteraction(int id);
    }
}
