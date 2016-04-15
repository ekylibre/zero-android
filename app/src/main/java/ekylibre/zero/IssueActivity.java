package ekylibre.zero;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class IssueActivity extends Activity {

    Spinner mIssueNatureSpinner;
    NumberPicker mSeverity;
    NumberPicker mEmergency;
    EditText mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue);

        //récuperation of XML elements
        mIssueNatureSpinner = (Spinner) findViewById(R.id.spinner);
        mSeverity = (NumberPicker) findViewById(R.id.numberPickerSeverity);
        mEmergency = (NumberPicker) findViewById(R.id.numberPickerEmergency);
        mDescription = (EditText) findViewById(R.id.description);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.issueNatures_entries, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mIssueNatureSpinner.setAdapter(adapter);


        //setting the minimum, maximum and initial value of the number pickers
        mSeverity.setMaxValue(4);
        mSeverity.setMinValue(0);
        mSeverity.setValue(2);

        mEmergency.setMaxValue(4);
        mEmergency.setMinValue(0);
        mEmergency.setValue(2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            // case R.id.action_search:
            //     // openSearch();
            //     return true;
            case R.id.action_save:
                saveIssue(item.getActionView());
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static  String selectedImagePath="";
    int count = 0;
    public void takePicture(View v){

        selectedImagePath = Environment.getExternalStorageDirectory() + "/issue"+count+".jpg";
        count++;
        File file = new File(selectedImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);  // set the image file name

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*ImageView mPreview = (ImageView) findViewById(R.id.preview);
        if(resultCode != RESULT_CANCELED){

            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                if (data != null && data.getExtras()!=null) {
                    if (data.getExtras().get("data") != null) {

                        Bitmap bit = (Bitmap) data.getExtras().get("data");
                        mPreview.setImageBitmap(bit);
                    }
                }
            }
        }*/

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("zero", data.toString());
            try {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray(); // convert camera photo to byte array
                // save it in your external storage.
                count++;
                try {
                    FileOutputStream fo = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/_camera.png"));

                    try {
                        fo.write(byteArray);
                        fo.flush();
                        fo.close();
                    } catch (IOException e) {
                    }
                } catch (FileNotFoundException f) {
                }
            }
            catch (NullPointerException n){
            }


            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                    // Image capture failed, advise user
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void saveIssue(View v) {
        Context context = getApplicationContext();
        CharSequence text = "Issue saved";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //http://developer.android.com/guide/topics/providers/content-provider-basics.html

        // Defines an object to contain the new values to insert
        ContentValues mNewValues = new ContentValues();

        /*
        * Sets the values of each column and inserts the word. The arguments to the "put"
        * method are "column name" and "value"
        */
        String[] mTestArray;
        mTestArray = getResources().getStringArray(R.array.issueNatures_values);


        mNewValues.put(ZeroContract.IssuesColumns.NATURE, mTestArray[mIssueNatureSpinner.getSelectedItemPosition()]);
        mNewValues.put(ZeroContract.IssuesColumns.EMERGENCY, mEmergency.getValue());
        mNewValues.put(ZeroContract.IssuesColumns.SEVERITY, mSeverity.getValue());
        mNewValues.put(ZeroContract.IssuesColumns.DESCRIPTION, mDescription.getText().toString());
        mNewValues.put(ZeroContract.IssuesColumns.PINNED, Boolean.FALSE);
//        mNewValues.put(ZeroContract.IssuesColumns.SYNCED_AT, rightNow.get(Calendar.DAY_OF_MONTH)+1 + "/" + rightNow.get(Calendar.MONTH)+1 + "/" + rightNow.get(Calendar.YEAR));
        mNewValues.put(ZeroContract.IssuesColumns.OBSERVED_AT, (new java.util.Date()).getTime());

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // Or use LocationManager.GPS_PROVIDER
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null){
            mNewValues.put(ZeroContract.IssuesColumns.LATITUDE, lastKnownLocation.getLatitude());
            mNewValues.put(ZeroContract.IssuesColumns.LONGITUDE, lastKnownLocation.getLongitude());
        }


        getContentResolver().insert(
                ZeroContract.Issues.CONTENT_URI,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );

        //est censé fermer l'activity
        this.finish();

    }
}
