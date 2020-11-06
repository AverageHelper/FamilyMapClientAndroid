package com.example.familymapclient.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import model.Gender;
//import model.User;

public class Auth {
	private @Nullable String authToken;
//	private @Nullable User user;
	
	private Auth() {
		this.authToken = null;
//		this.user = null;
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
//	public @Nullable User getUser() {
//		return user;
//	}
	
	/**
	 * @return <code>true</code> if the user is currently logged in.
	 */
//	public boolean isSignedIn() {
//		return user != null;
//	}
	
	/**
	 * Attempts to get a new auth token from the server.
	 *
	 * @param username The user's unique username.
	 * @param password The user's password.
	 * @throws LoginException An exception if there was some problem logging in.
	 */
	public void signIn(@NonNull String username, @NonNull String password) throws LoginException {
		authToken = "logged in lol";
//		user = new User(
//			"",
//			"",
//			"",
//			"",
//			"",
//			Gender.MALE,
//			""
//		);
	}
}
