package test;

import model.Pair;

public class PairTest {
	
	public static void main(String[] args){
		Pair<Integer, String> pair1 = new Pair(1, "a");
		Pair<Integer, String> pair2 = new Pair(1, "a");
		System.out.println(pair1.equals(pair2));
	}
}
