package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Region;
import model.Type;

public class RegionTest {

	@Test
	public void testRegionEquality() {
		Region r = Region.getInstance(Type.Row, 1);
		Region r2 = Region.getInstance(Type.Row, 1);
		assertTrue(r == r2);
	}

}
