package com.example.familymapclient.auth;

import com.example.familymapclient.async.TaskRunner;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RegisterRequestTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Gender;
import model.User;
import requests.LoginRequest;
import requests.RegisterRequest;

public class Auth implements OnDataFetched<String> {
	private @Nullable String authToken;
	private @Nullable User user;
	private @Nullable TaskRunner runner;
	
	private Auth() {
		this.authToken = null;
		this.user = null;
		this.runner = null;
	}
	
	private static @Nullable Auth _instance = null;
	public static @NonNull Auth shared() {
		if (_instance == null) {
			_instance = new Auth();
		}
		return _instance;
	}
	
	public @Nullable String getAuthToken() {
		return authToken;
	}
	
	/**
	 * @return The signed-in user if the user is signed in. <code>null</code> otherwise.
	 */
	public @Nullable User getUser() {
		return user;
	}
	
	/**
	 * @return <code>true</code> if the user is currently logged in.
	 */
	public boolean isSignedIn() {
		return user != null;
	}
	
	public boolean isRunning() {
		return runner != null;
	}
	
	/**
	 * Attempts to get a new auth token from the server.
	 *
	 * @param username The user's unique username.
	 * @param password The user's password.
	 * @throws LoginException An exception if there was some problem logging in.
	 */
	public void signIn(@NonNull String username, @NonNull String password) throws LoginException {
		authToken = "logged in lol";
		user = new User(
			"",
			"",
			"",
			"",
			"",
			Gender.MALE,
			null
		);
		
		LoginRequest req = new LoginRequest(
			username,
			password
		);
		
		// Send the request
	}
	
	public void register(
		@NonNull String username,
		@NonNull String password,
		@NonNull String email,
		@NonNull String firstName,
		@NonNull String lastName,
		@NonNull Gender gender
	) throws RegisterException {
		// TODO: Check that none of the passed fields were empty.
//		if (username.isEmpty()) {
//			throw new RegisterException(RegisterFailureReason.MISSING_USERNAME);
//		}
//		if (password.isEmpty()) {
//			throw new RegisterException(RegisterFailureReason.MISSING_PASSWORD);
//		}
//		if (email.isEmpty()) {
//			throw new RegisterException(RegisterFailureReason.MISSING_EMAIL);
//		}
//		if (firstName.isEmpty()) {
//			throw new RegisterException(RegisterFailureReason.MISSING_FIRST_NAME);
//		}
//		if (lastName.isEmpty()) {
//			throw new RegisterException(RegisterFailureReason.MISSING_LAST_NAME);
//		}
		
		RegisterRequest req = new RegisterRequest(
			username,
			password,
			email,
			firstName,
			lastName,
			gender
		);
		
		// Send the request
		RegisterRequestTask task = new RegisterRequestTask(
			"localhost",
			8080,
			req,
			this
		);
		runner = new TaskRunner();
		runner.executeAsync(task);
	}
	
	
	
	@Override
	public void taskWillBeginRunning() {
		// Tell fragments to update (we're loading, so clients should read that)
	}
	
	
	
	@Override
	public void taskDidFinishRunning(@NonNull String result) {
		// Determine whether this was a login or result
		// Parse the result
		// Set our logged-in state, or store an error message
		// Tell fragments to update
		
		this.runner = null;
	}
}
