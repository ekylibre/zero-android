package ekylibre.zero.inter.fragment;

import android.os.Bundle;

public class InterventionFormFragment {



    public InterventionFormFragment() {}

    public static ProcedureChoiceFragment newInstance(String fragmentTag) {
        ProcedureChoiceFragment fragment = new ProcedureChoiceFragment();
//        Bundle args = new Bundle();
//        args.putString(FRAGMENT_TAG, fragmentTag);
//        fragment.setArguments(args);
        return fragment;
    }
}
