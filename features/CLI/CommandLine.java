import java.util.Scanner;


public class CommandLine {

	public static void commandLineInterface() {
		Scanner cmdLine = new Scanner(System.in);
		System.out.println("Hello, and welcome to Team mike's UML editor.");
		System.out.println("To exit the program, type quit");
		System.out.println("To see all the commands available, type help");


		while(true) {
			String line = cmdLine.nextLine();
			//parse command line string into a list of commands by spaces
			String[] commands = line.split(" ");

			if (command[0].equals("quit")) {
				break;
			}
			
			switch(command[0]) {
				case "help":
					help(); 
					break;
					
				case "save":
					//call save depending on if pathname was specified or not
					if (commands.length == 2) {
						save(commands[1]);
					} else if (commands.length == 3) {
						save(commands[1], commands[2]);							
					} else {
						commandError();
					}
					break;
					
				case "load":
					if (commands.length == 2) {
						load(commands[1]);					
					} else {
						commandError();
					}
					break;
					
				case "cla":
					// Call class create, delete, or rename based on length & input
					if (commands.length == 3) {
						if (commands[1].equals("create")) {
							claCreate(commands[2]);
						} else if (commands[1].equals("delete")) {
							claDelete(commands[2]);
						} else {
							commandError();
						}
					} else if (commands.length == 4 && commands[1].equals("rename")) {
						claRename(commands[2], commands[3]);
					} else {
						commandError();
					}
					break;

				case "rel":
					// Call relationship create, or delete based on length & input
					if (commands.length == 5 && commands[1].equals("create")) {
						relCreate(relName, commands[3], commands[4]);
					} else if (commands.length == 3 && commands[1].equals("delete")) {
						relDelete(relName);
					} else {
						commandError();
					}
					break;

				case "att":
					// Call attribute create, delete, or rename based on length & input
					if (commands.length == 4) {
						if (commands[1].equals("create")) {
							attCreate(commands[2], commands[3]);
						} else if (commands[1].equals("delete")) {
							attDelete(commands[2], commands[3]);
						} else {
							commandError();
						}
					} else if (commands.length == 5 && commands[1].equals("rename")) {
						attRename(commands[2], commands[3], commands[4]);
					} else {
						commandError();
					}
					break;

				case "list":
					// Call class or relationship list based on length & input
					if (commands.length == 2) {
						if (commands[1].equals("classes")) {
							listClasses();	
						} else if (commands[1].equals("relationships")) {
							listRelationships();
						}
					} else {
						commandError();
					}
					break;
				
				// Proper command not dectected, throw error
				default:
					commandError();
			}	
		}
	}
	
	public static void help() {
		System.out.println("Here is a list of available commands:");
		
		System.out.println("save <name> (optional <path>) - Save file to specific path\n"
				+ "load <path> - Loads a file at a specific path\n\n"
				
				+ "cla create <name> - create a class with title <name>\n"
				+ "cla rename <name> <newname> - rename class <name> to <newname>\n"
				+ "cla delete <name> - delete a class with title <name>\n\n"
				
				+ "rel create <name> <classname1> <classname2> - create a relationship\n"
				+ "	between <classname1> and <classname2> titled <name>\n"
				+ "rel delete <name> - delete a relationship with title <name>\n\n"
				
				+ "att create <classname> <attribute> - create an attribute in <classname>\n"
				+ "	titled <attribute>\n"
				+ "att rename <classname> <attribute> <newname> - rename attribute\n"
				+ "	<name> to <newname> in class titled <classname>\n"
				+ "att delete <classname> <attribute> - delete attribute titled\n"
				+ "	<attribute> in class titled <classname>\n\n"
				
				+ "list <object> - List all existing classes and their attributes or\n"
				+ "	all relationships");
	}

	// Prints an error if the user gives an incorrect or unlisted command.
	public static void commandError() {
		throw new IllegalArgumentException("Invalid command.\nType help to see command usage.");
	}

}
