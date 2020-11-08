package com.example.familymapclient.ui;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.familymapclient.auth.NonNullValueHandler;

import androidx.annotation.NonNull;

public class TextFieldListener implements TextWatcher {
	private final @NonNull NonNullValueHandler<String> valueHandler;
	
	public TextFieldListener(@NonNull NonNullValueHandler<String> valueHandler) {
		this.valueHandler = valueHandler;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	@Override
	public void afterTextChanged(Editable s) {
		if (s == null) {
			valueHandler.call("");
		} else {
			valueHandler.call(s.toString());
		}
	}
}
