package com.example.familymapclient.auth;

import com.example.familymapclient.async.TaskRunner;
import com.example.familymapclient.transport.LoginRequestTask;
import com.example.familymapclient.transport.MutableServerLocation;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RegisterRequestTask;
import com.example.familymapclient.transport.RequestTask;

import java.util.HashMap;
import java.util.Map;

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
	private final @NonNull MutableServerLocation location;
	private final @NonNull Map<Integer, AuthStateDidChangeHandler> authStateDidChangeHandlers;
	
	private Auth() {
		this.authToken = null;
		this.user = null;
		this.runner = null;
		this.location = new MutableServerLocation();
		this.authStateDidChangeHandlers = new HashMap<>();
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
	
	private void setUser(@Nullable User user) {
		this.user = user;
		for (AuthStateDidChangeHandler h : authStateDidChangeHandlers.values()) {
			h.didChange(user);
		}
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
	
	public void setHostname(@Nullable String hostname) {
		location.setHostname(hostname);
	}
	
	public void setPortNumber(@Nullable Integer portNumber) {
		location.setPortNumber(portNumber);
	}
	
	public void setUsesSecureProtocol(boolean usesSecureProtocol) {
		location.setUsesSecureProtocol(usesSecureProtocol);
	}
	
	public @Nullable String getHostname() {
		return location.getHostname();
	}
	
	public @Nullable Integer getPortNumber() {
		return location.getPortNumber();
	}
	
	public boolean usesSecureProtocol() {
		return location.usesSecureProtocol();
	}
	
	/**
	 * Registers an auth-state handler.
	 *
	 * @param handler The handler to call when the user's auth state changes.
	 * @return A key that identifies the handler to the auth proxy. Pass this value to
	 * <code>removeAuthStateDidChangeHandler</code> to unregister this handler.
	 */
	public int addAuthStateDidChangeHandler(@NonNull AuthStateDidChangeHandler handler) {
		int newKey = 0;
		for (Integer key : authStateDidChangeHandlers.keySet()) {
			if (newKey <= key) {
				newKey = key;
			}
		}
		newKey += 1;
		
		authStateDidChangeHandlers.put(newKey, handler);
		return newKey;
	}
	
	/**
	 * Unregisters an auth-state handler. Does nothing if there is no registered handler for the given key.
	 * @param key The handler key.
	 */
	public void removeAuthStateDidChangeHandler(int key) {
		authStateDidChangeHandlers.remove(key);
	}
	
	/**
	 * Attempts to get a new auth token from the server.
	 *
	 * @param username The user's unique username.
	 * @param password The user's password.
	 * @throws LoginException An exception if there was some problem logging in.
	 */
	public void signIn(@NonNull String username, @NonNull String password) throws LoginException {
//		authToken = "logged in lol";
//		setUser(new User(
//			"",
//			"",
//			"",
//			"",
//			"",
//			Gender.MALE,
//			null
//		));
		
		LoginRequest req = new LoginRequest(
			username,
			password
		);
		
		// Send the request
		LoginRequestTask task = new LoginRequestTask(location, req, this);
		runner = new TaskRunner();
		runner.executeAsync(task);
	}
	
	
	/**
	 * Logs the user out. Forgets the stored auth token and user.
	 */
	public void logOut() {
		authToken = null;
		setUser(null);
	}
	
	
	
	
	
	
	public void register(
		@NonNull String username,
		@NonNull String password,
		@NonNull String email,
		@NonNull String firstName,
		@NonNull String lastName,
		@Nullable Gender gender
	) throws RegisterException {
		if (username.isEmpty()) {
			throw new RegisterException(RegisterFailureReason.MISSING_USERNAME);
		}
		if (password.isEmpty()) {
			throw new RegisterException(RegisterFailureReason.MISSING_PASSWORD);
		}
		if (email.isEmpty()) {
			throw new RegisterException(RegisterFailureReason.MISSING_EMAIL);
		}
		if (firstName.isEmpty()) {
			throw new RegisterException(RegisterFailureReason.MISSING_FIRST_NAME);
		}
		if (lastName.isEmpty()) {
			throw new RegisterException(RegisterFailureReason.MISSING_LAST_NAME);
		}
		if (gender == null) {
			throw new RegisterException(RegisterFailureReason.MISSING_GENDER);
		}
		
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
			location,
			req,
			this
		);
		runner = new TaskRunner();
		runner.executeAsync(task);
	}
	
	
	
	public void taskWillBeginRunning(@NonNull LoginRequestTask task) {
		System.out.println("Log In: taskWillBeginRunning " + task.toString());
		// Tell fragments to update (we're loading, so clients should read that)
	}
	
	public void taskDidFinishRunning(@NonNull LoginRequestTask task, @NonNull String result) {
		System.out.println("Log In: taskDidFinishRunning " + result);
		// Parse the result
		// Set our logged-in state, or store an error message
		// Tell fragments to update
		
		this.runner = null;
	}
	
	public void taskDidFail(@NonNull LoginRequestTask task, @NonNull Throwable error) {
		System.out.println("Log In: taskDidFail " + error);
		// Tell fragments to display the result
		this.runner = null;
	}
	
	
	
	public void taskWillBeginRunning(@NonNull RegisterRequestTask task) {
		System.out.println("Register: taskWillBeginRunning " + task.toString());
		// Tell fragments to update (we're loading, so clients should read that)
	}
	
	public void taskDidFinishRunning(@NonNull RegisterRequestTask task, @NonNull String result) {
		System.out.println("Register: taskDidFinishRunning " + result);
		// Parse the result
		// Set our logged-in state, or store an error message
		// Tell fragments to update
		
		this.runner = null;
	}
	
	public void taskDidFail(@NonNull RegisterRequestTask task, @NonNull Throwable error) {
		System.out.println("Register: taskDidFail " + error);
		// Tell fragments to display the result
		this.runner = null;
	}
	
	
	
	
	@Override
	public <Task extends RequestTask<?>> void taskWillBeginRunning(@NonNull Task task) {
		if (task instanceof LoginRequestTask) {
			taskWillBeginRunning((LoginRequestTask) task);
			
		} else if (task instanceof RegisterRequestTask) {
			taskWillBeginRunning((RegisterRequestTask) task);
			
		} else {
			System.out.println("taskWillBeginRunning called with unknown task: " + task.toString());
		}
	}
	
	@Override
	public <Task extends RequestTask<?>> void taskDidFinishRunning(@NonNull Task task, @NonNull String result) {
		if (task instanceof LoginRequestTask) {
			taskDidFinishRunning((LoginRequestTask) task, result);
			
		} else if (task instanceof RegisterRequestTask) {
			taskDidFinishRunning((RegisterRequestTask) task, result);
			
		} else {
			System.out.println("taskDidFinishRunning called with unknown task: " + task.toString());
		}
	}
	
	@Override
	public <Task extends RequestTask<?>> void taskDidFail(@NonNull Task task, @NonNull Throwable error) {
		if (task instanceof LoginRequestTask) {
			taskDidFail((LoginRequestTask) task, error);
			
		} else if (task instanceof RegisterRequestTask) {
			taskDidFail((RegisterRequestTask) task, error);
			
		} else {
			System.out.println("taskDidFail called with unknown task: " + task.toString());
		}
	}
}
