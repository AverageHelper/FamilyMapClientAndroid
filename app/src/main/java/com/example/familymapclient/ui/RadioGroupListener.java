package com.example.familymapclient.ui;

import android.widget.RadioGroup;

import com.example.familymapclient.auth.NonNullValueHandler;

import androidx.annotation.NonNull;

public class RadioGroupListener implements RadioGroup.OnCheckedChangeListener {
	private final @NonNull NonNullValueHandler<Integer> valueHandler;
	
	public RadioGroupListener(@NonNull NonNullValueHandler<Integer> valueHandler) {
		this.valueHandler = valueHandler;
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		valueHandler.call(checkedId);
	}
}
