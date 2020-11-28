package com.example.familymapclient.data.fetch;

import com.example.familymapclient.async.TaskRunner;
import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RequestTask;
import com.example.familymapclient.transport.ServerLocation;
import com.example.familymapclient.transport.events.EventsRequestTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;
import responses.FetchMultipleEventsResponse;
import transport.JSONSerialization;
import transport.MissingKeyException;

public class EventsRequester implements OnDataFetched<String> {
	private @Nullable List<Event> events;
	private @Nullable Throwable fetchError;
	private @Nullable TaskRunner runner;
	private final @NonNull NonNullValueHandler<List<Event>> onSuccess;
	private final @NonNull NonNullValueHandler<Throwable> onFailure;
	
	private final @NonNull ServerLocation location;
	private final @NonNull String authToken;
	
	/**
	 * Creates a new <code>EventsRequester</code> object.
	 * @param location The server's protocol, hostname, and port number.
	 * @param authToken The user's auth token.
	 * @param onSuccess A runnable to call upon successful return of a <code>Event</code> object.
	 * @param onFailure A runnable to call upon failed return of a <code>Event</code> object.
	 */
	public EventsRequester(
		@NonNull ServerLocation location,
		@NonNull String authToken,
		@NonNull NonNullValueHandler<List<Event>> onSuccess,
		@NonNull NonNullValueHandler<Throwable> onFailure
	) {
		this.location = location;
		this.authToken = authToken;
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
		this.events = null;
		this.fetchError = null;
		this.runner = null;
	}
	
	
	/**
	 * @return The events that were fetched, or <code>null</code> if the fetch failed or no fetch was
	 * attempted.
	 */
	public @Nullable List<Event> getEvents() {
		return events;
	}
	
	/**
	 * @return The reason the fetch failed, or <code>null</code> if the fetch succeeded or no fetch was
	 * attempted.
	 */
	public @Nullable Throwable getFetchError() {
		return fetchError;
	}
	
	/**
	 * @return <code>true</code> if the task is presently running.
	 */
	public boolean isRunning() {
		return runner != null;
	}
	
	
	
	/**
	 * Sends the request to the server.
	 */
	public void start() {
		if (getEvents() != null) {
			// Forward the known Person
			this.onSuccess.call(getEvents());
			return;
		}
		// Start the fetch
		EventsRequestTask task = new EventsRequestTask(location, authToken, this);
		runner = new TaskRunner();
		runner.executeAsync(task);
	}
	
	
	private void setEvents(@NonNull List<Event> events) {
		this.events = events;
		this.fetchError = null;
		this.onSuccess.call(events);
	}
	
	private void setFetchError(@NonNull Throwable error) {
		this.events = null;
		this.fetchError = error;
		this.onFailure.call(error);
	}
	
	
	@Override
	public <Task extends RequestTask<?>> void taskWillBeginRunning(@NonNull Task task) {
		// nop
	}
	
	@Override
	public <Task extends RequestTask<?>> void taskDidFinishRunning(@NonNull Task task, @NonNull String response) {
		try {
			FetchMultipleEventsResponse res =
				JSONSerialization.fromJson(response, FetchMultipleEventsResponse.class);
			setEvents(res.getData());
			
		} catch (MissingKeyException e) {
			setFetchError(e);
		}
	}
	
	@Override
	public <Task extends RequestTask<?>> void taskDidFail(@NonNull Task task, @NonNull Throwable error) {
		System.out.println("Person fetch: taskDidFail " + error);
		setFetchError(error);
	}
}
