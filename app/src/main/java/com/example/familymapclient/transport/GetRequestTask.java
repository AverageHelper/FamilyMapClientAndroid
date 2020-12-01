package com.example.familymapclient.transport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class GetRequestTask extends RequestTask<GetRequest> {
	public GetRequestTask(
		@NonNull ServerLocation location,
		@Nullable String path,
		@Nullable GetRequest req
	) {
		super(location, path, req);
	}
}
