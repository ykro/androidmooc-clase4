package com.ug.telescopio;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ug.telescopio.data.Place;

public class App extends Application {
	private ArrayList<Place> places;
	private RequestQueue requestQueue;
	private LruCache<Place, Bitmap> thumbnails;
	private static final int CACHE_SIZE_BYTES = 4 * 1024 * 1024;
	
	@Override
	public void onCreate() {
		super.onCreate();
		places = new ArrayList<Place>();
        setThumbnails(new LruCache<Place, Bitmap>(CACHE_SIZE_BYTES) {
            @Override
            protected int sizeOf(Place key, Bitmap value) {
                return value.getByteCount();

            }});     		
		
		requestQueue = Volley.newRequestQueue(this);
		requestQueue.start();
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}

	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public void setRequestQueue(RequestQueue requestQueue) {
		this.requestQueue = requestQueue;
	}

	public LruCache<Place, Bitmap> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(LruCache<Place, Bitmap> thumbnails) {
		this.thumbnails = thumbnails;
	}
	
	
}
