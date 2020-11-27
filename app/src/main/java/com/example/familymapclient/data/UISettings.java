package com.example.familymapclient.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.familymapclient.R;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;

public class UISettings {
	private final @NonNull Set<LineType> enabledLineTypes;
	private final @NonNull Set<FilterType> enabledFilterTypes;
	
	public UISettings() {
		this.enabledLineTypes = new HashSet<>();
		this.enabledFilterTypes = new HashSet<>();
	}
	
	
	// ** Lines
	
	public boolean isLineTypeEnabled(@NonNull LineType lineType) {
		return enabledLineTypes.contains(lineType);
	}
	
	public void setLineTypeEnabled(@NonNull LineType lineType, boolean isEnabled) {
		if (isEnabled) {
			enabledLineTypes.add(lineType);
		} else {
			enabledLineTypes.remove(lineType);
		}
	}
	
	/**
	 * Reads line settings from the provided <code>SharedPreferences</code>, using key constants
	 * defined by the given <code>Context</code>.
	 */
	public void setLineTypesEnabled(@NonNull Context context, @NonNull SharedPreferences preferences) {
		setLineTypeEnabled(
			LineType.LIFE_STORY,
			preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_lines_life_story),
				true
			)
		);
		setLineTypeEnabled(
			LineType.FAMILY_TREE,
			preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_lines_family_tree),
				true
			)
		);
		setLineTypeEnabled(
			LineType.SPOUSE,
			preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_lines_spouse),
				false
			)
		);
	}
	
	
	// ** Filters
	
	public boolean isFilterEnabled(@NonNull FilterType filterType) {
		return enabledFilterTypes.contains(filterType);
	}
	
	public void setFilterEnabled(@NonNull FilterType filterType, boolean isEnabled) {
		if (isEnabled) {
			enabledFilterTypes.add(filterType);
		} else {
			enabledFilterTypes.remove(filterType);
		}
	}
	
	/**
	 * Reads filter settings from the provided <code>SharedPreferences</code>, using key constants
	 * defined by the given <code>Context</code>.
	 */
	public void setFiltersEnabled(@NonNull Context context, @NonNull SharedPreferences preferences) {
		setFilterEnabled(
			FilterType.SIDE_FATHER, preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_filters_side_father),
				true
			)
		);
		setFilterEnabled(
			FilterType.SIDE_MOTHER, preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_filters_side_mother),
				true
			)
		);
		setFilterEnabled(
			FilterType.GENDER_MALE, preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_filters_gender_male),
				true
			)
		);
		setFilterEnabled(
			FilterType.GENDER_FEMALE, preferences.getBoolean(
				context.getString(R.string.preference_key_enabled_filters_gender_female),
				true
			)
		);
	}
	
}
