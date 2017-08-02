package model;

public class Triple<E, T , V> {
	private E first;
	private T second;
	private V third;
	public Triple(E e, T t, V v){
		first = e;
		second = t;
		third = v;
	}
	public E getFirst(){
		return first;
	}
	public T getSecond(){
		return second;
	}
	public V getThird(){
		return third;
	}
	public String toString(){
		return first.toString()+" "+second.toString()+" "+third.toString();
	}
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(!(obj instanceof Triple<?,?,?>)) return false;
		Triple<?,?,?> p = (Triple<?,?,?>) obj;
		if(this.toString().equals(p.toString()))return true;
		else return false;
	}
}
