package ekylibre.zero;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ekylibre.zero.provider.IssueContract;

public class IssueActivity extends Activity {

    Spinner spinnerIssueTitle;
    NumberPicker mSeverity;
    NumberPicker mEmergency;
    EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        //récuperation of XML elements
        spinnerIssueTitle = (Spinner) findViewById(R.id.spinner);
        mSeverity = (NumberPicker) findViewById(R.id.numberPickerSeverity);
        mEmergency = (NumberPicker) findViewById(R.id.numberPickerEmergency);
        mDescription = (EditText) findViewById(R.id.description);



        //creation of the list of the choices in the spinner
        List issueTitleList = new ArrayList();
        issueTitleList.add("animalTitle1");
        issueTitleList.add("animalTitle2");
        issueTitleList.add("animalTitle3");
        issueTitleList.add("plantTitle1");
        issueTitleList.add("plantTitle2");
        issueTitleList.add("plantTitle3");
        issueTitleList.add("equipmentTitle1");
        issueTitleList.add("equipmentTitle2");
        issueTitleList.add("equipmentTitle3");


        //fill the spinner with the list
        ArrayAdapter adapterTitleList = new ArrayAdapter(this,android.R.layout.simple_spinner_item,issueTitleList);
        adapterTitleList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssueTitle.setAdapter(adapterTitleList);

        //setting the minimum, maximum and initial value of the number pickers
        mSeverity.setMaxValue(5);
        mSeverity.setMinValue(1);
        mSeverity.setValue(3);

        mEmergency.setMaxValue(5);
        mEmergency.setMinValue(1);
        mEmergency.setValue(3);

    }

    public void issueSave(View v){
        Context context = getApplicationContext();
        CharSequence text = "Issue saved";
        int duration = Toast.LENGTH_SHORT;
        Calendar rightNow = Calendar.getInstance();

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //http://developer.android.com/guide/topics/providers/content-provider-basics.html

        // Defines a new Uri object that receives the result of the insertion
        Uri mNewUri;

        // Defines an object to contain the new values to insert
        ContentValues mNewValues = new ContentValues();

        /*
        * Sets the values of each column and inserts the word. The arguments to the "put"
        * method are "column name" and "value"
        */


        mNewValues.put(IssueContract.IssueColumns.NATURE, "fusariose");
        mNewValues.put(IssueContract.IssueColumns.EMERGENCY, mEmergency.getValue());
        mNewValues.put(IssueContract.IssueColumns.SEVERITY,  mSeverity.getValue());
        mNewValues.put(IssueContract.IssueColumns.DESCRIPTION, mDescription.getText().toString());
        mNewValues.put(IssueContract.IssueColumns.PINNED, Boolean.FALSE);
        mNewValues.put(IssueContract.IssueColumns.SYNCED_AT, rightNow.get(Calendar.DAY_OF_MONTH)+1 + "/" + rightNow.get(Calendar.MONTH)+1 + "/" + rightNow.get(Calendar.YEAR));



        getContentResolver().insert(
                IssueContract.Issues.CONTENT_URI,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );

        //est censé fermer l'activity
        this.finish();

    }
}
