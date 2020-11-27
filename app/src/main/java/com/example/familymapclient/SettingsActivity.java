package com.example.familymapclient;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
	
	public static final String RESPONSE_KEY = "response";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if (savedInstanceState == null) {
			getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.settings, new SettingsFragment())
				.commit();
		}
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class SettingsFragment extends PreferenceFragmentCompat {
		
		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.root_preferences, rootKey);
		}
		
		@Override
		public boolean onPreferenceTreeClick(Preference preference) {
			if (preference.getKey().equals(getString(R.string.preference_action_log_out))) {
				if (getActivity() != null) {
					getActivity().getIntent().putExtra(RESPONSE_KEY, SettingsActivityResponse.SHOULD_LOG_OUT);
					getActivity().finish();
				}
				return true;
			}
			
			return super.onPreferenceTreeClick(preference);
		}
	}
}