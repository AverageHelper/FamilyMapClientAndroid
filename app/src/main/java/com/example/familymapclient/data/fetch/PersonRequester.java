package com.example.familymapclient.data.fetch;

import com.example.familymapclient.async.TaskRunner;
import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RequestTask;
import com.example.familymapclient.transport.ServerLocation;
import com.example.familymapclient.transport.persons.PersonRequestTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Person;
import responses.FetchSinglePersonResponse;
import transport.JSONSerialization;
import transport.MissingKeyException;

/**
 * An object that performs a fetch request, updating the provided cache with the returned data.
 */
public class PersonRequester implements OnDataFetched<String> {
	private @Nullable Person person;
	private @Nullable Throwable fetchError;
	private @Nullable TaskRunner runner;
	private final @NonNull NonNullValueHandler<Person> onSuccess;
	private final @NonNull NonNullValueHandler<Throwable> onFailure;
	
	private final @NonNull String personID;
	private final @NonNull ServerLocation location;
	private final @NonNull String authToken;
	
	/**
	 * Creates a new <code>PersonRequester</code> object.
	 * @param location The server's protocol, hostname, and port number.
	 * @param personID The ID of the person to fetch.
	 * @param authToken The user's auth token.
	 * @param onSuccess A runnable to call upon successful return of a <code>Person</code> object.
	 * @param onFailure A runnable to call upon failed return of a <code>Person</code> object.
	 */
	public PersonRequester(
		@NonNull ServerLocation location,
		@NonNull String personID,
		@NonNull String authToken,
		@NonNull NonNullValueHandler<Person> onSuccess,
		@NonNull NonNullValueHandler<Throwable> onFailure
	) {
		this.location = location;
		this.personID = personID;
		this.authToken = authToken;
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
		this.person = null;
		this.fetchError = null;
		this.runner = null;
	}
	
	
	/**
	 * @return The person that was fetched, or <code>null</code> if the fetch failed or no fetch was
	 * attempted.
	 */
	public @Nullable Person getPerson() {
		return person;
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
		if (getPerson() != null) {
			// Forward the known Person
			this.onSuccess.call(getPerson());
			return;
		}
		// Start the fetch
		PersonRequestTask task = new PersonRequestTask(location, personID, authToken, this);
		runner = new TaskRunner();
		runner.executeAsync(task);
	}
	
	
	private void setPerson(@NonNull Person person) {
		this.person = person;
		this.fetchError = null;
		this.onSuccess.call(person);
	}
	
	private void setFetchError(@NonNull Throwable error) {
		this.person = null;
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
			FetchSinglePersonResponse res =
				JSONSerialization.fromJson(response, FetchSinglePersonResponse.class);
			setPerson(new Person(
				res.getPersonID(),
				res.getAssociatedUsername(),
				res.getFirstName(),
				res.getLastName(),
				res.getGender(),
				res.getFatherID(),
				res.getMotherID(),
				res.getSpouseID()
			));
			
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
