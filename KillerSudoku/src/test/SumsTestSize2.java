package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import model.Combination;
import model.Sums;

public class SumsTestSize2 {

	@Test
	public void testSumSize2Total3() {
		int length = 2;
		List<Combination> c = Sums.getSums(length, 3);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(2);
		assertTrue(c.get(0).getNumbers().equals(answer));
	}
	@Test
	public void testSumSize2Total4() {
		int length = 2;
		List<Combination> c = Sums.getSums(length, 4);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(3);
		assertTrue(c.get(0).getNumbers().equals(answer));
	}
	@Test
	public void testSumSize2Total5() {
		int total = 5;
		int length = 2;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(4);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(2);
		answer2.add(3);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		assertTrue(c.equals(answers));
	}
	@Test
	public void testSumSize2Total6() {
		int total = 6;
		int length = 2;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(5);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(2);
		answer2.add(4);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		assertTrue(c.equals(answers));
	}
	@Test
	public void testSumSize2Total7() {
		int total = 7;
		int length = 2;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(6);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(2);
		answer2.add(5);
		Set<Integer> answer3 = new TreeSet<>();
		answer3.add(3);
		answer3.add(4);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		Combination a3 = new Combination(total, answer3);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		answers.add(a3);
		assertTrue(c.equals(answers));
	}
	@Test
	public void testSumSize2Total10() {
		int total = 10;
		int length = 2;
		List<Combination> c = Sums.getSums(length, total);
		Set<Integer> answer = new TreeSet<>();
		answer.add(1);
		answer.add(9);
		Set<Integer> answer2 = new TreeSet<>();
		answer2.add(2);
		answer2.add(8);
		Set<Integer> answer3 = new TreeSet<>();
		answer3.add(3);
		answer3.add(7);
		Set<Integer> answer4 = new TreeSet<>();
		answer4.add(4);
		answer4.add(6);
		Combination a = new Combination(total,answer);
		Combination a2 = new Combination(total,answer2);
		Combination a3 = new Combination(total, answer3);
		Combination a4 = new Combination(total, answer4);
		List<Combination> answers = new ArrayList<>();
		answers.add(a);
		answers.add(a2);
		answers.add(a3);
		answers.add(a4);
		assertTrue(c.equals(answers));
	}
}
