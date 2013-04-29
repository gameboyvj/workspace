package com.example.maps;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import com.pushback.coal.R;

public class Splash extends Activity{

	//MediaPlayer ourSound;
	
	@Override
	protected void onCreate(Bundle herpyMcDerpy) {
		// TODO Auto-generated method stub
		super.onCreate(herpyMcDerpy);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		//ourSound = MediaPlayer.create(Splash.this, R.raw.fist);
		//ourSound.start();
		Thread timer = new Thread() {
			public void run() {
				try{
					sleep(2000);
				} catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openHome = new Intent("com.HOME");
					startActivity(openHome);
				}	
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ourSound.release();
		finish();
	}
	
}
