package com.example.maps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Builder extends Activity implements OnItemSelectedListener{
	
	TextView destination;
	Spinner theSpinner;
	double enactedValue=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.builder);
		destination=(TextView)findViewById(R.id.tvDestination);
		theSpinner=(Spinner)findViewById(R.id.sp1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.enact_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		theSpinner.setAdapter(adapter);
	}

	//what a waste to type
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if(arg2==0)
			enactedValue=1;
		else if(arg2==1)
			enactedValue=.5;
		else if(arg2==2)
			enactedValue=1;
		else if(arg2==3)
			enactedValue=2;
		else if(arg2==3)
			enactedValue=5;
		else
			enactedValue=10;
			
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		enactedValue=1;
	}
}
