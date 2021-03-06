# Running Team mike's UML Editor Application

	1. Navigate to the directory with the files and have a command prompt ready
	2. gradle build
	3. gradle shadowJar
	4. java -jar ./build/libs/mike-all.jar (for GUI) or java -jar ./build/libs/mike-all.jar cli (for CLI)
	
## Switching between the CLI and GUI

	1. Save progress to a file on your computer
	2. Quit the current program
	3. Run the desired interface (GUI or CLI)
	4. Load the file that you saved earlier
	
# CLI commands
	save <filepath> - Save progress to a file with a specific path or to the current directory if no path is specified. Adding .json to the end of the file will save it as a JSON file.
  	load <filepath> - Loads saved progress from an absolute file path. If just the name of the file is given, then the program will try to find the file in the current directory. Add .json to the end of the file name if it is a JSON file.

  	create class <name> - create a class with title <name>
  	create field <class name> <field visibility> <field type> <field name> - create a field in <class name> with visibility <field visibility> (public, private, protected), type <field type> titled <field name>
  	create method <class name> <method visibility> <method type> <method name> - create a method in <class name> with visibility <method visibility> (public, private, protected), type <method type> titled <method name>
  	create relationship <type> <class name1> <class name2> - create a relationship between <class name1> and <class name2> with type <type> (Aggregation, Realization, Composition, Inheritance). Recursive relationships can be created.
  	create parameter <class name> <method> <parameter type> <parameter name> - create a parameter in <class name> for <method> with type <parameter type> titled <parameter name>

  	delete class <name> - delete a class with title <name>
  	delete field <class name> <field name> - delete field <field name> in class titled <class name>
  	delete method <class name> <method name> - delete method <method name> in class titled <class name>
  	delete relationship <type> <class name1> <class name2> - delete a relationship with type <type> (Aggregation, Realization, Composition, Inheritance) between <class name1> and <class name2>
  	delete parameter <class name> <method name>, <parameter name> - delete a parameter in <class name> for <method name> with  <parameter name>

  	rename class <name> <newname> - rename class <name> to <new name>
  	rename field <class name> <field name> <newname> - rename field <field name> to <newname> in class titled <class name>
  	rename method <class name> <method name> <newname> - rename method <method name> to <newname> in class titled <class name>
  	rename parameter <class name> <method name> <parameter name> <parameter newname> - rename parameter in <class name> for <method> titled <parameter name> to <parameter newname>

  	settype field <class name> <field name> <newtype> - set type of field <field name> in <class name> to <type>
  	settype method <class name> <method name> <newtype> - set type of method <method name> in <class name> to <type>
  	settype parameter <class name> <method name> <parameter name> <newtype> - set type of parameter <class name> in <method name> titled <parameter name> to <type>

	setvis field <class name> <field name> <visibility> - set visibility of field <field name> in <class name> to <visibility> (public, private, protected)
	setvis method <class name> <method name> <visibility> - set visibility of method <method name> in <class name> to <visibility> (public, private, protected)

  	list classes - List all existing classes
  	list relationships - List all existing relationships
  	list all - List all existing classes and relationships

  	clear - clear all classes and relationships
  	undo - Reverts the most recent change to the UML Editor
  	redo - Restores the most recently undone action.
  	help - prints list of commands and their usage
  	quit - exits the program

### Tab Completion
	
The CLI comes with tab completion. Hitting tab before typing presents a list of valid commands, as such:

![Tab Options](https://github.com/mucsci-students/2020fa-420-mike/blob/develop/images/cli-tab-options.PNG)
	
Hitting tab after typing in text will present a list of valid commands, as such:

![c Tab Options](https://github.com/mucsci-students/2020fa-420-mike/blob/develop/images/c-tab-options.PNG)
	
If there is only one valid comment left, hitting tab will autocomplete the command, as such:

![Pre Tab Create](https://github.com/mucsci-students/2020fa-420-mike/blob/develop/images/pre-tab-create.PNG)

![Post Tab Create](https://github.com/mucsci-students/2020fa-420-mike/blob/develop/images/post-tab-create.PNG)

# Using the GUI

## Topbar buttons:
	
	Save:
		Saves progress made since last save to the file that was specified using Save As.
		If Save As was not called beforehand, Save will have the same functionality as Save As.

	Save As:
		Opens a file chooser and allows the user to save to a selected directory. Optionally, files can be saved in JSON format by adding .json to the end of the file name.

	Load:
		Opens a file chooser and allows the user to select and load a saved version of a UML diagram.

	Undo:
		Reverses the most recent command performed on the UML diagram.

	Redo:
		Reverses the last UNDO command.

	Add Class:
		Adds a class with the specified name to the GUI. If a class with the specified name already exists,
		the user will be alerted so.

	Enable/Disable Edit Mode:
		When Enable Edit Mode is clicked, it gives the user the ability to edit a class by clicking on it. More
		on the buttons and options available within edit mode are listed later in this document. In edit mode,
		the ability to Save/Save As/Load are disabled to prevent saving changes that are still being made.
		When in edit mode, the "Enable Edit Mode" button turns to a "Disable Edit Mode" button, 
		which exits edit mode when pressed.

## GUI Edit Mode

### Adding items:

	Adding fields:
		Under the area of a class labeled "Fields" are two text boxes and a drop down menu. The drop down menu is for
		selecting the visibility type. The left text box is for the field type and the right one is for the field name. 
		Enter the desired information into these text boxes and hit the blue '+' located to the right of the text boxes 
		to add the field. Each time a field is added, new boxes will appear with the ability to add more fields.

	Adding methods:
		Under the area of a class labeled "Methods" are two text boxes and a drop down menu. The drop down menu is for
		selecting the visibility type. The left one is for the method type and the right one is for the method name. 
		Enter the desired information into these text boxes and hit the blue '+' located to the right of the text boxes 
		to add the method. Each time a method is added, new boxes will appear with the ability to add more methods.

	Adding parameters:
		After creating a method, two text boxes will be located under the method preceded with "----". These lines 
		help decipher which parameters belong with which method. The left text box is for the parameter type
		and the right one is for the parameter name. Enter the desired information into these text boxes and hit
		the blue '+' located to the right of the text boxes to add the parameter. Each time a parameter is added,
		new boxes will appear with the ability to add more parameters.

	Adding relationships:
		When a class is clicked on in edit mode, a new "Create Relationship" button will appear
		along the top. Press on the button to create a relationship for the class that is currently in 
		edit mode. A pop up will appear asking for the type of the relationship and the class to link it to.
		When these both selections are made, hit OK and a line will be drawn between the classes
		representing the relationship. If it is a recursive relationship, a circular line will be drawn from the
		class to itself.
		
### Renaming items:

	Renaming a class:
		Enter the new name of the class into the text field where the previous class name is located.
		Then hit the "Save" button at the top of the class to confirm changes.

	Renaming fields:
		When in edit mode, click on the class containing the fields that you wish to rename.
		Under the section labeled "Fields", there will be two text boxes for each field. The left one
		is for the type and the right one is for the name. Enter the new type and name into these
		text boxes then hit the "Save" button at the top of the class.

	Renaming methods:
		When in edit mode, click on the class containing the methods that you wish to rename.
		Under the section labeled "Methods" there will be two text boxes for each method. The left one
		is for the type and the right one is for the name. Enter the new type and name into these
		text boxes then hit the "Save" button at the top of the class.

	Renaming parameters:
		When in edit mode, click on the class containing the parameters that you wish to rename.
		Under each method, there will be two text boxes for each parameter. The left one
		is for the type and the right one is for the name. Enter the new type and name into these
		text boxes then hit the "Save" button at the top of the class.
		
### Editing items:
		
	Editing field types:
		When in edit mode, click on the class containing the fields you wish to edit the type of.
		Under the section labeled "Fields", there will be two text boxes for each field. The left text box contains 
		the field's type. Type in the desired type, then click the "Save" button at the top of the class.
	
	Editing method types:
		When in edit mode, click on the class containing the methods you wish to edit the type of.
		Under the section labeled "Methods", there will be two text boxes for each method. The left text box contains 
		the method's type. Type in the desired type, then click the "Save" button at the top of the class.
	
	Editing parameter types:
		When in edit mode, click on the class containing the parameters you wish to edit the type of.
		Under each method, there will be two text boxes for each parameter. The left text box contains 
		the parameter's type. Type in the desired type, then click the "Save" button at the top of the class.
		
	Editing field visibility:
		When in edit mode, click on the class containing the fields you wish to edit the visibility of.
		Under the section labeled "Fields", on the left side of each field will be a drop down menu containing
		each of the visibility types. Select the desired visibility type, then click the "Save" button at the top of the class.
		
	Editing method visibility:
		When in edit mode, click on the class containing the methods you wish to edit the visibility of.
		Under the section labeled "Methods", on the left side of each method will be a drop down menu containing
		each of the visibility types. Select the desired visibility type, then click the "Save" button at the top of the class.
		
	Saving changes made to a class:
		When clicking on a class in edit mode, a button will appear on the top of the class that says 
		"Save". When you are done editing the class, hit the save button to save changes made to that
		class.

	Canceling changes made to a class:
		When clicking on a class in edit mode, a button will appear on the top of the class that says 
		"Cancel". If you do not want to keep the changes you made, hit the cancel button to cancel 
		changes made to that class.

### Deleting items:

	Deleting classes:
		When in edit mode, click on the class you wish to delete. There will be a button marked 
		with an 'X' to the left of the class name. Click on this button, and a pop up will appear asking
		if you are sure that you want to delete the class. Click OK and the class will disappear.

	Deleting fields:
		When in edit mode, click on the class containing the fields that you wish to delete.
		To the left of each field will be a blue 'X' button. Click on this button to delete the field.

	Deleting methods:
		When in edit mode, click on the class containing the methods that you wish to delete.
		To the left of each method will be a blue 'X' button. Click on this button to delete the method.

	Deleting parameters:
		When in edit mode, click on the class containing the parameters that you wish to delete.
		To the left of each parameter will be a blue 'X' button. Click on this button to delete the parameter.

	Deleting relationships: 
		When a class is clicked on in edit mode, a new "Delete Relationship" button will appear
		along the top. Press on the button to receive a drop-down menu of all the relationships
		associated with that class. Select which relationship you desire to delete, then hit OK.
		The line that was previously drawn to the represent the relationship will now disappear.

## GUI Keybindings
	
	Save: CTRL+S

	Undo: CTRL+Z

	Redo: CTRL+Y
	
