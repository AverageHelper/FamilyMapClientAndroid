package com.example.familymapclient.transport;

import androidx.annotation.NonNull;

public class RequestFailureException extends Exception {
	public RequestFailureException(@NonNull String message) {
		super(message);
	}
}
