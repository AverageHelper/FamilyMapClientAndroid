package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import model.Event;

/**
 * An activity that displays details about an event. Use
 * {@link EventActivity#newIntent(Context, Event)} to initialize
 * the activity with its expected arguments.
 */
public class EventActivity extends UIPreferencesActivity {
	
	public static final String ARG_EVENT_JSON = "event_json";
	
	/**
	 * Creates a new {@link Intent} to start the activity.
	 * @param packageContext The activity that launches the intent.
	 * @param event The {@link Event} record that the activity describes.
	 * @return An {@link Intent} object that callers can use to launch the activity.
	 */
	public static Intent newIntent(Context packageContext, @NonNull Event event) {
		Intent eventDetails = new Intent(packageContext, EventActivity.class);
		eventDetails.putExtra(EventActivity.ARG_EVENT_JSON, event.toJson());
		return eventDetails;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_event);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}