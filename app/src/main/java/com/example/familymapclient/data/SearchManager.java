package com.example.familymapclient.data;

import com.example.familymapclient.auth.NonNullValueHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;
import model.Gender;
import model.Person;

/**
 * An object that manages a single search operation.
 */
public class SearchManager {
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	private @NonNull String previousQuery = "";
	private @Nullable NonNullValueHandler<List<Object>> callback = null;
	private @Nullable UISettings filter = null;
	
	public void setCallback(@Nullable NonNullValueHandler<List<Object>> callback) {
		this.callback = callback;
		if (callback == null) {
			cancelSearch();
		}
	}
	
	public void setFilter(@Nullable UISettings filter) {
		this.filter = filter;
		runNewSearchWithQuery(previousQuery);
	}
	
	public void cancelSearch() {
		// TODO: Stop any active async operation
		runNewSearchWithQuery("");
	}
	
	/**
	 * Cancels any pending search operation and begins a new one with the provided query.
	 * @param query The search query to use.
	 */
	public void runNewSearchWithQuery(@NonNull String query) {
		previousQuery = query;
		if (callback == null) { return; }
		
		// TODO: Run this in the background
		List<Object> results = new ArrayList<>();
		
		String q = query.toLowerCase().trim();
		
		if (q.isEmpty()) {
			results.addAll(personCache.values());
			results.addAll(eventCache.values());
			results = listApplyingFilters(results);
			callback.call(results);
			return;
		}
		
		for (Person person : listApplyingFilters(personCache.values())) {
			if (person.getFirstName().toLowerCase().contains(q) ||
				person.getLastName().toLowerCase().contains(q)
			) {
				results.add(person);
			}
		}
		
		for (Event event : listApplyingFilters(eventCache.values())) {
			if (event.getCity().toLowerCase().contains(q) ||
				event.getCountry().toLowerCase().contains(q) ||
				event.getEventType().toLowerCase().contains(q)
			) {
				results.add(event);
			}
		}
		
		callback.call(results);
	}
	
	private @NonNull <T> List<T> listApplyingFilters(@NonNull Collection<T> original) {
		List<T> results = new ArrayList<>();
		
		for (T value : original) {
			if (value.getClass().equals(Event.class)) {
				Event event = (Event) value;
				if (eventMatchesUIFilter(event)) {
					results.add(value);
				}
			} else {
				results.add(value);
			}
		}
		
		return results;
	}
	
	private boolean eventMatchesUIFilter(@NonNull Event event) {
		if (filter == null) { return true; }
		
		String personID = event.getPersonID();
		@Nullable Person person = personCache.getValueWithID(personID);
		if (person == null) { return true; }
		
		if (person.getGender().equals(Gender.MALE) && !filter.isFilterEnabled(FilterType.GENDER_MALE)) {
			return false;
		}
		return !person.getGender().equals(Gender.FEMALE) ||
			filter.isFilterEnabled(FilterType.GENDER_FEMALE);
	}
	
}
