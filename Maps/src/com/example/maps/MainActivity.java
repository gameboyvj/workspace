package com.example.maps;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.w3c.dom.Node;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/*
 * THINGS TO IMRPOVE/CHECK:
 * won't work if lat/long is exactly 0
 * should be fine if address is invalid or empty because try/catch?
 * To VISHAL: add more stuff to these comments as necessary, and PUT COMMENTS EVERYWHERE
 * Check if vibration and gps location updates turn off after hitting stop
 * (vibrator isn't set up until distance <=1 so duno what will happen)
 * What happens if hit stop without starting(should be fine i think)?
 * Do we want to stop vibration on leaving app? (onPause)
 * 
 */
public class MainActivity extends MapActivity implements View.OnClickListener {

	private LocationManager lm;
	protected LocationListener locationListener;

	MapView map;
	private MapController mc;
	TextView latlong;
	EditText destAddress;
	Button start, stop;
	double destLat = 0;
	double destLong = 0;
	Vibrator vibrate;
	String gotLocation = "";
	String afterRead = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);
		// GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo;
		try {
			Bundle gotBasket = getIntent().getExtras();
			gotLocation = gotBasket.getString("location");
			afterRead = gotBasket.getString("afterreading");
			if(!gotLocation.equals("")){
				destAddress.setText(gotLocation);
				start.performClick();
				//loadNow(gotLocation);
			}
			// question.setText(gotBread);
		}catch(NullPointerException e5){
			e5.printStackTrace();
		} finally {
			latlong = (TextView) findViewById(R.id.tvlatlong);
			destAddress = (EditText) findViewById(R.id.etAddress);
			start = (Button) findViewById(R.id.bStart);
			stop = (Button) findViewById(R.id.bStop);
			start.setOnClickListener(this);
			stop.setOnClickListener(this);

			// //////////////////////
			mc = map.getController();

			// String coordinates[] = {"36.487303","-94.308493"};

			// double lat = Double.parseDouble(coordinates[0]);
			// double lng = Double.parseDouble(coordinates[1]);

			// GeoPoint p = new GeoPoint( //represents a geographical location
			// (int) (lat * 1E6),
			// (int) (lng * 1E6));

			// mc.animateTo(p); //map goes to the coordinates
			// mc.setZoom(17); //set your zoom level
			// map.invalidate();
			// ////////////////////

			// Touchy t = new Touchy();
			// List<Overlay> overlayList = map.getOverlays();
			// overlayList.add(t);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			if (loc != null && destLat != 0 && destLong != 0) {
				// Toast.makeText(
				// getBaseContext(),
				// "Location changed : Lat: " + loc.getLatitude()
				// + " Lng: " + loc.getLongitude(),
				// Toast.LENGTH_SHORT).show();

				// GeoPoint p = new GeoPoint((int) (loc.getLatitude() * 1E6),
				// (int) (loc.getLongitude() * 1E6));
				// mc.animateTo(p);
				// mc.setZoom(16);
				// map.invalidate();
				double currentLat = loc.getLatitude();
				double currentLong = loc.getLongitude();
				double dist = distance(destLat, destLong, currentLat,
						currentLong);
				latlong.setText("Distance to destination: "
						+ String.valueOf(dist));

				if (dist <= 1) {
					Intent goEndPoint = new Intent("com.ENDPOINT");
					startActivity(goEndPoint);
					/*
					 * // Get instance of Vibrator from current Context vibrate
					 * = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					 * 
					 * // Start immediately // Vibrate for 200 milliseconds //
					 * Sleep for 500 milliseconds long[] pattern = { 0, 200, 500
					 * };
					 * 
					 * // The "0" means to repeat the pattern starting at the //
					 * beginning // CUIDADO: If you start at the wrong index
					 * (e.g., 1) then // your pattern will be off -- // You will
					 * vibrate for your pause times and pause for your //
					 * vibrate times ! vibrate.vibrate(pattern, 0);
					 */
				}
			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// vibrate.cancel(); // stops the vibration alarm on leaving app?
		super.onPause();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bStart:
			// hides keyboard after clicking Start
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(destAddress.getWindowToken(), 0);

			// creates error alert screen
			if (destAddress.getText().toString().equals("")) {
				AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setTitle("Missing Address");
				adb.setMessage("Please enter a valid Address");
				adb.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				// create alert dialog
				AlertDialog alertDialog = adb.create();

				// show it
				alertDialog.show();

			} else {
				try {
					saveAddress(destAddress.getText().toString());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Geocoder coder = new Geocoder(this);
					List<Address> address;
					// test
					address = coder.getFromLocationName(destAddress.getText()
							.toString(), 5);
					Address location = address.get(0);
					destLat = location.getLatitude();
					destLong = location.getLongitude();

					GeoPoint p1 = new GeoPoint(
							(int) (location.getLatitude() * 1E6),
							(int) (location.getLongitude() * 1E6));

					mc.animateTo(p1); // map goes to the coordinates
					mc.setZoom(17); // set your zoom level
					map.invalidate();

					// double currentLat = loc.getLatitude();
					// double currentLong = loc.getLongitude();
					// double currentLat = 40.4286995;
					// double currentLong = -74.3635812;
					// double dist = distance(destLat, destLong, currentLat,
					// currentLong);
					// latlong.setText("Distance to destination: " +
					// String.valueOf(dist));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// ---use the LocationManager class to obtain GPS
					// locations---
					lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

					locationListener = new MyLocationListener(); // a location
																	// listener
																	// to
																	// be used
																	// below
					SharedPreferences getPrefs = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					long interval = getPrefs.getLong("updateInterval", 1); // gets
																			// value
																			// for
																			// gps
																			// update
																			// interval,
																			// defaults
																			// to
																			// 1
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							interval * 60 * 1000, 0, locationListener); // updates
																		// your
					// location
					// every 1
					// min
				}
				break;
				/*
				 * case R.id.bStop: vibrate.cancel(); // should stop vibration
				 * on stop button lm.removeUpdates(locationListener); // should
				 * stop gps updates of // locationListener break;
				 */
			}
		}
	}

	private double distance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	private void saveAddress(String address) throws IOException {

		RefUnsortedList<String> savedList = new RefUnsortedList<String>();
		Scanner sc1 = new Scanner(afterRead);
		while (sc1.hasNext()) {
			String value = sc1.nextLine();
			// searches.add(value);
			savedList.add(value);
			// )[i] = sc1.nextLine();
		}
		if (savedList.contains(address)) {
			savedList.remove(address);
			savedList.add(address);
		} else {
			savedList.add(address);
		}
		//RefUnsortedList<String> list1=savedList;
		LLNode<String> list1=savedList.list;
		FileOutputStream fos = openFileOutput("saved", Context.MODE_APPEND);
		while(list1!=null){
			fos.write(list1.getInfo().getBytes());
			fos.write("\n".getBytes());
			list1=list1.getLink();
		}
		
		fos.close();
	}
	
	private void loadNow(String address){
		
		
	}
}
