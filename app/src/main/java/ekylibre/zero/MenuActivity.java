package ekylibre.zero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void GotoTracking(View v){
        Intent intent = new Intent(this,TrackingActivity.class);
        startActivity(intent);

    }public void GotoNewIssue(View v){
        Intent intent = new Intent(this,IssueTypeActivity.class);
        startActivity(intent);

    }public void GotoConsultIssue(View v){
        Intent intent = new Intent(this,IssuesActivity.class);
        startActivity(intent);
    }
}
