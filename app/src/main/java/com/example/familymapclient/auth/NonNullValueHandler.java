package com.example.familymapclient.auth;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface NonNullValueHandler<T> {
	void call(@NonNull T val);
}
