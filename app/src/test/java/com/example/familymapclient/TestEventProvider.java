package com.example.familymapclient;

import com.example.familymapclient.data.DataProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import model.Event;

public class TestEventProvider implements DataProvider<String, Event> {
	
	@Override
	public @Nullable Event getValueWithID(@NonNull String s) {
		for (Event event : events) {
			if (event.getId().equals(s)) {
				return event;
			}
		}
		return null;
	}
	
	@Override
	public @NonNull Collection<Event> values() {
		return events;
	}
	
	private final List<Event> events = Arrays.asList(
		new Event(
			"Sheila_Birth",
			"sheila",
			"Sheila_Parker",
			-36.1833,
			144.9667,
			"Australia",
			"Melbourne",
			"birth",
			1970
		),
		new Event(
			"Sheila_Marriage",
			"sheila",
			"Sheila_Parker",
			34.0500,
			-117.7500,
			"United States",
			"Los Angeles",
			"marriage",
			2012
		),
		new Event(
			"Sheila_Asteroids",
			"sheila",
			"Sheila_Parker",
			77.4667,
			-68.7667,
			"Denmark",
			"Qaanaaq",
			"completed asteroids",
			2014
		),
		new Event(
			"Other_Asteroids",
			"sheila",
			"Sheila_Parker",
			74.4667,
			-60.7667,
			"Denmark",
			"Qaanaaq",
			"COMPLETED ASTEROIDS",
			2014
		),
		new Event(
			"Sheila_Death",
			"sheila",
			"Sheila_Parker",
			40.2444,
			111.6608,
			"China",
			"Hohhot",
			"death",
			2015
		),
		new Event(
			"Davis_Birth",
			"sheila",
			"Davis_Hyer",
			41.7667,
			140.7333,
			"Japan",
			"Hakodate",
			"birth",
			1970
		),
		new Event(
			"Blaine_Birth",
			"sheila",
			"Blaine_McGary",
			56.1167,
			101.6000,
			"Russia",
			"Bratsk",
			"birth",
			1948
		),
		new Event(
			"Betty_Death",
			"sheila",
			"Betty_White",
			52.4833,
			-0.1000,
			"United Kingdom",
			"Birmingham",
			"death",
			2017
		),
		new Event(
			"BYU_graduation",
			"sheila",
			"Ken_Rodham",
			40.2338,
			-111.6585,
			"United States",
			"Provo",
			"Graduated from BYU",
			1879
		),
		new Event(
			"Rodham_Marriage",
			"sheila",
			"Ken_Rodham",
			39.15,
			127.45,
			"North Korea",
			"Wonsan",
			"marriage",
			1895
		),
		new Event(
			"Mrs_Rodham_Backflip",
			"sheila",
			"Mrs_Rodham",
			32.6667,
			-114.5333,
			"Mexico",
			"Mexicali",
			"Did a backflip",
			1890
		),
		new Event(
			"Mrs_Rodham_Java",
			"sheila",
			"Mrs_Rodham",
			36.7667,
			3.2167,
			"Algeria",
			"Algiers",
			"learned Java",
			1890
		),
		new Event(
			"Jones_Frog",
			"sheila",
			"Frank_Jones",
			25.0667,
			-76.6667,
			"Bahamas",
			"Nassau",
			"Caught a frog",
			1993
		),
		new Event(
			"Jones_Marriage",
			"sheila",
			"Frank_Jones",
			9.4,
			0.85,
			"Ghana",
			"Tamale",
			"marriage",
			1997
		),
		new Event(
			"Mrs_Jones_Barbecue",
			"sheila",
			"Mrs_Jones",
			-24.5833,
			-48.75,
			"Brazil",
			"Curitiba",
			"Ate Brazilian Barbecue",
			2012
		),
		new Event(
			"Mrs_Jones_Surf",
			"sheila",
			"Mrs_Jones",
			-27.9833,
			153.4,
			"Australia",
			"Gold Coast",
			"Learned to Surf",
			2000
		),
		new Event(
			"Thanks_Woodfield",
			"patrick",
			"Patrick_Spencer",
			76.4167,
			-81.1,
			"Canada",
			"Grise Fiord",
			"birth",
			2016
		),
		new Event(
			"True_Love",
			"patrick",
			"Happy_Birthday",
			43.6167,
			-115.8,
			"United States",
			"Boise",
			"marriage",
			2016
		),
		new Event(
			"Together_Forever",
			"patrick",
			"Golden_Boy",
			43.6167,
			-115.8,
			"United States",
			"Boise",
			"marriage",
			2016
		)
	);
	
}
