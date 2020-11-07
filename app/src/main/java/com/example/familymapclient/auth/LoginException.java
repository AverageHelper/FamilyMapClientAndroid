package com.example.familymapclient.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoginException extends Exception {
	private final @NonNull LoginFailureReason reason;
	
	public LoginException(@NonNull LoginFailureReason reason) {
		this(reason, null);
	}
	
	public LoginException(@NonNull LoginFailureReason reason, @Nullable Exception e) {
		super(reason.getMessage(), e);
		this.reason = reason;
	}
	
	public @NonNull LoginFailureReason getReason() {
		return reason;
	}
}
