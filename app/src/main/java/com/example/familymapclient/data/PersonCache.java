package com.example.familymapclient.data;

import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.data.fetch.PersonRequester;
import com.example.familymapclient.data.fetch.PersonsRequester;
import com.example.familymapclient.transport.ServerLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;
import model.Person;

public class PersonCache extends IDMap<String, Person> {
	private final Map<String, Set<Event>> personEvents;
	
	private static @Nullable PersonCache instance = null;
	public static @NonNull PersonCache shared() {
		if (instance == null) {
			instance = new PersonCache();
		}
		return instance;
	}
	
	private PersonCache() {
		super();
		this.personEvents = new HashMap<>();
	}
	
	
	/**
	 * Gets the list of known events associated with the given <code>person</code>.
	 *
	 * @param person The person whose events need to be listed.
	 * @return A list of <code>Event</code> entries, or <code>null</code> if the given person is not known.
	 */
	public @Nullable Set<Event> eventsForPerson(@NonNull Person person) {
		return eventsForPerson(person.getId());
	}
	
	public @Nullable Set<Event> eventsForPerson(@NonNull String personID) {
		return personEvents.get(personID);
	}
	
	@Override
	public void add(@NonNull Person person) {
		super.add(person);
		
		// Prepare the events map if it isn't prepared already
		if (eventsForPerson(person) == null) {
			personEvents.put(person.getId(), new HashSet<>());
		}
	}
	
	/**
	 * Clears the cache.
	 */
	public void clear() {
		personEvents.clear();
		super.clear();
	}
	
	@Override
	public @Nullable Person removeValueWithID(@NonNull String id) {
		personEvents.remove(id);
		return super.removeValueWithID(id);
	}
	
	
	public @NonNull Set<Relationship> relationshipsForPerson(@NonNull Person subject) {
		Set<Relationship> relationships = new HashSet<>();
		
		if (subject.getFatherID() != null) {
			Person father = getValueWithID(subject.getFatherID());
			relationships.add(new Relationship(subject, father, RelationshipType.FATHER));
		}
		if (subject.getMotherID() != null) {
			Person mother = getValueWithID(subject.getMotherID());
			relationships.add(new Relationship(subject, mother, RelationshipType.MOTHER));
		}
		if (subject.getSpouseID() != null) {
			Person spouse = getValueWithID(subject.getSpouseID());
			relationships.add(new Relationship(subject, spouse, RelationshipType.SPOUSE));
		}
		for (Person child : values()) {
			if (
				(child.getFatherID() != null && child.getFatherID().equals(subject.getId())) ||
				(child.getMotherID() != null && child.getMotherID().equals(subject.getId()))
			) {
				relationships.add(new Relationship(subject, child, RelationshipType.CHILD));
			}
		}
		
		return relationships;
	}
	
	
	// ** Fetching Persons
	
	public @NonNull PersonsRequester fetchAllPersons(
		@NonNull ServerLocation location,
		@NonNull String authToken,
		@NonNull NonNullValueHandler<List<Person>> onSuccess,
		@NonNull NonNullValueHandler<Throwable> onFailure
	) {
		PersonsRequester requester = new PersonsRequester(
			location,
			authToken,
			persons -> {
				// Add the new persons to the cache
				this.addAll(persons);
				onSuccess.call(persons);
			},
			onFailure
		);
		requester.start();
		return requester;
	}
	
	public @NonNull PersonRequester fetchPersonWithID(
		@NonNull ServerLocation location,
		@NonNull String personID,
		@NonNull String authToken,
		@NonNull NonNullValueHandler<Person> onSuccess,
		@NonNull NonNullValueHandler<Throwable> onFailure
	) {
		PersonRequester responder =
			new PersonRequester(
				location,
				personID,
				authToken,
				person -> {
					// Add the new Person to the cache
					this.add(person);
					onSuccess.call(person);
				},
				onFailure
			);
		responder.start();
		return responder;
	}
	
}
