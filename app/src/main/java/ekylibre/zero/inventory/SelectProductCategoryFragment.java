 package ekylibre.zero.inventory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.UiSelectProductCategoryAdapter;
import ekylibre.zero.inventory.adapters.UiSelectZoneAdapter;

public class SelectProductCategoryFragment extends DialogFragment {

    private OnFragmentInteractionListener fragmentListener;
    public List<ItemCategoryInventory> categoryList;

    public SelectProductCategoryFragment(){    }


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

    public static SelectProductCategoryFragment newInstance(List<ItemCategoryInventory> list){
        SelectProductCategoryFragment fragment = new SelectProductCategoryFragment();
        fragment.categoryList = list;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View inflatedView = inflater.inflate(R.layout.select_category_dialog,container,false);

        Button validateCategories = inflatedView.findViewById(R.id.validate_button);
        validateCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.onFragmentInteraction(categoryList);
                dismiss();

                //todo:Make the popup close to get back to the Second Activity
            }
        });
        RecyclerView recyclerView = inflatedView.findViewById(R.id.select_category_dialog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflatedView.getContext()));

        UiSelectProductCategoryAdapter adapter = new UiSelectProductCategoryAdapter(categoryList, fragmentListener) ;
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
    void onFragmentInteraction(List<ItemCategoryInventory> categories);
    }
}
