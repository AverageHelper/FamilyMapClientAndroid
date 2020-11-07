package com.example.familymapclient.transport;

import com.example.familymapclient.async.CustomCallable;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import transport.JSONSerialization;

/**
 * A task that sends a serializable request object to a remote server.
 * @param <Request> The type of request object to send.
 */
public abstract class RequestTask<Request extends JSONSerialization> implements CustomCallable<String> {
	private final @NonNull ServerLocation location;
	private final @Nullable Request req;
	private final @NonNull String path;
	
	public RequestTask(
		@NonNull ServerLocation location,
		@Nullable String path,
		@Nullable Request req
	) {
		this.location = location;
		this.req = req;
		if (path == null) {
			this.path = "/";
		} else {
			this.path = path;
		}
	}
	
	
	
	
	/**
	 * Called to determine what HTTP method this request should use.
	 * @return The HTTP method.
	 */
	public abstract @NotNull String httpMethod();
	
	
	
	
	
	@Override
	public @NonNull String call() throws IOException {
		// send the request to the server
		URL url = location.getURL(path);
		
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		
		http.setRequestMethod(httpMethod());
		http.setDoOutput(req != null); // false if there is no request body
		
		http.addRequestProperty("Authorization", "auth_token");
		http.addRequestProperty("Accept", "application/json");
		
		System.out.println("Attempting to connect to " + url.toString());
		http.connect();
		if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Success
			InputStream respBody = http.getInputStream();
			return readString(respBody);
			
		} else {
			// Fail
			return http.getResponseMessage();
		}
	}
	
	
	
	
	
	private @NonNull String readString(@NonNull InputStream stream) throws IOException {
		BufferedReader httpInput = new BufferedReader(new InputStreamReader(
			stream,
			"UTF-8"
		));
		StringBuilder in = new StringBuilder();
		String input;
		while ((input = httpInput.readLine()) != null) {
			in.append(input).append("\n");
		}
		httpInput.close();
		return in.toString();
	}
}
