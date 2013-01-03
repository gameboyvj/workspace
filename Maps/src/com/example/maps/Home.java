package com.example.maps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Home extends Activity implements OnClickListener {

	ArrayList<String> searches = new ArrayList<String>();
	RefUnsortedList<String> savedList=new RefUnsortedList<String>();
	private ListView mainListView;
	String finalfinalread="";
	private ArrayAdapter<String> listAdapter;
	Button b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.customtitle);
		b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(this);
		mainListView = (ListView) findViewById(R.id.listView1);
		// LinearLayout layout = (LinearLayout)
		// getWindow().findViewById(R.id.title_complex);
		// layout.addView(new Button(this));
		// layout.addView(new Button(this));
		String afterRead = "";
		
		//checks if there are any save files
		if (fileList().length == 0) {
			//creates a new save file called "saved"
			try {
				FileOutputStream fos = openFileOutput("saved",
						Context.MODE_PRIVATE);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//if a save file already exists, it goes to load the file and fill in the listView
			try {
				loadEntries();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.aboutUs:
			Intent i = new Intent("com.ABOUT");
			startActivity(i);
			break;
		case R.id.options:
			Intent p = new Intent("com.OPTIONS");
			startActivity(p);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return false;
	}
	
	//does all the reading, conversion from bytes to string and fills the ListView with it
	protected void loadEntries() throws IOException {
		//Some redundancy here due to copy pasting, everything still works tho
		FileOutputStream fos;
		String afterRead = "";
		// checks if any files exist, if they don't, it creates one called
		// "saved"
		if (fileList().length == 0) {
			fos = openFileOutput("saved", Context.MODE_PRIVATE);
		} else {
			// reads through "saved" and builds a string out of it
			FileInputStream fis = openFileInput("saved");
			int ch;
			StringBuffer fileContent = new StringBuffer("");
			byte[] buffer = new byte[1024];
			int length;
			
			//some magic i got from stackOverflow
			//reads the bytes from "saved" and converts to 1 string
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer));
			}
			afterRead = fileContent.toString();
						
			// builds searches arraylist and adds the location
			Scanner sc1 = new Scanner(afterRead);
			// int i = 0;
			while (sc1.hasNext()) {
				String value=sc1.nextLine();
				if(!value.equals("\\n")){
					searches.add(value);
				}
			}
			//stuff for building listview
			listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, searches);
			final String finalread=afterRead;
			finalfinalread=afterRead;
			mainListView.setAdapter(listAdapter);
			//sets up onclicklistener for the listview
			mainListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String location = searches.get(arg2);// [arg2];
					Class ourClass = null;
					//always goes to MainActivity
					try {
						ourClass = Class.forName("com.example.maps.MainActivity");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//builds basket that contains the location and the string containing previous searches
					Bundle basket = new Bundle();
					basket.putString("location", location);
					basket.putString("afterreading", finalread);
					//builds intent, adds the basket and starts activity
					Intent ourIntent = new Intent(Home.this, ourClass);
					ourIntent.putExtras(basket);
					startActivity(ourIntent);

					}

			});

		}
	}

	//this will be the New button where you can put in a new search rather than use an older one
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			//basically the same as before but sends an empty string for the address and sends the string containing all previous searches
			Intent i=new Intent("com.MAIN");
			Bundle basket = new Bundle();
			basket.putString("location", "");
			basket.putString("afterreading", finalfinalread);
			i.putExtras(basket);
			startActivity(i);
			break;
		}
	}
}
