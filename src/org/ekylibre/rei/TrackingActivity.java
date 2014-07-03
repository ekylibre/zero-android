package org.ekylibre.rei;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.content.Intent;

public class TrackingActivity extends Activity {
		
		public long masterDuration;
		public long masterStart;
		public Chronometer masterChrono;
		public ImageButton startButton;
		public ImageButton stopButton;
		public ImageButton pauseButton;
		public ImageButton resumeButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

				this.masterChrono = (Chronometer) findViewById(R.id.master_chrono);
				this.startButton  = (ImageButton) findViewById(R.id.start_intervention_button);
				this.stopButton   = (ImageButton) findViewById(R.id.stop_intervention_button);
				this.pauseButton  = (ImageButton) findViewById(R.id.pause_intervention_button);
				this.resumeButton = (ImageButton) findViewById(R.id.resume_intervention_button);
    }

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu items for use in the action bar
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.tracking_menu, menu);
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
		}


    public void stopIntervention(View view) {
				this.masterChrono.stop();
				stopButton.setVisibility(View.GONE);
				pauseButton.setVisibility(View.GONE);
		}


    public void pauseIntervention(View view) {
				this.masterDuration += SystemClock.elapsedRealtime() - this.masterStart;
				this.masterChrono.stop();
				pauseButton.setVisibility(View.GONE);
				startButton.setVisibility(View.GONE);
				stopButton.setVisibility(View.GONE);
				resumeButton.setVisibility(View.VISIBLE);
		}

    public void resumeIntervention(View view) {
				this.masterStart = SystemClock.elapsedRealtime();
				this.masterChrono.setBase(this.masterStart - this.masterDuration);
				this.masterChrono.start();
				pauseButton.setVisibility(View.VISIBLE);
				startButton.setVisibility(View.VISIBLE);
				stopButton.setVisibility(View.VISIBLE);
				resumeButton.setVisibility(View.GONE);
		}

		
}
