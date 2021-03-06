package com.example.maps;

//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pushback.coal.R;

public class StandbyScreen extends FragmentActivity implements ActionBar.TabListener, OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
  //  MapView map;
	//private MapController mc;
    public static final String PREFS_NAME = "MyPrefsFile";
    static String address;
    float enactedValue;
    boolean vibrate;
	boolean sound;
	Button stopbn;
    //static TextView destText;
    
	public static LocationManager lm;
	public static LocationListener locationListener;
	static double destLat = 0;
	static double destLong = 0;
	double currentLat=0;
	double currentLong=0;
	static GoogleMap map;
	
	
	//seems to be working
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standbyscreen);
        
      //  map = (MapView) findViewById(R.id.mvMain);
		//map.setBuiltInZoomControls(true);
		//mc = map.getController();
		
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.		
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        //actionBar.hide();
        getSettings();
        doNotification();
        doLocationStuff();
        
    }

    public void doLocationStuff(){
    	try {

			Geocoder coder = new Geocoder(this);
			List<Address> listaddress;
			// test
			listaddress = coder.getFromLocationName(address, 5);
			Address location = listaddress.get(0);

			destLat = location.getLatitude();
			destLong = location.getLongitude();
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
	}
    
    @SuppressLint("NewApi")
	public void doNotification(){
    	
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_action_search)
    	        .setContentTitle("Commuter Alarm")
    	        .setContentText("Navigating to  "+ address);
    	// Creates an explicit intent for an Activity in your app
    	Intent resultIntent = new Intent(this, StandbyScreen.class);

    	// The stack builder object will contain an artificial back stack for the
    	// started Activity.
    	// This ensures that navigating backward from the Activity leads out of
    	// your application to the Home screen.
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	// Adds the back stack for the Intent (but not the Intent itself)
    	stackBuilder.addParentStack(StandbyScreen.class);
    	// Adds the Intent that starts the Activity to the top of the stack
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_UPDATE_CURRENT
    	        );
    	mBuilder.setContentIntent(resultPendingIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	// mId allows you to update the notification later on.
    	int mId=1;
    	mNotificationManager.notify(mId, mBuilder.build());
    	
    	
    	
    }
    //retrieves data from the sharedprefs
    public void getSettings(){
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	address = settings.getString("address", "error");
    	enactedValue = settings.getFloat("enactedValue", 1);
        vibrate = settings.getBoolean("vibrate", true);
        sound = settings.getBoolean("sound", false);
        System.out.println(address);
        System.out.println(enactedValue);
        System.out.println(vibrate);
        System.out.println(sound);
    }
    
    public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			if (loc != null && destLat != 0 && destLong != 0) {
				
				 Toast.makeText(
				 getBaseContext(),
			 "Location changed : Lat: " + loc.getLatitude()
				 + " Lng: " + loc.getLongitude(),
				 Toast.LENGTH_SHORT).show();
				 System.out.println("MyLocationListener Working");
				// GeoPoint p = new GeoPoint((int) (loc.getLatitude() * 1E6),
				// (int) (loc.getLongitude() * 1E6));
				// mc.animateTo(p);
				// mc.setZoom(16);
				// map.invalidate();
				currentLat = loc.getLatitude();
				currentLong = loc.getLongitude();
				
				//Log.v("Current Lat", String.valueOf(currentLat));
				//Log.v("Current Long", String.valueOf(currentLong));
				double dist = distance(destLat, destLong, currentLat,
						currentLong);
				
				//latlong.setText("Distance to destination: "
				//		+ String.valueOf(dist));
				LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
			    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
			    
			    map.animateCamera(cameraUpdate);
			    
			    //checks distance away from destination is less than enacted value and take me to endpoint.java
				if (dist <= enactedValue) {
					lm.removeUpdates(locationListener);
					map.clear();
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
    public double distance(double lat1, double lng1, double lat2, double lng2) {
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
    
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	if(position==0){
        		return "Alarm Progress";
        	}else{
        		return "Map";
        	}
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {
    	TextView destText1;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
            destText1=(TextView) rootView.findViewById(R.id.tvaddress);
            destText1.setText(address);
            //rootView.findViewById(R.id.tvaddress).setText("Hello");
            
            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.bnStop)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        	lm.removeUpdates(locationListener);
        					map.clear();
        					Intent goHome = new Intent("com.HOME");
        					startActivity(goHome);
                           // Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
                          //  startActivity(intent);
                        	//lm.removeUpdates(locationListener); // should stop gps updates of //
							// locationListener
                        	//finish();
                        }
                    });

            // Demonstration of navigating to external activities.
            rootView.findViewById(R.id.demo_external_activity)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an intent that asks the user to pick a photo, but using
                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                            // the application from the device home screen does not return
                            // to the external activity.
                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                            externalActivityIntent.setType("image/*");
                            externalActivityIntent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(externalActivityIntent);
                        }
                    });

            return rootView;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    //Map is displayed on this fragment
    public static class DummySectionFragment extends Fragment {
    	MapView mapView;
    	
        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
         // Gets the MapView from the XML layout and creates it
            mapView = (MapView) rootView.findViewById(R.id.mapview);
            
            mapView.onCreate(savedInstanceState);
            mapView.onResume(); //without this, map showed but was empty 

            
            // Gets to GoogleMap from the MapView and does initialization stuff
            map = mapView.getMap(); 
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            map.setMapType(1);
            map.addMarker(new MarkerOptions()
            .title(address)
            .snippet("Destination")
            .position(new LatLng(destLat, destLong)));
            //map.setPadding(-200, 0, 0, 0);
            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
          //  try {
                MapsInitializer.initialize(this.getActivity());
            //} catch (GooglePlayServicesNotAvailableException e) {
            //    e.printStackTrace();
          //  }
            //Bundle args = getArguments();
            //((TextView) rootView.findViewById(android.R.id.text1)).setText(
            //        getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
            
        }
        @Override
        public void onResume() {
        	super.onResume();
        	mapView.onResume();
        }

        @Override
        public void onPause() {
        	super.onPause();
        	mapView.onPause();
        }

        @Override
        public void onDestroy() {
        	super.onDestroy();
        	mapView.onDestroy();     
        }
      
        @Override
        public void onLowMemory() {
        	super.onLowMemory();    
        	mapView.onLowMemory();      
        }
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//when you press stop button, it removes locationlistener and heads back to home screen
		lm.removeUpdates(locationListener);
		map.clear();
		//finish();  
		Intent goBackHome = new Intent("com.HOME");
		goBackHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		//goBackHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(goBackHome);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		//makes it so you can't hit back button
	}
    
}
