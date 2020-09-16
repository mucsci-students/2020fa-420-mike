package cli;

import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;
import datastructures.Classes;

public class CommandLine extends HelperMethods {
	
	public CommandLine() {
	}

	public static void commandInterface() {
		Classes userClasses = new Classes();
		String[] commandUsage = {
			"\n  save <name (optional <path>)", 
			"\n  load <path>",
			"\n  create class <name>",
			"\n  create att <classname> <attribute>",
			"\n  create rel <name> <classname1> <classname2>",
			"\n  delete class <name>",
			"\n  delete att <classname>",
			"\n  delete rel <name> <classname1> <classname2>",
			"\n  rename class <name> <newname>",
			"\n  rename att <classname> <attribute <newname>",
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


		while(true) {
			System.out.print("Enter a command: ");
			
			String line = cmdLine.nextLine();
			//parse command line string into a list of commands by spaces
			String[] commands = line.split(" ");

			if (commands[0].equals("quit")) {
				break;
			}

			switch(commands[0]) {
				case "help":
					help(commandUsage); 
					break;

				case "save":
					//call save depending on if pathname was specified or not
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

				case "load":
					if (commands.length == 2) {
						try {
						load(commands[1], userClasses);
						}
						catch (IOException | ParseException e) {
							System.out.println("Failed to parse directory. Exiting.");
						}
					} else {
						System.out.println(errorMessage + commandUsage[1] + "\n");
					}
					break;

				case "create":
					// Call create class, attribute, or relationship based on length and user input
					
					if (commands[1].equals("class")) {
						if(commands.length != 3 || !userClasses.createClass(commands[2])) {
							System.out.println(errorMessage + commandUsage[2] + "\n");
						}
					} else if ( commands[1].equals("att")) {
						if(commands.length != 4 || !userClasses.createAttribute(commands[2], commands[3])) {
							System.out.println(errorMessage + commandUsage[3] + "\n");
						}
					} else if (commands[1].equals("rel")) {
						if(commands.length != 5 || !userClasses.createRelationship(commands[2], commands[3], commands[4])) {
							System.out.println(errorMessage + commandUsage[4] + "\n");
						}
					}
					else {
						System.out.println(errorMessage  + commandUsage[2]  + commandUsage[3]  + commandUsage[4] + "\n");
					}
					break;

				case "delete":
					// Call delete class, attribute, or relationship based on length and user input
					if (commands[1].equals("class")) {
						if (commands.length != 3 || !userClasses.deleteClass(commands[2])) {
							System.out.println(errorMessage + commandUsage[5] + "\n");
						}
					} else if (commands[1].equals("att")) {
						if (commands.length != 4 || !userClasses.deleteAttribute(commands[2], commands[3])) {
							System.out.println(errorMessage + commandUsage[6] + "\n");
						}
					} else if (commands[1].equals("rel")) {
						if (commands.length != 5 || !userClasses.deleteRelationship(commands[2], commands[3], commands[4])) {
							System.out.println(errorMessage + commandUsage[7] + "\n");
						}
					} else {
						System.out.println(errorMessage + commandUsage[5] + commandUsage[6] + commandUsage[7] + "\n");
					}
					break;

				case "rename":
					// Call rename class or attribute based on length and user input
					if (commands[1].equals("class")) {
						if (commands.length != 4 || !userClasses.renameClass(commands[2], commands[3])) {
							System.out.println(errorMessage + commandUsage[8] + "\n");
						}
					} else if (commands[1].equals("att")) {
						if (commands.length != 5 || !userClasses.renameAttribute(commands[2], commands[3], commands[4])) {
							System.out.println(errorMessage + commandUsage[9] + "\n");
						}
					} else {
						System.out.println(errorMessage + commandUsage[8] + commandUsage[9] + "\n");
					}
					break;

				case "list":
					// Call list class or relationship based on length and user input
					if (commands[1].equals("classes")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[10] + "\n");
						} else {
							System.out.println();
							listClasses(userClasses);
							System.out.println();							
						}
					} else if (commands[1].equals("relationships")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[11] + "\n");
						} else {
							System.out.println();							
							listRelationships(userClasses);
							System.out.println();							
						}
					} else if (commands[1].equals("all")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[12] + "\n");
						} else {
							System.out.println();							
							listClasses(userClasses);
							System.out.println();
							listRelationships(userClasses);
							System.out.println();							
						}
					} else {
						System.out.println(errorMessage + commandUsage[10] + commandUsage[11] + commandUsage[12] + "\n");
					}
					break;
					
				case "clear":
					if (commands.length != 1) {
						System.out.println(errorMessage + commandUsage[13] + "\n");
					} else {
						userClasses.clear();					
					}
				break;

				// Proper command not detected, print an error
				default:
					System.out.println("Invalid command.\n Type help to see a list of all commands.");
			}	
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
				+ " - create an attribute in <classname> titled <attribute>"
				+ commandUsage[4]
				+ " - create a relationship between <classname1> and <classname2> titled <name>\n"
				
				+ commandUsage[5]
				+ " - rename class <name> to <newname>"
				+ commandUsage[6]
				+ "- rename attribute <name> to <newname> in class titled <classname>\n"
				
				+ commandUsage[7]
				+ " - delete a class with title <name>"
				+ commandUsage[8]
				+ " - delete attribute titled <attribute> in class titled <classname>"
				+ commandUsage[9]
				+ " - delete a relationship with title <name>\n"

				+ commandUsage[10]
				+ " - List all existing classes"
				+ commandUsage[11]
				+ " - List all existing relationships"
				+ commandUsage[12]
				+ " - List all existing classes and relationships\n"
				
				+ commandUsage[13]
				+ " - Clear all classes and relationships\n");
	}
}
