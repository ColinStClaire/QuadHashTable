FrequencyTable implemented with a HashTable
Author: Colin St. Claire colinst.claire@gmail.com
Modified: 11/5/15

Description:

    The FrequencyTable ADT, implemented in Java, supports:

	void click(K key);
	int count(K key);

    The click() method increments a count associated with
    the key value, and inserted any new keys. The count()
    method returns the count value associated with the key
    passed as the argument, returning 0 if no such key exists.
    
    This ADT is implemented on top of a HashTable that uses
    quadratic probing to help avoid clustering.

To build in Terminal:
    javac HashFrequencyTable.java

To run in Terminal:
    java HashFrequencyTable

To test:
    javac SortedWordCount.java
    java SortedWordCount

Contents:
README.txt~~~~~~~~~~~~~~~~~this file
HashFrequencyTable.java~~~~FrequencyTable with Quadratic Hash Table
SortedWordCount.java~~~~~~~A robust test 
FrequencyTable.java~~~~~~~~The FrequencyTable interface
mobydick-words.txt~~~~~~~~~Used in SortedWordCount test
