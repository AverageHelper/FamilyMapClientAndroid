package com.example.familymapclient.transport.register;

import android.content.Context;

import com.example.familymapclient.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public enum RegisterFailureReason {
	MISSING_USERNAME,
	MISSING_PASSWORD,
	MISSING_EMAIL,
	MISSING_FIRST_NAME,
	MISSING_LAST_NAME,
	MISSING_GENDER;
	
	public @NonNull String getMessage(@Nullable Context context) {
		if (context == null) {
			return this.name();
		}
		switch (this) {
			case MISSING_USERNAME:
				return context.getString(R.string.login_error_missing_username);
			
			case MISSING_PASSWORD:
				return context.getString(R.string.login_error_missing_password);
				
			case MISSING_EMAIL:
				return context.getString(R.string.login_error_missing_email);
				
			case MISSING_FIRST_NAME:
				return context.getString(R.string.login_error_missing_first_name);
				
			case MISSING_LAST_NAME:
				return context.getString(R.string.login_error_missing_last_name);
				
			case MISSING_GENDER:
				return context.getString(R.string.login_error_missing_gender);
				
			default:
				return this.name();
		}
	}
}
