package it.unipg.browser;

import it.unipg.browser.R;
import it.unipg.database.BookmarkDataSource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private WebView webView;
	private EditText urlBox;
	private BookmarkDataSource bookmarkDataSource = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bookmarkDataSource = new BookmarkDataSource(getApplicationContext());
		bookmarkDataSource.open();

		// Request a window feature to show progress bar in the application
		// title
		requestWindowFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.main);
		setProgressBarVisibility(true);

		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
			// Load opened URL in the application instead of standard browser
			// application
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				MainActivity.this.setProgress(progress * 100);
			}
		});

		// Enable some feature like Javascript and pinch zoom
		WebSettings websettings = webView.getSettings();
		websettings.setJavaScriptEnabled(true);						// Warning! You can have XSS vulnerabilities!
		websettings.setBuiltInZoomControls(true);

		urlBox = (EditText) findViewById(R.id.url);
		urlBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						webView.loadUrl(urlBox.getText().toString());
						InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.hideSoftInputFromWindow(
								urlBox.getWindowToken(), 0);
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create a menu that is append to main activity
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection (every case is a different option ID)
		switch (item.getItemId()) {
		case R.id.bookmark:
			Intent intentBookmark = new Intent(MainActivity.this,
					BookmarkActivity.class);
			startActivity(intentBookmark);
			return true;
		case R.id.addBookmark:
			String urlBookmark = urlBox.getText().toString();
			if (!bookmarkDataSource.insertBookmark(urlBookmark)) {
				Toast.makeText(getApplicationContext(),
						"Bookmark already stored", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Bookmark added", Toast.LENGTH_LONG)
						.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Reload URL
		String bookmarkUrl = getIntent().getStringExtra("url");
		if (bookmarkUrl == null) {
			webView.loadUrl(urlBox.getText().toString());
		} else {
			urlBox.setText(bookmarkUrl);
			webView.loadUrl(bookmarkUrl);
		}
		bookmarkDataSource.open();
	}

	@Override
	protected void onPause() {
		bookmarkDataSource.close();
		super.onPause();
	}

	/*
	 * WebView methods
	 */

	public void go(View v) {
		webView.loadUrl(urlBox.getText().toString());
	}

	public void reload(View v) {
		webView.reload();
	}

	public void stop(View v) {
		webView.stopLoading();
	}

	public void back(View v) {
		webView.goBack();
	}

	public void forward(View v) {
		webView.goForward();
	}
}
