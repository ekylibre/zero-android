package ekylibre.zero.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ekylibre.zero.R;
import ekylibre.zero.inventory.adapters.UiSelectZoneAdapter;

public class ImageDialogFragment extends DialogFragment {

    private OnFragmentInteractionListener fragmentListener;
    //public List<ItemZoneInventory> zoneList;

    public ImageDialogFragment(){    }


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

    public static ImageDialogFragment newInstance(){
        ImageDialogFragment fragment = new ImageDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.photolayout,container,false);
        ImageView imaprendre = inflatedView.findViewById(R.id.takepictureprendre);
        ImageView imachoisir = inflatedView.findViewById(R.id.choosefromgalery);
        TextView takepictext = inflatedView.findViewById(R.id.textphoto);
        TextView choosepictextt = inflatedView.findViewById(R.id.textgallery);
        takepictext.setText("Prendre une photo");
        choosepictextt.setText("Choisir une photo");
                imaprendre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("myatg","alors ?");
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }
                });
        imachoisir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });
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
    void onFragmentInteraction();
    }
}
