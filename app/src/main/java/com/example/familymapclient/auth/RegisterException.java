package com.example.familymapclient.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RegisterException extends Exception {
	private final @NonNull RegisterFailureReason reason;
	
	public RegisterException(@NonNull RegisterFailureReason reason) {
		this(reason, null);
	}
	
	public RegisterException(@NonNull RegisterFailureReason reason, @Nullable Exception e) {
		super(reason.getMessage(), e);
		this.reason = reason;
	}
	
	public @NonNull RegisterFailureReason getReason() {
		return reason;
	}
}
