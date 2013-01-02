package com.example.maps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

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

	private ListView mainListView;

	private ArrayAdapter<String> listAdapter;
	Button b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		if (fileList().length == 0) {
			try {
				FileOutputStream fos = openFileOutput("saved",
						Context.MODE_PRIVATE);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				loadEntries();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * try { loadEntries(); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		// setListAdapter(new ArrayAdapter<String>(Home.this,
		// android.R.layout.simple_list_item_1, searches));
		
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

	protected void loadEntries() throws IOException {
		FileOutputStream fos;
		String afterRead = "";
		// checks if any files exist, if they don't, it creates one called
		// "saved"
		if (fileList().length == 0) {
			fos = openFileOutput("saved", Context.MODE_PRIVATE);
		} else {
			// reads through saved and builds a string out of it
			FileInputStream fis = openFileInput("saved");
			int ch;
			StringBuffer fileContent = new StringBuffer("");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer));
			}
			afterRead = fileContent.toString();
			// reads through the built string for the number of new lines
			// Scanner sc = new Scanner(afterRead);
			// int count = 0;
			// while (sc.hasNext()) {
			// if (sc.next().equals("/n")) {
			// count++;
			// }
			// }
			// builds searches array and adds the locations
			// searches = new String[count];
			Scanner sc1 = new Scanner(afterRead);
			// int i = 0;
			while (sc1.hasNext()) {
				searches.add(sc1.nextLine());
				// )[i] = sc1.nextLine();
			}

			//String[] planets = new String[] { "Mercury", "Venus", "Earth",
			//		"Mars", "Jupiter", "Saturn", "Uranus", "Neptune" };

			//ArrayList<String> planetList = new ArrayList<String>();
		//	planetList.addAll(Arrays.asList(planets));
			listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, searches);
			mainListView.setAdapter(listAdapter);
			mainListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String location = searches.get(arg2);// [arg2];
					Class ourClass = null;

					try {
						ourClass = Class.forName("com.example.maps.MainActivity");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Bundle basket = new Bundle();
					basket.putString("location", location);
					Intent ourIntent = new Intent(Home.this, ourClass);
					ourIntent.putExtras(basket);
					startActivity(ourIntent);

					// myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// myIntent.putExtra("pos", pos);
					// startActivity(myIntent);
				}

			});

		}
	}

	/*
	 * @Override protected void onListItemClick(ListView l, View v, int
	 * position, long id) { // TODO Auto-generated method stub
	 * super.onListItemClick(l, v, position, id);
	 * 
	 * String location = searches[position]; try { Class ourClass
	 * =Class.forName("com.example.maps.MainActivity"); Bundle basket=new
	 * Bundle(); basket.putString("location", location); Intent ourIntent = new
	 * Intent(Home.this, ourClass); ourIntent.putExtras(basket);
	 * startActivity(ourIntent);
	 * 
	 * } catch(ClassNotFoundException e) { e.printStackTrace(); } }
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent i=new Intent("com.MAIN");
			startActivity(i);
			break;
		}
	}

}
