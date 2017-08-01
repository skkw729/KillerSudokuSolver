package model;
import java.util.*;
public class Sums {
	private static List<Integer> LIST = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	private static Set<Integer> NUMBERS = new TreeSet<>(LIST);
	

	public static List<Combination> getSums(int length, int total, Set<Integer> possibleNumbers){
		List<Combination> solutions = new ArrayList<>();
		if(length==1) {
			Set<Integer> used = new TreeSet<>();
			used.add(total);
			solutions.add(new Combination(total, used));
			return solutions;
		}
		getSumRecursively(length, total, possibleNumbers, new TreeSet<>(), solutions);
		return solutions;
	}
	private static void getSumRecursively(int length, int total, Set<Integer> possibleNumbers, Set<Integer> numbersUsed, List<Combination> solutions){
		int lengthRemaining = length - numbersUsed.size();//iterations remaining
		int current = 0;//current total of numbers used
		if(numbersUsed.size()>0){
			for(Integer number: numbersUsed){
				current += number;
			}
		}
		if(lengthRemaining==0){
			if(current == total){
				Set<Integer> numbersUsedCopy = new TreeSet<>();
				//copy contents of the set
				for(Integer number : numbersUsed){
					numbersUsedCopy.add(number);
				}
				Combination combination = new Combination(total,numbersUsedCopy);
				if(!solutions.contains(combination)){solutions.add(combination);}//new combination
			}
			return;
		}
		if(lengthRemaining == length) {
			for(Integer i : possibleNumbers){
				numbersUsed = new TreeSet<>();
				if(i < total){
					numbersUsed.add(i);
					getSumRecursively(length, total, possibleNumbers, numbersUsed, solutions);
				}
			}
			return;
		}
		if(lengthRemaining > 0){
			for(Integer j : possibleNumbers){
				if(!numbersUsed.contains(j)){
					numbersUsed.add(j);
					getSumRecursively(length, total, possibleNumbers, numbersUsed, solutions);
					numbersUsed.remove(j);
				}
			}
			return;
		}
	}
//	public static List<Combination> getSumsWithDupes(int length, int total){
//		
//	}
//	public static List<Combination> getSumswithDupesRecur(int legnth, int total, List<Integer> numbersUsed, List<Combination> solutions){
//		
//	}
	public static List<Combination> getSums(int length, int total){
		return getSums(length, total, NUMBERS);
	}
}
