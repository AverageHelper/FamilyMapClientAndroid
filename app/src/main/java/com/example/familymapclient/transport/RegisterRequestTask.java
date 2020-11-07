package com.example.familymapclient.transport;

import org.jetbrains.annotations.NotNull;

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
		@NonNull String serverHostName,
		int serverPortNumber,
		@Nullable RegisterRequest req,
		@Nullable OnDataFetched<String> listener
	) {
		super(serverHostName, serverPortNumber, REGISTER_PATH, req);
		this.listener = listener;
	}
	
	@Override
	public @NotNull String httpMethod() {
		return "POST";
	}
	
	
	@Override
	public void willBeginRunning() {
		if (listener != null) {
			listener.taskWillBeginRunning();
		}
	}
	
	
	@Override
	public void didFinishRunning(@NonNull String result) {
		if (listener != null) {
			listener.taskDidFinishRunning(result);
		}
	}
}
