package com.example.familymapclient.ui;

import android.widget.CompoundButton;

import com.example.familymapclient.auth.NonNullValueHandler;

import androidx.annotation.NonNull;

public class CheckboxListener implements CompoundButton.OnCheckedChangeListener {
	private final @NonNull NonNullValueHandler<Boolean> valueHandler;
	
	public CheckboxListener(@NonNull NonNullValueHandler<Boolean> valueHandler) {
		this.valueHandler = valueHandler;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		valueHandler.call(isChecked);
	}
}
