package com.example.familymapclient.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Identifiable;

/**
 * A generic cache of objects of a specific identifiable type.
 *
 * @param <ID> The type that is used to identify each element.
 * @param <Element> The type of element that can be stored.
 */
public class IDMap<ID, Element extends Identifiable<ID>> implements DataProvider<ID, Element> {
	private final @NonNull Map<ID, Element> values;
	
	public IDMap() {
		this.values = new HashMap<>();
	}
	
	@Override
	public @Nullable Element getValueWithID(@NonNull ID id) {
		return values.get(id);
	}
	
	/**
	 * Adds the given <code>element</code> to the cache.
	 * @param element The element to store.
	 */
	public void add(@NonNull Element element) {
		values.put(element.getId(), element);
	}
	
	/**
	 * Adds each of the given elements to the cache.
	 * @param collection The collection of elements that each should be added.
	 */
	public void addAll(@NonNull Collection<Element> collection) {
		for (Element e : collection) {
			add(e);
		}
	}
	
	/**
	 * Removes all of the mappings from this map. The map will be empty after this call returns.
	 */
	public void clear() {
		values.clear();
	}
	
	/**
	 * Returns <code>true</code> if this map contains no key-value mappings.
	 * @return <code>true</code> if this map contains no key-value mappings
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	/**
	 * Forgets and returns the element with the given <code>id</code>.
	 *
	 * @param id The unique identifier of the element to remove.
	 * @return the previous value associated with <code>id</code>, or <code>null</code> if there was no
	 * value known for <code>id</code>.
	 */
	public @Nullable Element removeValueWithID(@NonNull ID id) {
		return values.remove(id);
	}
	
	@Override
	public @NonNull Collection<Element> values() {
		return this.values.values();
	}
}
