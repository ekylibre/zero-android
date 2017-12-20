package ekylibre.zero.reception;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;


public class ArticleNewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String[] natureList = {getResources().getString(R.string.spinner_selection_nature), getResources().getString(R.string.fertilizer), getResources().getString(R.string.seed), getResources().getString(R.string.chemical)};
        String[] unityList = {getResources().getString(R.string.spinner_selection_unity), "kg", "L", "t"};

        setContentView(R.layout.article_new);

        Button btn_save = (Button) findViewById(R.id.buttonSaveArticleId);
        btn_save.setOnClickListener(this);
        Spinner spn_nat = (Spinner) findViewById(R.id.spinnerNatureArticleId);
        Spinner spn_uni = (Spinner) findViewById(R.id.spinnerUnityArticleId);
        ArrayAdapter<String> adapter_nature = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, natureList);
        spn_nat.setAdapter(adapter_nature);
        ArrayAdapter<String> adapter_unity = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, unityList);
        spn_uni.setAdapter(adapter_unity);
    }

    public void add_article(Context context) {
        //Article name, nature and unity add in data base
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();

        EditText newArticleName = (EditText) findViewById(R.id.editArticleNameId);

        Spinner SpinnerArticleNature = (Spinner) findViewById(R.id.spinnerNatureArticleId);
        Spinner SpinnerArticleUnity = (Spinner) findViewById(R.id.spinnerUnityArticleId);


        mNewValues.put(ZeroContract.Articles.NAME, newArticleName.getText().toString());
        mNewValues.put(ZeroContract.Articles.NATURE, SpinnerArticleNature.getSelectedItem().toString());
        mNewValues.put(ZeroContract.Articles.UNITY, SpinnerArticleUnity.getSelectedItem().toString());
        mNewValues.put(ZeroContract.Articles.EK_ID, 1);

        contentResolver.insert(ZeroContract.Articles.CONTENT_URI, mNewValues);
    }

    @Override
    public void onClick(View _buttonView) {
        if (_buttonView.getId() == R.id.buttonSaveArticleId) {
            if (is_complete()) {
                add_article(this);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.article_saved, Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), R.string.article_cannot_save, Toast.LENGTH_SHORT);
                toast.show();

            }


        }
    }

    private boolean is_complete() {
        EditText newArticleName = (EditText) findViewById(R.id.editArticleNameId);

        Spinner SpinnerArticleNature = (Spinner) findViewById(R.id.spinnerNatureArticleId);
        Spinner SpinnerArticleUnity = (Spinner) findViewById(R.id.spinnerUnityArticleId);

        if (SpinnerArticleNature.getSelectedItemPosition() == 0 || SpinnerArticleUnity.getSelectedItemPosition() == 0 ||  newArticleName.getText().toString().trim().equals("")){
            return false;
        }
        else {
            return true;
        }
    }
}

