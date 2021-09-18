//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Alerts
// Files:           BTree.java, FileSaver.java
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

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * This class defines pop-up windows with the following functions described in the order where 
 * their methods appear in the class definition:
 * 1. Tell the user what was just deleted.
 * 2. Provide a window for saving the reduced county list when the user clicks the save button.
 * 3. Provide a window for warning the user against losing their changes and giving an opportunity
 * to save.
 * 4. Window to handle the case where the county input by the user in the main menu cannot be found 
 * in the tree.
 * 5. Window to handle case where the user attempts to read in an improperly formatted file.
 */
public class Alerts {
  
  /*
   * This method builds the window informing the user of what was just deleted from the 
   * filter page table.
   * @parameter ObservableList<Count> counties - counties selected for removal
   */
  public static void deletionDisplay(ObservableList<County> counties) {
    Stage window = new Stage();

    // Make the user look at this window
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Items Deleted");
    window.setMinWidth(300);
    window.setMinHeight(500);

    // label describing removal
    Label label = new Label();
    String labelText = "";
    // if 5 or fewer items were deleted, name them explicitly
    if (counties.size() <= 5) {
      for (County county : counties) {
        labelText = labelText + county.getCountyName() + ", " + county.getState() + "\n";
      }
      label.setText(labelText);
    }
    // if more than five were deleted, name the first five and tell the total number deleted
    else {
      labelText =
          "First deleted: " + counties.get(0).getCountyName() + ", " + counties.get(0).getState()
              + "\n" + "Last deleted: " + counties.get(counties.size() - 1).getCountyName() + ", "
              + counties.get(counties.size() - 1).getState() + "\n" + "Total deleted: "
              + counties.size();
      label.setText(labelText);
    }
    // acknowledgement
    Button closeButton = new Button("Acknowledge");
    closeButton.setOnAction(e -> window.close());

    // layout
    VBox layout = new VBox(10);
    layout.getChildren().addAll(label, closeButton);
    layout.setAlignment(Pos.CENTER);

    // display and wait
    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.showAndWait();
  }

  /*
   * This methods builds the window to handle normal saving.
   * It also calls to FileSaver to write the current tree.
   * @param BTree<Integer, County> tree - current tree to be written
   */
  public static void saveWindow(BTree<Integer, County> tree) {
    Stage window = new Stage();

    // Make the user look at this window
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Save file");
    window.setMinWidth(500);
    window.setMinHeight(500);

    // labels
    Label warningLabel = new Label("You will save the entire database, less what you removed.\n"
        + "Your file will be saved to the working directory of the jar file\n"
        + "which must remain with all other supporting files.\n" + "Use the format filename.csv.");
    Label textLabel = new Label("File name:");

    // TextField for user input of the output file name
    TextField userInput = new TextField();

    // confirmation and cancel buttons
    Button confirmButton = new Button("Confirm");
    confirmButton.setOnAction(e -> {
      // make sure that the formatting is write to avoid an exception
      if (userInput.getText().contains(".csv")) {
        FileSaver.write(userInput.getText(), tree);
        window.close();
      }
    });
    // cancel saving
    Button cancelButton = new Button("Cancel");
    cancelButton.setOnAction(e -> window.close());

    // layouts management
    BorderPane root = new BorderPane();
    HBox buttons = new HBox(confirmButton, cancelButton);
    buttons.setSpacing(10);
    HBox mid = new HBox(textLabel, userInput);
    VBox top = new VBox(warningLabel);
    BorderPane.setMargin(buttons, new Insets(10, 10, 10, 10));
    BorderPane.setMargin(mid, new Insets(10, 10, 10, 10));
    BorderPane.setMargin(top, new Insets(10, 10, 10, 10));
    root.setTop(top);
    root.setCenter(mid);
    root.setBottom(buttons);

    // display and wait
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.showAndWait();
  }

  /*
   * This method builds the window for saving when the user attempts to close the program.
   * Again, FileSaver is called for writing.
   * @param BTree<Integer, County> tree - current tree
   */
  public static void closeAttemptWindow(BTree<Integer, County> tree) {
    Stage window = new Stage();

    // Make the user look at this window
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Save file");
    window.setMinWidth(500);
    window.setMinHeight(500);

    // labels
    Label warningLabel =
        new Label("Warning: you are about to lose any changes to your county list!\n"
            + "Save your current county list below, or continue exiting.\n"
            + "You will save the entire database, less what you removed.\n"
            + "Your file will be saved to the directory of this jar file and its supporting files.\n"
            + "Use the format 'filename.csv.'");
    Label textLabel = new Label("File name:");

    // TextField for user input of the output file name
    TextField userInput = new TextField();

    // confirmation and cancel buttons
    Button confirmButton = new Button("Save changes to file");
    confirmButton.setOnAction(e -> {
      // force the user to give a proper filename
      if (userInput.getText().contains(".csv")) {
        FileSaver.write(userInput.getText(), tree);
        window.close();
      }
    });
    Button cancelButton = new Button("Exit without saving");
    cancelButton.setOnAction(e -> window.close());


    // layout management
    BorderPane root = new BorderPane();
    HBox buttons = new HBox(confirmButton, cancelButton);
    buttons.setSpacing(10);
    HBox mid = new HBox(textLabel, userInput);
    VBox top = new VBox(warningLabel);
    BorderPane.setMargin(buttons, new Insets(10, 10, 10, 10));
    BorderPane.setMargin(mid, new Insets(10, 10, 10, 10));
    BorderPane.setMargin(top, new Insets(10, 10, 10, 10));
    root.setTop(top);
    root.setCenter(mid);
    root.setBottom(buttons);


    // display and wait
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.showAndWait();
  }

  /*
   * Window to display when the user's input county cannot be found.
   */
  public static void unfindableCounty() {
    Stage window = new Stage();

    // Make the user look at this window
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("County not found.");
    window.setMinWidth(500);
    window.setMinHeight(500);

    // label
    Label warningLabel = new Label("Your county was not found. Make sure to enter the county name\n"
        + "based on its official name (e.g. Dane County) and the same for the state (e.g. Wisconsin).\n"
        + "If formatting it as such does not work, you may have misspelled it, you already removed\n"
        + "it and you should reload the original file, or the county is one of the rare exceptions\n"
        + "with a differently formatted name (e.g. several in Puerto Rico)");

    // confirmation 
    Button confirmButton = new Button("Okay");
    confirmButton.setOnAction(e -> {
      window.close();
    });

    // layouts
    BorderPane root = new BorderPane();
    HBox buttons = new HBox(confirmButton);
    HBox mid = new HBox(warningLabel);
    root.setCenter(mid);
    root.setBottom(buttons);

    // display and wait
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.showAndWait();
  }

  /*
   * Window to display when the user attempts to provide an unacceptable file.
   */
  public static void improperFile() {
    Stage window = new Stage();

    // Make the user look at this window
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Wrong File Structure");
    window.setMinWidth(500);
    window.setMinHeight(500);

    // label
    Label warningLabel = new Label(
        "That was an improper file. \n" + "Try again with one like acs2017_county_data.csv");

    // confirmations
    Button confirmButton = new Button("Okay");
    confirmButton.setOnAction(e -> {
      window.close();
    });

    // layouts
    BorderPane root = new BorderPane();
    HBox mid = new HBox(warningLabel);
    BorderPane.setMargin(confirmButton, new Insets(10, 10, 10, 10));
    BorderPane.setMargin(mid, new Insets(10, 10, 10, 10));
    root.setCenter(mid);
    root.setBottom(confirmButton);

    // display and wait
    Scene scene = new Scene(root);
    window.setScene(scene);
    window.show();

  }
}

