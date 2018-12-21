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
import ekylibre.zero.inventory.adapters.UiSelectProductTypeAdapter;

 public class SelectProductTypeFragment extends DialogFragment {

     private OnTypeFragmentInteractionListener fragmentListener;
     public List<ItemTypeInventory> typeList;

     public SelectProductTypeFragment(){    }


     @Override
     public void onAttach(Context context) {
         super.onAttach(context);
         if (context instanceof OnTypeFragmentInteractionListener) {
             fragmentListener = (OnTypeFragmentInteractionListener) context;
         } else {
             throw new RuntimeException(context.toString()
                     + " must implement OnFragmentInteractionListener");
         }
     }

     public static SelectProductTypeFragment newInstance(List<ItemTypeInventory> list){
         SelectProductTypeFragment fragment = new SelectProductTypeFragment();
         fragment.typeList = list;
         return fragment;
     }

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

     }

     @Nullable
     @Override
     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         final View inflatedView = inflater.inflate(R.layout.select_type_dialog,container,false);

         Button validateTypes = inflatedView.findViewById(R.id.validatetype_button);
         validateTypes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int length = typeList.size();
                 for (int i=0; i<length; i++) {
                     if (typeList.get(i).is_selected){
                         //todo:Add a chip to the category chipGroup when it doesn't make the app crash anymore
                     }
                 }

                 //todo:Make the popup close to get back to the Second Activity
             }
         });
         RecyclerView recyclerView = inflatedView.findViewById(R.id.select_type_dialog_list);
         recyclerView.setLayoutManager(new LinearLayoutManager(inflatedView.getContext()));

         UiSelectProductTypeAdapter adapter = new UiSelectProductTypeAdapter(typeList, fragmentListener) ;
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

     public  interface OnTypeFragmentInteractionListener {
     void onFragmentInteraction(ItemTypeInventory zone);
     }
 }
