import java.util.*;
public class Sums {
	private static List<Integer> LIST = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	private static Set<Integer> NUMBERS = new TreeSet<>(LIST);
	

	public static List<Set<Integer>> getSums(int length, int total, Set<Integer> possibleNumbers){
		List<Set<Integer>> solutions = new ArrayList<>();
		getSumRecursively(length, total, possibleNumbers, new TreeSet<>(), solutions);
		return solutions;
	}
	private static void getSumRecursively(int length, int total, Set<Integer> possibleNumbers, Set<Integer> numbersUsed, List<Set<Integer>> solutions){
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
				if(!solutions.contains(numbersUsed)){solutions.add(numbersUsedCopy);}
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
	public static List<Set<Integer>> getSums(int length, int total){
		return getSums(length, total, NUMBERS);
	}
}
