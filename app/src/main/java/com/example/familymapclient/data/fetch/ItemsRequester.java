package com.example.familymapclient.data.fetch;

import com.example.familymapclient.async.TaskRunner;
import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.transport.GetRequestTask;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RequestTask;
import com.example.familymapclient.transport.ServerLocation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.ModelData;
import responses.FetchMultipleItemsResponse;
import transport.MissingKeyException;

/**
 * An object that requests multiple items from the Family Map server.
 */
public abstract class ItemsRequester<Value extends ModelData> implements OnDataFetched<String> {
	private @Nullable List<Value> values;
	private @Nullable Throwable fetchError;
	private @Nullable TaskRunner runner;
	private final @NonNull NonNullValueHandler<List<Value>> onSuccess;
	private final @NonNull NonNullValueHandler<Throwable> onFailure;
	
	private final @NonNull ServerLocation location;
	private final @NonNull String authToken;
	
	/**
	 * Creates a new {@link ItemsRequester} object.
	 * @param location The server's protocol, hostname, and port number.
	 * @param authToken The user's auth token.
	 * @param onSuccess A runnable to call upon successful return of a list of values.
	 * @param onFailure A runnable to call upon a failed fetch event.
	 */
	public ItemsRequester(
		@NonNull ServerLocation location,
		@NonNull String authToken,
		@NonNull NonNullValueHandler<List<Value>> onSuccess,
		@NonNull NonNullValueHandler<Throwable> onFailure
	) {
		this.location = location;
		this.authToken = authToken;
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
		this.values = null;
		this.fetchError = null;
		this.runner = null;
	}
	
	/**
	 * @return The values that were fetched, or <code>null</code> if the fetch failed or no fetch was
	 * attempted.
	 */
	public @Nullable List<Value> getValues() {
		return values;
	}
	
	/**
	 * @return The reason the fetch failed, or <code>null</code> if the fetch succeeded or no fetch was
	 * attempted.
	 */
	public @Nullable Throwable getFetchError() {
		return fetchError;
	}
	
	
	/**
	 * Retrieves a {@link GetRequestTask} to run.
	 * @param location The server's internet address information.
	 * @param authToken The user's auth token.
	 * @return A {@link GetRequestTask} to run.
	 */
	public abstract @NonNull GetRequestTask getRequestTask(
		@NonNull ServerLocation location,
		@NonNull String authToken
	);
	
	public abstract @NonNull FetchMultipleItemsResponse<Value> getResponseFromJSON(
		@NonNull String json
	) throws MissingKeyException;
	
	/**
	 * Sends the request to the server.
	 */
	public void start() {
		if (getValues() != null) {
			// Forward the known Person
			this.onSuccess.call(getValues());
			return;
		}
		// Start the fetch
		GetRequestTask task = getRequestTask(location, authToken);
		runner = new TaskRunner();
		runner.executeAsync(task);
	}
	
	
	private void setValues(@NonNull List<Value> values) {
		this.values = values;
		this.fetchError = null;
		this.onSuccess.call(values);
	}
	
	private void setFetchError(@NonNull Throwable error) {
		this.values = null;
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
			FetchMultipleItemsResponse<Value> res = getResponseFromJSON(response);
			setValues(res.getData());
			
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
