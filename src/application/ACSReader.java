//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           ASC Reader
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
/*
 * This class is responsible for reading the selected American Community Survey .csv
 * into a B-tree data structure.
 */
public class ACSReader {
  // every time read is run, a new hash map object is generated to relate countyIDs
  // to more user friendly county and state information
  static Map<String, Integer> countyToID;

  /*
   * Getter method to translate user input county and state information
   * to the unique Integer keys used by the tree.
   * @Return Map<String, Integer>
   */
  public static Map<String, Integer> countyToID() {
    return countyToID;
  }

  public static BTree<Integer, County> read(String filename) {
    /*************** Variables ******************/
    // ArrayList<Integer> countyID, totalPop, men, women, numVotingAge, income, incomeError,
    // incomePerCapita, incomePerCapitaError, employed;
    // ArrayList<String> state, countyName;
    // ArrayList<Float> hispanic, white, nativePerson, asian, pacific, poverty, childPoverty,
    // professional, service, office, construction, production, drive, carpool, transit, walk,
    // otherTransport, workAtHome, meanCommute, privateWork, publicWork, selfEmployed, familyWork,
    // unemployed;
    BTree<Integer, County> tree;
    List<ArrayList<Integer>> intVariables;
    List<ArrayList<String>> stringVariables;
    List<ArrayList<Double>> doubleVariables;
    int j, k, l;
    // new tree
    tree = new BTree<Integer, County>();
    // implement countyToID as a HashMap
    countyToID = new HashMap<String, Integer>();

    // there are a lot of possible lists to handle, so instead of making several individually,
    // make lists of ArrayLists
    intVariables = new ArrayList<ArrayList<Integer>>(10);
    stringVariables = new ArrayList<ArrayList<String>>(2);
    doubleVariables = new ArrayList<ArrayList<Double>>(25);
    // populate the above lists with Lists of the appropriate types
    // indices correspond to the variables commented out above
    // example: countyID is at index = 0 of intVariables and employed is at index = 9.
    for (int i = 0; i < 25; i++) {
      if (i < 10) {
        intVariables.add(i, new ArrayList<Integer>());
      }
      if (i < 2) {
        stringVariables.add(i, new ArrayList<String>());
      }
      doubleVariables.add(i, new ArrayList<Double>());
    }
    // index
    int rowNumber = 0;

    // parse csv
    // try/catch for file reading
    try {
      // reading file, temporarily saving data, and storing to tree
      Scanner csvScnr = new Scanner(new File(filename));
      while (csvScnr.hasNextLine()) {
        String row = csvScnr.nextLine();
        String[] data = row.split(",");
        j = 0;
        k = 0;
        l = 0;
        if (rowNumber > 0) {
          // extract county data
          // iterates through the whole row of data and inserts values
          // into the appropriate lists based on data type
          for (int i = 0; i < data.length; i++) {
            if (i == 0 || i == 3 || i == 4 || i == 5 || i == 12 || i == 13 || i == 14 || i == 15
                || i == 16 || i == 31) {
              intVariables.get(j).add(Integer.parseInt(data[i]));
              j++;
            } else if (i == 1 || i == 2) {
              stringVariables.get(k).add(data[i]);
              k++;
            } else {
              doubleVariables.get(l).add(Double.parseDouble(data[i]));
              l++;
            }
          }
          County node = setCountyValues(rowNumber, intVariables, stringVariables, doubleVariables); 
          
          // map county and state name string to the unique ID in the file
          countyToID.put(node.getCountyName() + node.getState(),
              intVariables.get(0).get(rowNumber - 1));
          try { 
            // insert into the tree using the unique ID as a key, and the County object as a value
            tree.insert(intVariables.get(0).get(rowNumber - 1), node);
          }
          // exceptions
          catch (IllegalKeyException e) {
            System.out.println(e.getMessage());
          } catch (DuplicateKeyException m) {
            System.out.println(m.getMessage());
          } catch (Exception e) {
            System.out.println("Unknown exception");
          }
        }
        rowNumber++;
      }
      csvScnr.close(); 
    // file end
    } catch (FileNotFoundException e1) {
      System.out.println("File not found.");
    }
    return tree;
  }
  
  private static County setCountyValues(int rowNumber, List<ArrayList<Integer>> intVariables, List<ArrayList<String>> stringVariables, 
		  List<ArrayList<Double>> doubleVariables){

	  County node = new County();
	  
	  node.setTotalPop(intVariables.get(1).get(rowNumber - 1));
      node.setMen(intVariables.get(2).get(rowNumber - 1));
      node.setWomen(intVariables.get(3).get(rowNumber - 1));
      node.setNumVotingAge(intVariables.get(4).get(rowNumber - 1));
      node.setIncome(intVariables.get(5).get(rowNumber - 1));
      node.setIncomeError(intVariables.get(6).get(rowNumber - 1));
      node.setIncomePerCapita(intVariables.get(7).get(rowNumber - 1));
      node.setIncomePerCapitaError(intVariables.get(8).get(rowNumber - 1));
      node.setEmployed(intVariables.get(9).get(rowNumber - 1));
      // strings
      node.setState(stringVariables.get(0).get(rowNumber - 1));
      node.setCountyName(stringVariables.get(1).get(rowNumber - 1));
      // floats
      node.setHispanic(doubleVariables.get(0).get(rowNumber - 1));
      node.setWhite(doubleVariables.get(1).get(rowNumber - 1));
      node.setBlack(doubleVariables.get(2).get(rowNumber - 1));
      node.setNativePerson(doubleVariables.get(3).get(rowNumber - 1));
      node.setAsian(doubleVariables.get(4).get(rowNumber - 1));
      node.setPacific(doubleVariables.get(5).get(rowNumber - 1));
      node.setPoverty(doubleVariables.get(6).get(rowNumber - 1));
      node.setChildPoverty(doubleVariables.get(7).get(rowNumber - 1));
      node.setProfessional(doubleVariables.get(8).get(rowNumber - 1));
      node.setService(doubleVariables.get(9).get(rowNumber - 1));
      node.setOffice(doubleVariables.get(10).get(rowNumber - 1));
      node.setConstruction(doubleVariables.get(11).get(rowNumber - 1));
      node.setProduction(doubleVariables.get(12).get(rowNumber - 1));
      node.setDrive(doubleVariables.get(13).get(rowNumber - 1));
      node.setCarpool(doubleVariables.get(14).get(rowNumber - 1));
      node.setTransit(doubleVariables.get(15).get(rowNumber - 1));
      node.setWalk(doubleVariables.get(16).get(rowNumber - 1));
      node.setOtherTransport(doubleVariables.get(17).get(rowNumber - 1));
      node.setWorkAtHome(doubleVariables.get(18).get(rowNumber - 1));
      node.setMeanCommute(doubleVariables.get(19).get(rowNumber - 1));
      node.setPrivateWork(doubleVariables.get(20).get(rowNumber - 1));
      node.setPublicWork(doubleVariables.get(21).get(rowNumber - 1));
      node.setSelfEmployed(doubleVariables.get(22).get(rowNumber - 1));
      node.setFamilyWork(doubleVariables.get(23).get(rowNumber - 1));
      node.setUnemployed(doubleVariables.get(24).get(rowNumber - 1));
      
      return node;
  }
}
