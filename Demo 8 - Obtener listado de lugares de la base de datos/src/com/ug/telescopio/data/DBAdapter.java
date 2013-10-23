package com.ug.telescopio.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.ug.telescopio.data.models.Place;

public class DBAdapter {
	 private DBHelper dbHelper;
	 private static final String DATABASE_NAME = "places.db";
	 private static final int DATABASE_VERSION = 2;
	  
     public DBAdapter (Context context){
    	 dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
     }
     
     public void insertPlace(Place p){    	 
    	 ContentValues values = buildContentValuesFromPlace(p);
         SQLiteDatabase db = dbHelper.getWritableDatabase();
         try{
        	 db.insertWithOnConflict(DBHelper.PLACES_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);                 
         } finally {
        	 db.close();
         }
     }
     
     public int getTotalPlacesinDatabase() {
    	 SQLiteDatabase db = dbHelper.getReadableDatabase();
    	 Cursor cursor = db.query(DBHelper.PLACES_TABLE, null, null, null, null, null, null);
    	 int total = cursor.getCount();
    	 cursor.close();
    	 return total;
     }
     
     public ArrayList<Place> getPlaces(){
    	 SQLiteDatabase db = dbHelper.getReadableDatabase();
    	 Cursor cursor = db.query(DBHelper.PLACES_TABLE, null, null, null, null, null, null);
    	 ArrayList<Place> places = new ArrayList<Place>();
    	 
    	 while (cursor.moveToNext()) {
    		 Place p = new Place();
    		 p.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
    		 p.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE)));
    		 p.setTime(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME)));
    		 p.setAuthor(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_AUTHOR)));
    		 p.setLocation(new LatLng(
    				 	cursor.getDouble(cursor.getColumnIndex(DBHelper.KEY_LATITUDE)),
    				 	cursor.getDouble(cursor.getColumnIndex(DBHelper.KEY_LONGITUDE))
    				 	));
    		 p.setThumbnailURL(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_THUMBNAILURL)));
    		 places.add(p);
    	 }
    	 cursor.close();
    	 return places;
     }
     
     public ContentValues buildContentValuesFromPlace (Place p) {
    	 ContentValues values = new ContentValues();
    	 values.put(DBHelper.KEY_ID, p.getId());
    	 values.put(DBHelper.KEY_DATE, p.getDate());
    	 values.put(DBHelper.KEY_TIME, p.getTime());
    	 values.put(DBHelper.KEY_AUTHOR, p.getAuthor());
    	 values.put(DBHelper.KEY_LATITUDE, p.getLocation().latitude);
    	 values.put(DBHelper.KEY_LONGITUDE, p.getLocation().longitude);
    	 values.put(DBHelper.KEY_THUMBNAILURL, p.getThumbnailURL());
    	 return values;
     }
}
