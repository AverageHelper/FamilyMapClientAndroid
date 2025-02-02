package com.example.familymapclient.transport.register;

import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RequestFailureException;
import com.example.familymapclient.transport.RequestTask;
import com.example.familymapclient.transport.ServerLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import requests.RegisterRequest;

/**
 * A task that sends a Family Map register request to a server.
 */
public class RegisterRequestTask extends RequestTask<RegisterRequest> {
	private static final String REGISTER_PATH = "/user/register";
	
	private final @Nullable OnDataFetched<String> listener;
	
	public RegisterRequestTask(
		@NonNull ServerLocation location,
		@Nullable RegisterRequest req,
		@Nullable OnDataFetched<String> listener
	) {
		super(location, REGISTER_PATH, req);
		this.listener = listener;
	}
	
	@Override
	public @NonNull String httpMethod() {
		return "POST";
	}
	
	
	
	@Override
	public void willBeginRunning() {
		if (listener != null) {
			listener.taskWillBeginRunning(this);
		}
	}
	
	@Override
	public void didFinishRunning(@NonNull String result) {
		if (listener != null) {
			listener.taskDidFinishRunning(this, result);
		}
	}
	
	@Override
	public void didFail(@NonNull Throwable error) {
		if (listener != null) {
			// Try to parse the error into a localized message
			if (error instanceof RequestFailureException) {
				RequestFailureException e = (RequestFailureException) error;
				
				if (e.getMessage() != null) {
					if (e.getMessage().contains("DUPLICATE_USERNAME")) {
						listener.taskDidFail(this,
							new RegisterException(RegisterFailureReason.DUPLICATE_USERNAME));
						return;
					}
				}
			}
			
			// Fallback to the server's raw message
			listener.taskDidFail(this, error);
		}
	}
}
