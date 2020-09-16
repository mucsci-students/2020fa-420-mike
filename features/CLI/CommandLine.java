import java.io.IOException;
import java.util.Scanner;

import org.json.simple.parser.ParseException;

public class CommandLine extends HelperMethods {
	
	CommandLine(){
	}

	public static void commandInterface() throws IOException, ParseException {
		Classes userClasses = new Classes();
		Scanner cmdLine = new Scanner(System.in);
		System.out.println("Hello, and welcome to Team mike's UML editor.");
		System.out.println("To exit the program, type quit");
		System.out.println("To see all the commands available, type help");


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
					help(); 
					break;

				case "save":
					//call save depending on if pathname was specified or not
					if (commands.length == 2) {
						save(commands[1], System.getProperty("user.dir"), userClasses);
					} else if (commands.length == 3) {
						save(commands[1], commands[2], userClasses);							
					} else {
						commandError();
					}
					break;

				case "load":
					if (commands.length == 2) {
						load(commands[1], userClasses);					
					} else {
						commandError();
					}
					break;

				case "create":
					// Call create class, attribute, or relationship based on length and user input
					if (commands.length == 3 && commands[1].equals("class")) {
						userClasses.createClass(commands[2]);
					} else if (commands.length == 4 && commands[1].equals("att")) {
						userClasses.createAttribute(commands[2], commands[3]);
					} else if (commands.length == 5 && comands[1].equals("rel")) {
						userClasses.createRelationship(commands[2], commands[3], commands[4]);
					}
					else {
						commandError();
					}
					break;

				case "delete":
					// Call delete class, attribute, or relationship based on length and user input
					if (commands.length == 3 && commands[1].equals("class")){
						userClasses.deleteClass(commands[2]);
					} else if (commands.length == 4 && commands[1].equals("att")) {
						userClasses.deleteAttribute(commands[2], commands[3]);
					} else if (commands.length == 5 && commands[1].equals("rel")) {
						userClasses.deleteRelationship(commands[2], commands[3], commands[4]);
					} else {
						commandError();
					}
					break;

				case "rename":
					// Call rename class or attribute based on length and user input
					if (commands.length == 4 && commands[1].equals("class")) {
						userClasses.renameClass(commands[2], commands[3]);
					} else if (commands.length == 5 && commands[1].equals("att")) {
						userClasses.renameAttribute(commands[2], commands[3], commands[4]);
					} else {
						commandError();
					}
					break;

				case "list":
					// Call list class or relationship based on length and user input
					if (commands.length == 2) {
						if (commands[1].equals("classes")) {
							listClasses(userClasses);	
						} else if (commands[1].equals("relationships")) {
							listRelationships(userClasses);
						} else {
							commandError();
						}
					} else {
						commandError();
					}
					break;

				// Proper command not detected, print an error
				default:
					commandError();
			}
		}
		cmdLine.close();
	}

	public static void help() {
		System.out.println("Here is a list of available commands:");

		System.out.println("save <name> (optional <path>) - Save file to specific path\n"
				+ "load <path> - Loads a file at a specific path\n\n"

				+ "create class <name> - create a class with title <name>\n"
				+ "rename class <name> <newname> - rename class <name> to <newname>\n"
				+ "delete class <name> - delete a class with title <name>\n\n"

				+ "create rel  <name> <classname1> <classname2> - create a relationship\n"
				+ "	between <classname1> and <classname2> titled <name>\n"
				+ "delete rel <name> <classname1> <classname2> - delete a relationship with title <name>\n\n"

				+ "create att <classname> <attribute> - create an attribute in <classname>\n"
				+ "	titled <attribute>\n"
				+ "rename att <classname> <attribute> <newname> - rename attribute\n"
				+ "	<name> to <newname> in class titled <classname>\n"
				+ "delete att <classname> <attribute> - delete attribute titled\n"
				+ "	<attribute> in class titled <classname>\n\n"

				+ "list <object> - List all existing classes and their attributes or\n"
				+ "	all relationships");
	}

	// Prints an error if the user gives an incorrect or unlisted command.
	public static void commandError() {
		System.out.println("Invalid command.\nType help to see command usage.");
	}
}
