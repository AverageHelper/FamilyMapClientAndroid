package com.example.familymapclient.auth;

import androidx.annotation.NonNull;

public enum RegisterFailureReason {
	MISSING_USERNAME,
	MISSING_PASSWORD,
	MISSING_EMAIL,
	MISSING_FIRST_NAME,
	MISSING_LAST_NAME;
	
	public @NonNull String getMessage() {
		return "";
	}
}
