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
	private final @NonNull String serverHostName;
	private final int serverPortNumber;
	private final @Nullable Request req;
	private final @NonNull String path;
	
	public RequestTask(
		@NonNull String serverHostName,
		int serverPortNumber,
		@Nullable String path,
		@Nullable Request req
	) {
		this.serverHostName = serverHostName;
		this.serverPortNumber = serverPortNumber;
		if (path == null) {
			this.path = "/";
		} else {
			this.path = path;
		}
		this.req = req;
	}
	
	
	
	
	/**
	 * Called to determine what HTTP method this request should use.
	 * @return The HTTP method.
	 */
	public abstract @NotNull String httpMethod();
	
	
	
	
	
	@Override
	public @NonNull String call() throws IOException {
		// send the request to the server
		URL mUrl = new URL("http://" + serverHostName + ":" + serverPortNumber + path);
		
		HttpURLConnection http = (HttpURLConnection) mUrl.openConnection();
		
		http.setRequestMethod(httpMethod());
		http.setDoOutput(req != null); // false if there is no request body
		
		http.addRequestProperty("Authorization", "auth_token");
		http.addRequestProperty("Accept", "application/json");
		
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
