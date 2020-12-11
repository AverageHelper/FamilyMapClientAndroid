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
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;
import model.Person;

public class PersonCache extends IDMap<String, Person> {
	private final Map<String, Set<Event>> personEvents;
	private final Set<Person> mothersSide;
	
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
		this.mothersSide = new HashSet<>();
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
		mothersSide.clear();
		super.clear();
	}
	
	@Override
	public @Nullable Person removeValueWithID(@NonNull String id) {
		personEvents.remove(id);
		for (Person person : mothersSide) {
			if (person.getId().equals(id)) {
				mothersSide.remove(person);
				break;
			}
		}
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
	
	
	
	// ** Inspecting Persons
	
	/**
	 * Recursively checks whether the given person is on the mother's side of the given root.
	 *
	 * Performs a DFS search of the <code>root</code> node's mother side. If <code>query</code> is
	 * found, then this method returns <code>true</code>.
	 */
	private boolean personIsReallyOnMothersSide(@NonNull Person root, @NonNull Person query) {
		if (root.getMotherID() == null) { return false; } // There is no mother
		if (root.getMotherID().equals(query.getMotherID())) { return true; } // This is the mother
		
		Person mother = getValueWithID(root.getMotherID());
		if (mother == null) { return false; } // Mother not downloaded
		
		// DFS the mother's subtree
		Stack<Person> stack = new Stack<>();
		stack.push(mother);
		while (!stack.isEmpty()) {
			Person vertex = stack.pop();
			if (vertex.getId().equals(query.getId())) {
				return true;
			}
			mother = getValueWithID(vertex.getMotherID());
			Person father = getValueWithID(vertex.getFatherID());
			if (mother != null) {
				stack.push(mother);
			}
			if (father != null) {
				stack.push(father);
			}
		}
		
		return false;
	}
	
	/**
	 * Checks whether the given person is on the mother's side of the given <code>root</code> person;
	 * @param root The root of the family tree.
	 * @param query The person whose side status should be determined.
	 * @return whether the person is on the mother's side of the family.
	 */
	public boolean personIsOnMothersSide(@NonNull Person root, @NonNull Person query) {
		if (mothersSide.contains(query)) {
			return true;
		}
		
		boolean result = personIsReallyOnMothersSide(root, query);
		if (result) {
			mothersSide.add(query);
		}
		return result;
	}
	
	/**
	 * Checks whether the given person is on the father's side of the given <code>root</code> person;
	 * @param root The root of the family tree.
	 * @param query The person whose side status should be determined.
	 * @return whether the person is on the mother's side of the family.
	 */
	public boolean personIsOnFathersSide(@NonNull Person root, @NonNull Person query) {
		return !personIsOnMothersSide(root, query);
	}
}
