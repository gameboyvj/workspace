package com.example.maps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Builder extends Activity implements OnItemSelectedListener{
	
	TextView destination;
	EditText address;
	Spinner theSpinner;
	double enactedValue=1;
	CheckBox vib;
	CheckBox sou;
	int vibrate=1;
	int sound=0;
	Button save, start, cancel;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.builder);
		destination=(TextView)findViewById(R.id.tvDestination);
		address=(EditText)findViewById(R.id.etAddress);
		theSpinner=(Spinner)findViewById(R.id.sp1);
		vib=(CheckBox)findViewById(R.id.cbVibrate);
		sou=(CheckBox)findViewById(R.id.cbSound);
		save=(Button)findViewById(R.id.bnSave);
		start=(Button)findViewById(R.id.bnSaveStart);
		cancel=(Button)findViewById(R.id.bnCancel);
		
		if (!vib.isChecked()) {
            vib.setChecked(true);
        }
		if (sou.isChecked()) {
            sou.setChecked(false);
        }
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.enact_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		theSpinner.setAdapter(adapter);
	}

	//probably a better way to do this
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
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.cbVibrate:
	            if (checked)
	                vibrate=1;
	            else
	                vibrate=0;
	            break;
	        case R.id.cbSound:
	            if (checked)
	                sound=1;
	            else
	                sound=0;
	            break;
	    }
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bnSave:
			finish();
			break;
		case R.id.bnSaveStart:
			finish();
			break;
		case R.id.bnCancel:
			finish();
			break;
		}
	}
}
