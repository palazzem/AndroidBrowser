package it.unipg.browser;

import it.unipg.browser.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {
	// Splash screen state
	private boolean activated = true;

	// Timeout of splash screen visualization
	private int splashTimeout = 5000;
	private Thread splashTread = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		// Thread to manage splash screen
		splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int actualTime = 0;
					while (activated && (actualTime < splashTimeout)) {
						sleep(100);
						actualTime += 100;
					}
				} catch (InterruptedException e) {
					// TODO Use Log Class to catch and log exception
				} finally {
					finish();
					// Start the main activity
					Intent mainIntent = new Intent(SplashScreenActivity.this,
							MainActivity.class);
					startActivity(mainIntent);
				}
			}
		};

		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			activated = false;
		}
		return super.onTouchEvent(event);
	}
}
