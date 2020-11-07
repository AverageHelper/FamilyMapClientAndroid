package com.example.familymapclient.transport;

import androidx.annotation.NonNull;
import responses.MessageResponse;
import transport.JSONSerialization;
import transport.MissingKeyException;

public class RequestFailureException extends Exception {
	public RequestFailureException(@NonNull String message) {
		super(message);
	}
	
	public static @NonNull RequestFailureException fromServerResponse(@NonNull String message) {
		try {
			MessageResponse resp = JSONSerialization.fromJson(message, MessageResponse.class);
			return new RequestFailureException(resp.getMessage());
			
		} catch (MissingKeyException e) {
			return new RequestFailureException(message);
		}
	}
}
