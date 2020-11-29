package com.example.familymapclient;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import model.Person;

public class PersonActivity extends AppCompatActivity {
	
	public static final String ARG_PERSON_ID = "person_id";
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	private @Nullable Person selectedPerson = null;
	
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
	
}