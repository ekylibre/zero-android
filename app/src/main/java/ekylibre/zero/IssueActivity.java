package ekylibre.zero;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import ekylibre.database.ZeroContract;
import ekylibre.util.AccountTool;
import ekylibre.util.PermissionManager;
import ekylibre.util.UpdatableActivity;
import ekylibre.util.UpdatableClass;

public class IssueActivity extends UpdatableActivity
{

    public static final int SHOW_PICTURE = 30;

    public static final int REQUEST_TAKE_PHOTO = 10;

    private final String TAG = "ISSUE_DEBUG";

    File photoFile = null;


    private File picturesFile;
    private String appDirectoryName = "ZERO_ISSUE";
    public int count = 0;

    String mCurrentPhotoPath;
    Spinner mIssueNatureSpinner;
    NumberPicker mSeverity;
    NumberPicker mEmergency;
    EditText mDescription;
    ImageView mImagePreview;
    Account mAccount;
    
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue);
        super.setToolBar();

        mAccount = AccountTool.getCurrentAccount(this);
        mIssueNatureSpinner = (Spinner) findViewById(R.id.spinner);
        mSeverity = (NumberPicker) findViewById(R.id.numberPickerSeverity);
        mEmergency = (NumberPicker) findViewById(R.id.numberPickerEmergency);
        mDescription = (EditText) findViewById(R.id.description);
        mImagePreview = (ImageView) findViewById(R.id.imagePreview);

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

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //create a new file for this issue

        picturesFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString());
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
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_save:
                saveIssue(item.getActionView());
                return true;
            case android.R.id.home:
            {
                onBackPressed();
                return (true);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void takePicture(View v) throws IOException{

        if (!PermissionManager.storagePermissions(this, this))
            return;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go

            photoFile = createImageFile(picturesFile);
            if (photoFile == null)
                return;

            Log.d(TAG, "file name = " + photoFile.toString());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(photoFile)));
        }
    }

    public File createImageFile(File picturesFile) throws IOException {

        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = (query_last_issue_id() + 1) + "_ISSUE_" + count++;
        Log.d(TAG, "image file name = " + imageFileName);

        File image_path = new File(picturesFile, "." + imageFileName + ".jpg");
        if (!image_path.createNewFile())
            return (null);

        Log.d(TAG, "image path = " + image_path.toString());

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image_path.getAbsolutePath();
        return (image_path);
    }

    private int query_last_issue_id()
    {
        String[] projectionIssueID = {ZeroContract.IssuesColumns._ID};

        Cursor cursorIssue = getContentResolver().query(
                ZeroContract.Issues.CONTENT_URI,
                projectionIssueID,
                null,
                null,
                ZeroContract.IssuesColumns._ID + " DESC LIMIT 1");
        if (cursorIssue == null || cursorIssue.getCount() == 0)
            return (0);
        cursorIssue.moveToFirst();
        int ret = cursorIssue.getInt(0);
        cursorIssue.close();
        return (ret);
    }

    /**
     // Finds the first image in the specified folder and uses it to open the devices native gallery app with all images in that folder.
    public void OpenGalleryFromPathToFolder(Context context, String folderPath) throws IOException{

        File folder = new File(folderPath);
        File[] allFiles = folder.listFiles();

        if (allFiles != null && allFiles.length > 0)
        {
            Uri imageInFolder = getImageContentUri(context, allFiles[0]);

            if (imageInFolder != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(imageInFolder);

                startActivityForResult(intent, SHOW_PICTURE);
            }
        }
    }

    // converts the absolute path of a file to a content path
    // absolute path example: /storage/emulated/0/Pictures/folderName/Image1.jpg
    // content path example: content://media/external/images/media/47560
    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{android.provider.MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return android.net.Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Bitmap image = BitmapFactory.decodeFile(photoFile.getPath());
                mImagePreview.setImageBitmap(image);
                mImagePreview.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                photoFile.delete();
                count--;
                // User cancelled the image capture
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SHOW_PICTURE) {

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

        mNewValues.put(ZeroContract.IssuesColumns.USER, AccountTool.getCurrentAccount(this).name);
        mNewValues.put(ZeroContract.IssuesColumns.SYNCED, 0);
        mNewValues.put(ZeroContract.IssuesColumns.NATURE, mTestArray[mIssueNatureSpinner.getSelectedItemPosition()]);
        mNewValues.put(ZeroContract.IssuesColumns.EMERGENCY, mEmergency.getValue());
        mNewValues.put(ZeroContract.IssuesColumns.SEVERITY, mSeverity.getValue());
        mNewValues.put(ZeroContract.IssuesColumns.DESCRIPTION, mDescription.getText().toString());
        mNewValues.put(ZeroContract.IssuesColumns.PINNED, Boolean.FALSE);
//        mNewValues.put(ZeroContract.IssuesColumns.SYNCED_AT, rightNow.get(Calendar.DAY_OF_MONTH)+1 + "/" + rightNow.get(Calendar.MONTH)+1 + "/" + rightNow.get(Calendar.YEAR));
        mNewValues.put(ZeroContract.IssuesColumns.OBSERVED_AT, (new Date()).getTime());

        if ((Build.VERSION.SDK_INT >= 23
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                || Build.VERSION.SDK_INT < 23)
        {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            // Or use LocationManager.GPS_PROVIDER
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null) {
                mNewValues.put(ZeroContract.IssuesColumns.LATITUDE, lastKnownLocation.getLatitude());
                mNewValues.put(ZeroContract.IssuesColumns.LONGITUDE, lastKnownLocation.getLongitude());
            }
        }

        getContentResolver().insert(
                ZeroContract.Issues.CONTENT_URI,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );
        this.finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!AccountTool.isAnyAccountExist(this))
        {
            AccountTool.askForAccount(this, this);
            return;
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Issue Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ekylibre.zero/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Issue Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ekylibre.zero/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
