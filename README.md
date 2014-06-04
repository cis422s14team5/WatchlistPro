WatchistPro
===========
A program to create a list of movies and TV shows the user has seen, are watching, or plan to watch.

Introduction
------------
WatchListPro was designed by the team The Dragons. WatchListPro is a program that allows the user to make a list of movies and TV shows they have seen, are watching,or plan to watch. To add additional value to using this app over a simple list, the program provides a way for the user to enter information about each movie or TV show. This includes fields for the title, whether it has been watched, description, ratings, genre, director, and other information. Some additional desired features include searching the currently open Watch List, letting the user tell the system to attempt to automatically fill in all the fields by requesting information from a public database, and saving your Watch Lists onto a server.

Installation
------------
The program is designed to run on a machine that has Java 8.
Once you have Java, you can download the WatchListPro .jar file.

Opening the program
-------------------
To open up the application, double click on the .jar file titled WatchListPro, and the application will run. If your system refuses to run it try right clicking and specify Open With > Jar Launcher or some variation. It is also possible to open this with the Terminal program. To do this, navigate to the file that the .jar is located inside using the command line, and type in the command java -jar WatchListPro.jar, and the jar file will open.

Using the program
-----------------
WatchListPro, automatically opens a blank Media Viewer the first time the application is run. In every subsequent launch, the program will load the TV shows and movies that have already been added to your media list. The first launch of the application should look like this:

Main View
---------
The main view is the initial window that loads when the user runs the program. This is the view where the user navigate through the list of entries once they have been added.

Adding a Film
-------------
From the Main View make sure that the button below the text field displays the words "Add Film". If not, then select the down arrow next to the "Add TV Show" button, and select the option that says "Add Film" from the drop down list. Then the user types in the title of the film into the text field that says "Enter Name of Film to Add". Use either the enter key or press the "Add Film" button and the entry should be added to the list of entries and editable. Doing this will automatically open the new entry in the view and change it into Editing a Film/TV Show mode.

Adding a TV Show
----------------
From the Main View make sure that the button below the text field displays the words "Add TV Show". If not, then select the down arrow next to the "Add Film" button, and select the option that says "Add TV" from the drop down list. Then the user types in the title of the TV Show into the text field that says "Enter Name of TV Show to Add". Use either the enter key or press the "Add TV Show" button and the entry should be added to the list of entries and editable. Doing this will automatically open the new entry in the view and change it into Editing a Film/TV Show mode.

Selecting a Film/TV Show from the list
--------------------------------------
On the left side of the window of the main view there is a box that contains all entries of shows and films. Click on an item in the list to select it. The view on the right side of the window will change to display information on the specified TV show or film. Above this list is a drop down list that displays the text "View All" as default. Selecting this item will bring up three options to choose from: "View All", "View Films Only", and "View TV Shows Only". "View All" allows the user to display, choose from, and sort through their whole list of media. The "View Films Only" option changes the list to only display films from the user's list. The "View TV Shows Only" option changes the list to only display TV shows from the user's list.

Deleting a Film/TV Show
-----------------------
In the list of media on the left side of the window, the user selects the film or TV show they wish to delete and then clicks the "Delete" button. This will remove it from the list permanently.

Searching for a Film/TV Show from the list
------------------------------------------
On the left side of the window on the main view there is a text field that has the text "Enter Title to Search" in it. Type in the title of the entry you wish to search for and it will narrow down the entries from the list until the one being searched for is found, or if it is not found, then the list will be blank. The contents of the list will depend on which option the user has selected in the drop down list above the media list. 

Editing a Film/TV Show
----------------------
Select an entry from the list of films/TV shows on the left side of the window of the main view and then click the "Edit" button on the top right side of the window. This will change the view of the selected item from a display view of information, to a list of text fields allowing the user to edit information on the film/TV show. In addition to the text fields is a check box that asks whether the user has watched the film or TV show. Once the user has entered in all the information they want, they must press the "Done" button to save their changes. 

Important Note: Clicking on another media from the list will clear the fields of any data you have entered into them.

Fetching Content from the server
--------------------------------
While in the Editing pane, the program allows a user to fetch information on the media from the server using the "Fetch" button. Once pressed, the program will take the entered name of the film or TV show and query a database about information regarding it, such as run time, genre, description, and other information. If the title is not found, it will return nothing. When fetching information about a TV show, the program will populate the table at the bottom of the pane with all of the seasons and episodes from the fetched show. From here, the user can check each episode of season that has been watched.
 Important Note: Using this button will overwrite any information in all of the fields.

Saving a Watch List
-------------------
From the main view go to the file option under "File" at the top of the program and scroll down to "Save" or "Save as...". Choosing "Save" will save the contents of the currently open Watch List. Choosing "Save as..." will bring up a file navigator menu which allows the user to save the currently opened Watch List under a new title, and will then change the open file to be the newly named file. 
 
Creating an account
-------------------
From the main view go to the file option under "Server" at the top of the program and scroll down to "Create Account". This will open a pane that allows you to create an account.  Enter a username and password that you will use to log in. The program will only allow a user name that is more than two and less than thirteen characters, only contains numbers or letters, and has no special characters. There are no limitations on  the password. Press the "Create Account" Button to create the new account. Choosing this option will automatically log in the user if the new account is valid.

Logging In
----------
From the main view go to the file option under "Server" at the top of the program and scroll down to "Login". This will open a pane that allows you to login to an existing account.  Enter your username and password, then press the "Login" Button to login to account.
 
Saving to the Server
--------------------
From the main view go to the file option under "Server" at the top of the program and scroll down to "Save to Server". The user must be logged in to access this feature. This will save your currently open Watch List to the server. If there is already a Watch List on the server with the same name, the program will bring up a pop up box where the user should enter a new name for the file. If the user decides to keep the same name, it will overwrite the already named file on the server. If a new name is chosen, this new name will be reflected locally, and change the currently open file to be the new file name.

Loading from the Server
-----------------------
From the main view go to the file option under "Server" at the top of the program and scroll down to "Open from Server". The user must be logged in to access this feature. This will load data from all of the Watch Lists that had been saved previously to the server, and opens up a new panel for the user to look at of all the files saved on the server. Choosing one of the files will load that to the user's computer. If the loaded file exists locally it will overwrite the local copy of the file, and if it does not exist, the program will create a new file with that name and then open it .

Creating a New File
-------------------
From the main view go to the file option under "File" at the top of the program and scroll down to "New...". This will open up a new Watch List for you to use.
Important Note: This will not save your previously edited Watch List, so save before hand.

Opening an Already Created File/Opening Recently Opened File
------------------------------------------------------------
From the main view go to the file option under "File" at the top of the program and scroll down to "Open..." or "Open Recent". Choosing "Open..." will bring up a file navigator menu which allows the user to open a previously saved Watch List. Choosing "Open Recent" will bring up a submenu that contains the ten most recently opened files.
Important Note: This will not save your previously edited Watch List, so save before hand.
 
Quitting the program
--------------------
To quit the program simply click the "x" box at the top of the window, or from the main view go to the file option under "File" at the top of the program and scroll down to "Close".

Encountering an Error
---------------------
Although no errors should ever be present in WatchListPro, if one was encountered, the best way to solve it is to delete any old Watch List files by navigating to the file WatchLists in the home directory of the computer. Deleting these files should solve any errors if they are found.
