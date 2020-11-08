package com.example.familymapclient.transport.login;

import androidx.annotation.NonNull;

public enum LoginFailureReason {
	MISSING_USERNAME,
	MISSING_PASSWORD;
	
	public @NonNull String getMessage() {
		return this.name();
	}
}
