package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import model.Person;

/**
 * An activity that displays details about a person. Use
 * {@link PersonActivity#newIntent(Context, Person)} to initialize
 * the activity with its expected arguments.
 */
public class PersonActivity extends AppCompatActivity {
	
	public static final String ARG_PERSON_JSON = "person_json";
	public static boolean shouldPopToRoot = false;
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	/**
	 * Creates a new {@link Intent} to start the activity.
	 * @param packageContext The activity that launches the intent.
	 * @param person The {@link Person} record that the activity describes.
	 * @return An {@link Intent} object that callers can use to launch the activity.
	 */
	public static Intent newIntent(Context packageContext, @NonNull Person person) {
		Intent personDetails = new Intent(packageContext, PersonActivity.class);
		personDetails.putExtra(PersonActivity.ARG_PERSON_JSON, person.toJson());
		return personDetails;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
		shouldPopToRoot = false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (shouldPopToRoot) {
			finish();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			shouldPopToRoot = true;
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}