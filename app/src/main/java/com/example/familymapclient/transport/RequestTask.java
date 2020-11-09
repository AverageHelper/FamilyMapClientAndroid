package com.example.familymapclient.transport;

import com.example.familymapclient.async.CustomCallable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	public abstract @NonNull String httpMethod();
	
	
	
	
	
	@Override
	public @NonNull String call() throws IOException, RequestFailureException {
		// send the request to the server
		URL url = location.getURL(path);
		
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		
		http.setRequestMethod(httpMethod());
		http.setDoOutput(req != null); // false if there is no request body
		
//		http.addRequestProperty("Authorization", "auth_token");
		if (req == null) {
			http.addRequestProperty("Accept", "application/json");
		} else {
			http.addRequestProperty("Content-Type", "application/json");
		}
		
		System.out.println("Attempting to connect to " + url.toString());
		http.connect();
		
		
		if (req != null) {
			// Send the request payload
			String reqData = req.serialize();
			OutputStream reqBody = http.getOutputStream();
			writeString(reqData, reqBody);
			reqBody.close();
		}
		
		if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Success
			InputStream respBody = http.getInputStream();
			return readString(respBody);
			
		} else {
			// Fail
			InputStream respBody = http.getErrorStream();
			String message = readString(respBody);
			if (message.isEmpty()) {
				message = http.getResponseMessage();
			}
			
			throw RequestFailureException.fromServerResponse(message);
		}
	}
	
	
	/**
	 * Reads a UTF-8 string from the given input <code>stream</code>.
	 * @param stream The stream of data from which a string should be interpreted.
	 * @return A string representation of the stream's data.
	 * @throws IOException if there was a problem reading UTF-8 bytes from the stream.
	 */
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
	
	/**
	 * Writes the provided <code>text</code> to the output <code>stream</code>.
	 * @param text The string to write.
	 * @param stream The stream to which <code>text</code> should be written.
	 * @throws IOException if there was a problem writing to the stream.
	 */
	private void writeString(@NonNull String text, @NonNull OutputStream stream) throws IOException {
		stream.write(text.getBytes());
	}
}
