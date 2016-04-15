package ekylibre.zero;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SortingActivity extends Activity {


    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorting);

        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEditText = (EditText) findViewById(R.id.editText);


        TextView textView = new TextView(this);
        textView.setText("New text");
    }


    public void addValue(View view){

        final EditText input = new EditText(this);
        TextView t = createTextview(mEditText.getText().toString());
        mLayout.addView(t);

    }


    public TextView createTextview(String text){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }


}
