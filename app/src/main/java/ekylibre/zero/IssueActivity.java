package ekylibre.zero;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class IssueActivity extends Activity {

    Spinner spinnerIssueTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        spinnerIssueTitle = (Spinner) findViewById(R.id.spinner);

        //List of the choices in the spinner
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

        ArrayAdapter adapterTitleList = new ArrayAdapter(this,android.R.layout.simple_spinner_item,issueTitleList);
        adapterTitleList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssueTitle.setAdapter(adapterTitleList);



    }


}
