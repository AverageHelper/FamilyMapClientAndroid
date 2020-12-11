package com.example.familymapclient;

import com.example.familymapclient.data.DataFilter;
import com.example.familymapclient.data.DataProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Person;

import static org.junit.jupiter.api.Assertions.*;

public class DataFilterTest {
	private Person root;
	private DataProvider<String, Person> testPersonProvider;
	private DataFilter dataFilter;
	
	@BeforeEach
	void setUp() {
		testPersonProvider = new TestPersonProvider();
		root = testPersonProvider.getValueWithID(TestConstants.sheilaParker.getPersonID());
		dataFilter = new DataFilter(testPersonProvider);
	}
	
	
	// ** Immediate Family
	
	@Test
	public void test_motherOnMothersSide() {
		Person mother = testPersonProvider.getValueWithID(root.getMotherID());
		assertTrue(dataFilter.personIsOnMothersSide(root, mother));
	}
	
	@Test
	public void test_fatherNotOnMothersSide() {
		Person father = testPersonProvider.getValueWithID(root.getFatherID());
		assertFalse(dataFilter.personIsOnMothersSide(root, father));
	}
	
	@Test
	public void test_fatherOnFathersSide() {
		Person father = testPersonProvider.getValueWithID(root.getFatherID());
		assertTrue(dataFilter.personIsOnFathersSide(root, father));
	}
	
	@Test
	public void test_motherNotOnFathersSide() {
		Person mother = testPersonProvider.getValueWithID(root.getMotherID());
		assertFalse(dataFilter.personIsOnFathersSide(root, mother));
	}
	
	
	// ** Grandparents
	
	@Test
	public void test_paternalGrandfatherOnFathersSide() {
		Person person = testPersonProvider.getValueWithID("Ken_Rodham");
		assertTrue(dataFilter.personIsOnFathersSide(root, person));
	}
	
	@Test
	public void test_paternalGrandfatherNotOnMothersSide() {
		Person person = testPersonProvider.getValueWithID("Ken_Rodham");
		assertFalse(dataFilter.personIsOnMothersSide(root, person));
	}
	
	@Test
	public void test_maternalGrandmotherNotOnFathersSide() {
		Person person = testPersonProvider.getValueWithID("Mrs_Jones");
		assertFalse(dataFilter.personIsOnFathersSide(root, person));
	}
	
	@Test
	public void test_maternalGrandmotherOnMothersSide() {
		Person person = testPersonProvider.getValueWithID("Mrs_Jones");
		assertTrue(dataFilter.personIsOnMothersSide(root, person));
	}
}