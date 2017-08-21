package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import model.Combination;
import model.Sums;

public class SumsTestSize3 {

	@Test
	public void testSumSize3Total3() {
		int length = 3;
		List<Combination> c = Sums.getSums(length, 3);
		assertTrue(c.isEmpty());//no combination possible
	}
	@Test
	public void testSumSize3Total6() {
		int length = 3;
		int total = 6;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(2);
		answer.add(3);
		assertTrue(c.get(0).getNumbers().equals(answer));
	}
	@Test
	public void testSumSize3Total8() {
		int total = 8;
		int length = 3;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(2);
		answer.add(5);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(1);
		answer2.add(3);
		answer2.add(4);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		assertTrue(c.equals(answers));
	}
	@Test
	public void testSumSize3Total9() {
		int total = 9;
		int length = 3;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(2);
		answer.add(6);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(1);
		answer2.add(3);
		answer2.add(5);
		Set<Integer> answer3 = new TreeSet<>();
		answer3.add(2);
		answer3.add(3);
		answer3.add(4);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		Combination a3 = new Combination(total,answer3);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		answers.add(a3);
		assertTrue(c.equals(answers));
	}
	@Test
	public void testSumSize3Total10() {
		int total = 10;
		int length = 3;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(2);
		answer.add(7);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(1);
		answer2.add(3);
		answer2.add(6);
		Set<Integer> answer3 = new TreeSet<>();
		answer3.add(1);
		answer3.add(4);
		answer3.add(5);
		Set<Integer> answer4 = new TreeSet<>();
		answer4.add(2);
		answer4.add(3);
		answer4.add(5);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		Combination a3 = new Combination(total,answer3);
		Combination a4 = new Combination(total,answer4);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		answers.add(a3);
		answers.add(a4);
		assertTrue(c.equals(answers));
	}
	@Test
	public void testSumSize3Total15() {
		int total = 15;
		int length = 3;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(5);
		answer.add(9);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(1);
		answer2.add(6);
		answer2.add(8);
		Set<Integer> answer3 = new TreeSet<>();
		answer3.add(2);
		answer3.add(4);
		answer3.add(9);
		Set<Integer> answer4 = new TreeSet<>();
		answer4.add(2);
		answer4.add(5);
		answer4.add(8);
		Set<Integer> answer5 = new TreeSet<>();
		answer5.add(2);
		answer5.add(6);
		answer5.add(7);
		Set<Integer> answer6 = new TreeSet<>();
		answer6.add(3);
		answer6.add(4);
		answer6.add(8);
		Set<Integer> answer7 = new TreeSet<>();
		answer7.add(3);
		answer7.add(5);
		answer7.add(7);
		Set<Integer> answer8 = new TreeSet<>();
		answer8.add(4);
		answer8.add(5);
		answer8.add(6);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		Combination a3 = new Combination(total,answer3);
		Combination a4 = new Combination(total,answer4);
		Combination a5 = new Combination(total,answer5);
		Combination a6 = new Combination(total,answer6);
		Combination a7 = new Combination(total,answer7);
		Combination a8 = new Combination(total,answer8);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		answers.add(a3);
		answers.add(a4);
		answers.add(a5);
		answers.add(a6);
		answers.add(a7);
		answers.add(a8);
		assertTrue(c.equals(answers));
	}
}
