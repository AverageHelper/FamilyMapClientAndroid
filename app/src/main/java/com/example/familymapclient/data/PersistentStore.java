package com.example.familymapclient.data;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An interface for a persistent store proxy.
 */
public interface PersistentStore {
	
	// ** Read
	
	/**
	 * Checks whether the store contains a value for the given <code>key</code>;
	 * @param key The key for which a value may be stored.
	 * @return A boolean value indicating whether the store contains a value for the given <code>key</code>.
	 */
	public boolean containsKey(@NonNull String key);
	
	/**
	 * Retrieves a <code>String</code> value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @return The <code>String</code> value for the given <code>key</code>, or <code>null</code> if
	 * there is no value or the value is not a <code>String</code>.
	 */
	public @Nullable String getString(@NonNull String key);
	
	/**
	 * Retrieves an <code>Integer</code> value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @return The <code>Integer</code> value for the given <code>key</code>, or <code>null</code> if
	 * there is no value of the value is not a <code>Integer</code>.
	 */
	public @Nullable Integer getInt(@NonNull String key);
	
	/**
	 * Retrieves a <code>boolean</code> value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @param defaultValue A value to return if there is no value stored for the given
	 *                     <code>key</code>, or the stored value is not a <code>boolean</code>.
	 * @return The stored <code>boolean</code> value, or the given <code>defaultValue</code> if there
	 * is no stored value or te stored value is not a <code>boolean</code>.
	 */
	public boolean getBoolean(@NonNull String key, boolean defaultValue);
	
	/**
	 * Retrieves a <code>Boolean</code> value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @return The <code>Boolean</code> value for the given <code>key</code>, or <code>null</code> if
	 * there is no value of the value is not a <code>Boolean</code>.
	 */
	public @Nullable Boolean getBoolean(@NonNull String key);
	
	/**
	 * Retrieves a <code>Float</code> value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @return The <code>Float</code> value for the given <code>key</code>, or <code>null</code> if
	 * there is no value of the value is not a <code>Float</code>.
	 */
	public @Nullable Float getFloat(@NonNull String key);
	
	/**
	 * Retrieves a <code>Long</code> value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @return The <code>Long</code> value for the given <code>key</code>, or <code>null</code> if
	 * there is no value of the value is not a <code>Long</code>.
	 */
	public @Nullable Long getLong(@NonNull String key);
	
	/**
	 * Retrieves a string set value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 * @return The string set value for the given <code>key</code>, or <code>null</code> if
	 * there is no value of the value is not a string set.
	 */
	public @Nullable Set<String> getStringSet(@NonNull String key);
	
	
	
	
	
	// ** Write
	
	/**
	 * Removes any stored value for the given <code>key</code>.
	 * @param key The key for which a value may be stored.
	 */
	public void remove(@NonNull String key);
	
	/**
	 * Stores a <code>String</code> value, addressed to the given <code>key</code>.
	 * @param key The key for which to store the new value.
	 * @param value The value to store. If the value is <code>null</code>, then the key is removed
	 *              from the store.
	 */
	public void putString(@NonNull String key, @Nullable String value);
	
	/**
	 * Stores an <code>Integer</code> value, addressed to the given <code>key</code>.
	 * @param key The key for which to store the new value.
	 * @param value The value to store. If the value is <code>null</code>, then the key is removed
	 *              from the store.
	 */
	public void putInt(@NonNull String key, @Nullable Integer value);
	
	/**
	 * Stores a <code>Boolean</code> value, addressed to the given <code>key</code>.
	 * @param key The key for which to store the new value.
	 * @param value The value to store. If the value is <code>null</code>, then the key is removed
	 *              from the store.
	 */
	public void putBoolean(@NonNull String key, @Nullable Boolean value);
	
	/**
	 * Stores a <code>Float</code> value, addressed to the given <code>key</code>.
	 * @param key The key for which to store the new value.
	 * @param value The value to store. If the value is <code>null</code>, then the key is removed
	 *              from the store.
	 */
	public void putFloat(@NonNull String key, @Nullable Float value);
	
	/**
	 * Stores a <code>Long</code> value, addressed to the given <code>key</code>.
	 * @param key The key for which to store the new value.
	 * @param value The value to store. If the value is <code>null</code>, then the key is removed
	 *              from the store.
	 */
	public void putLong(@NonNull String key, @Nullable Long value);
	
	/**
	 * Stores a string set, addressed to the given <code>key</code>.
	 * @param key The key for which to store the new value.
	 * @param value The value to store. If the value is <code>null</code>, then the key is removed
	 *              from the store.
	 */
	public void putStringSet(@NonNull String key, @Nullable Set<String> value);
}
