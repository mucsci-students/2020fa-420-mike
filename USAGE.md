# Running Team mike's UML Editor Application

	1. Navigate to the directory with the files and have a command prompt ready
	2. gradle build
	3. gradle shadowJar
	4. java -jar ./build/libs/mike-all.jar (for GUI) or java -jar ./build/libs/mike-all.jar (for CLI)
	
# CLI commands
	save <name>.json (optional <path>) - Save file to specific path
  	load <path> - Loads a file at a specific path

  	create class <name> - create a class with title <name>
  	create field <class name> <field type> <field name> - create a field in <class name> with type <field type> titled <field name>
  	create method <class name> <method type> <method name> - create a method in <class name> with type <method type> titled <method name>
  	create rel <type> <class name1> <class name2> - create a relationship between <class name1> and <class name2> with type <type> (Aggregation, Realization, Composition, Inheritance)
  	create param <class name> <method> <parameter type> <parameter name> - create a parameter in <class name> for <method> with type <parameter type> titled <parameter name>

  	delete class <name> - delete a class with title <name>
  	delete field <class name> <field name> - delete field <field name> in class titled <class name>
  	delete method <class name> <method name> - delete method <method name> in class titled <class name>
  	delete rel <type> <class name1> <class name2> - delete a relationship with type <type> (Aggregation, Realization, Composition, Inheritance) between <class name1> and <class name2>
  	delete param <class name> <method name>, <parameter name> - delete a parameter in <class name> for <method name> with  <parameter name>

  	rename class <name> <newname> - rename class <name> to <new name>
  	rename field <class name> <field name> <newname> - rename field <field name> to <newname> in class titled <class name>
  	rename method <class name> <method name> <newname> - rename method <method name> to <newname> in class titled <class name>
  	rename param <class name> <method name> <parameter name> <parameter newname> - rename parameter in <class name> for <method> titled <parameter name> to <parameter newname>

  	list classes - List all existing classes
  	list relationships - List all existing relationships
  	list all - List all existing classes and relationships

  	clear - clear all classes and relationships
  	help - prints list of commands and their usage
  	quit - exits the program

# Using the GUI