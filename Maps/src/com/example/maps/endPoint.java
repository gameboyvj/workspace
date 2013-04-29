package com.example.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import com.pushback.coal.R;

public class endPoint extends Activity implements OnClickListener {

	Vibrator vibrate;
	Button end;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.endpoint);
		end = (Button) findViewById(R.id.bEnd);
		end.setOnClickListener(this);

		// Get instance of Vibrator from current Context
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// Start immediately
		// Vibrate for 200 milliseconds
		// Sleep for 500 milliseconds
		long[] pattern = { 0, 200, 500 };

		// The "0" means to repeat the pattern starting at the
		// beginning
		// CUIDADO: If you start at the wrong index (e.g., 1) then
		// your pattern will be off --
		// You will vibrate for your pause times and pause for your
		// vibrate times !
		vibrate.vibrate(pattern, 0);
	}

	
	public void onClick(View v) {
		// TODO Auto-generated method stub

		vibrate.cancel();
		finish();

	}
	
	//disables back button
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		vibrate.cancel();
		finish();
	}
	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (event.getAction() == KeyEvent.ACTION_DOWN) {
	        switch (keyCode) {
	        case KeyEvent.KEYCODE_HOME:
	        	vibrate.cancel();
	            finish();
	            return true;
	        }
	    }

	    return super.onKeyDown(keyCode, event);
	}
	*/

}
