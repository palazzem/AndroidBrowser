package it.unipg.browser;

import java.util.List;

import it.unipg.database.Bookmark;
import it.unipg.database.BookmarkDataSource;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BookmarkActivity extends ListActivity {
	// Logging tag
	private final String TAG = "BookmarkActivity";
	private BookmarkDataSource bookmarkDataSource = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks);
		bookmarkDataSource = new BookmarkDataSource(getApplicationContext());
		bookmarkDataSource.open();

		List<Bookmark> bookmarks = bookmarkDataSource.getAllBookmarks();

		// Create an adapter to fill ListView
		ArrayAdapter<Bookmark> adapter = new ArrayAdapter<Bookmark>(
				getApplicationContext(), android.R.layout.simple_list_item_1,
				bookmarks);
		setListAdapter(adapter);

		// Register the context menu
		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Get the selected bookmark url
		String url = ((Bookmark) getListView().getItemAtPosition(position))
				.getUrl();

		// Open browser activity with url as parameter
		Intent mainIntent = new Intent(BookmarkActivity.this,
				MainActivity.class);
		mainIntent.putExtra("url", url);
		startActivity(mainIntent);
		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onResume() {
		bookmarkDataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		bookmarkDataSource.close();
		super.onPause();
	}

	// Allow the creation of the context menu when called
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(getString(R.string.bookmark_menu_title));
		menu.add(0, 0, 0, R.string.delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;

		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			return false;
		}
		long id = getListAdapter().getItemId(info.position);
		Log.d(TAG, "idemId=" + id);

		@SuppressWarnings("unchecked")
		ArrayAdapter<Bookmark> adapter = (ArrayAdapter<Bookmark>) getListAdapter();

		Bookmark selectedBookmark = (Bookmark) getListAdapter().getItem(
				(int) id);
		bookmarkDataSource.deleteBookmark(selectedBookmark);
		adapter.remove(selectedBookmark);

		return true;
	}
}
