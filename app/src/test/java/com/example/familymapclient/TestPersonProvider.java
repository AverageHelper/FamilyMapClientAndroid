package com.example.familymapclient;

import com.example.familymapclient.data.DataProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Gender;
import model.Person;

public class TestPersonProvider implements DataProvider<String, Person> {
	
	@Override
	public @Nullable Person getValueWithID(@NonNull String s) {
		for (Person persons : persons) {
			if (persons.getId().equals(s)) {
				return persons;
			}
		}
		return null;
	}
	
	@Override
	public @NonNull Collection<Person> values() {
		return persons;
	}
	
	private final List<Person> persons = Arrays.asList(
		new Person(
			"Sheila_Parker",
			"sheila",
			"Sheila",
			"Parker",
			Gender.FEMALE,
			"Blaine_McGary",
			"Betty_White",
			"Davis_Hyer"
		),
		new Person(
			"Davis_Hyer",
			"sheila",
			"Davis",
			"Hyer",
			Gender.MALE,
			null,
			null,
			"Sheila_Parker"
		),
		new Person(
			"Blaine_McGary",
			"sheila",
			"Blaine",
			"McGary",
			Gender.MALE,
			"Ken_Rodham",
			"Mrs_Rodham",
			"Betty_White"
		),
		new Person(
			"Betty_White",
			"sheila",
			"Betty",
			"White",
			Gender.FEMALE,
			"Frank_Jones",
			"Mrs_Jones",
			"Blaine_McGary"
		),
		new Person(
			"Ken_Rodham",
			"sheila",
			"Ken",
			"Rodham",
			Gender.MALE,
			null,
			null,
			"Mrs_Rodham"
		),
		new Person(
			"Mrs_Rodham",
			"sheila",
			"Mrs",
			"Rodham",
			Gender.FEMALE,
			null,
			null,
			"Ken_Rodham"
		),
		new Person(
			"Frank_Jones",
			"sheila",
			"Frank",
			"Jones",
			Gender.MALE,
			null,
			null,
			"Mrs_Jones"
		),
		new Person(
			"Mrs_Jones",
			"sheila",
			"Mrs",
			"Jones",
			Gender.FEMALE,
			null,
			null,
			"Frank_Jones"
		),
		new Person(
			"Patrick_Spencer",
			"patrick",
			"Patrick",
			"Spencer",
			Gender.MALE,
			"Happy_Birthday",
			"Golden_Boy",
			null
		),
		new Person(
			"Happy_Birthday",
			"patrick",
			"Patrick",
			"Wilson",
			Gender.MALE,
			null,
			null,
			"Golden_Boy"
		),
		new Person(
			"Golden_Boy",
			"patrick",
			"Spencer",
			"Seeger",
			Gender.FEMALE,
			null,
			null,
			"Happy_Birthday"
		)
	);
	
}
