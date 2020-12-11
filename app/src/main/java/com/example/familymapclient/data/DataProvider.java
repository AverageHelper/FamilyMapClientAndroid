package com.example.familymapclient.data;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Identifiable;

/**
 * An object that can be queried for values.
 *
 * @param <ID> The type that is used to identify each element.
 * @param <Element> The type of element that can be stored.
 */
public interface DataProvider<ID, Element extends Identifiable<ID>> {
	/**
	 * Retrieves an element with the given <code>id</code>, or <code>null</code> if no such element is known.
	 *
	 * @param id The unique identifier of the element to retrieve.
	 * @return A matching element, or <code>null</code> if no known element matches.
	 */
	@Nullable Element getValueWithID(@NonNull ID id);
	
	/**
	 * Returns a {@link Collection} view of the object's values.
	 */
	@NonNull Collection<Element> values();
}
