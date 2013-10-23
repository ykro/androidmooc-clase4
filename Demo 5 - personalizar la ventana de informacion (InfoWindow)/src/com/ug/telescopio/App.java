package com.ug.telescopio;

import java.util.ArrayList;

import android.app.Application;

import com.ug.telescopio.data.Place;

public class App extends Application {
	private ArrayList<Place> places;
	
	@Override
	public void onCreate() {
		super.onCreate();
		places = new ArrayList<Place>();
	}

	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}
}
