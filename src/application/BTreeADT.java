//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           B-Tree Abstract Data Type
// Files:           N/A
// Course:          CS400, Fall, 2019
//
// Author:          Joshua Rawlins
// Email:           jrawlins@wisc.edu
// Lecturer's Name: Andrew Kuemmel
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: N/A
// Partner Email: N/A
// Partner Lecturer's Name: N/A
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: None
// Online Sources: None 
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

package application;

public interface BTreeADT<K, V> {

  /**
   * Gets the size ....An empty tree returns 0.
   * 
   * @return total number of keys
   */
  public int getSize();

  /*
   * Adds a key-value pair into the tree
   * 
   * @param K - key
   * 
   * @param V - value
   * 
   * @throws DuplicateKeyException if the key has already been inserted into the tree
   * 
   * @throws IllegalKeyException if the key is null
   */
  void insert(K element, V value) throws DuplicateKeyException, IllegalKeyException;

  /*
   * Remove a specified key from a tree, along with its value.
   * 
   * @param K key - key to remove
   * 
   * @return boolean - true if the key-value pair was removed, false otherwise
   */
  boolean remove(K element);

  /*
   * contains(K key) navigates B-tree looking for the specified key.
   * 
   * @param key to be found
   * 
   * @return true if the key is in the tree and false otherwise
   */
  boolean contains(K element);

  /*
   * getValue(K key) searches the B-tree to find the Node with the specified key and output the
   * associated value.
   * 
   * @param K key
   * 
   * @returns V value
   * 
   * @see search(Node, T) private helper method for tree navigation
   * 
   * @throws IllegalKeyException if the key is not present in the tree
   */
  public V getValue(K key) throws IllegalKeyException;
}
