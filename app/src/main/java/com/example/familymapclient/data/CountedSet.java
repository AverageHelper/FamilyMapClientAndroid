package com.example.familymapclient.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A set that maintains a count of its elements.
 * @param <Element> The type of elements in the set.
 */
public class CountedSet<Element> implements Set<Element> {
	private final Map<Element, Integer> storage;
	
	public CountedSet() {
		storage = new HashMap<>();
	}
	
	@Override
	public int size() {
		return storage.keySet().size();
	}
	
	@Override
	public boolean isEmpty() {
		return storage.keySet().isEmpty();
	}
	
	@Override
	public boolean contains(@Nullable Object o) {
		if (o == null) {
			return false;
		}
		return storage.containsKey(o);
	}
	
	@NonNull
	@Override
	public Iterator<Element> iterator() {
		return storage.keySet().iterator();
	}
	
	@NonNull
	@Override
	public Object[] toArray() {
		return storage.keySet().toArray();
	}
	
	@NonNull
	@Override
	public <T> T[] toArray(@NonNull T[] a) {
		return storage.keySet().toArray(a);
	}
	
	@Override
	public boolean add(Element element) {
		Integer currentCount = storage.get(element);
		
		if (currentCount != null) {
			// Increment the element's count
			storage.put(element, currentCount + 1);
			
		} else {
			// Add the element, and set its count to 1
			storage.put(element, 1);
		}
		
		return true;
	}
	
	/**
	 * Decrements the count associated with the given object.
	 * @param o The object to remove from the set.
	 * @return <code>true</code> if the object was removed, or <code>false</code> if the object was not
	 * known to the set.
	 */
	@Override
	public boolean remove(@Nullable Object o) {
		if (o == null) {
			return false;
		}
		Integer currentCount = storage.get(o);
		
		if (currentCount != null) {
			if ((currentCount - 1) <= 0) {
				// If the count is <= zero, remove the key
				storage.remove(o);
				
			} else {
				// Decrement the count
				storage.put((Element) o, currentCount - 1);
			}
		} else {
			// Nothing to remove
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean containsAll(@NonNull Collection<?> c) {
		return storage.keySet().containsAll(c);
	}
	
	@Override
	public boolean addAll(@NonNull Collection<? extends Element> c) {
		boolean didAddAnElement = false;
		for (Element e : c) {
			didAddAnElement = add(e) || didAddAnElement;
		}
		return didAddAnElement;
	}
	
	@Override
	public boolean retainAll(@NonNull Collection<?> c) {
		return storage.keySet().retainAll(c);
	}
	
	@Override
	public boolean removeAll(@NonNull Collection<?> c) {
		boolean didRemoveAnElement = false;
		for (Object e : c) {
			didRemoveAnElement = remove(e) || didRemoveAnElement;
		}
		return didRemoveAnElement;
	}
	
	@Override
	public void clear() {
		storage.clear();
	}
}
