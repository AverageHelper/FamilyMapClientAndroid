package com.example.familymapclient.data;

import com.example.familymapclient.auth.NonNullValueHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;
import model.Person;

/**
 * An object that manages a single search operation.
 */
public class SearchManager {
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	private @Nullable NonNullValueHandler<List<Object>> callback = null;
	
	public void setCallback(@Nullable NonNullValueHandler<List<Object>> callback) {
		this.callback = callback;
		if (callback == null) {
			cancelSearch();
		}
	}
	
	public void cancelSearch() {
		// TODO: Stop any active async operation
	}
	
	/**
	 * Cancels any pending search operation and begins a new one with the provided query.
	 * @param query The search query to use.
	 */
	public void runNewSearchWithQuery(@NonNull String query) {
		if (callback == null) { return; }
		
		// TODO: Run this in the background
		List<Object> results = new ArrayList<>();
		
		String q = query.toLowerCase().trim();
		
		if (q.isEmpty()) {
			results.addAll(personCache.values());
			results.addAll(eventCache.values());
			callback.call(results);
			return;
		}
		
		for (Person person : personCache.values()) {
			if (person.getFirstName().toLowerCase().contains(q) ||
				person.getLastName().toLowerCase().contains(q)
			) {
				results.add(person);
			}
		}
		
		for (Event event : eventCache.values()) {
			if (event.getCity().toLowerCase().contains(q) ||
				event.getCountry().toLowerCase().contains(q) ||
				event.getEventType().toLowerCase().contains(q)
			) {
				results.add(event);
			}
		}
		
		callback.call(results);
	}
	
}
