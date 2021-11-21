//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Main JavaFX
// Files:           StreamManager.java, BTree.java, Alerts.java, County.java, 
//                  Visualization.java, IllegalKeyException.java
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
// Online Sources: Filer chooser code was adapted from: 
//                 https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * This class is the core of the GUI.
 */
public class Main extends Application {
  /****** Variables and their defaults ********/
  String filename = "acs2017_county_data.csv";
  StreamManager streamer = new StreamManager(filename);
  BTree<Integer, County> tree = streamer.getTree();
  TableView<County> table;
  List<County> counties = tree.levelTraversal();

  @Override
  public void start(Stage primaryStage) {
    try {
      /********* Main Screen ***********/
      // prompt the user to save their changes upon attempting to close the window
      // regardless of what the user chooses, the close request proceeds afterward
      primaryStage.setScene(buildMain(primaryStage));
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Launcher
   */
  public static void main(String[] args) {
    launch(args);
  }

  /*
   * This method constructs all functionality of the starting window of the program.
   * @param Stage primaryStage
   * @return Scene home
   */
  private Scene buildMain(Stage primaryStage) {
    // watch out for close attempt
    primaryStage.setOnCloseRequest(e -> {
      Alerts.closeAttemptWindow(tree);
    });
    // menu bar
    MenuBar mainMenuBar = new MenuBar();
    // menus
    Menu fileMenu = new Menu("File");
    // menu items
    MenuItem openFile = new MenuItem("Load file...");
    setupLoadFileItem(openFile, primaryStage);
    // menu added
    fileMenu.getItems().addAll(openFile);
    mainMenuBar.getMenus().addAll(fileMenu);
    
    // textFields to go in center
    TextField countyMainText = new TextField();
    countyMainText.setPromptText("Enter your county of interest. Example: Dane County");
    TextField stateMainText = new TextField();
    stateMainText.setPromptText("Enter your state of interest. Example: Wisconsin");
    
    // buttons
    Button goButton = new Button("Go");
    setupMainSearchButton(goButton, countyMainText, stateMainText);
    Button filterButton = new Button("Filter");
    filterButton.setOnAction(e -> primaryStage.setScene(buildFilterPage(primaryStage)));
    
    // label to go in center
    Label mainDirections = new Label("Welcome to the US County Profiler. Enter county "
        + "and state information below " + "to get a quick run-down on a specific county\n"
        + "or click the filter button to browse all US counties.\n"
        + "By default, acs2017_county_data.csv is loaded, but may be changed from the file menu.");

    // scene creation
    BorderPane root = createMainLayout(goButton, filterButton, mainDirections, countyMainText, stateMainText, mainMenuBar);
    Scene scene = new Scene(root, 700, 700);
    
    // styling
    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    
    return scene;
  }

  /*
   * This method constructs all functionality of the filtering window.
   * @param primaryStage
   * @return Scene filtering window
   */
  private Scene buildFilterPage(Stage primaryStage) {
    // prompt for saving in response to a close attempt
    primaryStage.setOnCloseRequest(e -> {
      Alerts.closeAttemptWindow(tree);
    });
    
    // layout definition at the front to permit conditional additions
    BorderPane root = new BorderPane();
    GridPane filters = new GridPane();
    filters.setHgap(20);
    filters.setVgap(10);
    BorderPane.setMargin(filters, new Insets(10, 10, 10, 10));
    HBox buttons = new HBox();
    
    // buttons
    Button home = new Button("Home");
    home.setOnAction(e -> primaryStage.setScene(buildMain(primaryStage)));
    Button visualizeButton = new Button("Visualize selection(s)");
    visualizeButton.setVisible(false); // shown after the table is visible
    Button removeButton = new Button("Remove");
    removeButton.setVisible(false);
    Button saveButton = new Button("Save to file");
    saveButton.setOnAction(e -> {
      // save window
      Alerts.saveWindow(tree);
    });
    
    // tool tip to explain the query system
    Button query = new Button("Apply filters");
    setupButtonTooltip("Data will be filtered based on sliders you have marked with checkboxes " + "above.\n"
            + "The value on a slider represents the maximum value permitted for a variable. "
            + "E.g. selecting income " + "per capita and sliding\n"
            + "it to 20,000 would yield all counties with income per capita statistic equal to "
            + "or less than that amount.\n"
            + "Sliders do not have priority. Multiple selected sliders operate by AND logic.\n"
            + "Once clicked, a table with all counties fitting your criteria will appear.", query);
 
    setupSlidersCheckboxesActions(streamer, tree, filters, query, removeButton, visualizeButton, root);
                                      
    // define the removal and visualization functionality only when a table has been created
    if (root.centerProperty() != null) {
    	setupRemovalButton(removeButton, query);
    	setupVisualizeButton(visualizeButton);
    }

    // layout(s) configuration
    buttons.getChildren().addAll(home, query, saveButton, removeButton, visualizeButton);
    BorderPane.setMargin(buttons, new Insets(10, 10, 10, 10));
    buttons.setSpacing(10);
    root.setTop(filters);
    root.setBottom(buttons);
    
    // scene build
    Scene scene = new Scene(root, 800, 700);
    return scene;
  }

  /*
   * Creates a slider
   * 
   * @param int[] array - contains minimum and maximum values for the slider
   * 
   * @return Slider
   */
  private Slider generateSlider(int[] array) {
    double[] dArray = new double[2];
    dArray[0] = (double) array[0];
    dArray[1] = (double) array[1];
    return generateSlider(dArray);
  }

  /*
   * Creates a slider
   * 
   * @param int[] array - contains minimum and maximum values for the slider
   * 
   * @return Slider
   */
  private Slider generateSlider(double[] array) {
    Slider mySlider = new Slider();
    mySlider.setMin(array[0]);
    mySlider.setMax(array[1]);
    mySlider.setShowTickMarks(true);
    mySlider.setShowTickLabels(true);
    return mySlider;
  }

  /*
   * Creates a table of counties
   * 
   * @return TableView<Count> table
   * 
   * @param List<County> counties to display
   */
  @SuppressWarnings("unchecked")
  private TableView<County> getTable(List<County> counties) {
    // county name column
    TableColumn<County, String> countyColumn = new TableColumn<County, String>("County");
    countyColumn.setMinWidth(100);
    countyColumn.setCellValueFactory(new PropertyValueFactory<County, String>("countyName"));
    // state name column
    TableColumn<County, String> stateColumn = new TableColumn<County, String>("State");
    stateColumn.setMinWidth(100);
    stateColumn.setCellValueFactory(new PropertyValueFactory<County, String>("state"));
    // % white
    TableColumn<County, Double> whiteColumn = new TableColumn<>("% White");
    whiteColumn.setMinWidth(100);
    whiteColumn.setCellValueFactory(new PropertyValueFactory<>("white"));
    // % black
    TableColumn<County, Double> blackColumn = new TableColumn<>("% Black");
    blackColumn.setMinWidth(100);
    blackColumn.setCellValueFactory(new PropertyValueFactory<>("black"));
    // % asian
    TableColumn<County, Double> asianColumn = new TableColumn<>("% Asian");
    asianColumn.setMinWidth(100);
    asianColumn.setCellValueFactory(new PropertyValueFactory<>("asian"));
    // % hispanic
    TableColumn<County, Double> hispanicColumn = new TableColumn<>("% Hispanic");
    hispanicColumn.setMinWidth(100);
    hispanicColumn.setCellValueFactory(new PropertyValueFactory<>("hispanic"));
    // sex ratio
    TableColumn<County, Double> sexRatioColumn = new TableColumn<>("Men/women");
    sexRatioColumn.setMinWidth(100);
    sexRatioColumn.setCellValueFactory(new PropertyValueFactory<>("sexRatio"));
    // totalPop
    TableColumn<County, Integer> totalPopColumn = new TableColumn<>("Total Population");
    totalPopColumn.setMinWidth(100);
    totalPopColumn.setCellValueFactory(new PropertyValueFactory<>("totalPop"));
    // income
    TableColumn<County, Integer> incomeColumn = new TableColumn<>("Income Per Capita");
    incomeColumn.setMinWidth(100);
    incomeColumn.setCellValueFactory(new PropertyValueFactory<>("incomePerCapita"));
    // unemployment
    TableColumn<County, Double> unemployedColumn = new TableColumn<>("% Unemployed");
    unemployedColumn.setMinWidth(100);
    unemployedColumn.setCellValueFactory(new PropertyValueFactory<>("unemployed"));
    // poverty rate
    TableColumn<County, Double> povertyColumn = new TableColumn<>("Poverty Rate");
    povertyColumn.setMinWidth(100);
    povertyColumn.setCellValueFactory(new PropertyValueFactory<>("poverty"));
    // drive
    TableColumn<County, Double> driveColumn = new TableColumn<>("% Drivers");
    driveColumn.setMinWidth(100);
    driveColumn.setCellValueFactory(new PropertyValueFactory<>("drive"));
    // white collar
    TableColumn<County, Double> whiteCollarColumn = new TableColumn<>("% White Collar");
    whiteCollarColumn.setMinWidth(100);
    whiteCollarColumn.setCellValueFactory(new PropertyValueFactory<>("whiteCollar"));
    // ratio of private to public employment
    TableColumn<County, Double> prPuColumn = new TableColumn<>("Private/public employment");
    prPuColumn.setMinWidth(100);
    prPuColumn.setCellValueFactory(new PropertyValueFactory<>("prPuRatio"));
    // commute time
    TableColumn<County, Double> commuteColumn = new TableColumn<>("Mean Commute (min)");
    commuteColumn.setMinWidth(100);
    commuteColumn.setCellValueFactory(new PropertyValueFactory<>("meanCommute"));

    // counties must be in an observable list
    ObservableList<County> obsCounties = FXCollections.observableArrayList(counties);
    // set items in the table to the observable list and add the columns
    table = new TableView<>();
    table.setItems(obsCounties);
    table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    table.getColumns().addAll(countyColumn, stateColumn, whiteColumn, blackColumn, asianColumn,
        hispanicColumn, sexRatioColumn, totalPopColumn, incomeColumn, unemployedColumn,
        povertyColumn, driveColumn, whiteCollarColumn, prPuColumn, commuteColumn);
    table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    return table;
  }
  
  private void setupButtonTooltip(String text, Button button) {
  	Tooltip tip = new Tooltip(text);
  	tip.setStyle("-fx-font-size: 14");
  	tip.setShowDuration(new Duration((double) 200000));
  	Tooltip.install(button, tip);
  }
  
  private void setupRemovalButton(Button removeButton, Button query){
      removeButton.setOnAction(e -> {
          ObservableList<County> obsCounties = table.getSelectionModel().getSelectedItems();
          // iterate through obsCounties, get Integer keys for the County objects, and remove them
          // from the tree
          for (County current : obsCounties) {
            tree.remove(ACSReader.countyToID().get(current.getCountyName() + current.getState()));
          }
          // re-do the current query to update the table
          query.fire();
          Alerts.deletionDisplay(obsCounties);
        });    	  
  }
  
  private void setupVisualizeButton(Button button) {
      // also define visualize button functionality when a table is present
      button.setOnAction(e -> {
        ObservableList<County> obsCounties = table.getSelectionModel().getSelectedItems();
        if (obsCounties.size() == 1) {
          Visualization.visualizeWindow(obsCounties);
        }
      });	  
  }
  
  private void refreshFilterPage(List<County> counties, Button removeButton, Button visualizeButton, BorderPane root){
      // build a new table with the filtered counties
      table = getTable(counties);
      root.setCenter(table);
      // the removal and visualize buttons should only be visible when items are available to be
      // selected
      removeButton.setVisible(true);
      visualizeButton.setVisible(true);
  }
  
  private void setupSlidersCheckboxesActions(StreamManager streamer, BTree<Integer, County> tree, GridPane filters, Button query, Button removeButton, 
		  Button visualizeButton, BorderPane root){
      // sliders using the maximum and minimum values from StreamManager
      Slider white = generateSlider(streamer.boundsWhite());
      GridPane.setConstraints(white, 1, 0);
      Slider black = generateSlider(streamer.boundsBlack());
      GridPane.setConstraints(black, 1, 1);
      Slider asian = generateSlider(streamer.boundsAsian());
      GridPane.setConstraints(asian, 1, 2);
      Slider hispanic = generateSlider(streamer.boundsHispanic());
      GridPane.setConstraints(hispanic, 1, 3);
      Slider sexRatio = generateSlider(streamer.boundsSexRatio());
      GridPane.setConstraints(sexRatio, 1, 4);
      Slider totalPop = generateSlider(streamer.boundsTotalPop());
      GridPane.setConstraints(totalPop, 4, 0);
      totalPop.setMajorTickUnit(10000);
      totalPop.setShowTickMarks(false);
      Slider incomePerCapita = generateSlider(streamer.boundsIncomePerCapita());
      GridPane.setConstraints(incomePerCapita, 4, 1);
      incomePerCapita.setMajorTickUnit(1000);
      incomePerCapita.setShowTickMarks(false);
      Slider unemployed = generateSlider(streamer.boundsUnemployed());
      GridPane.setConstraints(unemployed, 4, 2);
      Slider poverty = generateSlider(streamer.boundsPoverty());
      GridPane.setConstraints(poverty, 4, 3);
      Slider drive = generateSlider(streamer.boundsDrive());
      GridPane.setConstraints(drive, 7, 0);
      Slider whiteCollar = generateSlider(streamer.boundsWhiteCollar());
      GridPane.setConstraints(whiteCollar, 7, 1);
      Slider prPuRatio = generateSlider(streamer.boundsPrPuRatio());
      GridPane.setConstraints(prPuRatio, 7, 2);
      Slider commute = generateSlider(streamer.boundsCommute());
      GridPane.setConstraints(commute, 7, 3);
      
      // labels with slider values
      Label whiteLabel = new Label();
      whiteLabel.textProperty().bind(Bindings.format("%.2f", white.valueProperty()));
      GridPane.setConstraints(whiteLabel, 2, 0);
      Label blackLabel = new Label();
      blackLabel.textProperty().bind(Bindings.format("%.2f", black.valueProperty()));
      GridPane.setConstraints(blackLabel, 2, 1);
      Label asianLabel = new Label();
      asianLabel.textProperty().bind(Bindings.format("%.2f", asian.valueProperty()));
      GridPane.setConstraints(asianLabel, 2, 2);
      Label hispanicLabel = new Label();
      hispanicLabel.textProperty().bind(Bindings.format("%.2f", hispanic.valueProperty()));
      GridPane.setConstraints(hispanicLabel, 2, 3);
      Label sexRatioLabel = new Label();
      sexRatioLabel.textProperty().bind(Bindings.format("%.2f", sexRatio.valueProperty()));
      GridPane.setConstraints(sexRatioLabel, 2, 4);
      Label totalPopLabel = new Label();
      totalPopLabel.textProperty().bind(Bindings.format("%.0f", totalPop.valueProperty()));
      GridPane.setConstraints(totalPopLabel, 5, 0);
      Label incomePerCapitaLabel = new Label();
      incomePerCapitaLabel.textProperty()
          .bind(Bindings.format("%.2f", incomePerCapita.valueProperty()));
      GridPane.setConstraints(incomePerCapitaLabel, 5, 1);
      Label unemployedLabel = new Label();
      unemployedLabel.textProperty().bind(Bindings.format("%.2f", unemployed.valueProperty()));
      GridPane.setConstraints(unemployedLabel, 5, 2);
      Label povertyLabel = new Label();
      povertyLabel.textProperty().bind(Bindings.format("%.2f", poverty.valueProperty()));
      GridPane.setConstraints(povertyLabel, 5, 3);
      Label driveLabel = new Label();
      driveLabel.textProperty().bind(Bindings.format("%.2f", drive.valueProperty()));
      GridPane.setConstraints(driveLabel, 8, 0);
      Label whiteCollarLabel = new Label();
      whiteCollarLabel.textProperty().bind(Bindings.format("%.2f", whiteCollar.valueProperty()));
      GridPane.setConstraints(whiteCollarLabel, 8, 1);
      Label prPuRatioLabel = new Label();
      prPuRatioLabel.textProperty().bind(Bindings.format("%.2f", prPuRatio.valueProperty()));
      GridPane.setConstraints(prPuRatioLabel, 8, 2);
      Label commuteLabel = new Label();
      commuteLabel.textProperty().bind(Bindings.format("%.2f", commute.valueProperty()));
      GridPane.setConstraints(commuteLabel, 8, 3);
      
      // checkboxes
      CheckBox whiteCheckBox = new CheckBox("% white");
      GridPane.setConstraints(whiteCheckBox, 0, 0);
      CheckBox blackCheckBox = new CheckBox("% black");
      GridPane.setConstraints(blackCheckBox, 0, 1);
      CheckBox asianCheckBox = new CheckBox("% asian");
      GridPane.setConstraints(asianCheckBox, 0, 2);
      CheckBox hispanicCheckBox = new CheckBox("% hispanic");
      GridPane.setConstraints(hispanicCheckBox, 0, 3);
      CheckBox sexRatioCheckBox = new CheckBox("sex ratio");
      GridPane.setConstraints(sexRatioCheckBox, 0, 4);
      CheckBox totalPopCheckBox = new CheckBox("total population");
      GridPane.setConstraints(totalPopCheckBox, 3, 0);
      CheckBox incomePerCapitaCheckBox =
          new CheckBox("income per capita \n" + "(total income($)/total population)");
      GridPane.setConstraints(incomePerCapitaCheckBox, 3, 1);
      CheckBox unemployedCheckBox = new CheckBox("% unemployed");
      GridPane.setConstraints(unemployedCheckBox, 3, 2);
      CheckBox povertyCheckBox = new CheckBox("% poverty");
      GridPane.setConstraints(povertyCheckBox, 3, 3);
      CheckBox driveCheckBox = new CheckBox("% drive");
      GridPane.setConstraints(driveCheckBox, 6, 0);
      CheckBox whiteCollarCheckBox = new CheckBox("% white collar");
      GridPane.setConstraints(whiteCollarCheckBox, 6, 1);
      CheckBox prPuRatioCheckBox = new CheckBox("private/public employment");
      GridPane.setConstraints(prPuRatioCheckBox, 6, 2);
      CheckBox commuteCheckBox = new CheckBox("mean commute (min)");
      GridPane.setConstraints(commuteCheckBox, 6, 3);

      query.setOnAction(e -> {
        // start from what is currently in the tree every time
        counties = tree.levelTraversal();
        // filter counties based on sliders with boxes checked
        // County objects with values less than the current slider value are selected
        if (whiteCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getWhite() <= white.getValue())
              .collect(Collectors.toList());
        }
        if (blackCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getBlack() <= black.getValue())
              .collect(Collectors.toList());
        }
        if (asianCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getAsian() <= asian.getValue())
              .collect(Collectors.toList());
        }
        if (hispanicCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getHispanic() <= hispanic.getValue())
              .collect(Collectors.toList());
        }
        if (sexRatioCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getSexRatio() <= sexRatio.getValue())
              .collect(Collectors.toList());
        }
        if (totalPopCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getTotalPop() <= totalPop.getValue())
              .collect(Collectors.toList());
        }
        if (incomePerCapitaCheckBox.isSelected()) {
          counties =
              counties.stream().filter(c -> c.getIncomePerCapita() <= incomePerCapita.getValue())
                  .collect(Collectors.toList());
        }
        if (unemployedCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getUnemployed() <= unemployed.getValue())
              .collect(Collectors.toList());
        }
        if (povertyCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getPoverty() <= poverty.getValue())
              .collect(Collectors.toList());
        }
        if (driveCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getDrive() <= drive.getValue())
              .collect(Collectors.toList());
        }
        if (whiteCollarCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getWhiteCollar() <= whiteCollar.getValue())
              .collect(Collectors.toList());
        }
        if (prPuRatioCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getPrPuRatio() <= prPuRatio.getValue())
              .collect(Collectors.toList());
        }
        if (commuteCheckBox.isSelected()) {
          counties = counties.stream().filter(c -> c.getMeanCommute() <= commute.getValue())
              .collect(Collectors.toList());
        }
        refreshFilterPage(counties, removeButton, visualizeButton, root);
      });
      
      filters.getChildren().addAll(whiteCheckBox, blackCheckBox, asianCheckBox, hispanicCheckBox,
    	        sexRatioCheckBox, totalPopCheckBox, incomePerCapitaCheckBox, unemployedCheckBox,
    	        povertyCheckBox, driveCheckBox, whiteCollarCheckBox, prPuRatioCheckBox, commuteCheckBox,
    	        whiteLabel, blackLabel, asianLabel, hispanicLabel, sexRatioLabel, totalPopLabel,
    	        incomePerCapitaLabel, unemployedLabel, povertyLabel, driveLabel, whiteCollarLabel,
    	        prPuRatioLabel, commuteLabel, white, black, asian, hispanic, sexRatio, totalPop,
    	        incomePerCapita, unemployed, poverty, drive, prPuRatio, commute, whiteCollar);
  }
  
  private void setupLoadFileItem(MenuItem openFile, Stage primaryStage) {
    openFile.setOnAction(e -> {
        // try to open the file; handle the case of improper selection
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter(
            "Spreadsheets identical in format " + "to acs2017_county_data_backup.csv", "*.csv"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
          filename = file.toString();
          // check that the first row is the same as acs2017_county_data.csv
          try {
            Scanner csvScnr = new Scanner(new File(filename));
            String row = csvScnr.nextLine();
            if (!row.equals("CountyId,State,County,TotalPop,"
                + "Men,Women,Hispanic,White,Black,Native,Asian,Pacific,VotingAgeCitizen,"
                + "Income,IncomeErr,IncomePerCap,IncomePerCapErr,Poverty,ChildPoverty,Professional,"
                + "Service,Office,Construction,Production,Drive,Carpool,Transit,Walk,"
                + "OtherTransp,WorkAtHome,MeanCommute,Employed,PrivateWork,PublicWork,SelfEmployed,"
                + "FamilyWork,Unemployment")) {
              Alerts.improperFile();
              csvScnr.close();
              return;
            }
            csvScnr.close();
            StreamManager fileStreamer = new StreamManager(filename);
            tree = fileStreamer.getTree();
          } catch (FileNotFoundException e1) {
            e1.printStackTrace();
          }
        }
      });
  }
  
  private void setupMainSearchButton(Button goButton, TextField countyMainText, TextField stateMainText) {
	    goButton.setOnAction(e -> {
	        // look for the input county and display pie charts; handle invalid user input
	        String countyKey = countyMainText.getText().trim() + stateMainText.getText().trim();
	        if (!ACSReader.countyToID.containsKey(countyKey)
	            || !tree.contains(ACSReader.countyToID.get(countyKey))) {
	          Alerts.unfindableCounty();
	          return;
	        }
	        Integer countyID = ACSReader.countyToID.get(countyKey);
	        County currentCounty;
	        try {
	          currentCounty = tree.getValue(countyID);
	          ObservableList<County> obsCounties = FXCollections.observableArrayList(currentCounty);
	          Visualization.visualizeWindow(obsCounties);
	        } catch (IllegalKeyException e1) {
	          // this is impossible to reach because of my first if condition, so I am really
	          // just getting the compiler to stop throwing a warning
	        }
	      });	  
  }
  
  private BorderPane createMainLayout(Button goButton, Button filterButton, Label mainDirections, TextField countyMainText, 
		  TextField stateMainText, MenuBar mainMenuBar) {
    // child layout set-up
    HBox mainBottom = new HBox(goButton, filterButton);
    // margin of the hbox relative the edge of the application
    BorderPane.setMargin(mainBottom, new Insets(0, 0, 10, 10));
    mainBottom.setSpacing(10); // spacing between controls in the hbox
    VBox mainCenter = new VBox(mainDirections, countyMainText, stateMainText);
    BorderPane.setMargin(mainCenter, new Insets(100, 10, 10, 10));
    mainCenter.setSpacing(10);
    
    // parent layout set-up
    BorderPane root = new BorderPane();
    root.setCenter(mainCenter);
    root.setBottom(mainBottom);
    root.setTop(mainMenuBar);
	    
	return root;
  }
}
