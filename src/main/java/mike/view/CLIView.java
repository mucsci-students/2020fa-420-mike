package mike.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import mike.datastructures.Model;
import mike.datastructures.Relationship.Type;
import mike.HelperMethods;

public class CLIView implements ViewInterface {
	private Model classes;
	
	public CLIView(Model classModel) throws IOException {
    // Initialize variables
		this.classes = classModel;
		boolean prompt = false;
		String[] commandUsage = {
			"\n  save <name>.json (optional <path>)", 
			"\n  load <path>",
			"\n  create class <name>",
			"\n  create field <class name> <field type> <field name>",
			"\n  create method <class name> <method type> <method name>",
			"\n  create relationship <type> <class name1> <class name2>",
			"\n  create parameter <class name> <method> <parameter type> <parameter name>",
			"\n  delete class <name>",
			"\n  delete field <class name> <field name>",
			"\n  delete method <class name> <method name>",
			"\n  delete relationship <type> <class name1> <class name2>",
			"\n  delete parameter <class name> <method name>, <parameter name>",
			"\n  rename class <name> <newname>",
			"\n  rename field <class name> <field name> <newname>",
			"\n  rename method <class name> <method name> <newname>",
			"\n  rename parameter <class name> <method name> <parameter name> <parameter newname>",
			"\n  list classes",
			"\n  list relationships",
			"\n  list all",
			"\n  clear"
		};
		String errorMessage = "\nError in parsing command. Proper command usage is: ";
		
		System.out.println("Hello, and welcome to Team mike's UML editor.");
		System.out.println("To exit the program, type 'quit'.");
		System.out.println("To see all the commands available, type 'help'.\n");
		
		Terminal terminal = TerminalBuilder.builder()
				.system(true)
				.build();
		
		AggregateCompleter completer = new AggregateCompleter(
			new ArgumentCompleter(
				new StringsCompleter("save"),
				new NullCompleter()
			),
			new ArgumentCompleter(
				new StringsCompleter("load"),
				new NullCompleter()
			),
			new ArgumentCompleter(
				new StringsCompleter("quit"),
				new NullCompleter()
			),
			new ArgumentCompleter(
				new StringsCompleter("clear"),
				new NullCompleter()
			),
			new ArgumentCompleter(
				new StringsCompleter("create", "delete"),
				new StringsCompleter("class", "field", "method", 
						"parameter", "relationship"),
				new NullCompleter()
			),
			new ArgumentCompleter(
				new StringsCompleter("rename"),
				new StringsCompleter("class", "field", "method", 
						"parameter"),
				new NullCompleter()
			),
			new ArgumentCompleter(
				new StringsCompleter("list"),
				new StringsCompleter("classes", "relationships",
						"all"),
				new NullCompleter()
			)
		);
		
		StringsCompleter savePromptCompleter = new StringsCompleter(
				"yes",
				"no"
				);
		
		DefaultParser parser = new DefaultParser();
		parser.setEscapeChars(new char[] {});
		
		LineReader reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
		LineReader savePromptReader = LineReaderBuilder.builder().terminal(terminal).completer(savePromptCompleter).variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
	
		while(true) {
			String line = null;
			
			line = reader.readLine("Enter a command: ", "", (MaskingCallback) null, null);
			line = line.trim();
			
			String[] commands = line.split(" ");

			if (commands[0].equals("quit")) {
				if (prompt == true) {
					System.out.println("\nYou have unsaved changes, are you sure you want to continue?");
					System.out.println("Type 'yes' to quit, or 'no' to go back.");
					prompt = savePrompt(prompt, savePromptReader);
				}
				if (!prompt) {
					terminal.close();
					break;
				}
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
							File file = new File(commands[1]);
							Path path;
							if(file.isAbsolute()){
								path = Paths.get(commands[1]);
							}
							else {
								path = Paths.get(System.getProperty("user.dir") + "\\" + commands[1]);  
							}
							HelperMethods.save(path, classes);
							System.out.println("File saved at: " + path.toString());
							prompt = false;
						}
						catch (IOException e) {
							System.out.println("Failed to parse directory. Exiting.");
						}
					} else if (commands.length == 3) {
						try {
							File file = new File(commands[2] + "\\" + commands[1]);
							Path path;
							if(file.isAbsolute()){
								path = Paths.get(commands[2] + "\\" + commands[1]);
							}
							else {
								path = Paths.get(System.getProperty("user.dir") + "\\" + commands[2] + "\\" + commands[1]);  
							}
							HelperMethods.save(path, classes);
							System.out.println("File saved at: " + path.toString());
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
								prompt = savePrompt(prompt, savePromptReader);
							}
							if (!prompt) {
								File file = new File(commands[1]);
								Path path;
								if(file.isAbsolute()){
									path = Paths.get(commands[1]);
								}
								else {
									path = Paths.get(System.getProperty("user.dir") + "\\" + commands[1]);  
								}
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								HelperMethods.load(path, classes, null, null); //POSSIBLY VERY, VERY SCUFFED
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
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
				// Call create class, field, method, or relationship based on length and user input
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
						if (classes.createClass(commands[2])) {
							prompt = true;
						} else {
							System.out.println("\nCreate class failed. Make sure the class name doesn't already exist.\n");							
						}
					} else if (commands[1].equals("field")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[3] + "\n");
							break;
						}
						if (classes.createField(commands[2], commands[4], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nCreate field failed. Make sure the field doesn't already exist and the class name does exist.\n");							
						}
					} else if (commands[1].equals("method")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[4] + "\n");
							break;
						}
						if (classes.createMethod(commands[2], commands[4], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nCreate method failed. Make sure the method doesn't already exist and the class name does exist.\n");
						}
					} else if (commands[1].equals("relationship")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[5] + "\n");
							break;
						}
						if (classes.createRelationship(checkEnum(commands[2].toUpperCase()), commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nCreate relationship failed. Make sure the classes exist, the relationship type is valid, and that it is not a duplicate.\n");							
						}
					} else if (commands[1].equals("parameter")) {
						if (commands.length != 6) {
							System.out.println(errorMessage + commandUsage[6] + "\n");
							break;
						} 
						if (classes.createParameter(commands[2], commands[3], commands[5], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nCreate parameter failed. Make sure the class exists, method exists, and the parameter does NOT exist.\n");
						}
					}
					else {
						System.out.println(errorMessage  + commandUsage[2]  + commandUsage[3]  + commandUsage[4] + commandUsage[5] + commandUsage [6] + "\n");
					}
					break;
				// Call delete class, field, method, or relationship based on length and user input
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
						if (classes.deleteClass(commands[2])) {
							prompt = true;
						} else {
							System.out.println("\nDelete class failed. Make sure the class name exists.\n");							
						}
					} else if (commands[1].equals("field")) {
						if (commands.length != 4) {
							System.out.println(errorMessage + commandUsage[8] + "\n");
							break;
						}
						if (classes.deleteField(commands[2], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nDelete field failed. Make sure the field and class name exist.\n");							
						}
					} else if (commands[1].equals("method")) {
						if(commands.length != 4) {
							System.out.println(errorMessage + commandUsage[9] + "\n");
							break;
						}
						if(classes.deleteMethod(commands[2], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nDelete method failed. Make sure the method and class name exist.\n");							
						}
					} else if (commands[1].equals("relationship")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commandUsage[10] + "\n");
							break;
						}
						if(classes.deleteRelationship(checkEnum(commands[2].toUpperCase()), commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nDelete relationship failed. Make sure the relationship exists.\n");							
						}
					} else if (commands[1].equals("parameter")) {
						if (commands.length != 5) {
							System.out.println(errorMessage + commands[11] + "\n");
							break;
						}
						if (classes.deleteParameter(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nDelete parameter failed. Make sure the class, method, and parameter all exist.\n");
						}
					}
					else {
						System.out.println(errorMessage + commandUsage[7] + commandUsage[8] + commandUsage[9] + commandUsage[10] + commandUsage[11] + "\n");
					}
					break;
				// Call rename class, field, or method depending on user input and length
				case "rename":
					if (commands.length < 4) {
						System.out.println(errorMessage + commandUsage[12] + commandUsage[13] + commandUsage[14] + commandUsage[15] + "\n");
						break;
					}
					if (commands[1].equals("class")) {
						if (commands.length != 4) {
							System.out.println(errorMessage + commandUsage[12] + "\n");
							break;
						}
						if(classes.renameClass(commands[2], commands[3])) {
							prompt = true;
						} else {
							System.out.println("\nRename class failed. Make sure the class exists and the new class name doesn't exist.\n");
						}
					} else if ( commands[1].equals("field")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[13] + "\n");
							break;
						}
						if(classes.renameField(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nRename field failed. Make sure the class and field exist and the new field name doesn't exist.\n");
						}
					} else if ( commands[1].equals("method")) {
						if(commands.length != 5) {
							System.out.println(errorMessage + commandUsage[14] + "\n");
							break;
						}
						if(classes.renameMethod(commands[2], commands[3], commands[4])) {
							prompt = true;
						} else {
							System.out.println("\nRename method failed. Make sure the class and method exist and the new method name doesn't exist.\n");
						}
					} else if (commands[1].equals("parameter")) {
						if (commands.length != 6) {
							System.out.println(errorMessage + commandUsage[15] + "\n");
							break;
						}
						if(classes.renameParameter(commands[2], commands[3], commands[4], commands[5])) {
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
							HelperMethods.listClasses(classes);
							System.out.println();							
						}
					} else if (commands[1].equals("relationships")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[17] + "\n");
						} else {
							System.out.println();							
							HelperMethods.listRelationships(classes);
							System.out.println();							
						}
					} else if (commands[1].equals("all")) {
						if (commands.length != 2) {
							System.out.println(errorMessage + commandUsage[18] + "\n");
						} else {
							System.out.println();							
							HelperMethods.listClasses(classes);
							System.out.println();
							HelperMethods.listRelationships(classes);
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
					} else if (!classes.empty()) {
						System.out.println("\nAre you sure you want to delete everything?");
						System.out.println("Type 'yes' to delete, or 'no' to go back.");
						boolean answer = savePrompt(true, savePromptReader);
							
						if (!answer) {
							classes.clear();
							prompt = true;	
						}
					}
					break;

				// Proper command not detected, print an error
				default:
					System.out.println("\nInvalid command.\nType help to see a list of all commands.\n");
			}
		}
	}

  // Prints out how to use all the commands in the CLI
	private static void help(String[] commandUsage) {
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
				+ " - create a relationship between <class name1> and <class name2> with type <type> (Aggregation, Realization, Composition, Inheritance)"
				+ commandUsage[6]
				+ " - create a parameter in <class name> for <method> with type <parameter type> titled <parameter name>\n"

				+ commandUsage[7]
				+ " - delete a class with title <name>"
				+ commandUsage[8]
				+ " - delete field <field name> in class titled <class name>"
				+ commandUsage[9]
				+ " - delete method <method name> in class titled <class name>"				
				+ commandUsage[10]
				+ " - delete a relationship with type <type> (Aggregation, Realization, Composition, Inheritance) between <class name1> and <class name2>"
				+ commandUsage[11]
				+ " - delete a parameter in <class name> for <method name> with  <parameter name>\n"
				
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

  // Gets user input to set save prompt flag.
  // False if they wish to continue
  // True if they want to return
	private static boolean savePrompt (boolean prompt, LineReader reader) {
		while (prompt == true) {
			String line = reader.readLine("", "", (MaskingCallback) null, null);
			line = line.trim();
			
			if (line.equals("yes")) {
				System.out.println("Proceeding.\n");
				prompt = false;
				break;
			} else if (line.equals("no")) {
				System.out.println("Stopping.\n");
				prompt = true;
				break;
			}
			System.out.println("Invalid command. Type 'yes' to proceed, or 'no' to go back.");
		}
		return prompt;
	}

	// Return enum type that user requested, null if invalid
	private static Type checkEnum (String command) {
		switch(command){
			case "REALIZATION":
				return Type.REALIZATION;
			case "AGGREGATION":
				return Type.AGGREGATION;
			case "COMPOSITION":
				return Type.COMPOSITION;
			case "INHERITANCE":
				return Type.INHERITANCE;
			default:
				return null;
		}
	}
}