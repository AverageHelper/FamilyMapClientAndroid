package com.example.familymapclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
	
	public static final String SHOULD_LOG_OUT_KEY =
		"com.example.familymapclient.settings.shouldLogOut";
	
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
			finish();
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
				setShouldLogOutResult();
				if (getActivity() != null) {
					getActivity().finish();
				}
				return true;
			}
			
			return super.onPreferenceTreeClick(preference);
		}
		
		private void setShouldLogOutResult() {
			if (getActivity() != null) {
				Intent data = new Intent();
				data.putExtra(SHOULD_LOG_OUT_KEY, true);
				getActivity().setResult(Activity.RESULT_OK, data);
			}
		}
	}
}