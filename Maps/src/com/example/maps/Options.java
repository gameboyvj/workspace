package com.example.maps;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.pushback.coal.R;

public class Options extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}
}
