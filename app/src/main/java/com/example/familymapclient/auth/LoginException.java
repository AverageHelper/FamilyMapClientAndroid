package com.example.familymapclient.auth;

public class LoginException extends Exception {
	public LoginException() {
		super();
	}
	
	public LoginException(String message) {
		super(message);
	}
	
	public LoginException(Exception e) {
		super(e);
	}
	
	public LoginException(String message, Exception e) {
		super(message, e);
	}
}
