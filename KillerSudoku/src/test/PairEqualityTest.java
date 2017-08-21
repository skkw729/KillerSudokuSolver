package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.*;

import org.junit.Test;

public class PairEqualityTest {

	@Test
	public void testCageRegion() {
		Cage c = new Cage(1, new ArrayList<Location>());
		Region r = Region.getInstance(Type.Row, 1);
		Region r2 = Region.getInstance(Type.Row, 1);
		Pair<Cage, Region> p1 = new Pair<>(c, r);
		Pair<Cage, Region> p2 = new Pair<>(c, r2);
		assertTrue(p1.equals(p2));
	}
	@Test
	public void testIntString(){
		int i = 1;
		int j = 1;
		String s = "abc";
		String s2 = "abc";
		Pair<Integer, String> pair1 = new Pair<>(i, s);
		Pair<Integer, String> pair2 = new Pair<>(j, s2);
		assertTrue(pair1.equals(pair2));
	}

}
