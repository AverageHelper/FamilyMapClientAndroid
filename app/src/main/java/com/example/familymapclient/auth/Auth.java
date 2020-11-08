package com.example.familymapclient.auth;

import com.example.familymapclient.async.TaskRunner;
import com.example.familymapclient.transport.login.LoginException;
import com.example.familymapclient.transport.login.LoginFailureReason;
import com.example.familymapclient.transport.login.LoginRequestTask;
import com.example.familymapclient.transport.MutableServerLocation;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.register.RegisterException;
import com.example.familymapclient.transport.register.RegisterFailureReason;
import com.example.familymapclient.transport.register.RegisterRequestTask;
import com.example.familymapclient.transport.RequestTask;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Gender;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import transport.JSONSerialization;
import transport.MissingKeyException;

public class Auth implements OnDataFetched<String> {
	private @Nullable String authToken;
	private @Nullable Throwable loginError;
	private @Nullable TaskRunner runner;
	private final @NonNull MutableServerLocation location;
	
	private final @NonNull Map<Integer, NullableValueHandler<String>> authStateDidChangeHandlers;
	private final @NonNull Map<Integer, NonNullValueHandler<Throwable>> authStateDidFailToChangeHandlers;
	
	private Auth() {
		this.authToken = null;
		this.loginError = null;
		this.runner = null;
		this.location = new MutableServerLocation();
		this.authStateDidChangeHandlers = new HashMap<>();
		this.authStateDidFailToChangeHandlers = new HashMap<>();
	}
	
	private static @Nullable Auth _instance = null;
	public static @NonNull Auth shared() {
		if (_instance == null) {
			_instance = new Auth();
		}
		return _instance;
	}
	
	
	
	
	
	/**
	 * @return The user's auth token if the user is signed in. <code>null</code> otherwise.
	 */
	public @Nullable String getAuthToken() {
		return authToken;
	}
	
	/**
	 * @return The error that prevented the most recent login or register task from completing, or
	 * <code>null</code> if the user is signed in or a sign-in attempt has not been made.
	 */
	public @Nullable Throwable getLoginError() {
		return loginError;
	}
	
	
	
	
	private void setAuthToken(@Nullable String authToken) {
		this.loginError = null;
		boolean shouldCallHandlers =
			(this.authToken == null && authToken != null) ||
				(this.authToken != null && !this.authToken.equals(authToken));
		
		this.authToken = authToken;
		if (shouldCallHandlers) {
			for (NullableValueHandler<String> h : authStateDidChangeHandlers.values()) {
				h.call(authToken);
			}
		}
	}
	
	private void setLoginError(@NonNull Throwable error) {
		setAuthToken(null);
		this.loginError = error;
		for (NonNullValueHandler<Throwable> h : authStateDidFailToChangeHandlers.values()) {
			h.call(error);
		}
	}
	
	
	
	
	/**
	 * @return <code>true</code> if the user is currently logged in.
	 */
	public boolean isSignedIn() {
		return authToken != null;
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
	public int addAuthStateDidChangeHandler(@NonNull NullableValueHandler<String> handler) {
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
	 * Registers a failure handler.
	 *
	 * @param handler The handler to call when the auth proxy fails to log in or register a user.
	 * @return A key that identifies the handler to the auth proxy. Pass this value to
	 * <code>removeFailureHandler</code> to unregister this handler.
	 */
	public int addFailureHandler(@NonNull NonNullValueHandler<Throwable> handler) {
		int newKey = 0;
		for (Integer key : authStateDidChangeHandlers.keySet()) {
			if (newKey <= key) {
				newKey = key;
			}
		}
		newKey += 1;
		
		authStateDidFailToChangeHandlers.put(newKey, handler);
		return newKey;
	}
	
	/**
	 * Unregisters a failure handler. Does nothing if there is no registered handler for the given key.
	 * @param key The handler key.
	 */
	public void removeFailureHandler(int key) {
		authStateDidFailToChangeHandlers.remove(key);
	}
	
	
	
	
	
	/**
	 * Attempts to get a new auth token from the server.
	 *
	 * @param username The user's unique username.
	 * @param password The user's password.
	 * @throws LoginException An exception if there was some problem logging in.
	 */
	public void signIn(@NonNull String username, @NonNull String password) throws LoginException {
		if (username.isEmpty()) {
			throw new LoginException(LoginFailureReason.MISSING_USERNAME);
		}
		if (password.isEmpty()) {
			throw new LoginException(LoginFailureReason.MISSING_PASSWORD);
		}
		
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
		setAuthToken(null);
	}
	
	
	
	
	
	
	
	/**
	 * Attempts to register a new user with the provided details.
	 *
	 * @param username The unique identifier of the new user.
	 * @param password The user's password.
	 * @param email The user's email address.
	 * @param firstName The user's first name.
	 * @param lastName The user's last name.
	 * @param gender The user's gender.
	 * @throws RegisterException if there was a problem with the provided inputs.
	 */
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
	
	
	
	
	
	// ** Login Callbacks
	
	public void taskWillBeginRunning(@NonNull LoginRequestTask task) {
		// Tell fragments to update (we're loading, so clients should read that)
	}
	
	public void taskDidFinishRunning(@NonNull LoginRequestTask task, @NonNull String response) {
		try {
			LoginResponse loginResponse = JSONSerialization.fromJson(response, LoginResponse.class);
			setAuthToken(loginResponse.getAuthToken());
			
		} catch (MissingKeyException e) {
			setLoginError(e);
		}
	}
	
	public void taskDidFail(@NonNull LoginRequestTask task, @NonNull Throwable error) {
		System.out.println("Log In: taskDidFail " + error);
		setLoginError(error);
	}
	
	
	
	
	// ** Register Callbacks
	
	public void taskWillBeginRunning(@NonNull RegisterRequestTask task) {
		System.out.println("Register: taskWillBeginRunning " + task.toString());
		// Tell fragments to update (we're loading, so clients should read that)
	}
	
	public void taskDidFinishRunning(@NonNull RegisterRequestTask task, @NonNull String response) {
		System.out.println("Register: taskDidFinishRunning " + response);
		try {
			RegisterResponse registerResponse = JSONSerialization.fromJson(response, RegisterResponse.class);
			setAuthToken(registerResponse.getAuthToken());
			
		} catch (MissingKeyException e) {
			setLoginError(e);
		}
	}
	
	public void taskDidFail(@NonNull RegisterRequestTask task, @NonNull Throwable error) {
		System.out.println("Register: taskDidFail " + error);
		setLoginError(error);
	}
	
	
	
	
	// ** Async Callback Dispatchers
	
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
		this.runner = null;
		
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
		this.runner = null;
		
		if (task instanceof LoginRequestTask) {
			taskDidFail((LoginRequestTask) task, error);
			
		} else if (task instanceof RegisterRequestTask) {
			taskDidFail((RegisterRequestTask) task, error);
			
		} else {
			System.out.println("taskDidFail called with unknown task: " + task.toString());
		}
	}
}
