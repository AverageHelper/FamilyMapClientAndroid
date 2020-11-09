package com.example.familymapclient.transport.persons;

import com.example.familymapclient.transport.GetRequest;
import com.example.familymapclient.transport.MutableServerLocation;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RequestFailureException;
import com.example.familymapclient.transport.RequestTask;
import com.example.familymapclient.transport.ServerLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersonRequestTask extends RequestTask<GetRequest> {
	private static final String GET_PERSON_PATH = "/person";
	
	private final @NonNull String authToken;
	private final @Nullable OnDataFetched<String> listener;
	
	public PersonRequestTask(
		@NonNull MutableServerLocation location,
		@NonNull String personID,
		@NonNull String authToken,
		@Nullable OnDataFetched<String> listener
	) {
		this(new ServerLocation(location), personID, authToken, listener);
	}
	
	public PersonRequestTask(
		@NonNull ServerLocation location,
		@NonNull String personID,
		@NonNull String authToken,
		@Nullable OnDataFetched<String> listener
	) {
		super(location, GET_PERSON_PATH + "/" + personID, null);
		this.listener = listener;
		this.authToken = authToken;
	}
	
	@Override
	public @NonNull String httpMethod() {
		return "GET";
	}
	
	@Override
	public @NonNull String authToken() {
		return authToken;
	}
	
	@Override
	public void willBeginRunning() {
		if (listener != null) {
			listener.taskWillBeginRunning(this);
		}
	}
	
	@Override
	public void didFinishRunning(@NonNull String result) {
		if (listener != null) {
			listener.taskDidFinishRunning(this, result);
		}
	}
	
	@Override
	public void didFail(@NonNull Throwable error) {
		if (listener != null) {
			// Try to parse the error into a localized message
			if (error instanceof RequestFailureException) {
				RequestFailureException e = (RequestFailureException) error;
				
				if (e.getMessage() != null) {
//					if (e.getMessage().contains("USER_NOT_FOUND")) {
//						listener.taskDidFail(this, new LoginException(LoginFailureReason.USER_NOT_FOUND));
//						return;
//					} else if (e.getMessage().contains("INCORRECT_PASSWORD")) {
//						listener.taskDidFail(this, new LoginException(LoginFailureReason.INCORRECT_PASSWORD));
//						return;
//					}
				}
			}
			
			// Fallback to the server's raw message
			listener.taskDidFail(this, error);
		}
	}
}
