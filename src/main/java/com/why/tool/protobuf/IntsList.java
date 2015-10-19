package com.why.tool.protobuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.protobuf.InvalidProtocolBufferException;
import com.why.tool.proto.ProtocolBuffer.IntList;

/**
 * 
 * @author luzj
 * 
 */
public class IntsList implements List<Integer>, ProtobufSerializable<IntList> {

	private List<Integer> elements;

	public IntsList() {
		elements = new ArrayList<Integer>();
	}

	public IntsList(byte[] bytes) {
		this();
		parseFrom(bytes);
	}

	@Override
	public void copyFrom(IntList list) {
		int size = list.getElementsCount();
		for (int i = 0; i < size; i++) {
			this.elements.add(list.getElements(i));
		}
	}

	@Override
	public IntList copyTo() {
		IntList.Builder builder = IntList.newBuilder();
		for (Integer element : elements) {
			builder.addElements(element);
		}
		return builder.build();
	}

	@Override
	public void parseFrom(byte[] bytes) {
		try {
			IntList list = IntList.parseFrom(bytes);
			copyFrom(list);
		} catch (InvalidProtocolBufferException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public byte[] toByteArray() {
		IntList list = copyTo();
		return list.toByteArray();
	}

	@Override
	public boolean add(Integer e) {
		return elements.add(e);
	}

	@Override
	public void add(int index, Integer element) {
		elements.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		return elements.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Integer> c) {
		return elements.addAll(index, c);
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public Integer get(int index) {
		return elements.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<Integer> iterator() {
		return elements.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	@Override
	public ListIterator<Integer> listIterator() {
		return elements.listIterator();
	}

	@Override
	public ListIterator<Integer> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public Integer remove(int index) {
		return elements.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	@Override
	public Integer set(int index, Integer element) {
		return elements.set(index, element);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public List<Integer> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		return (T[]) elements.toArray();
	}

}
