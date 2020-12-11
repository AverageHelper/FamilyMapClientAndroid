package com.example.familymapclient;

import com.example.familymapclient.data.EventCache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import model.Event;

import static org.junit.jupiter.api.Assertions.*;

class EventCacheTest {
	
	private EventCache cache;
	
	@BeforeEach
	void setUp() {
		cache = EventCache.shared();
	}
	
	@Test
	void test_newCacheIsEmpty() {
		assertTrue(cache.isEmpty());
		assertTrue(cache.values().isEmpty());
	}
	
	@Test
	void test_lifeEvents() {
		cache.addAll(new TestEventProvider().values());
		List<Event> lifeEvents = cache.lifeEventsForPerson(TestConstants.sheilaParker.getPersonID());
		assertEquals(5, lifeEvents.size());
	}
}