package com.example.familymapclient.transport;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import transport.JSONSerialization;
import transport.MissingKeyException;

/**
 * An object that represents a server hostname, port number, and protocol.
 */
public class MutableServerLocation extends JSONSerialization {
	private @Nullable String hostname;
	private @Nullable Integer portNumber;
	private boolean usesSecureProtocol;
	
	public MutableServerLocation() {
		this.hostname = null;
		this.portNumber = null;
		this.usesSecureProtocol = false;
	}
	
	public MutableServerLocation(
		@Nullable String hostname,
		@Nullable Integer portNumber,
		boolean usesSecureProtocol
	) {
		this.hostname = hostname;
		this.portNumber = portNumber;
		this.usesSecureProtocol = usesSecureProtocol;
	}
	
	public MutableServerLocation(@NonNull ServerLocation location) {
		this.hostname = location.getHostname();
		this.portNumber = location.getPortNumber();
		this.usesSecureProtocol = location.usesSecureProtocol();
	}
	
	public @Nullable String getHostname() {
		return hostname;
	}
	
	public void setHostname(@Nullable String hostname) {
		this.hostname = hostname;
	}
	
	public @Nullable Integer getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber(@Nullable Integer portNumber) {
		this.portNumber = portNumber;
	}
	
	public boolean usesSecureProtocol() {
		return usesSecureProtocol;
	}
	
	public void setUsesSecureProtocol(boolean usesSecureProtocol) {
		this.usesSecureProtocol = usesSecureProtocol;
	}
	
	/**
	 * Constructs a URL based on stored server location properties and the given <code>path</code>.
	 *
	 * @param path A path string relative to the server's root. Must begin with a "<code>/</code>" character.
	 * @return the URL of a resource on the server at the given relative path.
	 * @throws MalformedURLException if the stored properties do not form a proper URL.
	 */
	public @NonNull URL getURL(@NonNull String path) throws MalformedURLException {
		String secure = usesSecureProtocol() ? "s" : "";
		@NonNull String hostName = "localhost";
		@NonNull Integer portNumber = usesSecureProtocol() ? 43 : 80;
		if (getHostname() != null) {
			hostName = getHostname();
		}
		if (getPortNumber() != null) {
			portNumber = getPortNumber();
		}
		return new URL("http" + secure + "://" + hostName + ":" + portNumber + path);
	}
	
	@Override
	public void assertCorrectDeserialization() throws MissingKeyException {}
	
	@Override
	public String toString() {
		String protocol = usesSecureProtocol
			? "https"
			: "http";
		return protocol + "://" + hostname + ":" + portNumber;
	}
}
