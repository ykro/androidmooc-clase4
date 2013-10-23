package com.ug.telescopio.activities;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.ug.telescopio.App;
import com.ug.telescopio.R;
import com.ug.telescopio.data.DBAdapter;
import com.ug.telescopio.data.models.Place;
import com.ug.telescopio.fragments.ErrorDialogFragment;
import com.ug.telescopio.fragments.PlacesFragment;

public class MainActivity extends FragmentActivity
						  implements OnConnectionFailedListener, ConnectionCallbacks, 
						  			 LocationListener, OnClickListener{
	
	private LocationClient locationClient;
	private LocationRequest locationRequest;
		
	public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * 5;
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * 1;	
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnDelete = (Button)findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		
        locationClient = new LocationClient(this, this, this);
        locationRequest = LocationRequest.create();
        
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        
	}	

    @Override
    protected void onResume() {
        super.onResume();
    	FragmentManager manager = getSupportFragmentManager();
    	PlacesFragment fragment = (PlacesFragment)manager.findFragmentById(R.id.fragmentMap);
    	
        if (servicesConnected()) {
            manager.beginTransaction().show(fragment).commit();
        } else {        	
        	manager.beginTransaction().hide(fragment).commit();
        }      
    }
    
	@Override
    public void onStart() {
        super.onStart();
        locationClient.connect();        
    }	    
    
    @Override
    public void onStop() {
        if (locationClient.isConnected()) {
        	locationClient.removeLocationUpdates(this);
        }

        locationClient.disconnect();
        super.onStop();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private boolean servicesConnected() {
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), "errorDialog");
            }
            return false;
        }
    }

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
            	Log.e("ERROR",Log.getStackTraceString(e));
            }
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            		connectionResult.getErrorCode(),
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

                if (errorDialog != null) {
                    ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                    errorFragment.setDialog(errorDialog);
                    errorFragment.show(getSupportFragmentManager(), "dialog");
                }
            
        }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		updateLocation(locationClient.getLastLocation());
		locationClient.requestLocationUpdates(locationRequest, this);		
	}

	@Override
	public void onDisconnected() {
	}
	
	public void updateLocation(Location location) {
        TextView txtCurrentLocation = (TextView)findViewById(R.id.txtCurrentLocation);	        
        String latLng = "No disponible";
        if (location != null) {
	        latLng = getString(R.string.txt_lat_lon,
				        		location.getLatitude(),
				        		location.getLongitude());
        }
        txtCurrentLocation.setText(latLng);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.e("TAG","Ubicaci—n nueva Lat:" + location.getLatitude() + " Lon:" + location.getLongitude());
		updateLocation(location);
	}

	@Override
	public void onClick(View v) {
		ArrayList<Place> places = ((App)getApplicationContext()).getPlaces();
		places.clear();
		
		DBAdapter db = ((App)getApplicationContext()).getDB();
		db.deleteAllPlaces();
		
    	FragmentManager manager = getSupportFragmentManager();
    	PlacesFragment fragment = (PlacesFragment)manager.findFragmentById(R.id.fragmentMap);
    	fragment.removeAllMarkers();
	}
}
