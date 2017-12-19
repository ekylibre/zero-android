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
import android.widget.Button;
import android.widget.EditText;

import ekylibre.database.ZeroContract;
import ekylibre.zero.R;

/**
 * Created by NUMAG 1-NB on 19/12/2017.
 */

public class ArticleNewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_new);

        Button btn_save = (Button) findViewById(R.id.buttonSaveArticleId);
        btn_save.setOnClickListener(this);
    }

    public void add_article(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues mNewValues = new ContentValues();
        EditText newArticleName = (EditText) findViewById(R.id.editArticleNameId);
        mNewValues.put(ZeroContract.Articles.NAME, newArticleName.getText().toString());
        contentResolver.insert(ZeroContract.Articles.CONTENT_URI, mNewValues);
    }

    @Override
    public void onClick(View _buttonView) {
        //Récupère ce qu'a tapé l'utilisateur dans la zone de texte et la renvoie à la MainActivtity
        if (_buttonView.getId() == R.id.buttonSaveArticleId) {
            add_article(this);

        }
    }
}

