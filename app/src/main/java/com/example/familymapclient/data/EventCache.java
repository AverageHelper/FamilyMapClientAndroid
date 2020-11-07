package com.example.familymapclient.data;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;

public class EventCache extends IDMap<String, Event> {
	private @NonNull CountedSet<String> eventTypes;
	
	public EventCache() {
		super();
		eventTypes = new CountedSet<>();
	}
	
	/**
	 * @return the set of all known event types.
	 */
	public @NonNull Set<String> getEventTypes() {
		return eventTypes;
	}
	
	@Override
	public void add(@NonNull Event event) {
		super.add(event);
		eventTypes.add(event.getEventType());
	}
	
	@Nullable
	@Override
	public Event removeValueWithID(@NonNull String id) {
		Event event = super.removeValueWithID(id);
		if (event != null) {
			eventTypes.remove(event.getEventType());
		}
		return event;
	}
}
