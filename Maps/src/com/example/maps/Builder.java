package com.example.maps;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Builder extends Activity implements OnItemSelectedListener, OnClickListener{
	
	TextView destination;
	EditText address;
	Spinner theSpinner;
	double enactedValue=1;
	CheckBox vib;
	CheckBox sou;
	int vibrate=1;
	int sound=0;
	Button save, start, cancel;
	String gotLocation = "";
	String afterRead = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.builder);
		Bundle gotBasket = getIntent().getExtras();
		gotLocation = gotBasket.getString("location");
		afterRead = gotBasket.getString("afterreading");
		destination=(TextView)findViewById(R.id.tvDestination);
		address=(EditText)findViewById(R.id.etAddress);
		theSpinner=(Spinner)findViewById(R.id.sp1);
		vib=(CheckBox)findViewById(R.id.cbVibrate);
		sou=(CheckBox)findViewById(R.id.cbSound);
		save=(Button)findViewById(R.id.bnSave);
		start=(Button)findViewById(R.id.bnSaveStart);
		cancel=(Button)findViewById(R.id.bnCancel);
		save.setOnClickListener(this);
		start.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
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
			//Log.v("Saving","saving");
			try {
				save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Log.v("Saving","saving");
			finish();
			break;
		case R.id.bnSaveStart:
			try {
				save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//code to start page
			
			
			finish();
			break;
		case R.id.bnCancel:
			finish();
			break;
		}
	}
	
	public void save() throws IOException{
		RefUnsortedList<String> savedList = new RefUnsortedList<String>();
		Scanner sc1 = new Scanner(afterRead);
		sc1.useDelimiter(System.getProperty("line.separator"));
		while (sc1.hasNextLine()) {
			String value = sc1.nextLine();
			savedList.addEnd(value);
		}
		//savedList.remove(address);
		savedList.add(address.getText().toString()+"~"+String.valueOf(enactedValue)+"~"+String.valueOf(vibrate)+"~"+String.valueOf(sound));
		deleteFile("saved");
		FileOutputStream fos = openFileOutput("saved", Context.MODE_PRIVATE);
		fos.close();
		// traverses through the linked list and adds the data to the save file
		LLNode<String> list1 = savedList.list;
		FileOutputStream fos1 = openFileOutput("saved", Context.MODE_APPEND);
		while (list1 != null) {
			fos1.write(list1.getInfo().getBytes());
			fos1.write(System.getProperty("line.separator").getBytes());
			// Log.v("LinkedList", list1.getInfo());
			// fos1.write("\n".getBytes());
			list1 = list1.getLink();
		}
		fos1.close();
	}
}
