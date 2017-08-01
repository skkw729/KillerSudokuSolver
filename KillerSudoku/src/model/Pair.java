package model;

public class Pair<E, V> {
	private E e;
	private V v;
	public Pair(E e, V v){
		this.e = e;
		this.v = v;
	}
	public E getFirst(){
		return e;
	}
	public V getSecond(){
		return v;
	}
	public String toString(){
		return e.toString()+" "+v.toString();
	}
	public boolean equals(Object obj){
		if(this == obj) return true;
		if(!(obj instanceof Pair<?,?>)) return false;
		Pair<?,?> p = (Pair<?,?>) obj;
		if(e.equals(p.getFirst())&&v.equals(p.getSecond()))return true;
		else return false;
	}
}
