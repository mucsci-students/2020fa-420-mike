package mike.cli;

import java.io.IOException;
import java.util.Scanner;

import mike.datastructures.Classes;

public class CommandLine extends HelperMethods {
	
	public CommandLine() {
	}

	public static void commandInterface() {
		Classes userClasses = new Classes();
		boolean prompt = false;
		String[] commandUsage = {
			"\n  save <name>.json (optional <path>)", 
			"\n  load <path>",
			"\n  create class <name>",
			"\n  create field <class name> <field type> <field name>",
			"\n  create method <class name> <method type> <method name>",
			"\n  create rel <name> <class name1> <class name2>",
			"\n  create param <class name> <method> <parameter type> <parameter name>",
			"\n  delete class <name>",
			"\n  delete field <class name> <field name>",
			"\n  delete method <class name> <method name>",
			"\n  delete rel <name> <class name1> <class name2>",
			"\n  delete param <class name> <method name>, <parameter name>",
			"\n  rename class <name> <newname>",
			"\n  rename field <class name> <field name> <newname>",
			"\n  rename method <class name> <method name> <newname>",
			"\n  rename param <class name> <method name> <parameter name> <parameter newname>",
			"\n  list classes",
			"\n  list relationships",
			"\n  list all",
			"\n  clear"
		};
		String errorMessage = "\nError in parsing command. Proper command usage is: ";
		
		Scanner cmdLine = new Scanner(System.in);
		System.out.println("Hello, and welcome to Team mike's UML editor.");
		System.out.println("To exit the program, type 'quit'.");
		System.out.println("To see all the commands available, type 'help'.\n");
		System.out.print("Enter a command: ");

		while(cmdLine.hasNextLine()) {
			String line = cmdLine.nextLine();
			
			//redo loop if blank line			
			if(line.isEmpty()){
				System.out.print("Enter a command: ");
				continue;
			}
			//parse command line string into a list of commands by spaces
			String[] commands = line.split(" ");

			if (commands[0].equals("quit")) {
				if (prompt == true) {
					System.out.println("\nYou have unsaved changes, are you sure you want to continue?");
					System.out.println("Type 'yes' to quit, or 'no' to go back.");
					prompt = savePrompt(prompt, cmdLine);
				}
				if (!prompt) {
					break;
				}
				System.out.print("Enter a command: ");
				continue;
			}

			switch(commands[0]) {
				// Call help
				case "help":
					help(commandUsage); 
					break;
				// Call save depending on if pathname was specified or not
				case "save":				
					if (commands.length == 2) {
						try {
						save(commands[1], System.getProperty("user.dir"), userClasses);
						prompt = false;
						}
						catch (IOException e) {
							System.out.println("Failed to parse directory. Exiting.");
						}
					} else if (commands.length == 3) {
						try {
						save(commands[1], commands[2], userClasses);
						prompt = false;
						}
						catch (IOException e) {
							System.out.println("Failed to parse directory. Exiting.");
						}
					} else {
						System.out.println(errorMessage + commandUsage[0] + "\n");
					}
					break;
				// Call load given a directory+filename
				case "load":
					if (commands.length == 2) {
						try {
							if (prompt == true) {
								System.out.println("\nYou have unsaved changes, are you sure you want to continue?");
								System.out.println("Type 'yes' to continue loading, or 'no' to go back.");
								prompt = savePrompt(prompt, cmdLine);
							}
							if (!prompt) {
								load(commands[1], userClasses);
								prompt = false;
							}
						}
						catch (Exception e) {
							System.out.println("Failed to parse directory. Exiting.");
						}
					} else {
						System.out.println(errorMessage + commandUsage[1] + "\n");
					}
					break;
				// Call create class, attribute, or relationship based on length and user input
				case "create":
					if (commands.length < 3) {
						System.out.println(errorMessage  + commandUsage[2]  + commandUsage[3]  + commandUsage[4] + commandUsage[5] + commandUsage [6] + "\n");
						break;
					}
					if (commands[1].equals("class")) {
						if (commands.length != 3) {
							System.out.println(errorMessage + commandUsage[2] + "\n");
							break;
						}
						if (userClasses.createClass(commands[2])) {
							prompt = true;
						} else {
							System.out.println("\nCreate class failed. Make sure the class name doesn't already exist.\n");							
						}
					} else if (commands[1].equals("field")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[3] + "\n");
							break;
						}
						if (userClasses.createField(commands[2], commands[4], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nCreate field failed. Make sure the field doesn't already exist and the class name does exist.\n");							
						}
					} else if (commands[1].equals("method")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[4] + "\n");
							break;
						}
						if (userClasses.createMethod(commands[2], commands[4], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nCreate method failed. Make sure the method doesn't already exist and the class name does exist.\n");
						}
					} else if (commands[1].equals("rel")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[5] + "\n");
							break;
						}
						if (userClasses.createRelationship(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nCreate relationship failed. Make sure the classes exist and that it is not a duplicate.\n");							
						}
					} else if (commands[1].equals("param")) {
						if (commands.length != 6) {
							System.out.println(errorMessage + commandUsage[6] + "\n");
						} 
						if (userClasses.createParameter(commands[2], commands[3], commands[5], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nCreate parameter failed. Make sure the class exists, method exists, and the parameter does NOT exist.\n");
						}
					}
					else {
						System.out.println(errorMessage  + commandUsage[2]  + commandUsage[3]  + commandUsage[4] + commandUsage[5] + commandUsage [6] + "\n");
					}
					break;
				// Call delete class, attribute, or relationship based on length and user input
				case "delete":
					if (commands.length < 3)
					{
						System.out.println(errorMessage + commandUsage[7] + commandUsage[8] + commandUsage[9] + commandUsage[10] + commandUsage[11] + "\n");
						break;
					}
					if (commands[1].equals("class")) {
						if (commands.length != 3) {
							System.out.println(errorMessage + commandUsage[7] + "\n");
							break;
						}			
						if (userClasses.deleteClass(commands[2])) {
							prompt = true;
						} else {
							System.out.println("\nDelete class failed. Make sure the class name exists.\n");							
						}
					} else if (commands[1].equals("field")) {
						if (commands.length != 4) {
							System.out.println(errorMessage + commandUsage[8] + "\n");
							break;
						}
						if (userClasses.deleteField(commands[2], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nDelete field failed. Make sure the field and class name exist.\n");							
						}
					} else if (commands[1].equals("method")) {
						if(commands.length != 4) {
							System.out.println(errorMessage + commandUsage[9] + "\n");
							break;
						}
						if(userClasses.deleteMethod(commands[2], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nDelete method failed. Make sure the method and class name exist.\n");							
						}
					} else if (commands[1].equals("rel")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[10] + "\n");
							break;
						}
						if(userClasses.deleteRelationship(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nDelete relationship failed. Make sure the relationship exists.\n");							
						}
					} else if (commands[1].equals("param")) {
						if (commands.length != 5) {
							System.out.println(errorMessage); /* TODO Edit errorMessage array to include parameters and garbage. */
							break;
						}
						if (userClasses.deleteParameter(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nDelete parameter failed. Make sure the class, method, and parameter all exist.\n");
						}
					}
					else {
						System.out.println(errorMessage + commandUsage[7] + commandUsage[8] + commandUsage[9] + commandUsage[10] + commandUsage[11] + "\n");
					}
					break;
				// Call rename class or attribute based on length and user input
				case "rename":
					if (commands.length < 4) {
						System.out.println(errorMessage + commandUsage[12] + commandUsage[13] + commandUsage[14] + commandUsage[15] + "\n");
						break;
					}
					if (commands[1].equals("class")) {
						if (commands.length != 4) {
							System.out.println(errorMessage + commandUsage[11] + "\n");
							break;
						}
						if(userClasses.renameClass(commands[2], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nRename class failed. Make sure the class exists and the new class name doesn't exist.\n");
						}
					} else if ( commands[1].equals("field")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[12] + "\n");
							break;
						}
						if(userClasses.renameField(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nRename field failed. Make sure the class and field exist and the new field name doesn't exist.\n");
						}
					} else if ( commands[1].equals("method")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[13] + "\n");
							break;
						}
						if(userClasses.renameMethod(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nRename method failed. Make sure the class and method exist and the new method name doesn't exist.\n");
						}
					} else if (commands[1].equals("param")) {
						if (commands.length != 6) {
							System.out.println(errorMessage + commandUsage[14] + "\n");
							break;
						}
						if(userClasses.renameParameter(commands[2], commands[3], commands[4], commands[5])) {
							prompt = true;
						} else {
							System.out.println("\nRename parameter failed. Make sure the class, method, and parameter all exist and the new parameter name does not exist.");
						}
					}
					else {
						System.out.println(errorMessage + commandUsage[12] + commandUsage[13] + commandUsage[14] + commandUsage[15] + "\n");
					}
					break;
				// Call list class or relationship based on length and user input
				case "list":
					if (commands.length < 2) {
						System.out.println(errorMessage + commandUsage[16] + commandUsage[17] + commandUsage[18] + "\n");
						break;
					}
					if (commands[1].equals("classes")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[16] + "\n");
						} else {
							System.out.println();
							listClasses(userClasses);
							System.out.println();							
						}
					} else if (commands[1].equals("relationships")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[17] + "\n");
						} else {
							System.out.println();							
							listRelationships(userClasses);
							System.out.println();							
						}
					} else if (commands[1].equals("all")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[18] + "\n");
						} else {
							System.out.println();							
							listClasses(userClasses);
							System.out.println();
							listRelationships(userClasses);
							System.out.println();							
						}
					} else {
						System.out.println(errorMessage + commandUsage[16] + commandUsage[17] + commandUsage[18] + "\n");
					}
					break;
				// Calls clear 
				case "clear":
					if (commands.length != 1) {
						System.out.println(errorMessage + commandUsage[19] + "\n");
					} else if (!userClasses.empty()) {
						System.out.println("\nAre you sure you want to delete everything?");
						System.out.println("Type 'yes' to delete, or 'no' to go back.");
						boolean answer = savePrompt(true, cmdLine);
							
						if (!answer) {
							userClasses.clear();
							prompt = true;	
						}
					}
					break;

				// Proper command not detected, print an error
				default:
					System.out.println("\nInvalid command.\nType help to see a list of all commands.\n");
			}
			System.out.print("Enter a command: ");
		}
		cmdLine.close();
	}

	public static void help(String[] commandUsage) {
		System.out.print("\nHere is a list of available commands:");

		System.out.println(
				  commandUsage[0]
				+ " - Save file to specific path"
				+ commandUsage[1]
				+ " - Loads a file at a specific path\n"
				
				+ commandUsage[2]
				+ " - create a class with title <name>"
				+ commandUsage[3]
				+ " - create a field in <class name> with type <field type> titled <field name>"
				+ commandUsage[4]
				+ " - create a method in <class name> with type <method type> titled <method name>"				
				+ commandUsage[5]
				+ " - create a relationship between <class name1> and <class name2> titled <name>"
				+ commandUsage[6]
				+ " - create a parameter in <class name> for <method> with type <parameter type> titled <parameter name>\n"

				+ commandUsage[7]
				+ " - delete a class with title <name>"
				+ commandUsage[8]
				+ " - delete field <field name> in class titled <class name>"
				+ commandUsage[9]
				+ " - delete method <method name> in class titled <class name>"				
				+ commandUsage[10]
				+ " - delete a relationship with title <name> between <class name1> and <class name2>"
				+ commandUsage[11]
				+ " - delete a parameter in <class name> for <method name> titled <parameter name>\n"
				
				+ commandUsage[12]
				+ " - rename class <name> to <new name>"
				+ commandUsage[13]
				+ " - rename field <field name> to <newname> in class titled <class name>"
				+ commandUsage[14]
				+ " - rename method <method name> to <newname> in class titled <class name>"
				+ commandUsage[15]
				+ " - rename parameter in <class name> for <method> titled <parameter name> to <parameter newname>\n"
				
				+ commandUsage[16]
				+ " - List all existing classes"
				+ commandUsage[17]
				+ " - List all existing relationships"
				+ commandUsage[18]
				+ " - List all existing classes and relationships\n"
				
				+ commandUsage[19]
				+ " - Clear all classes and relationships\n"

				+ "  quit - exits the program\n");
	}

	public static boolean savePrompt (boolean prompt, Scanner cmdLine) {
		while (prompt == true) {		
			
			String answer = cmdLine.nextLine();
			
			if (answer.equals("yes")) {
				System.out.println("Proceeding.\n");
				prompt = false;
				break;
			} else if (answer.equals("no")) {
				System.out.println("Stopping.\n");
				prompt = true;
				break;
			}
			System.out.println("Invalid command. Type 'yes' to proceed, or 'no' to go back.");
		}
		return prompt;
	}
}

