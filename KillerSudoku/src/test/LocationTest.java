package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Location;

public class LocationTest {

	@Test
	public void testLocationEquality() {
		Location l = Location.getInstance(1, 1);
		Location l2 = Location.getInstance(1, 1);
		assertTrue(l==l2);
	}

}
