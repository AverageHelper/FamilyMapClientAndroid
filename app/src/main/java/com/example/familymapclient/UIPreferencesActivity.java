package com.example.familymapclient;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.familymapclient.data.UISettings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class UIPreferencesActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	private @Nullable UISettings uiSettings = null;
	
	public @NonNull UISettings getUISettings() {
		if (uiSettings == null) {
			return new UISettings();
		}
		return uiSettings;
	}
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupSharedPreferences();
	}
	
	private void setupSharedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		uiSettings = new UISettings();
		readPreferenceValuesIntoUISettings(sharedPreferences);
	}
	
	
	@Override
	public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, String key) {
		readPreferenceValuesIntoUISettings(sharedPreferences);
	}
	
	private void readPreferenceValuesIntoUISettings(@NonNull SharedPreferences sharedPreferences) {
		uiSettings.setLineTypesEnabled(this, sharedPreferences);
		uiSettings.setFiltersEnabled(this, sharedPreferences);
	}
	
}
