package com.example.familymapclient.auth;

import androidx.annotation.Nullable;

@FunctionalInterface
public interface NullableValueHandler<T> {
	public void call(@Nullable T val);
}
