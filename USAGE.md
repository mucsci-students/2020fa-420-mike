# Running Team mike's UML Editor Application

	1. Navigate to the directory with the files and have a command prompt ready
	2. gradle build
	3. gradle shadowJar
	4. java -jar ./build/libs/mike-all.jar (for GUI) or java -jar ./build/libs/mike-all.jar (for CLI)
	
# CLI commands
	save <name>.json (optional <path>) - Save file to specific path or to current directory if no path is specified
  	load <path> - Loads a file from an absolute path

  	create class <name> - create a class with title <name>
  	create field <class name> <field type> <field name> - create a field in <class name> with type <field type> titled <field name>
  	create method <class name> <method type> <method name> - create a method in <class name> with type <method type> titled <method name>
  	create relationship <type> <class name1> <class name2> - create a relationship between <class name1> and <class name2> with type <type> (Aggregation, Realization, Composition, Inheritance)
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

  	list classes - List all existing classes
  	list relationships - List all existing relationships
  	list all - List all existing classes and relationships

  	clear - clear all classes and relationships
  	help - prints list of commands and their usage
  	quit - exits the program

### Tab Completion
	The CLI comes with tab completion. Hitting tab before typing presents a list of valid commands. 
	Hitting tab after typing in text will present a list of valid commands, or autocomplete if there is only one valid command left.

# Using the GUI

### Topbar buttons:
	
	Save:
		Saves progress made since last save to the file that was specified using Save As.
		If Save As was not called beforehand, Save will have the same functionality as Save As.

	Save As:
		Saves progress to a file name in a specified directory. If no directory is specified, the file is saved
		in the current working directory. Files can be saved as json with the .json extension.

	Load:
		Loads the saved state of a UML diagram from within a specified filepath. If only the file name is given,
		the file will try to be found in the current working directory.

	Add Class:
		Adds a class with the specified name to the GUI. If a class with the specified name already exists,
		the user will be alerted so.

	Enable/Disable Edit Mode:
		When Enable Edit Mode is clicked, it gives the user the ability to edit a class by clicking on it. More
		on the buttons and options available with edit mode is listed later in this document. In edit mode,
		the ability to Save/Save As/Load are disabled to prevent saving changes that are still being made.
		When in edit mode, the "Enable Edit Mode" button turns to a "Disable Edit Mode" button, 
		which exits edit mode when pressed.

### GUI Edit Mode


