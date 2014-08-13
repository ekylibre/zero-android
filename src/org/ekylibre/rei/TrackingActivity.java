package org.ekylibre.rei;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class TrackingActivity extends Activity implements LocationListener {
		
		private long masterDuration;
		private long masterStart;
		private Chronometer masterChrono;
		private Button scanButton;
		private ImageButton startButton, stopButton, pauseButton, resumeButton;
		private TextView coordinates, barcode;
		private LocationManager locationManager;
		private String locationProvider;
    private DatabaseHelper dh;
    private SQLiteDatabase db;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking);
        
				this.masterChrono = (Chronometer) findViewById(R.id.master_chrono);
				this.coordinates  = (TextView)    findViewById(R.id.coordinates);
				this.barcode      = (TextView)    findViewById(R.id.barcode);
				this.startButton  = (ImageButton) findViewById(R.id.start_intervention_button);
				this.stopButton   = (ImageButton) findViewById(R.id.stop_intervention_button);
				this.pauseButton  = (ImageButton) findViewById(R.id.pause_intervention_button);
				this.resumeButton = (ImageButton) findViewById(R.id.resume_intervention_button);
				this.scanButton   = (Button)      findViewById(R.id.scan_code_button);

        // Initialize DB
        this.dh           = new DatabaseHelper(this.getApplication());
        this.db           = this.dh.getWritableDatabase();        

				// Acquire a reference to the system Location Manager
				this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
				this.locationProvider = LocationManager.GPS_PROVIDER;
    }

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu items for use in the action bar
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.tracking, menu);
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
        case R.id.action_settings:
						intent = new Intent(this, SettingsActivity.class);
						startActivity(intent);  
            return true;
        default:
            return super.onOptionsItemSelected(item);
				}
		}


    public void startIntervention(View view) {
				this.masterStart = SystemClock.elapsedRealtime();
				this.masterDuration = 0;
				this.masterChrono.setBase(this.masterStart);
				this.masterChrono.start();
				stopButton.setVisibility(View.VISIBLE);
				pauseButton.setVisibility(View.VISIBLE);
				scanButton.setVisibility(View.VISIBLE);
				this.startTracking();
		}


    public void stopIntervention(View view) {
				this.masterChrono.stop();
				stopButton.setVisibility(View.GONE);
				pauseButton.setVisibility(View.GONE);
				scanButton.setVisibility(View.GONE);
				this.stopTracking();
		}


    public void pauseIntervention(View view) {
				this.masterDuration += SystemClock.elapsedRealtime() - this.masterStart;
				this.masterChrono.stop();
				pauseButton.setVisibility(View.GONE);
				startButton.setVisibility(View.GONE);
				stopButton.setVisibility(View.GONE);
				scanButton.setVisibility(View.GONE);
				resumeButton.setVisibility(View.VISIBLE);
				this.stopTracking();
		}

    public void resumeIntervention(View view) {
				this.masterStart = SystemClock.elapsedRealtime();
				this.masterChrono.setBase(this.masterStart - this.masterDuration);
				this.masterChrono.start();
				resumeButton.setVisibility(View.GONE);
				pauseButton.setVisibility(View.VISIBLE);
				startButton.setVisibility(View.VISIBLE);
				stopButton.setVisibility(View.VISIBLE);
				scanButton.setVisibility(View.VISIBLE);
				this.startTracking();
		}


    public void scanCode(View view) {
				IntentIntegrator integrator = new IntentIntegrator(this);
				integrator.initiateScan();
		}

		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
				if (scanResult != null) {
						// handle scan result
				}
				// else continue with any other code you need in the method
		}

		private void startTracking() {
				this.locationManager.requestLocationUpdates(this.locationProvider, 500, 0, this);
		}

		private void stopTracking() {
				this.locationManager.removeUpdates(this);
		}

		public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				this.coordinates.setText("LATLNG: " + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
        this.addCrumb(location);
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		public void onProviderEnabled(String provider) {}
		
		public void onProviderDisabled(String provider) {}


    

		private void addCrumb(Location location) {
        Crumb crumb = new Crumb(location);
        crumb.insert(this.db);
		}


		
}
