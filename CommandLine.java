import java.util.Scanner;


public class CommandLine {

	public static void main(String[] args) {
		Scanner cmdLine = new Scanner(System.in);
		System.out.println("Hello, and welcome to Team mike's UML editor.");
		System.out.println("To exit the program, type quit");
		System.out.println("To see all the commands available, type help");


		while(true)
		{
			String line = cmdLine.nextLine();
			//parse command line string into a list of commands by spaces
			String[] commands = line.split(" ");
			//get first command
			String command = commands[0];

			if (command.equals("quit"))
			{
				break;
			}
			
			switch(command)
			{
				case "help":
					help(); 
					break;
				case "save":
					ensureCapacity(commands, 2); // Why bother using ensure capacity? If commands.length == 2, save with no path
												//									else if commands.length == 3, save with path
												//									else break
					//call save depending on if pathname was specified or not
					if(commands.length == 2)
					{
						save(commands[1]);
					}
					else
					{
						ensureCapacity(commands, 3);
						save(commands[1], commands[2]);
					}
					break;
				case "load":
					ensureCapacity(commands, 2); // If commands.length != 2 break
					load(commands[1]);
					break;
				case "cla":
					ensureCapacity(commands, 3); // If commands.length == 3 do create or delete
					String command2 = commands[1]; // Else if commands.length == 4 do rename
					String className = commands[2]; // else break

					//check command after cla
					if(command2.equals("create"))
					{
						claCreate(className);
					}
					else if(command2.equals("delete"))
					{
						claDelete(className);
					}
					else
					{
						ensureCapacity(commands, 4);
						claRename(className, commands[3]);
					}
					break;

				case "rel":

					ensureCapacity(commands, 3); // If commands.length == 5 do create
					String command2 = commands[1]; // else if commands.length == 3 do delete
					String relName = commands[2]; // else break

					//check command after rel
					if(command2.equals("create"))
					{
						ensureCapacity(commands, 5);
						relCreate(relName, commands[3], commands[4]);
					}
					else
					{
						relDelete(relName);
					}
					break;

				case "att":

					ensureCapacity(commands, 4); // If commands.length == 4 do create or delete
					String command2 = commands[1]; // else if commands.length == 5 do rename
					String className = commands[2]; // else break
					String attName = commands[3];

					//check command after cla
					if(command2.equals("create"))
					{
						attCreate(className, attName);
					}
					else if(command2.equals("delete"))
					{
						attDelete(className, attName);
					}
					else
					{
						ensureCapacity(commands, 5);
						attRename(className, attName, commands[4]);
					}
					break;

				case "list":
					ensureCapacity(commands, 2); // if commands.length != 2 break
					list(commands[1]);
					break;

				default:
					throw new IllegalArgumentException("Invalid command. Type help for a list of\n"
							+ "available commands");
			}	
		}

	}
	
	public static void help()
	{
		System.out.println("Here is a list of available commands:");
		
		System.out.println("save <name> (optional <path) - Save file to specific path\n"
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

	//ensures the length of an array "array" is of at least int arrayLength
	//To be used for checking # of commands in command line to avoid errors
	public static void ensureCapacity(String[] array, int arrayLength)
	{
		if(array.length < arrayLength)
		{
			throw new IllegalArgumentException("There are not enough arguments for the "
				+ array[0] + " command.\n Type help to see how to use the command.");
		}
	}

}
