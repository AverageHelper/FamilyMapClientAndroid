package com.example.familymapclient.data;

import androidx.annotation.Nullable;

/**
 * An object that can be queried for the current user's Person ID.
 */
public interface PersonIDProvider {
	/**
	 * @return The user's person ID if the user is signed in. <code>null</code> otherwise.
	 */
	@Nullable String getPersonID();
}
