package mike.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import mike.datastructures.Classes;

public class CommandLine extends HelperMethods {
	
	public CommandLine() {
	}

	public static void commandInterface() throws IOException {
		Classes userClasses = new Classes();
		String[] commandUsage = {
			"\n  save <name (optional <path>)", 
			"\n  load <path>",
			"\n  create class <name>",
			"\n  create field <classname> <fieldname>",
			"\n  create method <classname> <methodname>",
			"\n  create rel <name> <classname1> <classname2>",
			"\n  delete class <name>",
			"\n  delete field <classname> <fieldname>",
			"\n  delete method <classname> <methodname>",
			"\n  delete rel <name> <classname1> <classname2>",
			"\n  rename class <name> <newname>",
			"\n  rename field <classname> <fieldname> <newname>",
			"\n  rename method <classname> <methodname> <newname>",	
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
		
		BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		System.out.print("Enter a command: ");
		String cmd;

		while((cmd = systemIn.readLine()) != null) {
			//redo loop if blank line
			if(cmd.isEmpty()){
				System.out.print("Enter a command: ");
				continue;
			}

			//parse command line string into a list of commands by spaces
			String[] commands = cmd.split(" ");

			if (commands[0].equals("quit")) {
				break;
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
						}
						catch (IOException e) {
							System.out.println("Failed to parse directory. Exiting.");
						}
					} else if (commands.length == 3) {
						try {
						save(commands[1], commands[2], userClasses);
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
						load(commands[1], userClasses);
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
					if (commands[1].equals("class")) {
						if(commands.length != 3) {
							System.out.println(errorMessage + commandUsage[2] + "\n");
							break;
						}
						if(!userClasses.createClass(commands[2])){
							System.out.println("\nCreate class failed. Make sure the class name doesn't already exist.\n");
						}
					} else if ( commands[1].equals("field")) {
						if(commands.length != 4) {
							System.out.println(errorMessage + commandUsage[3] + "\n");
							break;
						}
						if(!userClasses.createField(commands[2], commands[3])){
							System.out.println("\nCreate field failed. Make sure the field doesn't already exist and the class name does exist.\n");
						}
					} else if ( commands[1].equals("method")) {
						if(commands.length != 4) {
							System.out.println(errorMessage + commandUsage[4] + "\n");
							break;
						}
						if(!userClasses.createMethod(commands[2], commands[3])){
							System.out.println("\nCreate method failed. Make sure the method doesn't already exist and the class name does exist.\n");
						}
					} else if (commands[1].equals("rel")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[5] + "\n");
							break;
						}
						if(!userClasses.createRelationship(commands[2], commands[3], commands[4])){
							System.out.println("\nCreate relationship failed. Make sure the classes exist and that it is not a duplicate.\n");
						}
					}
					else {
						System.out.println(errorMessage  + commandUsage[2]  + commandUsage[3]  + commandUsage[4] + commandUsage[5] + "\n");
					}
					break;
				// Call delete class, attribute, or relationship based on length and user input
				case "delete":	
					if (commands[1].equals("class")) {
						if (commands.length != 3) {
							System.out.println(errorMessage + commandUsage[6] + "\n");
							break;
						}
						if(!userClasses.deleteClass(commands[2])){
							System.out.println("\nDelete class failed. Make sure the class name exists.\n");
						}
					} else if ( commands[1].equals("field")) {
						if(commands.length != 4) {
							System.out.println(errorMessage + commandUsage[7] + "\n");
							break;
						}
						if(!userClasses.deleteField(commands[2], commands[3])){
							System.out.println("\nDelete field failed. Make sure the field and class name exist.\n");
						}
					} else if ( commands[1].equals("method")) {
						if(commands.length != 4) {
							System.out.println(errorMessage + commandUsage[8] + "\n");
							break;
						}
						if(!userClasses.deleteMethod(commands[2], commands[3])){
							System.out.println("\nDelete method failed. Make sure the method and class name exist.\n");
						}
					} else if (commands[1].equals("rel")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[9] + "\n");
							break;
						}
						if(!userClasses.deleteRelationship(commands[2], commands[3], commands[4])){
							System.out.println("\nDelete relationship failed. Make sure the relationship exists.\n");
						}
					} else {
						System.out.println(errorMessage + commandUsage[6] + commandUsage[7] + commandUsage[8] + commandUsage[9] + "\n");
					}
					break;
				// Call rename class or attribute based on length and user input
				case "rename":		
					if (commands[1].equals("class")) {
						if (commands.length != 4) {
							System.out.println(errorMessage + commandUsage[10] + "\n");
							break;
						}
						if(!userClasses.renameClass(commands[2], commands[3])){
							System.out.println("\nRename class failed. Make sure the class exists and the new class name doesn't exist.\n");
						}
					} else if ( commands[1].equals("field")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[11] + "\n");
							break;
						}
						if(!userClasses.renameField(commands[2], commands[3], commands[4])){
							System.out.println("\nRename field failed. Make sure the class and field exist and the new field name doesn't exist.\n");
						}
					} else if ( commands[1].equals("method")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[12] + "\n");
							break;
						}
						if(!userClasses.renameMethod(commands[2], commands[3], commands[4])){
							System.out.println("\nRename method failed. Make sure the class and method exist and the new method name doesn't exist.\n");
						}
					} else {
						System.out.println(errorMessage + commandUsage[10] + commandUsage[11] + commandUsage[12] + "\n");
					}
					break;
				// Call list class or relationship based on length and user input
				case "list":				
					if (commands[1].equals("classes")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[13] + "\n");
						} else {
							System.out.println();
							listClasses(userClasses);
							System.out.println();							
						}
					} else if (commands[1].equals("relationships")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[14] + "\n");
						} else {
							System.out.println();							
							listRelationships(userClasses);
							System.out.println();							
						}
					} else if (commands[1].equals("all")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[15] + "\n");
						} else {
							System.out.println();							
							listClasses(userClasses);
							System.out.println();
							listRelationships(userClasses);
							System.out.println();							
						}
					} else {
						System.out.println(errorMessage + commandUsage[13] + commandUsage[14] + commandUsage[15] + "\n");
					}
					break;
				// Calls clear 
				case "clear":
					if (commands.length != 1) {
						System.out.println(errorMessage + commandUsage[16] + "\n");
					} else {
						userClasses.clear();					
					}
					break;

				// Proper command not detected, print an error
				default:
					System.out.println("Invalid command.\n Type help to see a list of all commands.");
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
				+ " - create a field in <classname> titled <fieldname>"
				+ commandUsage[4]
				+ " - create a method in <classname> titled <methodname>"				
				+ commandUsage[5]
				+ " - create a relationship between <classname1> and <classname2> titled <name>\n"

				+ commandUsage[6]
				+ " - delete a class with title <name>"
				+ commandUsage[7]
				+ " - delete field <fieldname> in class titled <classname>"
				+ commandUsage[8]
				+ " - delete method <methodname> in class titled <classname>"				
				+ commandUsage[9]
				+ " - delete a relationship with title <name> between <classname1> and <classname2>\n"
				
				+ commandUsage[10]
				+ " - rename class <name> to <newname>"
				+ commandUsage[11]
				+ " - rename field <fieldname> to <newname> in class titled <classname>"
				+ commandUsage[12]
				+ " - rename method <methodname> to <newname> in class titled <classname>\n"				
				
				+ commandUsage[13]
				+ " - List all existing classes"
				+ commandUsage[14]
				+ " - List all existing relationships"
				+ commandUsage[15]
				+ " - List all existing classes and relationships\n"
				
				+ commandUsage[16]
				+ " - Clear all classes and relationships\n"

				+ "  quit - exits the program\n");
	}
}

