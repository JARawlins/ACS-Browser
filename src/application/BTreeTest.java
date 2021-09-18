//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: JUnit Testing of B-Tree Data Structure
// Files: BTree.java
// Course: CS400, Fall, 2019
//
// Author: Joshua Rawlins
// Email: jrawlins@wisc.edu
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

/**
 * JUnit test class to test the 2-3-4 B-Tree, BTree.java.
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BTreeTest {
  List<Integer> numbers;
  BTree<Integer, Integer> tree;
  @Rule
  public Timeout globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

  @Before
  public void setUp() throws Exception {
    // standard inserts and tree
    numbers = Arrays.asList(8722, 5926, 1682, 4342, 9765, 505, 8414, 1038, 9877, 2300, 8209, 7050,
        2294, 7049, 1148, 8006, 5599, 3768, 268, 821, 9811, 4473, 3888, 3310, 9369);
    tree = new BTree<Integer, Integer>();
    for (int i = 0; i < 25; i++) {
      try {
        tree.insert(numbers.get(i), numbers.get(i));
      } catch (IllegalKeyException e) {
        e.printStackTrace();
        System.exit(0);
      } catch (DuplicateKeyException e) {
        e.printStackTrace();
        System.exit(0);
      }
    }
  }

  @After
  public void tearDown() throws Exception {
    tree = null;
  }
  /*
   * General note: the usage of the traversal in testing of insertion and remove confirm the proper
   * functioning of the traversal method.
   */

  /*
   * This test inputs a combination of keys which is known to result in the usage of splitRoot and
   * split operations when done by hand, thereby also testing these private methods. This
   * combination was found by inserting random numbers in BTree.java main method in combination with
   * print statements appearing in the console when splitRoot and split private methods were called.
   */
  @Test
  public final void test01_insert() {
    List<Integer> traversalTested = tree.levelTraversal();
    List<Integer> traversalExpected =
        Arrays.asList(1682, 5926, 1038, 268, 505, 821, 1148, 2300, 4342, 2294, 3310, 3768, 3888,
            4473, 5599, 8209, 8722, 9811, 7049, 7050, 8006, 8414, 9369, 9765, 9877);
    assertEquals("test01: failed - something is wrong with your insertion. "
        + "Your tree traversal output is not as expected.", traversalExpected, traversalTested);
  }

  /* test the BTree for throwing of DuplicateKey and IllegalKeyExceptions */
  @Test
  public final void test02_insertExceptions() {
    try {
      tree.insert(5599, 5599);
    } catch (DuplicateKeyException e) {
    } catch (Exception e1) {
      fail("test02: failed - a DuplicateKeyException was expected.");
    }
    try {
      tree.insert(null, 5599);
    } catch (IllegalKeyException e) {
    } catch (Exception e1) {
      fail("test02: failed - an IllegalKeyException was expected.");
    }
  }

  /*
   * Test of remove; this combination of insertions and removals was confirmed to result in handling
   * of the main removal cases - merging, fusion, root fusion, right rotation, and left rotation.
   * These numbers were found in a similar fashion to those for insert. The boolean returns for
   * successful and unsuccessful removal are also checked.
   */
  @Test
  public final void test03_remove() {
    // build a different tree with custom numbers
    BTree<Integer, Integer> tree2 = new BTree<Integer, Integer>();
    List<Integer> numbersInserted = Arrays.asList(4125, 8956, 3434, 2296, 9764, 2481, 5732, 4727,
        394, 1394, 7371, 3717, 3706, 5032, 5343);
    List<Integer> numbersRemoved = Arrays.asList(3434, 9764, 394, 7371, 5732);
    List<Integer> traversalExpected, traversalTested;
    for (int i = 0; i < 15; i++) {
      try {
        tree2.insert(numbersInserted.get(i), numbersInserted.get(i));
      } catch (IllegalKeyException e) {
        e.printStackTrace();
        System.exit(0);
      } catch (DuplicateKeyException e) {
        e.printStackTrace();
        System.exit(0);
      }
    }
    // selected simultaneously testing boolean return
    for (Integer number : numbersRemoved) {
      assertEquals("test03: failed - successfully removing an entry should return true.", true,
          tree2.remove(number));
    }
    // test for final tree structure
    traversalTested = tree2.levelTraversal();
    traversalExpected = Arrays.asList(2481, 4125, 5343, 1394, 2296, 3706, 3717, 4727, 5032, 8956);
    assertEquals("test03: failed - something is wrong with your removal method. "
        + "Your tree traversal output is not as expected.", traversalExpected, traversalTested);
    // test for false return
    assertEquals(
        "test03: failed - remove should return false when the input key is not in the tree "
            + "and thus not removed",
        false, tree2.remove(1));
  }

  /*
   * Size testing
   */
  @Test
  public final void test04_size() {
    assertEquals("test04: failed - the tree has 25 entries", 25, tree.getSize());
    assertEquals("test04: failed - the tree has 0 entries; it is empty", 0,
        new BTree<Integer, Integer>().getSize());
  }

  /*
   * Contains
   */
  @Test
  public final void test05_contains() {
    assertEquals("test05: failed - the tree does contain the key 3310.", true, tree.contains(3310));
    assertEquals("test05: failed - the tree does not contain the key 1.", false, tree.contains(1));
  }

  /*
   * getValue
   */
  @Test
  public final void test06_getValue() {
    BTree<Integer, String> tree2 = new BTree<Integer, String>();
    try {
      tree2.insert(1, "A");
      tree2.insert(2, "B");
      tree2.insert(3, "C");
      tree2.insert(4, "D");
      tree2.insert(5, "L");
    } catch (DuplicateKeyException | IllegalKeyException e) {
      e.printStackTrace();
    }
    try {
      assertEquals("test06: failed - the key 4 should yield the value D.", "D", tree2.getValue(4));
    } catch (IllegalKeyException e) {
      e.printStackTrace();
    }
  }

  /*
   * getValue's exception
   */
  @Test
  public final void test07_getValueException() {
    try {
      assertEquals("test06: failed - the key 4 should yield the value D.", "D", tree.getValue(4));
    } catch (IllegalKeyException e) {
    } catch (Exception e1) {
      fail("test07: failed - an unknown exception occurred.");
    }
  }
}
