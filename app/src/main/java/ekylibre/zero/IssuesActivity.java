package ekylibre.zero;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class IssuesActivity extends ListActivity {


    private String[] mStrings = {"issue1","issue2","issue3","issue4","issue5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
        setListAdapter(adapter);

    }
}
