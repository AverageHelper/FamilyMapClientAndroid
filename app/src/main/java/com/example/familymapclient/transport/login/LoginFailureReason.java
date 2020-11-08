package com.example.familymapclient.transport.login;

import android.content.Context;

import com.example.familymapclient.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public enum LoginFailureReason {
	MISSING_USERNAME,
	MISSING_PASSWORD;
	
	public @NonNull String getMessage(@Nullable Context context) {
		if (context == null) {
			return this.name();
		}
		switch (this) {
			case MISSING_USERNAME:
				return context.getString(R.string.login_error_missing_username);
			
			case MISSING_PASSWORD:
				return context.getString(R.string.login_error_missing_password);
			
			default:
				return this.name();
		}
	}
}
