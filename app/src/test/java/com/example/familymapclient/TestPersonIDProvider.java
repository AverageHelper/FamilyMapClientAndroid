package com.example.familymapclient;

import com.example.familymapclient.data.PersonIDProvider;

import androidx.annotation.Nullable;

public class TestPersonIDProvider implements PersonIDProvider {
	@Override
	public @Nullable String getPersonID() {
		return TestConstants.testPerson.getPersonID();
	}
}
