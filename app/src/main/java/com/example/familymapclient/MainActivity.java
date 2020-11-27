package com.example.familymapclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.data.KeyValueStore;
import com.example.familymapclient.data.PersistentStore;
import com.example.familymapclient.data.UISettings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	public static final String KEY_SERVER_HOST_NAME = "KEY_SERVER_HOST_NAME";
	public static final String KEY_SERVER_PORT = "KEY_SERVER_PORT";
	public static final String KEY_SERVER_USES_HTTPS = "KEY_SERVER_USES_HTTPS";
	
	private final Auth auth = Auth.shared();
	@Nullable Integer authStateHandler = null;
	private UISettings uiSettings;
	private PersistentStore keyValueStore;
	
	private MenuItem searchItem;
	private MenuItem settingsItem;
	
	
	// ** Lifecycle Events
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupSharedPreferences();
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
		
		searchItem = menu.findItem(R.id.action_search);
		settingsItem = menu.findItem(R.id.action_settings);
		
		searchItem.setVisible(auth.isSignedIn());
		settingsItem.setVisible(auth.isSignedIn());
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			startSettingsActivity();
			return true;
			
		} else if (id == R.id.action_search) {
			// TODO: Start search activity
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
	
	
	// ** Persistence
	
	private void setupSharedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		uiSettings = new UISettings();
		readPreferenceValuesIntoUISettings(sharedPreferences);
	}
	
	@Override
	public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, String key) {
		readPreferenceValuesIntoUISettings(sharedPreferences);
		// TODO: Inform fragments (?)
	}
	
	private void readPreferenceValuesIntoUISettings(@NonNull SharedPreferences sharedPreferences) {
		uiSettings.setLineTypesEnabled(this, sharedPreferences);
		uiSettings.setFiltersEnabled(this, sharedPreferences);
	}
	
	private void saveModelState(@NonNull PersistentStore outState) {
		// Save server location
		outState.putString(KEY_SERVER_HOST_NAME, auth.getHostname());
		outState.putInt(KEY_SERVER_PORT, auth.getPortNumber());
		outState.putBoolean(KEY_SERVER_USES_HTTPS, auth.usesSecureProtocol());
	}
	
	private void restoreModelState(@NonNull PersistentStore savedInstanceState) {
		// Restore server location
		auth.setUsesSecureProtocol(savedInstanceState.getBoolean(KEY_SERVER_USES_HTTPS, false));
		auth.setHostname(savedInstanceState.getString(KEY_SERVER_HOST_NAME));
		auth.setPortNumber(savedInstanceState.getInt(KEY_SERVER_HOST_NAME));
	}
	
	
	// ** Authentication
	
	private void setupAuthListeners() {
		authStateHandler = auth.addAuthStateDidChangeHandler(authToken -> {
			// Only show menu items if the user is signed in
			this.searchItem.setVisible(authToken != null);
			this.settingsItem.setVisible(authToken != null);
			this.saveModelState(this.keyValueStore);
		});
	}
	
	
	// ** Navigation
	
	private void startSettingsActivity() {
		Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(settings);
	}
}
