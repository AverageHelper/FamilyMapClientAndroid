package com.example.familymapclient;

import model.Gender;
import model.Person;
import model.User;

public class TestConstants {
	private TestConstants() {}
	
	public static final User sheilaParker = new User(
		"sheila",
		"parker",
		"sheila@parker.com",
		"Sheila",
		"Parker",
		Gender.FEMALE,
		"Sheila_Parker"
	);
	
	public static final User patrickSpencer = new User(
		"patrick",
		"spencer",
		"patrick@spencer.com",
		"Patrick",
		"Spencer",
		Gender.MALE,
		"Patrick_Spencer"
	);
	
	public static final Person testPerson = new Person(
		"Sheila_Parker",
		"sheila",
		"Sheila",
		"Parker",
		Gender.FEMALE,
		"Blaine_McGary",
		"Betty_White",
		"Davis_Hyer"
	);
}
