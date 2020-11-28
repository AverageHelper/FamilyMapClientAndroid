package com.example.familymapclient.data;

import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.data.fetch.EventsRequester;
import com.example.familymapclient.transport.ServerLocation;
import com.example.familymapclient.utilities.ArrayHelpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;

public class EventCache extends IDMap<String, Event> {
	private final @NonNull CountedSet<String> eventTypes;
	private final @NonNull Map<String, Color> colorsForEvents;
	private final @NonNull Set<Color> unusedColors;
	private final @NonNull Map<Integer, NonNullValueHandler<EventCache>> cacheDidUpdateHandler;
	
	private static @Nullable EventCache instance = null;
	public static @NonNull EventCache shared() {
		if (instance == null) {
			instance = new EventCache();
		}
		return instance;
	}
	
	private EventCache() {
		super();
		this.eventTypes = new CountedSet<>();
		this.colorsForEvents = new HashMap<>();
		this.cacheDidUpdateHandler = new HashMap<>();
		this.unusedColors = new HashSet<>(Arrays.asList(Color.values()));
	}
	
	
	/**
	 * Registers an update handler.
	 *
	 * @param handler The handler to call when the contents of the cache are updated.
	 */
	public int addUpdateHandler(@NonNull NonNullValueHandler<EventCache> handler) {
		int newKey = 0;
		for (Integer key : cacheDidUpdateHandler.keySet()) {
			if (newKey <= key) {
				newKey = key;
			}
		}
		newKey += 1;
		
		cacheDidUpdateHandler.put(newKey, handler);
		return newKey;
	}
	
	/**
	 * Unregisters an update handler. Does nothing if there is no registered handler for the given key.
	 * @param key The handler key.
	 */
	public void removeUpdateHandler(int key) {
		cacheDidUpdateHandler.remove(key);
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
		@NonNull String eventType = event.getEventType();
		eventTypes.add(eventType);
		
		// Generate a color for the event
		colorForEvent(event);
	}
	
	@Override
	public @Nullable Event removeValueWithID(@NonNull String id) {
		Event event = super.removeValueWithID(id);
		if (event != null) {
			eventTypes.remove(event.getEventType());
		}
		return event;
	}
	
	private @NonNull Color randomUnusedColor() {
		List<Color> allColors = Arrays.asList(Color.values());
		Color color = ArrayHelpers.randomElementFromList(allColors);
		
		while (!unusedColors.contains(color)) {
			if (allColors.isEmpty()) {
				color = Color.random();
				break;
			}
			allColors.remove(color);
			color = ArrayHelpers.randomElementFromList(allColors);
		}
		
		unusedColors.remove(color);
		return color;
	}
	
	/**
	 * Returns the event's associated color. If the event has no known color, then one is associated
	 * with the event for future use, and that new color is returned.
	 * @param event The event whose color to fetch.
	 * @return a {@link Color} value.
	 */
	public @NonNull Color colorForEvent(@NonNull Event event) {
		String eventType = event.getEventType();
		
		@Nullable Color color = colorsForEvents.get(eventType);
		if (color == null) {
			color = randomUnusedColor();
			colorsForEvents.put(eventType, color);
		}
		unusedColors.remove(color);
		
		return color;
	}
	
	
	// ** Fetching Events
	
	public @NonNull EventsRequester fetchAllEvents(
		@NonNull ServerLocation location,
		@NonNull String authToken,
		@NonNull NonNullValueHandler<List<Event>> onSuccess,
		@NonNull NonNullValueHandler<Throwable> onFailure
	) {
		EventsRequester responder =
			new EventsRequester(
				location,
				authToken,
				events -> {
					// Add the new Person to the cache
					this.addAll(events);
					onSuccess.call(events);
				},
				onFailure
			);
		responder.start();
		return responder;
	}
}
