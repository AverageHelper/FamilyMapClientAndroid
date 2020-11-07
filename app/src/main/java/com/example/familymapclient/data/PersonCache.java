package com.example.familymapclient.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;
import model.Person;

public class PersonCache extends IDMap<String, Person> {
	private final Map<String, List<Event>> personEvents;
	
	public PersonCache() {
		super();
		this.personEvents = new HashMap<>();
	}
	
	/**
	 * Gets the list of known events associated with the given <code>person</code>.
	 *
	 * @param person The person whose events need to be listed.
	 * @return A list of <code>Event</code> entries, or <code>null</code> if the given person is not known.
	 */
	public @Nullable List<Event> eventsForPerson(@NonNull Person person) {
		return eventsForPerson(person.getId());
	}
	
	public @Nullable List<Event> eventsForPerson(@NonNull String personID) {
		return personEvents.get(personID);
	}
	
	@Override
	public void add(@NonNull Person person) {
		super.add(person);
		
		// Prepare the events map if it isn't prepared already
		if (eventsForPerson(person) == null) {
			personEvents.put(person.getId(), new ArrayList<>());
		}
	}
	
	@Override
	public @Nullable Person removeValueWithID(@NonNull String id) {
		personEvents.remove(id);
		return super.removeValueWithID(id);
	}
}
