package com.example.familymapclient;

import android.os.Bundle;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.data.KeyValueStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
	
	public static final String KEY_SERVER_HOST_NAME = "KEY_SERVER_HOST_NAME";
	public static final String KEY_SERVER_PORT = "KEY_SERVER_PORT";
	public static final String KEY_SERVER_USES_HTTPS = "KEY_SERVER_USES_HTTPS";
	
	public Auth auth = Auth.shared();
	@Nullable Integer authStateHandler = null;
	
	MenuItem logOutItem;
	KeyValueStore keyValueStore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		keyValueStore = new KeyValueStore(this);
		restoreModelState(keyValueStore);
		auth.setPersistentStore(keyValueStore);
		
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		FloatingActionButton fab = findViewById(R.id.fab);
		fab
			.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
			.setAction("Action", null).show());
		
		setupAuthListeners();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		logOutItem = menu.findItem(R.id.action_log_out);
		logOutItem.setVisible(auth.isSignedIn());
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_log_out) {
			auth.logOut();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (authStateHandler != null) {
			auth.removeAuthStateDidChangeHandler(authStateHandler);
			authStateHandler = null;
		}
	}
	
	
	
	private void saveModelState(@NonNull KeyValueStore outState) {
		// Save server location
		outState.putString(KEY_SERVER_HOST_NAME, auth.getHostname());
		outState.putInt(KEY_SERVER_PORT, auth.getPortNumber());
		outState.putBoolean(KEY_SERVER_USES_HTTPS, auth.usesSecureProtocol());
		
		// TODO: Save auth token... ?
	}
	
	private void restoreModelState(@NonNull KeyValueStore savedInstanceState) {
		// Restore server location
		auth.setUsesSecureProtocol(savedInstanceState.getBoolean(KEY_SERVER_USES_HTTPS, false));
		auth.setHostname(savedInstanceState.getString(KEY_SERVER_HOST_NAME));
		auth.setPortNumber(savedInstanceState.getInt(KEY_SERVER_HOST_NAME));
	}
	
	private void setupAuthListeners() {
		authStateHandler = auth.addAuthStateDidChangeHandler(authToken -> {
			// Only show the Log Out item if the user is signed in
			this.logOutItem.setVisible(authToken != null);
			this.saveModelState(this.keyValueStore);
		});
	}
}
