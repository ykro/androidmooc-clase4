package com.ug.telescopio.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.ug.telescopio.Utils;
import com.ug.telescopio.data.DBAdapter;
import com.ug.telescopio.data.models.Place;

public class PlacesFragment extends SupportMapFragment implements OnMapLongClickListener, InfoWindowAdapter {
	private GoogleMap map;
	private DBAdapter db; 
	private ArrayList<Place> places;
	private Bundle savedInstanceState;
	private HashMap<Marker, Place> markerPlacesMap = new HashMap<Marker, Place>();
	
	public static final LatLng GUATEMALA = new LatLng(14.62, -90.56);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        places = ((App)getActivity().getApplicationContext()).getPlaces();
        db = ((App)getActivity().getApplicationContext()).getDB();
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
                    drawMarkers();
                }

                map.getUiSettings().setZoomControlsEnabled(false);
            }
        }    	
    }

    public void drawMarkers() {
    	for (Place p : places) {
    		
    		String title = getActivity().getString(R.string.txt_marker_title,p.getDate());
    		String snippet = getActivity().getString(R.string.txt_marker_snippet,p.getTime());
    		
            MarkerOptions options = new MarkerOptions()
	        .position(p.getLocation())
	        .title(title)
	        .snippet(snippet);

			Marker marker = map.addMarker(options);
			markerPlacesMap.put(marker, p);
			grabThumbnailImage(marker);
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
		Place place = createNewPlace(date, time, location);
		markerPlacesMap.put(marker, place);
		grabFromFlickr(marker, place);
	}
	
	public Place createNewPlace(String date, String time, LatLng location) {		
		Place newPlace = new Place();
		newPlace.setId(places.size() + 1);
		newPlace.setDate(date);
		newPlace.setTime(time);
		newPlace.setLocation(location);
		places.add(newPlace);
		db.insertPlace(newPlace);
		Log.e("TAG",db.getTotalPlacesinDatabase()+"");
		return newPlace;
	}
	
	public void grabFromFlickr(final Marker m, final Place p){		
		String url = Utils.FLICKR_API_URL;
		final RequestQueue queue = ((App)getActivity().getApplicationContext()).getRequestQueue();
	    Response.Listener<JSONObject> successListener = 
	    		new Response.Listener<JSONObject>() {
		            @Override
		            public void onResponse(JSONObject response) {			            	
						try {
			            	JSONArray items = response.getJSONArray("items");			            	
			            	JSONObject media = items.getJSONObject(0).getJSONObject("media");
			            	String url = media.getString("m");
			            	String author = items.getJSONObject(0).getString("author");			            	
			            	p.setAuthor(author);
			            	p.setThumbnailURL(url);
			            	db.updatePlace(p);
			            	markerPlacesMap.put(m, p);
			            	grabThumbnailImage(m);
			            	
						} catch (JSONException e) {
							Log.e("ERROR",Log.getStackTraceString(e));
						}
		            }
	    };
		    
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, 
															   url, 
															   null, 
															   successListener,
															   null);
		
		queue.add(jsObjRequest);

	}

	@Override
	public View getInfoContents(Marker marker) {
		Place currentPlace = markerPlacesMap.get(marker);
        View window = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.info_window, null);
        
        TextView txt_title = (TextView)window.findViewById(R.id.txt_title);
        TextView txt_snippet = (TextView)window.findViewById(R.id.txt_snippet);
        ImageView img_thumbnail = (ImageView)window.findViewById(R.id.img_thumbnail);
        txt_title.setText(marker.getTitle());
        
        String author = currentPlace.getAuthor();
        String snippet = marker.getSnippet();
        if (author != null) {
        	snippet += "\n" + author;
        }
        
        txt_snippet.setText(snippet);
        
        LruCache<Place, Bitmap> thumbnails = ((App)getActivity().getApplicationContext()).getThumbnails();
        Bitmap bitmap = thumbnails.get(currentPlace);
        if (bitmap != null) {
        	img_thumbnail.setImageBitmap(bitmap);
        }
        
        return window;
	}
	
	public void grabThumbnailImage(final Marker marker) {
		final Place currentPlace = markerPlacesMap.get(marker);
		final LruCache<Place, Bitmap> thumbnails = ((App)getActivity().getApplicationContext()).getThumbnails();
		final RequestQueue queue = ((App)getActivity().getApplicationContext()).getRequestQueue();
        if (thumbnails.get(currentPlace) == null) {
        	queue.add(
                    new ImageRequest(currentPlace.getThumbnailURL(), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                        	thumbnails.put(currentPlace, bitmap);
                            if (marker.isInfoWindowShown()) {
                            	marker.showInfoWindow();
                            };
                        }
                    }, 256, 256, Config.ARGB_4444, null));
        }			            	
		
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}
}
