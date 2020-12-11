package com.example.familymapclient;

import com.example.familymapclient.data.PersonCache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonCacheTest {
	
	private PersonCache cache;
	
	@BeforeEach
	void setUp() {
		cache = PersonCache.shared();
	}
	
	@Test
	void test_newCacheIsEmpty() {
		assertTrue(cache.isEmpty());
		assertTrue(cache.values().isEmpty());
	}
}
