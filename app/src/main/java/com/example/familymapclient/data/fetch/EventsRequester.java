package com.example.familymapclient.data.fetch;

import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.transport.ServerLocation;
import com.example.familymapclient.transport.events.EventsRequestTask;

import java.util.List;

import androidx.annotation.NonNull;
import model.Event;
import responses.FetchMultipleEventsResponse;
import responses.FetchMultipleItemsResponse;
import transport.JSONSerialization;
import transport.MissingKeyException;

public class EventsRequester extends ItemsRequester<Event> {
	/**
	 * Creates a new {@link EventsRequester} object.
	 *
	 * @param location  The server's protocol, hostname, and port number.
	 * @param authToken The user's auth token.
	 * @param onSuccess A runnable to call upon successful return of a list of values.
	 * @param onFailure A runnable to call upon a failed fetch event.
	 */
	public EventsRequester(@NonNull ServerLocation location, @NonNull String authToken, @NonNull NonNullValueHandler<List<Event>> onSuccess, @NonNull NonNullValueHandler<Throwable> onFailure) {
		super(location, authToken, onSuccess, onFailure);
	}
	
	@Override
	public @NonNull EventsRequestTask getRequestTask(@NonNull ServerLocation location, @NonNull String authToken) {
		return new EventsRequestTask(location, authToken, this);
	}
	
	@Override
	public @NonNull FetchMultipleItemsResponse<Event> getResponseFromJSON(@NonNull String json) throws MissingKeyException {
		return JSONSerialization.fromJson(json, FetchMultipleEventsResponse.class);
	}
}
