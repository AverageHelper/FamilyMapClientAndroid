package com.example.familymapclient.auth;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface NonNullValueHandler<T> {
	public void call(@NonNull T val);
}
