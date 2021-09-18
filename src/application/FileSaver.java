//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           FileSaver.java
// Files:           BTree.java, ACSReader.java
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileSaver {
  public static void write(String filename, BTree<Integer, County> tree) {
    /*************** Variables ******************/
    FileWriter fileWriter = null;
    // column labels
    String header = "CountyId,State,County,TotalPop,"
        + "Men,Women,Hispanic,White,Black,Native,Asian,Pacific,VotingAgeCitizen,"
        + "Income,IncomeErr,IncomePerCap,IncomePerCapErr,Poverty,ChildPoverty,Professional,"
        + "Service,Office,Construction,Production,Drive,Carpool,Transit,Walk,"
        + "OtherTransp,WorkAtHome,MeanCommute,Employed,PrivateWork,PublicWork,SelfEmployed,"
        + "FamilyWork,Unemployment";
    // list of counties
    List<County> counties = tree.levelTraversal();
    /******* Write to File **********/
    try {
      // new FileWriter object
      fileWriter = new FileWriter(filename);
      // append header first
      fileWriter.append(header);
      fileWriter.append("\n");
      // iterate through every county and add each one's data
      for (County county : counties) {
        fileWriter.append(Integer
            .toString(ACSReader.countyToID().get(county.getCountyName() + county.getState())));
        fileWriter.append(',');
        fileWriter.append(county.getState());
        fileWriter.append(',');
        fileWriter.append(county.getCountyName());
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getTotalPop()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getMen()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getWomen()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getHispanic()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getWhite()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getBlack()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getNativePerson()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getAsian()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getPacific()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getNumVotingAge()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getIncome()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getIncomeError()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getIncomePerCapita()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getIncomePerCapitaError()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getPoverty()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getChildPoverty()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getProfessional()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getService()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getOffice()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getConstruction()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getProduction()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getDrive()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getCarpool()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getTransit()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getWalk()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getOtherTransport()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getWorkAtHome()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getMeanCommute()));
        fileWriter.append(',');
        fileWriter.append(Integer.toString(county.getEmployed()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getPrivateWork()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getPublicWork()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getSelfEmployed()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getFamilyWork()));
        fileWriter.append(',');
        fileWriter.append(Double.toString(county.getUnemployed()));
        fileWriter.append(',');
        fileWriter.append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        // close-out file operations
        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
