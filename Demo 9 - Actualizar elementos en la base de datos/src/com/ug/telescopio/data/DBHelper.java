package com.ug.telescopio.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public final static String KEY_ID = "id";
	public final static String KEY_DATE = "date";
	public final static String KEY_TIME = "time";
	public final static String KEY_AUTHOR = "author";
	public final static String KEY_LATITUDE = "latitude";
	public final static String KEY_LONGITUDE = "longitude";
	public final static String KEY_THUMBNAILURL = "thumbnailURL";
	
	public final static String PLACES_TABLE = "places";
	private final static String DATABASE_CREATE = "CREATE TABLE " + PLACES_TABLE + 
												  "(" + KEY_ID + " integer primary key autoincrement, " +
												  KEY_DATE + " text, " + KEY_TIME + " text," +
												  KEY_AUTHOR + " text, " + KEY_THUMBNAILURL + " text, " +
												  KEY_LATITUDE + " real, " + KEY_LONGITUDE + " real)";

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("TAG","llamo");
		db.execSQL("DROP TABLE IF EXISTS " + PLACES_TABLE);        
        onCreate(db);
	}

}
