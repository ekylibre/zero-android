package ekylibre.zero;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IssueTypeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_type);
    }

    public void Type_Animal(View v) {
        Intent intent = new Intent(this, IssueActivity.class);
        startActivity(intent);
    }
    public void Type_Plant(View v) {
        Intent intent = new Intent(this, IssueActivity.class);
        startActivity(intent);
    }
    public void Type_Equipment(View v) {
        Intent intent = new Intent(this, IssueActivity.class);
        startActivity(intent);
    }
}
