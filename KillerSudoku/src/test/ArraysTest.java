package test;

import java.util.Arrays;
import java.util.List;

public class ArraysTest {
	private static int[] numbers = {1, 2, 3, 4, 5};
	private static int[] numbers2 = {2, 3, 4};
	public static void main(String[] args){
		List<int[]> list1 = Arrays.asList(numbers);
		List<int[]> list2 = Arrays.asList(numbers2);
	}
	
}
