package com.example.familymapclient.auth;

import androidx.annotation.Nullable;
import model.User;

@FunctionalInterface
public interface AuthStateDidChangeHandler {
	/**
	 * Called by the <code>Auth</code> proxy when the user signs in our out.
	 * @param user The currently signed-in user, or <code>null</code> if the user is not signed in.
	 */
	public void didChange(@Nullable User user);
}
