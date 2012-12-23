package com.myfirstproject.the.boston;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Splash extends Activity {

	MediaPlayer ourSound;

	@Override
	protected void onCreate(Bundle herpyMcDerpy) {
		// TODO Auto-generated method stub
		super.onCreate(herpyMcDerpy);
		setContentView(R.layout.splash);
		
		//access preferences
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean music = getPrefs.getBoolean("checkbox", true);
		ourSound = MediaPlayer.create(Splash.this, R.raw.fist);
		if (music == true) {
			ourSound.start();
		}
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openStartingPoint = new Intent("com.MENU");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSound.release();
		finish();
	}

}
