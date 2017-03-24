package com.why.tool.nativeCollection;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.RandomAccess;

public class SortedList<E> extends AbstractList<E> {

	public static final SortedList<Object> EMPTY_LIST = new EmptyList();

	private static final class EmptyList extends SortedList<Object> implements RandomAccess, Serializable {
		// use serialVersionUID from JDK 1.2.2 for interoperability
		private static final long serialVersionUID = 8842843931221139166L;

		private EmptyList(){
			super(null);
		}
		
		public int size() {
			return 0;
		}

		public boolean contains(Object obj) {
			return false;
		}

		public Object get(int index) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}

		// Preserves singleton property
		private Object readResolve() {
			return EMPTY_LIST;
		}
	}

	private ArrayList<E> internalList;
	private final Comparator<E> comparator;
	
	public SortedList(Comparator<E> comparator){
		this.comparator = comparator;
		this.internalList = new ArrayList<E>();
	}
	
	public SortedList(Comparator<E> comparator, Collection<? extends E> c) {
		this.comparator = comparator;
		this.internalList = new ArrayList<E>(c);
		this.sort();
	}

	// Note that add(E e) in AbstractList is calling this one
	@Override
	public void add(int position, E e) {
		internalList.add(e);
		sort();
	}
	
	private void sort(){
		Collections.sort(internalList, comparator);
	}
	
	@Override
	public E get(int i) {
		return internalList.get(i);
	}

	@Override
	public int size() {
		return internalList.size();
	}
	
	@Override
	public boolean remove(Object o) {
		return internalList.remove(o);
	}

	@SuppressWarnings("unchecked")
	public static final <E> SortedList<E> emptyList() {
		return (SortedList<E>) EMPTY_LIST;
	}

}