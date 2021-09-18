Execute the program by running the following command in your terminal of choice: <path_to_java.exe> -jar ACS_Browser_JoshRawlins.jar


Note that JavaFX is deprecated in Java 11 onward, so you will need to use Java 10 or older.

Final description: This project reads in a list of counties in the US using American Community Survey 
		data. You can filter through the data in a table, edit the table, and output the modified 
		tree to a file. You can also visualize information on single counties via pie-chart from the main
		screen or through selecting a single row from the aforementioned table.

Updates since 12/10/2019
12/14/2019: A table view was added to the filter page, along with an option for removing objects. 
Bugs in root fusion and deletion cases of theBTree were corrected. 
12/15/2019: I added an alert window which appears upon deletion, a mechanism for saving the results
of the user's query/deletions, a CSV writer, several alert boxes, a search function from the main menu,
and general GUI formatting. I also did JUnit testing of the B-tree.

What does it currently do and what can you except to see?
Running Main.java (or the executable jar file) should output a window like Main.PNG, which is
connected to the one shown in Secondary.PNG via the "Filter" button. I had to run the executable 
jar file from a command line. Running BTree.java alone will show the result of testing removal and 
insertion into the tree with random numbers, which is formatted as below. Note: you may have to run 
BTree.java a few times to get passed a duplicate key exception. Only the "Filter", "Home", and 
"Apply filters" buttons are wired. The tree is read in with ACSReader and stored in the 
StreamManager object. 

What work remains to be done?
Formal JUnit tests of the B-tree.
Addition and removal of an object in the GUI.
Button to write the modified data to a file.
?Add a more creative computation than gender ratio or private vs. public employment.?
Alert the user to save their file when they attempt to exit.
Add help menu information and/or alert messages (such as when no results match the query).
?Graphic implementation of searches (in-GUI table view)?

Change Log for Project Check-in #2: December 10, 2019
11/30/2019: County.java was coded.
12/01/2019: I modified the B-tree such that each node contains key-value pairs. 
			The B-tree is maintained based on the keys. I also added
			CSV parsing functionality in ACSReader.java, which included creation of County objects
			and their insertion into the B tree as values. A map was added to ACS reader to 
			map from county and state to the unique IDs in the data.
12/02/2019: I partially completed the main menu. The controls do not actually do anything yet.
12/04/2019: Menu items were added and buttons were repositioned. Event handling was added to the
			"Filter" button to redirect to the filtering page. Calculations were added to County.java.
			The filtering page was partially completed.
12/05/2019: A traversal method was added to the B-tree to get a list of counties in the tree for
			use with streams. I also created a StreamManager class to segregate slider min and max
			computations. 
12/06/2019: Sliders were added to the filter page GUI, along with check boxes to mark
			whether their values should be included in filtering, and labels to actively show their
			values.
12/08/2019: List filtering was added. 
12/09/2019: I completed a large part of the code for B-tree removal.
12/10/2019: The B-tree remove method was completed.




Initial comments (Nov. 26, 2019)
Title: US County Profiler

This program takes as its input a CSV file containing 2017 American Community Survey information.
The user will be able to search for statistical summaries of specific US counties, sort all 
counties based on individual continuous variables and state, and 
view counties which meet a predefined set of search criteria that cannot be found by the more basic 
filter, giving results like "top 20 counties with the best alternative transportation
support" or "top 20 counties with the best economic prospects." 
At any point the user will be able to add counties to an in-application list of saved counties and 
save them to a json file. Time permitting, the saved list may be used to compare counties side by 
side while highlighting values based on user specified desirability criteria. Also with time permitting, 
the user will be able to modify the information to be output to the json file - 
removing certain variables and manually adding other desired information.

Data structure: B-tree of key, value pairs with adjustable minimum degree. 
It is currently configured as a 2-3-4 tree. Key = county ID, value = county object. 

My program currently outputs a test of its insert method to the console when run in Eclipse (Java SE 1.8)
I have annotated it below.
------------------------------------------
[665]root
               [77, 274]child(0) 
                              [19, 34, 57]child(0)
                                             [4]
                                             [24, 33]
                                             [38, 56]
                                             [76]
                              [88, 194]child(1)
                                             [83]
                                             [93, 96, 139]
                                             [261]
                              [429, 644]child(2)
                                             [344]
                                             [526, 541, 556]
                                             [652, 663]
               [824]child(1)
                              [768]child(3)
                                             [680, 737, 747]
                                             [774, 781, 806]
                              [876]child(4)
                                             [834]
                                             [957, 991]
------------------------------------------
Done
