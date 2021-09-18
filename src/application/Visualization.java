//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Visualization.java
// Files:           County.java
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
// Online Sources: Adapted from https://docs.oracle.com/javafx/2/charts/pie-chart.htm#CIHFDADD
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
package application;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
 
/*
 * This class is responsible for defining the chart(s) used in the program. It only
 * works for a single county.
 */
public class Visualization {
  static Stage stage = null;
  /*
   * Responsible for handling the stage and scene management of county profiling
   * @return Stage
   * @param ObservableList<County> counties - selected county(ies)
   */
  public static Stage visualizeWindow(ObservableList<County> counties) {
    // pie charts built from the selected county(ies)
    List<PieChart> charts = buildPieCharts(counties);
    
    // add the pie charts to the BorderPane via a HBox
    HBox chartBox = new HBox();
    for (PieChart chart : charts) {
      chartBox.getChildren().add(chart);
    }
    
    // layout
    BorderPane root = new BorderPane();
    root.setCenter(chartBox);
    
    // scene building
    Scene scene = new Scene(root, 800, 600);
    // add scene to stage and display
    // create a new stage if one is not already present, and just update the scene otherwise
    if (stage == null) {
      stage = new Stage();
    }
    stage.setTitle(counties.get(0).getCountyName() + ", " + counties.get(0).getState());
    stage.setScene(scene);
    stage.show();
    
    return stage;
  }

  /*
   * Builds pie charts with race/ethnicity, employment, and commute information.
   * @return List<PieChart>
   * @param ObservableList<County> counties - selected county(ies)
   */
  private static List<PieChart> buildPieCharts(ObservableList<County> counties) {
    List<PieChart> charts = new ArrayList<PieChart>();
    
    buildRacePieChart(counties, charts);
    buildEmploymentPieChart(counties, charts);
    buildTransportPieChart(counties, charts);

    return charts;
  }
  
  private static void buildRacePieChart(ObservableList<County> counties, List<PieChart> charts) {
    // new list of data for the pie chart
    ObservableList<PieChart.Data> raceData = FXCollections.observableArrayList();
    
    // populate the pie chart with data
    raceData.add(new PieChart.Data("White", counties.get(0).getWhite()));
    raceData.add(new PieChart.Data("Black", counties.get(0).getBlack()));
    raceData.add(new PieChart.Data("Asian", counties.get(0).getAsian()));
    raceData.add(new PieChart.Data("Hispanic", counties.get(0).getHispanic()));
    raceData.add(new PieChart.Data("Other", 100 - counties.get(0).getWhite()
        - counties.get(0).getBlack() - counties.get(0).getAsian() - counties.get(0).getHispanic()));
    
    // make the pie chart
    PieChart raceChart = new PieChart(raceData);
    raceChart.setTitle("Racial/Ethnic Breakdown");
    
    // add race pie chart to the list of charts
    charts.add(raceChart);
  }
  
  private static void buildEmploymentPieChart(ObservableList<County> counties, List<PieChart> charts) {
    // list of date going into the pie chart
    ObservableList<PieChart.Data> employmentData = FXCollections.observableArrayList();
    
    // populate the pie chart with data
    employmentData.add(new PieChart.Data("Professional", counties.get(0).getProfessional()));
    employmentData.add(new PieChart.Data("Service", counties.get(0).getService()));
    employmentData.add(new PieChart.Data("Office", counties.get(0).getOffice()));
    employmentData.add(new PieChart.Data("Construction", counties.get(0).getConstruction()));
    employmentData.add(new PieChart.Data("Production", counties.get(0).getProduction()));
    employmentData.add(new PieChart.Data("Other",
        100 - counties.get(0).getProfessional() - counties.get(0).getService()
            - counties.get(0).getOffice() - counties.get(0).getConstruction()
            - counties.get(0).getProduction()));
    
    // make the pie chart
    PieChart employmentChart = new PieChart(employmentData);
    employmentChart.setTitle("Employment Profile");
    
    // add the new pie chart to the list of charts
    charts.add(employmentChart);	  
  }
  
  public static void buildTransportPieChart(ObservableList<County> counties, List<PieChart> charts) {
    // list of data going into the pie chart
    ObservableList<PieChart.Data> transportData = FXCollections.observableArrayList();
    
    // populate the pie chart with data
    transportData.add(new PieChart.Data("Drive", counties.get(0).getDrive()));
    transportData.add(new PieChart.Data("Carpool", counties.get(0).getCarpool()));
    transportData.add(new PieChart.Data("Public Transit", counties.get(0).getTransit()));
    transportData.add(new PieChart.Data("Walk", counties.get(0).getWalk()));
    transportData.add(new PieChart.Data("Other", 100 - counties.get(0).getDrive()
        - counties.get(0).getCarpool() - counties.get(0).getTransit() - counties.get(0).getWalk()));
    
    // make the pie chart
    PieChart transportChart = new PieChart(transportData);
    transportChart.setTitle("Individuals' Primary Mode of Transportation");
    
    // add the new pie chart to the list of charts
    charts.add(transportChart);	  
  }
}
