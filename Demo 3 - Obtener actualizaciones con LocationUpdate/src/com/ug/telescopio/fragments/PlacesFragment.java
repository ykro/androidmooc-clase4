package com.ug.telescopio.fragments;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PlacesFragment extends SupportMapFragment {
	private GoogleMap map;
	private Bundle savedInstanceState;
	public static final LatLng GUATEMALA = new LatLng(14.62, -90.56);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        setupMap();
    }	
    
    public void setupMap() {
    	if (map == null) {
    		map = getMap();
            if (map != null) {
                if (savedInstanceState == null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(GUATEMALA, 10));
                }

                map.getUiSettings().setZoomControlsEnabled(false);
            }
        }    	
    }
}
