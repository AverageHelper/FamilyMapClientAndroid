package com.example.familymapclient.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.familymapclient.R;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A utility class that facilitates access to the shared preferences file.
 */
public class KeyValueStore {
	
	private final @NonNull Context context;
	
	public KeyValueStore(@NonNull Context context) {
		this.context = context;
	}
	
	private static @NonNull SharedPreferences preferencesForContext(@NonNull Context context) {
		return context.getSharedPreferences(
			context.getString(R.string.preference_file_key),
			Context.MODE_PRIVATE
		);
	}
	
	
	
	
	// ** Read
	
	public boolean containsKey(@NonNull String key) {
		return KeyValueStore.containsKey(context, key);
	}
	
	public static boolean containsKey(@NonNull Context context, @NonNull String key) {
		SharedPreferences sharedPref = preferencesForContext(context);
		return sharedPref.contains(key);
	}
	
	
	public @Nullable String getString(@NonNull String key) {
		return KeyValueStore.getString(context, key);
	}
	
	public static @Nullable String getString(
		@NonNull Context context,
		@NonNull String key
	) {
		SharedPreferences sharedPref = preferencesForContext(context);
		try {
			return sharedPref.getString(key, null);
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	
	public @Nullable Integer getInt(@NonNull String key) {
		return KeyValueStore.getInt(context, key);
	}
	
	public static @Nullable Integer getInt(
		@NonNull Context context,
		@NonNull String key
	) {
		SharedPreferences sharedPref = preferencesForContext(context);
		if (sharedPref.contains(key)) {
			try {
				return sharedPref.getInt(key, -1);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}
	
	
	public boolean getBoolean(@NonNull String key, boolean defaultValue) {
		Boolean result = KeyValueStore.getBoolean(context, key);
		if (result == null) {
			result = defaultValue;
		}
		return result;
	}
	
	public @Nullable Boolean getBoolean(@NonNull String key) {
		return KeyValueStore.getBoolean(context, key);
	}
	
	public static @Nullable Boolean getBoolean(
		@NonNull Context context,
		@NonNull String key
	) {
		SharedPreferences sharedPref = preferencesForContext(context);
		if (sharedPref.contains(key)) {
			try {
				return sharedPref.getBoolean(key, false);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}
	
	
	public @Nullable Float getFloat(@NonNull String key) {
		return KeyValueStore.getFloat(context, key);
	}
	
	public static @Nullable Float getFloat(
		@NonNull Context context,
		@NonNull String key
	) {
		SharedPreferences sharedPref = preferencesForContext(context);
		if (sharedPref.contains(key)) {
			try {
				return sharedPref.getFloat(key, 0);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}
	
	
	public @Nullable Long getLong(@NonNull String key) {
		return KeyValueStore.getLong(context, key);
	}
	
	public static @Nullable Long getLong(
		@NonNull Context context,
		@NonNull String key
	) {
		SharedPreferences sharedPref = preferencesForContext(context);
		if (sharedPref.contains(key)) {
			try {
				return sharedPref.getLong(key, 0);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}
	
	
	public @Nullable Set<String> getStringSet(@NonNull String key) {
		return KeyValueStore.getStringSet(context, key);
	}
	
	public static @Nullable Set<String> getStringSet(
		@NonNull Context context,
		@NonNull String key
	) {
		SharedPreferences sharedPref = preferencesForContext(context);
		if (sharedPref.contains(key)) {
			try {
				return sharedPref.getStringSet(key, null);
			} catch (ClassCastException e) {
				return null;
			}
		}
		return null;
	}
	
	
	
	
	
	// ** Write
	
	public void remove(@NonNull String key) {
		KeyValueStore.remove(context, key);
	}
	
	public static void remove(@NonNull Context context, @NonNull String key) {
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.apply();
	}
	
	
	public void putString(@NonNull String key, @Nullable String value) {
		KeyValueStore.putString(context, key, value);
	}
	
	public static void putString(
		@NonNull Context context,
		@NonNull String key,
		@Nullable String value
	) {
		if (value == null) {
			remove(context, key);
		}
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.apply();
	}
	
	
	public void putInt(@NonNull String key, @Nullable Integer value) {
		KeyValueStore.putInt(context, key, value);
	}
	
	public static void putInt(
		@NonNull Context context,
		@NonNull String key,
		@Nullable Integer value
	) {
		if (value == null) {
			remove(context, key);
			return;
		}
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(key, value);
		editor.apply();
	}
	
	
	public void putBoolean(@NonNull String key, @Nullable Boolean value) {
		KeyValueStore.putBoolean(context, key, value);
	}
	
	public static void putBoolean(
		@NonNull Context context,
		@NonNull String key,
		@Nullable Boolean value
	) {
		if (value == null) {
			remove(context, key);
			return;
		}
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
	
	
	public void putFloat(@NonNull String key, @Nullable Float value) {
		KeyValueStore.putFloat(context, key, value);
	}
	
	public static void putFloat(
		@NonNull Context context,
		@NonNull String key,
		@Nullable Float value
	) {
		if (value == null) {
			remove(context, key);
			return;
		}
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(key, value);
		editor.apply();
	}
	
	
	public void putLong(@NonNull String key, @Nullable Long value) {
		KeyValueStore.putLong(context, key, value);
	}
	
	public static void putLong(
		@NonNull Context context,
		@NonNull String key,
		@Nullable Long value
	) {
		if (value == null) {
			remove(context, key);
			return;
		}
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(key, value);
		editor.apply();
	}
	
	
	public void putStringSet(@NonNull String key, @Nullable Set<String> value) {
		KeyValueStore.putStringSet(context, key, value);
	}
	
	public static void putStringSet(
		@NonNull Context context,
		@NonNull String key,
		@Nullable Set<String> value
	) {
		if (value == null) {
			remove(context, key);
			return;
		}
		SharedPreferences sharedPref = preferencesForContext(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putStringSet(key, value);
		editor.apply();
	}
}
