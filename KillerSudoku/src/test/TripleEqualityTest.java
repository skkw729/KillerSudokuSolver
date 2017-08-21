package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Location;
import model.SudokuCell;
import model.Triple;

public class TripleEqualityTest {

	@Test
	public void testCellCellInt() {
		SudokuCell c1 = new SudokuCell(Location.getInstance(1, 1));
		SudokuCell c2 = new SudokuCell(Location.getInstance(1, 1));
		SudokuCell c3 = new SudokuCell(Location.getInstance(1, 2));
		SudokuCell c4 = new SudokuCell(Location.getInstance(1, 2));
		int i = 3;
		int j = 3;
		Triple<SudokuCell, SudokuCell, Integer> t1 = new Triple<>(c1, c3, i);
		Triple<SudokuCell, SudokuCell, Integer> t2 = new Triple<>(c2, c4, j);
		assertTrue(t1.equals(t2));
	}
	@Test
	public void testIntStringBoolean(){
		int i = 1;
		int j = 1;
		String s = "abc";
		String s2 = "abc";
		boolean b = true;
		boolean c = true;
		Triple<Integer, String, Boolean> t1 = new Triple<>(i, s, b);
		Triple<Integer, String, Boolean> t2 = new Triple<>(j, s2, c);
		assertTrue(t1.equals(t2));
	}
}
