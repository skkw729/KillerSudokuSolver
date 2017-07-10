import java.util.*;
public class Sums {
	private static List<Integer> NUMBERS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

	public static Set<Set<Integer>> getSums(int length, int total){
		Set<Set<Integer>> solutions = new TreeSet<>();
		getSumRecursively(length, total, new TreeSet<>(), solutions);
		return solutions;
	}
	private static void getSumRecursively(int length, int total, Set<Integer> numbersUsed, Set<Set<Integer>> solutions){
		int iteration = 1;
		int lengthRemaining = length - numbersUsed.size();//iterations remaining
		int current = 0;//current total of numbers used
		if(numbersUsed.size()>0){
			for(Integer number: numbersUsed){
				current += number;
			}
		}
		if(lengthRemaining==0){
			if(current == total){
				solutions.add(numbersUsed);
			}
			return;
		}
		if(lengthRemaining == length) {
			for(Integer i : NUMBERS){
				numbersUsed = new TreeSet<>();
				if(i < total){
					numbersUsed.add(i);
					getSumRecursively(length, total, numbersUsed, solutions);
				}
			}
			return;
		}
		if(lengthRemaining > 0){
			for(Integer j : NUMBERS){
				if(numbersUsed.contains(j)){
					return;
				}
				numbersUsed.add(j);
				getSumRecursively(length, total, numbersUsed, solutions);
			}
			return;
		}
	}
}
