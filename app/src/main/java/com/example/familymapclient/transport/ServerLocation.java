package com.example.familymapclient.transport;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An object that represents a server hostname, port number, and protocol.
 */
public class ServerLocation {
	private final @NonNull MutableServerLocation location;
	
	public ServerLocation() {
		this.location = new MutableServerLocation();
	}
	
	public ServerLocation(
		@Nullable String hostname,
		@Nullable Integer portNumber,
		boolean usesSecureProtocol
	) {
		this.location = new MutableServerLocation(hostname, portNumber, usesSecureProtocol);
	}
	
	public ServerLocation(@NonNull MutableServerLocation location) {
		this.location = location;
	}
	
	public @Nullable String getHostname() {
		return location.getHostname();
	}
	
	public @Nullable Integer getPortNumber() {
		return location.getPortNumber();
	}
	
	public boolean usesSecureProtocol() {
		return location.usesSecureProtocol();
	}
	
	/**
	 * Constructs a URL based on stored server location properties and the given <code>path</code>.
	 *
	 * @param path A path string relative to the server's root. Must begin with a "<code>/</code>" character.
	 * @return the URL of a resource on the server at the given relative path.
	 * @throws MalformedURLException if the stored properties do not form a proper URL.
	 */
	public @NonNull URL getURL(@NonNull String path) throws MalformedURLException {
		return location.getURL(path);
	}
}
