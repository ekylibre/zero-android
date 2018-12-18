package ekylibre.zero;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectZoneDialogFragment extends DialogFragment {

    private OnFragmentInteractionListener fragmentListener;
    public List<String> zoneList;

    public SelectZoneDialogFragment(){    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public static SelectZoneDialogFragment newInstance(List<String> list){
        SelectZoneDialogFragment fragment = new SelectZoneDialogFragment();
        fragment.zoneList = list;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.select_zone_dialog,container,false);
        RecyclerView recyclerView = inflatedView.findViewById(R.id.select_zone_dialog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflatedView.getContext()));

        UiSelectZoneAdapter adapter = new UiSelectZoneAdapter(zoneList) ;
        recyclerView.setAdapter(adapter);
        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public  interface OnFragmentInteractionListener {
    void onFragmentInteraction(Object object);
    }
}
