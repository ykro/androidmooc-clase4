package com.ug.telescopio.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ug.telescopio.R;

public class PlacesFragment extends SupportMapFragment implements OnMapLongClickListener {
	private GoogleMap map;
	private Bundle savedInstanceState;
	private HashMap<String, Marker> markers = new HashMap<String, Marker>();
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
                    map.setMyLocationEnabled(true);
                    map.setOnMapLongClickListener(this);
                }

                map.getUiSettings().setZoomControlsEnabled(false);
            }
        }    	
    }

	@Override
	public void onMapLongClick(LatLng location) {
		String date = new SimpleDateFormat("dd/MM/yyyy", 
			       Locale.getDefault())
							.format(Calendar.getInstance().getTime());

		String time = new SimpleDateFormat("HH:mm", 
			       Locale.getDefault())
							.format(Calendar.getInstance().getTime());
		
		String title = getActivity().getString(R.string.txt_marker_title,date);
		String snippet = getActivity().getString(R.string.txt_marker_snippet,time);
		
        MarkerOptions options = new MarkerOptions()
							        .position(location)
							        .title(title)
							        .snippet(snippet);

		Marker marker = map.addMarker(options);
		markers.put(marker.getTitle(), marker);
	}
}
