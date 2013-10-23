package com.ug.telescopio.activities;

import android.app.Dialog;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.ug.telescopio.R;
import com.ug.telescopio.fragments.ErrorDialogFragment;
import com.ug.telescopio.fragments.PlacesFragment;

public class MainActivity extends FragmentActivity
						  implements OnConnectionFailedListener, ConnectionCallbacks {
	
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
	}

	@Override
	public void onDisconnected() {
	}
}
