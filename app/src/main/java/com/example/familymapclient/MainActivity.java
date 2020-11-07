package com.example.familymapclient;

import android.os.Bundle;

import com.example.familymapclient.auth.Auth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.PersistableBundle;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
	
	public Auth auth = Auth.shared();
	@Nullable Integer authStateHandler = null;
	
	MenuItem logOutItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
	public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		// TODO: Save auth state... ?
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (authStateHandler != null) {
			auth.removeAuthStateDidChangeHandler(authStateHandler);
			authStateHandler = null;
		}
	}
	
	private void setupAuthListeners() {
		authStateHandler = auth.addAuthStateDidChangeHandler(user -> {
			// Only show the Log Out item if the user is signed in
			this.logOutItem.setVisible(user != null);
		});
	}
}
