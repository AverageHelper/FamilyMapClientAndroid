package com.example.familymapclient.transport.login;

import android.content.Context;

import com.example.familymapclient.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public enum LoginFailureReason {
	MISSING_USERNAME,
	MISSING_PASSWORD,
	USER_NOT_FOUND,
	INCORRECT_PASSWORD;
	
	public @NonNull String getMessage(@Nullable Context context) {
		if (context == null) {
			return this.name();
		}
		switch (this) {
			case MISSING_USERNAME:
				return context.getString(R.string.login_error_missing_username);
			
			case MISSING_PASSWORD:
				return context.getString(R.string.login_error_missing_password);
				
			case USER_NOT_FOUND:
				return context.getString(R.string.login_error_user_not_found);
				
			case INCORRECT_PASSWORD:
				return context.getString(R.string.login_error_incorrect_password);
			
			default:
				return this.name();
		}
	}
}
