//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           StreamManager
// Files:           ACSReader.java, BTree.java
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

import java.util.List;

/*
 * This class is responsible for instantiating a tree of County objects and defining methods for
 * getting the bounds on the data for implementation in sliders.
 */
public class StreamManager {
  /****** Set-up ********/
  String filename;
  List<County> counties;
  BTree<Integer, County> tree;
  
  // tree getter
  public BTree<Integer, County> getTree() {
    return tree;
  }

  // constructor for operating on the whole data set
  public StreamManager(String filename) {
    this.filename = filename;
    // make a B-tree
    this.tree = ACSReader.read(filename);
    // obtain a list of all counties
    this.counties = tree.levelTraversal();
  }
  
  /*
   * Determine the minimum and maximum of total population
   * 
   * @return int[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public int[] boundsTotalPop() {
    int[] totalPop = new int[2];
    // minimum
    totalPop[0] = counties.stream().mapToInt(County::getTotalPop).min().getAsInt();
    // maximum
    totalPop[1] = counties.stream().mapToInt(County::getTotalPop).max().getAsInt();
    return totalPop;
  }

  /*
   * Determine the minimum and maximum of incomePerCapita
   * 
   * @return int[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public int[] boundsIncomePerCapita() {
    int[] incomePerCapita = new int[2];
    // minimum
    incomePerCapita[0] = counties.stream().mapToInt(County::getIncomePerCapita).min().getAsInt();
    // maximum
    incomePerCapita[1] = counties.stream().mapToInt(County::getIncomePerCapita).max().getAsInt();
    return incomePerCapita;
  }

  /*
   * Determine the minimum and maximum of sexRatio.
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsSexRatio() {
    double[] sexRatio = new double[2];
    // minimum
    sexRatio[0] = counties.stream().mapToDouble(County::getSexRatio).min().getAsDouble();
    // maximum
    sexRatio[1] = counties.stream().mapToDouble(County::getSexRatio).max().getAsDouble();
    return sexRatio;
  }

  /*
   * Determine the minimum and maximum of realEmployment.
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsRealEmployment() {
    double[] realEmployment = new double[2];
    // minimum
    realEmployment[0] =
        counties.stream().mapToDouble(County::getRealEmployment).min().getAsDouble();
    // maximum
    realEmployment[1] =
        counties.stream().mapToDouble(County::getRealEmployment).max().getAsDouble();
    return realEmployment;
  }

  /*
   * Determine the minimum and maximum of % hispanic.
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsHispanic() {
    double[] hispanic = new double[2];
    // minimum
    hispanic[0] = counties.stream().mapToDouble(County::getHispanic).min().getAsDouble();
    // maximum
    hispanic[1] = counties.stream().mapToDouble(County::getHispanic).max().getAsDouble();
    return hispanic;
  }

  /*
   * Determine the minimum and maximum of % white
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsWhite() {
    double[] white = new double[2];
    // minimum
    white[0] = counties.stream().mapToDouble(County::getWhite).min().getAsDouble();
    // maximum
    white[1] = counties.stream().mapToDouble(County::getWhite).max().getAsDouble();
    return white;
  }

  /*
   * Determine the minimum and maximum of % black
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsBlack() {
    double[] black = new double[2];
    // minimum
    black[0] = counties.stream().mapToDouble(County::getBlack).min().getAsDouble();
    // maximum
    black[1] = counties.stream().mapToDouble(County::getBlack).max().getAsDouble();
    return black;
  }

  /*
   * Determine the minimum and maximum of % asian
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsAsian() {
    double[] asian = new double[2];
    // minimum
    asian[0] = counties.stream().mapToDouble(County::getAsian).min().getAsDouble();
    // maximum
    asian[1] = counties.stream().mapToDouble(County::getAsian).max().getAsDouble();
    return asian;
  }

  /*
   * Determine the minimum and maximum of % poverty
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsPoverty() {
    double[] poverty = new double[2];
    // minimum
    poverty[0] = counties.stream().mapToDouble(County::getPoverty).min().getAsDouble();
    // maximum
    poverty[1] = counties.stream().mapToDouble(County::getPoverty).max().getAsDouble();
    return poverty;
  }

  /*
   * Determine the minimum and maximum of % white collar
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsWhiteCollar() {
    double[] whiteCollar = new double[2];
    // minimum
    whiteCollar[0] = counties.stream().mapToDouble(County::getWhiteCollar).min().getAsDouble();
    // maximum
    whiteCollar[1] = counties.stream().mapToDouble(County::getWhiteCollar).max().getAsDouble();
    return whiteCollar;
  }

  /*
   * Determine the minimum and maximum of % driving prevalence
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsDrive() {
    double[] drive = new double[2];
    // minimum
    drive[0] = counties.stream().mapToDouble(County::getDrive).min().getAsDouble();
    // maximum
    drive[1] = counties.stream().mapToDouble(County::getDrive).max().getAsDouble();
    return drive;
  }

  /*
   * Determine the minimum and maximum of private/public ratio
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsPrPuRatio() {
    double[] prPuRatio = new double[2];
    // minimum
    prPuRatio[0] = counties.stream().mapToDouble(County::getPrPuRatio).min().getAsDouble();
    // maximum
    prPuRatio[1] = counties.stream().mapToDouble(County::getPrPuRatio).max().getAsDouble();
    return prPuRatio;
  }

  /*
   * Determine the minimum and maximum of mean commute time
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsCommute() {
    double[] commute = new double[2];
    // minimum
    commute[0] = counties.stream().mapToDouble(County::getMeanCommute).min().getAsDouble();
    // maximum
    commute[1] = counties.stream().mapToDouble(County::getMeanCommute).max().getAsDouble();
    return commute;
  }

  /*
   * Determine the minimum and maximum of unemployed
   * 
   * @return double[] where i = 0 is the minimum and i = 1 is the maximum
   */
  public double[] boundsUnemployed() {
    double[] unemployed = new double[2];
    // minimum
    unemployed[0] = counties.stream().mapToDouble(County::getUnemployed).min().getAsDouble();
    // maximum
    unemployed[1] = counties.stream().mapToDouble(County::getUnemployed).max().getAsDouble();
    return unemployed;
  }
}
