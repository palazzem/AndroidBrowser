package it.unipg.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Database general parameters
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "bookmarks.db";
	public static final String TABLE_NAME = "bookmark";

	// Table field
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_URL = "url";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
			+ "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_URL + " TEXT NOT NULL);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Nothing to do
	}
}
