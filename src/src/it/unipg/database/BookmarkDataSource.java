package it.unipg.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BookmarkDataSource {
	// Database fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID,
			DatabaseHelper.COLUMN_URL };

	public BookmarkDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean insertBookmark(String url) {
		boolean result = false;

		// Prepare a new record value
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_URL, url);

		// Insert the new record into the database
		if (isStored(url) == null) {
			database.insert(DatabaseHelper.TABLE_NAME, null, values);
			result = true;
		}
		return result;
	}

	public Bookmark isStored(String url) {
		Bookmark bookmark = null;
		Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, allColumns,
				DatabaseHelper.COLUMN_URL + " = '" + url + "'", null, null,
				null, null);
		if (cursor.moveToFirst()) {
			bookmark = cursorToBookmark(cursor);
		}
		cursor.close();
		return bookmark;
	}

	public Bookmark isStored(long id) {
		Bookmark bookmark = null;
		Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, allColumns,
				DatabaseHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		bookmark = cursorToBookmark(cursor);
		cursor.close();
		return bookmark;
	}

	public void deleteBookmark(Bookmark bookmark) {
		long id = bookmark.getId();
		database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Bookmark> getAllBookmarks() {
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();

		// Create a list of all bookmarks loaded in query result
		while (!cursor.isAfterLast()) {
			Bookmark comment = cursorToBookmark(cursor);
			bookmarks.add(comment);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return bookmarks;
	}

	private Bookmark cursorToBookmark(Cursor cursor) {
		// Use the query result pointer to get the bookmark
		Bookmark bookmark = new Bookmark();
		bookmark.setId(cursor.getLong(0));
		bookmark.setUrl(cursor.getString(1));
		return bookmark;
	}
}
