package com.ug.telescopio.data;

import com.ug.telescopio.data.models.Place;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	 private DBHelper dbHelper;
	 private static final String DATABASE_NAME = "places.db";
	 private static final int DATABASE_VERSION = 1;
	  
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
     
     public ContentValues buildContentValuesFromPlace (Place p) {
    	 ContentValues values = new ContentValues();
    	 values.put(DBHelper.KEY_ID, p.getId());
    	 values.put(DBHelper.KEY_DATE, p.getDate());
    	 values.put(DBHelper.KEY_TIME, p.getTime());
    	 values.put(DBHelper.KEY_AUTHOR, p.getAuthor());
    	 values.put(DBHelper.KEY_THUMBNAILURL, p.getThumbnailURL());
    	 return values;
     }
}
