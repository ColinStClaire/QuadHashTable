import java.io.PrintStream;
import java.util.Iterator;
import java.util.ArrayList;


/**
 * @filename HashFrequencyTable
 * @author Colin St. Claire
 * @created 11/3/15
 * @modified 11/5/15
 */


public class HashFrequencyTable<K> implements FrequencyTable<K>, Iterable<K> {
	
	private class Entry {
		public K key; 
		public int count;
		public Entry(K k) {
			key = k;
			count = 1;
		}
	}
	
	
	private ArrayList<Entry> table;
	private float maxLoadFactor; // largest count/size ratio
	private int numEntries; 
	
	
	public HashFrequencyTable(int initialCapacity, float maxLoadFactor) {
		this.maxLoadFactor = maxLoadFactor;
		int size = nextPowerOfTwo(initialCapacity); // round to next power of 2
		table = new ArrayList<>(size); // make table 
		for (int i = 0; i < size; i++) {
			table.add(null); // fill with nulls
		}
		numEntries = 0;
	}
	
	
	private class TableIterator implements Iterator<K> {
		private int i;
		
		public TableIterator() {
			i = 0;
		}
		
		///// TableIterator() methods /////
		
		public boolean hasNext() {
			while (i < table.size() && table.get(i) == null) {
				i++;
			}
			return i < table.size();
		}
		
		
		public K next() {
			return table.get(i++).key;
		}
		
		
		public void remove() {
			throw new UnsupportedOperationException("Remove not supported");
		}
	}
	
	
	////// private methods //////
	
	private static int nextPowerOfTwo(int n) {
		// returns the next power of two i.e. given 10 return 16
		int e = 1;
		while((1 << e) < n) {
			e++;
		}
		return 1 << e;
	}
	
	
	private int hash(K key) {
		return key.hashCode() & (table.size() - 1);
	}
	
	
	private int reHash(int hash, int i) {
		int newHash = (hash + i * (i + 1) / 2) & (table.size() - 1);
		return newHash;
	}
	
	
	private float loadFactor() {
		return (float) numEntries / table.size();
	}
	
	
	private void doubleSizeAndRehash() {
		ArrayList<Entry> oldTable = table; // save old table
		int newSize = oldTable.size() << 1; // make new table double the size
		table = new ArrayList<>(newSize);
		for (int i = 0; i < newSize; i++) { // fill with nulls
			table.add(null);
		}
		numEntries = 0;
		for(int i = 0; i < oldTable.size(); i++) {
			Entry e = oldTable.get(i);
			if (e != null) {
				int hash = search(e.key);
				table.set(hash, e);
				numEntries++;
			}
		}
		return;
	}
	
	
	private int search(K key) {
		int hash = hash(key);
		Entry newEntry;
		int quadHash = hash;
		int i = 0; // counter
		while ((newEntry = table.get(quadHash)) != null) {
			if (key.equals(newEntry.key)) {
				return quadHash;
			}
			quadHash = reHash(hash, i); // get a new hash value
			i++;	
		}
		return quadHash;
	}
	
	
	////// public methods //////
	
	public int size() {
		return numEntries;
	}
	
	
	public void click(K key) {
		/*Click increments the integer counter associated with the 
		 * key if the key is already in the table; if the key is 
		 * not in table it is inserted and given an initial count of 1
		 */
		
		int index = search(key); // find index of key
		Entry newEntry = table.get(index); // get it
		
		if (newEntry == null) {
			//insert(key); // insert if it's not already in there
			newEntry = new Entry(key); // default count = 1
			table.set(index, newEntry); 
			numEntries++;
			if (loadFactor() >= maxLoadFactor) doubleSizeAndRehash();
		} else {
			newEntry.count++; // it's in there, increment count
		}
		
		if (loadFactor() >= maxLoadFactor) doubleSizeAndRehash();
		return;
	}

	
	public int count(K key) {
		/*Count returns the value of the counter associated with 
		 * the key – if the key is not in the table, 0 is returned
		 */
		int index = search(key); // find index of key
		Entry newEntry = table.get(index); // get it
		
		if (newEntry == null) {
			return 0;
		}
		return newEntry.count;
	}
	
	
	public Iterator<K> iterator() {
		return new TableIterator();
	}
	
	
	public void dump(PrintStream str) {
		Entry newEntry;
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i) != null) {
				newEntry = table.get(i);
				System.out.println(i + ": key = '" + newEntry.key + "', count = " + newEntry.count);
			} else {
				System.out.println(i + ": null");
			}
		}
	}
	
	
	public static void main(String[] args) {
		// simple unit test
		String hamlet = 
			"To be or not to be that is the question " +
			"Whether 'tis nobler in the mind to suffer " +
			"The slings and arrows of outrageous fortune ";
		
		String words[] = hamlet.split("\\s+");
		HashFrequencyTable<String> table = new HashFrequencyTable<String>(10, 0.95F);
		for (int i = 0; i < words.length; i++) {
			if (words[i].length() > 0) {
				table.click(words[i]);
			}
		}
		table.dump(System.out);
	}
}
