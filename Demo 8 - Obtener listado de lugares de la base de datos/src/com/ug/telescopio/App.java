package com.ug.telescopio;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ug.telescopio.data.DBAdapter;
import com.ug.telescopio.data.models.Place;

public class App extends Application {
	private DBAdapter db;
	private ArrayList<Place> places;
	private RequestQueue requestQueue;
	private LruCache<Place, Bitmap> thumbnails;
	private static final int CACHE_SIZE_BYTES = 4 * 1024 * 1024;
	
	@Override
	public void onCreate() {
		super.onCreate();
		db = new DBAdapter(getApplicationContext());		
		places = db.getPlaces();
		thumbnails = new LruCache<Place, Bitmap>(CACHE_SIZE_BYTES) {
            @Override
            protected int sizeOf(Place key, Bitmap value) {
                return value.getByteCount();

            }};     		
		
		requestQueue = Volley.newRequestQueue(this);
		requestQueue.start();
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public LruCache<Place, Bitmap> getThumbnails() {
		return thumbnails;
	}

	public DBAdapter getDB() {
		return db;
	}	
}
