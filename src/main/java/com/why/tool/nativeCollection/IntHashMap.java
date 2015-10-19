package com.why.tool.nativeCollection;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 定制特殊的hashMap
 * 
 * 使用int原始类型做为key 以提高大量使用map时的性能
 * 
 * 不支持keySet方法 因为key为原始int类型
 * 
 * @author luzj
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class IntHashMap<T> {

	private static final int DEFAULT_INITIAL_CAPACITY = 32;

	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private static final int MAXIMUM_CAPACITY = 1 << 30;

	private transient Entry[] table;

	private transient int size;

	private int threshold;

	private final float loadFactor;
	
	private transient volatile int modCount;

	public static class Entry<T> {
		final int hash;
		final int key;// hash also key, not used key
		T value;
		Entry<T> next;

		Entry(int hash, int key, T value, Entry<T> next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}

		public int getKey() {
			return key;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
		
	}

	public IntHashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public IntHashMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	public IntHashMap(int initialCapacity, float loadFactor) {
		super();
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
		}
		if (loadFactor <= 0) {
			throw new IllegalArgumentException("Illegal Load: " + loadFactor);
		}
		if (initialCapacity == 0) {
			initialCapacity = 1;
		}
		if (initialCapacity > MAXIMUM_CAPACITY) {
			initialCapacity = MAXIMUM_CAPACITY;
		}

		this.loadFactor = loadFactor;
		table = new Entry[initialCapacity];
		threshold = (int) (initialCapacity * loadFactor);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean contains(Object value) {
		if (value == null) {
			return containsNullValue();
		}

		Entry[] tab = table;
		for (int i = tab.length; i-- > 0;) {
			for (Entry<T> e = tab[i]; e != null; e = e.next) {
				if (e.value.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean containsNullValue() {
		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++)
			for (Entry e = tab[i]; e != null; e = e.next)
				if (e.value == null)
					return true;
		return false;
	}

	final Entry<T> getEntry(int key) {
		int hash = key;
		for (Entry<T> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
			if (e.hash == hash)
				return e;
		}
		return null;
	}

	public boolean containsKey(int key) {
		return getEntry(key) != null;
	}

	static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	public T get(int key) {
		Entry tab[] = table;
		int hash = key;
		int index = indexFor(hash, table.length);
		for (Entry<T> e = tab[index]; e != null; e = e.next) {
			if (e.hash == hash) {
				return e.value;
			}
		}
		return null;
	}

	void resize(int newCapacity) {
		Entry[] oldTable = table;
		int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry[] newTable = new Entry[newCapacity];
		transfer(newTable);
		table = newTable;
		threshold = (int) (newCapacity * loadFactor);
	}

	void transfer(Entry[] newTable) {
		Entry[] src = table;
		int newCapacity = newTable.length;
		for (int j = 0; j < src.length; j++) {
			Entry<T> e = src[j];
			if (e != null) {
				src[j] = null;
				do {
					Entry<T> next = e.next;
					int i = indexFor(e.hash, newCapacity);
					e.next = newTable[i];
					newTable[i] = e;
					e = next;
				} while (e != null);
			}
		}
	}

	public T put(int key, T value) {
		Entry tab[] = table;
		int hash = key;
		int index = indexFor(hash, table.length);
		for (Entry<T> e = tab[index]; e != null; e = e.next) {
			if (e.hash == hash) {
				T old = e.value;
				e.value = value;
				return old;
			}
		}

		modCount++;
		addEntry(hash, key, value, index);
		return null;
	}

	void addEntry(int hash, int key, T value, int bucketIndex) {
		Entry<T> e = table[bucketIndex];
		table[bucketIndex] = new Entry<T>(hash, key, value, e);
		if (++size > threshold) {
			resize(2 * table.length);
		}
	}

	public boolean containsValue(Object value) {
		return contains(value);
	}

	public T remove(int key) {
		Entry tab[] = table;
		int hash = key;
		int index = indexFor(hash, table.length);
		for (Entry<T> e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (e.hash == hash) {
				modCount ++;
				if (prev != null) {
					prev.next = e.next;
				} else {
					tab[index] = e.next;
				}
				size--;
				T oldValue = e.value;
				e.value = null;
				return oldValue;
			}
		}
		return null;
	}

	public void clear() {
		modCount ++;
		Entry<T> tab[] = table;
		for (int index = tab.length; --index >= 0;) {
			tab[index] = null;
		}
		size = 0;
	}
	
	transient volatile Collection<T> values = null;
	
    public Collection<T> values() {
        Collection<T> vs = values;
        return (vs != null ? vs : (values = new Values()));
    }
    
    private final class Values extends AbstractCollection<T> {
        public Iterator<T> iterator() {
            return new ValueIterator();
        }
        public int size() {
            return size;
        }
        public boolean contains(Object o) {
            return containsValue(o);
        }
        public void clear() {
            IntHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends IntHashIterator<T>{
    	@Override
    	public T next() {
    		return nextEntry().value;
    	}
    }
    
    private abstract class IntHashIterator<V> implements Iterator<V> {
    	Entry<T> next;
        int expectedModCount;
        int index;
        Entry<T> current;

        IntHashIterator() {
            expectedModCount = modCount;
            if (size > 0) {
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Entry<T> nextEntry() {
        	if (modCount != expectedModCount)
        		throw new ConcurrentModificationException();
        	Entry<T> e = next;
        	if (e == null)
        		throw new NoSuchElementException();

        	if ((next = e.next) == null) {
        		Entry[] t = table;
        		while (index < t.length && (next = t[index++]) == null)
        			;
        	}
        	current = e;
        	return e;
        }

        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            int k = current.key;
            current = null;
            IntHashMap.this.remove(k);
            expectedModCount = modCount;
        }

    }
    
    private transient Set<Entry<T>> entrySet = null;
    
    public Set<Entry<T>> entrySet() {
	    Set<Entry<T>> es = entrySet;
	    return es != null ? es : (entrySet = new EntrySet());
    }

    private final class EntrySet extends AbstractSet<Entry<T>> {
        public Iterator<Entry<T>> iterator() {
            return new EntryIterator();
        }
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            
            Entry<T> e = (Entry<T>) o;
            Entry<T> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }
        
        public boolean remove(Object o) {
            if (!(o instanceof Entry))
                return false;
            
            return IntHashMap.this.remove(((Entry<T>) o ).key) != null;
        }
        public int size() {
            return size;
        }
        
        public void clear() {
            IntHashMap.this.clear();
        }
    }
    
    private final class EntryIterator extends IntHashIterator<Entry<T>>{
		@Override
		public Entry<T> next() {
			return nextEntry();
		}
    }
    
}
