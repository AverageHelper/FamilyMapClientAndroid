package com.example.familymapclient.data.fetch;

import com.example.familymapclient.auth.NonNullValueHandler;
import com.example.familymapclient.transport.ServerLocation;
import com.example.familymapclient.transport.persons.PersonsRequestTask;

import java.util.List;

import androidx.annotation.NonNull;
import model.Person;
import responses.FetchMultipleItemsResponse;
import responses.FetchMultiplePersonsResponse;
import transport.JSONSerialization;
import transport.MissingKeyException;

public class PersonsRequester extends ItemsRequester<Person> {
	
	/**
	 * Creates a new {@link PersonsRequester} object.
	 *
	 * @param location  The server's protocol, hostname, and port number.
	 * @param authToken The user's auth token.
	 * @param onSuccess A runnable to call upon successful return of a list of values.
	 * @param onFailure A runnable to call upon a failed fetch event.
	 */
	public PersonsRequester(@NonNull ServerLocation location, @NonNull String authToken, @NonNull NonNullValueHandler<List<Person>> onSuccess, @NonNull NonNullValueHandler<Throwable> onFailure) {
		super(location, authToken, onSuccess, onFailure);
	}
	
	@Override
	public @NonNull PersonsRequestTask getRequestTask(@NonNull ServerLocation location, @NonNull String authToken) {
		return new PersonsRequestTask(location, authToken, this);
	}
	
	@Override
	public @NonNull FetchMultipleItemsResponse<Person> getResponseFromJSON(@NonNull String json) throws MissingKeyException {
		return JSONSerialization.fromJson(json, FetchMultiplePersonsResponse.class);
	}
}
