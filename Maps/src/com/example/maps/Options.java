package com.example.maps;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Options extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}
}
