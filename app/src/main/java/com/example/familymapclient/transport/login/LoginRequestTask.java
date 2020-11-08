package com.example.familymapclient.transport.login;

import com.example.familymapclient.transport.MutableServerLocation;
import com.example.familymapclient.transport.OnDataFetched;
import com.example.familymapclient.transport.RequestTask;
import com.example.familymapclient.transport.ServerLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import requests.LoginRequest;

/**
 * A task that sends a Family Map login request to a server.
 */
public class LoginRequestTask extends RequestTask<LoginRequest> {
	private static final String LOGIN_PATH = "/user/login";
	
	private final @Nullable OnDataFetched<String> listener;
	
	public LoginRequestTask(
		@NonNull MutableServerLocation location,
		@Nullable LoginRequest req,
		@Nullable OnDataFetched<String> listener
	) {
		this(new ServerLocation(location), req, listener);
	}
	
	public LoginRequestTask(
		@NonNull ServerLocation location,
		@Nullable LoginRequest req,
		@Nullable OnDataFetched<String> listener
	) {
		super(location, LOGIN_PATH, req);
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
			listener.taskDidFail(this, error);
		}
	}
}
