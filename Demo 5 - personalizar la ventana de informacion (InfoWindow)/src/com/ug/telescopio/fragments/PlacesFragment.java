package com.ug.telescopio.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ug.telescopio.App;
import com.ug.telescopio.R;
import com.ug.telescopio.data.Place;

public class PlacesFragment extends SupportMapFragment implements OnMapLongClickListener, InfoWindowAdapter {
	private GoogleMap map;
	private ArrayList<Place> places;
	private Bundle savedInstanceState;
	private HashMap<Marker, Place> markerPlacesMap = new HashMap<Marker, Place>();
	
	public static final LatLng GUATEMALA = new LatLng(14.62, -90.56);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        places = ((App)getActivity().getApplicationContext()).getPlaces();
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
                    map.setInfoWindowAdapter(this);
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
		Place place = createNewPlace(date, time);
		markerPlacesMap.put(marker, place);
	}
	
	public Place createNewPlace(String date, String time) {		
		Place newPlace = new Place();
		newPlace.setId(places.size() + 1);
		newPlace.setDate(date);
		newPlace.setTime(time);
		places.add(newPlace);
		return newPlace;
	}

	@Override
	public View getInfoContents(Marker marker) {
        View window = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.info_window, null);
        
        TextView txt_title = (TextView)window.findViewById(R.id.txt_title);
        TextView txt_snippet = (TextView)window.findViewById(R.id.txt_snippet);
        txt_title.setText(marker.getTitle());
        txt_snippet.setText(marker.getSnippet());        
        return window;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
}
