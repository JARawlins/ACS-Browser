//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           B-Tree Data Structure
// Files:           BTreeADT
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
// Online Sources: None outside of standard course resources.
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTree<K extends Comparable<K>, V> implements BTreeADT<K, V> {
  /******** BTree Variables *********/
  // minDegree = the minimum number of children that a Node may have
  // minKeys = the minimum permissible number of keys in a Node
  // maxKeys = the maximum permissible number of keys in a Node
  int minDegree, minKeys, maxKeys;
  int totalKeys = 0;
  Node root;

  /********* Tree Constructor *********/
  public BTree() {
    this.root = new Node();
    this.root.numKeys = 0;
    this.root.isLeaf = true;
    this.minDegree = 2;
    this.minKeys = minDegree - 1;
    this.maxKeys = 2 * minDegree - 1;
  }

  /*********** Node Inner Class **************/
  private class Node {
    int numKeys; // number of children for an interior Node
    List<K> keys;
    Map<K, V> objectMapping; // The time complexity is determined by maxKeys, not N.
    // worst case for map is O(logN + maxKeys), which is just O(logN) because maxKeys is a constant
    List<Node> children;
    boolean isLeaf;

    // default Node constructor
    public Node() {
      this.numKeys = 0;
      this.isLeaf = true;
      this.keys = new ArrayList<K>();
      this.objectMapping = new HashMap<K, V>();
      this.children = new ArrayList<Node>();
    }

    // constructor specifying whether a Node is internal
    public Node(boolean isLeaf, int numKeys) {
      this.numKeys = numKeys;
      this.isLeaf = isLeaf;
      this.keys = new ArrayList<K>();
      this.objectMapping = new HashMap<K, V>();
      this.children = new ArrayList<Node>();
    }
  }

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
  public void insert(K key, V value) throws DuplicateKeyException, IllegalKeyException {
    // null key -> exception
    if (key == null) {
      throw new IllegalKeyException("Null keys are not allowed.");
    }
    // duplicate key -> exception
    if (contains(key)) {
      throw new DuplicateKeyException("You cannot imput a key already in the tree");
    }
    // recursively insert
    this.root = insert(this.root, key, value);
    this.totalKeys++;
  }

  /*
   * Recursive private helper method to navigate the tree and insert at the appropriate location.
   * Splits are made whenever a full Node is encountered along the path of insertion.
   * 
   * @param Node current
   * 
   * @param K key
   * 
   * @param V value
   * 
   * @returns Node current
   */
  private Node insert(Node current, K key, V value) {
    int i = 0, j;
    // insertion into a brand new tree with no keys
    if (current.keys.size() == 0) {
      current.keys.add(key);
      current.objectMapping.put(key, value);
      current.numKeys++;
      return current;
    }
    // splitting and insertion into a full root with no children
    if ((current.equals(this.root) && current.numKeys == this.maxKeys
        && childrenCounter(current) == 0)) {
      current = splitRoot(current, key);
      insert(current, key, value);
    }
    // cases: a. current is a full root with children
    // b. any current Node which is not full
    // c. current is a full interior Node
    else if ((current.equals(this.root) && current.numKeys == this.maxKeys
        && childrenCounter(current) != 0) || current.numKeys < this.maxKeys
        || (current.numKeys == this.maxKeys && !current.isLeaf)) {
      // increment i until the appropriate location is found for splitting/insertion to
      // maintain ordering
      while (i < current.numKeys && key.compareTo(current.keys.get(i)) > 0) {
        i++;
      }
      // insert into a non-full at the end of the recursive probe of the tree structure
      if (current.isLeaf) {
        current.keys.add(i, key);
        current.objectMapping.put(key, value);
        current.numKeys++;
      }
      // current is not a leaf
      else {
        // check whether the next child in the path of the recursion is full
        if (current.children.get(i).keys.size() == this.maxKeys) {
          // the child is full AND current is a full root
          // split the root to add an additional row of height and re-call insert starting from the
          // new root
          if (current.equals(this.root) && current.numKeys >= this.maxKeys) {
            current = splitRoot(current, key);
            insert(current, key, value);
          }
          // otherwise, split the full child
          else {
            split(current, i, key);
            j = 0;
            // recompute the index for insertion, accounting for the updated tree structure
            while (j < current.numKeys && key.compareTo(current.keys.get(j)) > 0) {
              j++;
            }
            // recursively insert
            insert(current.children.get(j), key, value);
          }
        }
        // the designated child of current is not full, so recursively insert into it
        else {
          insert(current.children.get(i), key, value);
        }
      }
    }
    return current;
  }

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
  public V getValue(K key) throws IllegalKeyException {
    Node temp;
    int i = 0;
    K targetKey;
    // get the Node containing the key (or null)
    temp = search(this.root, key);
    // not present
    if (temp == null) {
      throw new IllegalKeyException("Key not found in tree");
    }
    // get index in key list
    while (i < temp.numKeys && key.compareTo(temp.keys.get(i)) > 0) {
      i++;
    }
    // return discovered value
    targetKey = temp.keys.get(i);
    return temp.objectMapping.get(targetKey);
  }

  /*
   * Remove a specified key from a tree, along with its value.
   * 
   * @param K key - key to remove
   * 
   * @return boolean - true if the key-value pair was removed, false otherwise
   */
  public boolean remove(K key) {
    // case of removal from a leaf root with one key
    if (this.root.keys.contains(key) && this.root.numKeys == 1 && this.root.isLeaf) {
      this.root.keys.remove(key);
      this.totalKeys--;
      return true;
    }
    // the key was not found
    if (remove(this.root, key) == null) {
      return false;
    }
    // the key was found and removed
    else {
      this.totalKeys--;
      return true;
    }
  }

  /*
   * Remove recursive, helper method.
   * 
   * @return Node current
   * 
   * @param Node current
   * 
   * @return K key
   */
  private Node remove(Node current, K key) {
    int i = 0, indexKey, oldChildKeys, newChildKeys;
    Node child, min, max, merged;
    K childKey;
    V childValue;
    // key found
    if (current.keys.contains(key)) {
      // leaf case (1)
      // because of the preemptive split just before the leaf is passed,
      // there is no concern about leaving an empty node after deletion
      if (current.isLeaf) {
        current.keys.remove(key);
        current.objectMapping.remove(key);
        current.numKeys--;
        return current;
      }
      // case of a non-leaf root
      else if (!current.isLeaf && current.equals(this.root)) {
        // index of the left child of the key
        while (i < current.numKeys && key.compareTo(current.keys.get(i)) > 0) {
          i++;
        }
        // if the left child has enough keys, proceed to its max descendant and make the root steal
        // it
        if (current.children.get(i).numKeys > minKeys) {
          max = getMax(current.children.get(i));
          swap(current, key, max.keys.get(max.numKeys - 1),
              max.objectMapping.get(max.keys.get(max.numKeys - 1)));
          max.objectMapping.remove(max.keys.get(max.numKeys - 1));
          max.keys.remove(max.numKeys - 1);
          max.numKeys--;
        }
        // left child has the minimum number of keys - merge, then go back into removal
        else {
          current.children.set(i, merge(current, i));
          remove(current, key);
        }
      } else {
        // get temporary, successor node
        indexKey = current.keys.indexOf(key);
        child = current.children.get(indexKey + 1);
        // get temporary key of successor
        min = getMin(child);
        childKey = min.keys.get(0);
        childValue = min.objectMapping.get(childKey);
        // recursive remove call // good up to this point...
        remove(this.root, childKey);
        // swap key-value and childKey-childValue
        swap(this.root, key, childKey, childValue);
        return current;
      }
    }
    // recurse down the tree
    else {
      while (i < current.numKeys && key.compareTo(current.keys.get(i)) > 0) {
        i++;
      }
      if (i >= childrenCounter(current)) {
        return null;
      }
      // merge any single key nodes encountered
      if (current.children.get(i).keys.size() == minKeys) {
        // determine the number of children for current before and after merging
        // to know whether to update current's children
        // fusion removes children, but merging with rotation does not
        // fusion also adds fusedNode as a child, so that case requires no action here
        oldChildKeys = current.children.size();
        merged = merge(current, i);
        newChildKeys = current.children.size();
        // for cases of fusion where current's list of children is modified
        if (oldChildKeys == newChildKeys) {
          current.children.set(i, merged);
        }
      }
      i = 0;
      while (i < current.numKeys && key.compareTo(current.keys.get(i)) > 0) {
        i++;
      }
      // covers the case of the key moving up to current with the above merge operation
      if (current.keys.contains(key)) {
        current = remove(this.root, key);
      }
      // other cases where the target key did not move up
      else {
        current = remove(current.children.get(i), key);
      }
    }
    return current;
  }

  /*
   * Finds the minimum key support removal.
   * 
   * @param Node current
   * 
   * @return Node with minimum key in subtree
   */
  private Node getMin(Node current) {
    while (current.children.size() != 0) {
      // again, we merge whenever we encounter a minKey node
      if (current.children.get(0).numKeys == this.minKeys) {
        current = merge(current, 0);
      } else {
        current = current.children.get(0);
      }
    }
    return current;
  }

  /*
   * Finds the maximum key to support removal.
   * 
   * @param Node current
   * 
   * @return Node with maximum key in subtree
   */
  private Node getMax(Node current) {
    while (current.children.size() != 0) {
      // again, we merge minKey nodes
      if (current.children.get(current.children.size() - 1).numKeys == this.minKeys) {
        current = merge(current, current.children.size() - 1);
      } else {
        current = current.children.get(current.children.size() - 1);
      }
    }
    return current;
  }

  /*
   * Updates the key-value pair at an oldKey with a new key-value pair.
   * 
   * @param Node current
   * 
   * @param K oldKey
   * 
   * @param K newKey
   * 
   * @param V newValue
   * 
   * @return boolean value indicating success of swap
   */
  private boolean swap(Node current, K oldKey, K newKey, V newValue) {
    int j = 0;
    int indexOfOldKey;
    if (current == null) {
      return false;
    }
    // the current Node does not contain oldKey, so recurse down to the appropriate child
    if (!current.keys.contains(oldKey)) {
      while (j < current.numKeys && oldKey.compareTo(current.keys.get(j)) > 0) {
        j++;
      }
      return swap(current.children.get(j), oldKey, newKey, newValue);
    }
    // the current Node contains the oldKey, replace it and the associated value
    else {
      // replace key
      indexOfOldKey = current.keys.indexOf(oldKey);
      current.keys.set(indexOfOldKey, newKey);
      // update mapping
      current.objectMapping.remove(oldKey);
      current.objectMapping.put(newKey, newValue);
    }
    return true;
  }

  /*
   * contains(K key) navigates B-tree looking for the specified key.
   * 
   * @param key to be found
   * 
   * @return true if the key is in the tree and false otherwise
   */
  public boolean contains(K key) {
    Node temp;
    // recursive search
    temp = search(this.root, key);

    if (temp == null) {
      return false;
    } else {
      return true;
    }
  }

  /*
   * search navigates the B-tree looking for the key provided by the user.
   * 
   * @param current Node being checked in the recursion stack
   * 
   * @param K key to be found
   * 
   * @throws illegal key exception if the key is not present in the tree
   * 
   * @return Node containing the key or null if the key is not present in the tree
   */
  private Node search(Node current, K key) {
    int i = 0;
    // key found
    if (current.keys.contains(key)) {
      return current;
    }
    // key not found
    else {
      while (i < current.numKeys && key.compareTo(current.keys.get(i)) > 0) {
        i++;
      }
      // this occurs if the key is not in the tree
      if (i >= childrenCounter(current)) {
        return null;
      }
      // recurse down tree
      current = search(current.children.get(i), key);
    }
    return current;
  }

  /*
   * This is specific to full roots. This is done for simplicity because splitting a root requires
   * creating a new Node and introducing a new level in contrast to other split scenarios.
   * 
   * @param Node current
   * 
   * @param K key
   * 
   * @return Node newParent - updated root
   */
  private Node splitRoot(Node current, K key) {
    Node newParent = new Node(false, 0), leftChild, rightChild;
    int medianIndexRoot;
    K currentKey;
    // medianIndexRoot = index of keys list element to be pushed up to the
    // even number of keys
    if (current.keys.size() % 2 == 0) {
      medianIndexRoot = current.keys.size() / 2 - 1;
    }
    // odd number of keys
    else {
      medianIndexRoot = current.keys.size() / 2;
    }
    // populate the new root
    currentKey = current.keys.get(medianIndexRoot);
    newParent.keys.add(currentKey);
    newParent.objectMapping.put(currentKey, current.objectMapping.get(currentKey));
    newParent.numKeys++;
    // creation of Nodes for the left and right children of the new root
    // in the case where the existing root has no leaves
    if (childrenCounter(current) == 0) {
      leftChild = new Node(true, 0);
      rightChild = new Node(true, 0);
    }
    // the root has leaves
    else {
      leftChild = new Node(false, 0);
      rightChild = new Node(false, 0);
    }
    // copy key list contents to the new left and right children
    for (int j = 0; j < medianIndexRoot; j++) {
      currentKey = current.keys.get(j);
      leftChild.keys.add(currentKey);
      leftChild.objectMapping.put(currentKey, current.objectMapping.get(currentKey));
      leftChild.numKeys++;
      // copies appropriate children to the new Nodes
      if (childrenCounter(current) != 0) {
        if (j == medianIndexRoot - 1) {
          leftChild.children.add(current.children.get(j));
          leftChild.children.add(current.children.get(j + 1));
        } else {
          leftChild.children.add(current.children.get(j));
        }
      }
    }
    for (int j = medianIndexRoot + 1; j < current.keys.size(); j++) {
      currentKey = current.keys.get(j);
      rightChild.keys.add(currentKey);
      rightChild.objectMapping.put(currentKey, current.objectMapping.get(currentKey));
      rightChild.numKeys++;
      // copies appropriate children to the new Nodes
      if (childrenCounter(current) != 0) {
        if (j == current.numKeys - 1) {
          rightChild.children.add(current.children.get(j));
          rightChild.children.add(current.children.get(j + 1));
        } else {
          rightChild.children.add(current.children.get(j));
        }
      }
    }
    // pair child Nodes with newParent
    newParent.children.add(leftChild);
    newParent.children.add(rightChild);
    return newParent;
  }

  /*
   * This is specific to non-root full Nodes. This is done for simplicity because splitting a root
   * requires creating a new Node and introducing a new level, but this case does not.
   * 
   * @param Node current
   * 
   * @param int childLocation
   * 
   * @param K key
   * 
   * @return Node current, now split and updated
   */
  private Node split(Node current, int childLocation, K key) {
    Node leftChild, rightChild, child;
    int medianIndexChild, numChildren = 0, i = 0, j = 0;
    K childKey;
    // current's child is to be split
    child = current.children.get(childLocation);
    // even number of keys
    if (child.keys.size() % 2 == 0) {
      medianIndexChild = child.keys.size() / 2 - 1;
    }
    // odd number of keys
    else {
      medianIndexChild = child.keys.size() / 2;
    }
    // insertion of the median key of the child into the current Node
    if (current.numKeys == 1) {
      childKey = child.keys.get(medianIndexChild);
      if (key.compareTo(current.keys.get(0)) < 0) {
        current.keys.add(0, childKey);
        current.objectMapping.put(childKey, child.objectMapping.get(childKey));
      } else {
        current.keys.add(childKey);
        current.objectMapping.put(childKey, child.objectMapping.get(childKey));
      }
      current.numKeys++;
    } else {
      childKey = child.keys.get(medianIndexChild);
      current.keys.add(childLocation, childKey);
      current.objectMapping.put(childKey, child.objectMapping.get(childKey));
      current.numKeys++;
    }
    // count the number of children belonging to current before adding another
    numChildren = childrenCounter(current);
    // create new Nodes with appropriate isLeaf values
    // if the child Node to be split has no children, then the new Nodes will be leaves too
    if (childrenCounter(child) != 0) {
      leftChild = new Node(false, 0);
      rightChild = new Node(false, 0);
    }
    // otherwise, the new Nodes are in the interior
    else {
      leftChild = new Node(true, 0);
      rightChild = new Node(true, 0);
    }
    // distribute the left and right sides of the split child
    for (K value : child.keys) {
      if (i < medianIndexChild) {
        leftChild.keys.add(value);
        leftChild.objectMapping.put(value, child.objectMapping.get(value));
        leftChild.numKeys++;
      } else if (i > medianIndexChild) {
        rightChild.keys.add(value);
        rightChild.objectMapping.put(value, child.objectMapping.get(value));
        rightChild.numKeys++;
      }
      i++;
    }
    // copy the child Nodes of child to the left and right children
    for (Node childOfChild : child.children) {
      if (j <= medianIndexChild) {
        leftChild.children.add(childOfChild);
      } else if (j > medianIndexChild) {
        rightChild.children.add(childOfChild);
      }
      j++;
    }
    // make the new Nodes children of current
    current.children.set(childLocation, leftChild);
    // right child added in the middle
    if (childLocation + 1 < numChildren) {
      current.children.add(childLocation + 1, rightChild);
    }
    // child added at the right
    else {
      current.children.add(rightChild);
    }
    return current;
  }

  /*
   * A simple helper method to avoid the need for looping through children lists in the main body of
   * other methods to count them.
   * 
   * @param current Node whose child list will be examined
   * 
   * @return int number of keys in a given child list
   */
  private int childrenCounter(Node current) {
    int numChildren = 0;
    for (@SuppressWarnings("unused") Node child : current.children) {
      numChildren++;
    }
    return numChildren;
  }

  /**
   * Print a tree sideways to show structure. This code was provided to students.
   */
  public void printSideways() {
    System.out.println("------------------------------------------");
    recursivePrintSideways(this.root, "");
    System.out.println("------------------------------------------");

  }

  /**
   * Print Nodes in the B-tree.
   * 
   * @param current - beginning Node of a subtree
   * @param indent  - spacing between printed objects
   */
  private void recursivePrintSideways(Node current, String indent) {
    if (current != null) {
      // case: single Node
      if (childrenCounter(current) == 0) {
        System.out.println(indent + current.keys);
      } else {
        // case: children exist; print each child Node's keys
        System.out.println(indent + current.keys);
        for (int i = 0; i < childrenCounter(current); i++) {
          recursivePrintSideways(current.children.get(i), indent + "               ");
        }
      }
    }
  }

  /*
   * A simple method to return the number of key-value pairs in the tree.
   * @return int - totalKeys
   */
  public int getSize() {
    return totalKeys;
  }

  /*
   * This method supports maintenance of B-tree properties in the event of a deletion.
   * 
   * @return Node current - child which was merged
   * 
   * @param Node parent
   * 
   * @parm int i, index of the child Node which has an insufficient number of keys
   */
  private Node merge(Node parent, int i) {
    Node leftSib, rightSib;
    Node current = parent.children.get(i);
    // current has a left sibling
    if (i > 0) {
      leftSib = parent.children.get(i - 1);
    }
    // current does not have a left sibling
    else {
      leftSib = null;
    }
    // current has a right sibling
    if (i < parent.children.size() - 1) {
      rightSib = parent.children.get(i + 1);
    }
    // current does not have a right sibling
    else {
      rightSib = null;
    }
    // right rotation; take from the left sibling
    if (leftSib != null && leftSib.numKeys >= minDegree) {
      rightRotate(leftSib, parent, i);
    }
    // left rotation; take from the right sibling
    else if (rightSib != null && rightSib.numKeys >= minDegree) {
      leftRotate(rightSib, parent, i);
    }
    // rotation to up the key count of current is not possible, so do a fusion
    else {
      if (leftSib == null) {
        current = fuse(current, rightSib, parent);
      } else {
        current = fuse(leftSib, current, parent);
      }
    }
    return current;
  }

  /*
   * This method supports merging. When rotation is not possible, fusion occurs.
   * 
   * @param Node left
   * 
   * @param Node right
   * 
   * @param Node parent
   * 
   * @return Node fusedNode - new node with the middle K-V of the parent and children of left/right
   */
  private Node fuse(Node left, Node right, Node parent) {
    V currentValue;
    // special case of root fusion
    if (parent.equals(this.root) && parent.numKeys == 1 && left.numKeys == 1
        && right.numKeys == 1) {
      return fuseRoot(parent);
    }
    // non-root fusion
    // index of the key to be taken from the parent (midKey)
    int i = parent.children.indexOf(left);
    K midKey = parent.keys.get(i);

    // create a new node
    Node fusedNode = new Node();
    // populate it with the middle key-value pair, plus the left and right node's contents
    // this ordering is required to maintain B-tree ordering
    fusedNode.keys.addAll(left.keys);
    fusedNode.keys.add(midKey);
    fusedNode.keys.addAll(right.keys);
    // key-value mapping in the new node
    // left
    for (K currentKey : left.keys) {
      currentValue = left.objectMapping.get(currentKey);
      fusedNode.objectMapping.put(currentKey, currentValue);
      fusedNode.numKeys++;
    }
    // center
    currentValue = parent.objectMapping.get(midKey);
    fusedNode.objectMapping.put(midKey, currentValue);
    fusedNode.numKeys++;
    // right
    for (K currentKey : right.keys) {
      currentValue = right.objectMapping.get(currentKey);
      fusedNode.objectMapping.put(currentKey, currentValue);
      fusedNode.numKeys++;
    }
    // give the children of left and right to fuseNode
    fusedNode.children.addAll(left.children);
    fusedNode.children.addAll(right.children);

    // set leaf status of fusedNode
    if (fusedNode.children.size() == 0) {
      fusedNode.isLeaf = true;
    } else {
      fusedNode.isLeaf = false;
    }

    // remove midKey from the parent
    parent.keys.remove(i);
    parent.numKeys--;

    // connect the parent to fusedNode
    parent.children.remove(i); // removes left
    parent.children.remove(i); // removes right after it shifts to where left was
    parent.children.add(i, fusedNode);

    return fusedNode;
  }

  /*
   * This occurs when the root and both of its children have one key each, if the tree is a 2-3-4 B
   * tree.
   * 
   * @param Node parent to receive keys/values
   * 
   * @return Node updated root
   */
  private Node fuseRoot(Node parent) {
    Node oldLeft = parent.children.get(0);
    K leftKey = oldLeft.keys.get(0);
    V leftValue = oldLeft.objectMapping.get(leftKey);

    Node oldRight = parent.children.get(1);
    K rightKey = oldRight.keys.get(0);
    V rightValue = oldRight.objectMapping.get(rightKey);

    // pass up the key-value pairs of the left and right child
    parent.keys.add(0, leftKey);
    parent.objectMapping.put(leftKey, leftValue);
    parent.keys.add(2, rightKey);
    parent.objectMapping.put(rightKey, rightValue);
    parent.numKeys = parent.numKeys + 2;

    // give the children of left and right to the parent in B-tree order
    parent.children = new ArrayList<Node>();
    for (Node child : oldLeft.children) {
      parent.children.add(child);
    }
    for (Node child : oldRight.children) {
      parent.children.add(child);
    }
    // the root is now the only node, so it is a leaf
    if (oldLeft.children.size() == 0) {
      parent.isLeaf = true;
    }
    return parent;
  }


  /*
   * This method supports merging.
   * 
   * @param Node current - the right sibling of the Node originally encountered and flagged for
   * merging
   * 
   * @param Node parent - parent of the siblings
   * 
   * @param int i - index of the left sibling
   */
  private void leftRotate(Node current, Node parent, int i) {
    int j = 0;
    Node leftSib = parent.children.get(i);
    // take a key-value pair from the parent
    K leftSibKey = parent.keys.get(i);
    V leftSibValue = parent.objectMapping.get(leftSibKey);

    // remove the key-value pair from the parent
    parent.keys.remove(i);
    parent.objectMapping.remove(leftSibKey);
    parent.numKeys--;

    // add the key-value pair to the left sibling
    // this works because the left sibling must only have a single key
    leftSib.keys.add(leftSibKey);
    leftSib.objectMapping.put(leftSibKey, leftSibValue);
    leftSib.numKeys++;

    // remove current's left-most child and give it to leftSib
    if (current.children.size() > 0) {
      Node leftChild = current.children.get(0);
      leftSib.children.add(leftChild);
      current.children.remove(0);
    }

    // cut & paste current's left-most key-value pair to its parent
    // copy
    K currentLeftKey = current.keys.get(0);
    V currentLeftValue = current.objectMapping.get(currentLeftKey);
    // paste
    for (K key : parent.keys) {
      if (currentLeftKey.compareTo(key) > 0) {
        j++;
      }
    }
    parent.keys.add(j, currentLeftKey);
    parent.objectMapping.put(currentLeftKey, currentLeftValue);
    parent.numKeys++;
    // remove (cut)
    current.keys.remove(0);
    current.objectMapping.remove(currentLeftKey);
    current.numKeys--;
  }

  /*
   * This method supports merging.
   * 
   * @param Node current - the left sibling of the Node originally encountered and flagged for
   * merging
   * 
   * @param Node parent - parent of the siblings
   * 
   * @param int i - index of the right sibling
   */
  private void rightRotate(Node current, Node parent, int i) {
    int j = 0;
    Node rightSib = parent.children.get(i);
    // take a key-value pair from the parent
    K rightSibKey = parent.keys.get(i - 1);
    V rightSibValue = parent.objectMapping.get(rightSibKey);

    // remove the key-value pair from the parent
    parent.keys.remove(i - 1);
    parent.objectMapping.remove(rightSibKey);
    parent.numKeys--;

    // add the key-value pair to the right sibling
    // this works because the right sibling must only have a single key
    for (K key : rightSib.keys) {
      if (rightSibKey.compareTo(key) > 0) {
        j++;
      }
    }
    rightSib.keys.add(j, rightSibKey);
    rightSib.objectMapping.put(rightSibKey, rightSibValue);
    rightSib.numKeys++;
    j = 0;

    // remove current's right-most child and give it to rightSib
    if (current.children.size() > 0) {
      Node rightChild = current.children.get(current.children.size() - 1);
      rightSib.children.add(0, rightChild);
      current.children.remove(current.children.size() - 1);
    }

    // cut & paste current's right-most key-value pair to its parent
    // copy
    K currentRightKey = current.keys.get(current.numKeys - 1);
    V currentRightValue = current.objectMapping.get(currentRightKey);
    // paste
    for (K key : parent.keys) {
      if (currentRightKey.compareTo(key) > 0) {
        j++;
      }
    }
    parent.keys.add(j, currentRightKey);
    parent.objectMapping.put(currentRightKey, currentRightValue);
    parent.numKeys++;
    // remove (cut)
    current.keys.remove(current.numKeys - 1);
    current.objectMapping.remove(currentRightKey);
    current.numKeys--;
  }

  /**
   * levelTraversal is a misnomer. This is a B-tree pre-order traversal.
   * 
   * @return List<V>
   */
  public List<V> levelTraversal() {
    List<Node> nodeOutput = new ArrayList<Node>();
    List<K> keysOutput = new ArrayList<K>();
    List<V> valuesOutput = new ArrayList<V>();
    // get the list of Nodes
    nodeOutput = levelTraversal(root, nodeOutput);
    // iterate through the keys of each node to get values
    for (Node current : nodeOutput) { // iterations will never exceed maxKeys+1
      keysOutput = current.keys;
      for (K key : keysOutput) { // up to N iterations
        valuesOutput.add(current.objectMapping.get(key)); // hash table O(1) + list append O(1)
      }
    }
    return valuesOutput;
  }

  /*
   * Recursively traverse the tree
   * 
   * @param current parent Node
   * 
   * @return List<Node> list of Nodes
   * 
   * @see public levelTraversal method
   */
  private List<Node> levelTraversal(Node current, List<Node> output) {
    // base case
    output.add(current);
    // traverse each child
    for (Node child : current.children) {
      levelTraversal(child, output);
    }
    return output;
  }
}
