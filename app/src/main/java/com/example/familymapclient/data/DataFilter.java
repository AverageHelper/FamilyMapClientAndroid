package com.example.familymapclient.data;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import androidx.annotation.NonNull;
import model.Person;

public class DataFilter {
	private final @NonNull DataProvider<String, Person> personProvider;
	private final Set<Person> mothersSide;
	
	public DataFilter(@NonNull DataProvider<String, Person> personProvider) {
		this.personProvider = personProvider;
		this.mothersSide = new HashSet<>();
	}
	
	/**
	 * Recursively checks whether the given person is on the mother's side of the given root.
	 *
	 * Performs a DFS search of the <code>root</code> node's mother side. If <code>query</code> is
	 * found, then this method returns <code>true</code>.
	 */
	private boolean personIsReallyOnMothersSide(@NonNull Person root, @NonNull Person query) {
		if (root.getMotherID() == null) { return false; } // There is no mother
		if (root.getMotherID().equals(query.getMotherID())) { return true; } // This is the mother
		
		Person mother = personProvider.getValueWithID(root.getMotherID());
		if (mother == null) { return false; } // Mother not downloaded
		
		// DFS the mother's subtree
		Stack<Person> stack = new Stack<>();
		stack.push(mother);
		while (!stack.isEmpty()) {
			Person vertex = stack.pop();
			if (vertex.getId().equals(query.getId())) {
				return true;
			}
			mother = personProvider.getValueWithID(vertex.getMotherID());
			Person father = personProvider.getValueWithID(vertex.getFatherID());
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
