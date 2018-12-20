package ekylibre.zero.inventory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ekylibre.zero.R;

public class TestPhoto extends AppCompatActivity implements ImageDialogFragment.OnFragmentInteractionListener {
    @Override
    public  void onFragmentInteraction() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_popups);

        TextView addInventoryZone = findViewById(R.id.bouton1);
        addInventoryZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View addInventoryZone) {
                ImageDialogFragment imageDialogFragment = ImageDialogFragment.newInstance();
                imageDialogFragment.show(getFragmentTransaction(),"dialog");

            }
        });

    }

    private FragmentTransaction getFragmentTransaction (){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev!= null){
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }


    /*class MyButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View _buttonView) {
            if (_buttonView.getId() == R.id.AddInventoryZone) {

            }
        }
    }*/
}

