package mike.cli;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jline.reader.LineReader;
import org.jline.reader.MaskingCallback;

import mike.HelperMethods;
import mike.datastructures.Model;

public class MiscCommand extends CommandObj {

    private static LineReader savePromptReader;

    public MiscCommand(Model m, String[] com, boolean p, LineReader reader) {
	super(m, com, p);
	prompt = p;
	savePromptReader = reader;
    }

    public boolean execute() {
	switch (commands[0]) {
	case "quit":
	    if (prompt) {
		System.out.println("\nYou have unsaved changes, are you sure you want to continue?");
		System.out.println("Type 'yes' to quit, or 'no' to go back.");
		prompt = savePrompt(prompt);
	    }
	    if (!prompt) {
		System.exit(0);
	    }
	    return true;
	case "help":
	    help(commandUsage);
	    return prompt;
	case "clear":
	    if (commands.length != 1) {
		System.out.println(errorMessage + commandUsage[24] + "\n");
		return prompt;
	    } else if (!model.empty()) {
		System.out.println("\nAre you sure you want to delete everything?");
		System.out.println("Type 'yes' to delete, or 'no' to go back.");
		boolean answer = savePrompt(true);

		if (!answer) {
		    model.clear();
		    prompt = true;
		}
	    }
	    return prompt;
	case "sudo":
	    if (commands[1].equals("quit") && commands.length == 2) {
		System.exit(0);
	    } else if (commands[1].equals("load") && commands.length == 3) {
		try {
		    File file = new File(commands[2]);
		    Path path;
		    if (file.isAbsolute()) {
			path = Paths.get(commands[2]);
		    } else {
			path = Paths.get(System.getProperty("user.dir") + "\\" + commands[2]);
		    }
		    HelperMethods.load(path, model, null, null);
		    prompt = false;
		} catch (Exception e) {
		    System.out.println("Failed to parse directory. Exiting.");
		}
	    } else if (commands[1].equals("clear") && commands.length == 2) {
		model.clear();
		prompt = true;
	    } else {
		System.out.println("\nInvalid command.\nType help to see a list of all commands.\n");
	    }
	    return prompt;
	}
	return prompt;
    }

    // Gets user input to set save prompt flag.
    // False if they wish to continue
    // True if they want to return
    private static boolean savePrompt(boolean prompt) {
	while (prompt) {

	    String line = savePromptReader.readLine("", "", (MaskingCallback) null, null);
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

    // Prints out how to use all the commands in the CLI
    private static void help(String[] commandUsage) {
	System.out.print("\nHere is a list of available commands:");
	System.out.println(commandUsage[0] + " - Save file to specific path" + commandUsage[1]
		+ " - Loads a file at a specific path\n" + commandUsage[2] + " - create a class with title <name>"
		+ commandUsage[3]
		+ " - create a field in <class name> with visibility <type visibility>, type <field type> titled <field name>"
		+ commandUsage[4]
		+ " - create a method in <class name> with visibility <method visibility>, type <method type> titled <method name>"
		+ commandUsage[5]
		+ " - create a relationship between <class name1> and <class name2> with type <type> (Aggregation, Realization, Composition, Inheritance)"
		+ commandUsage[6]
		+ " - create a parameter in <class name> for <method> with type <parameter type> titled <parameter name>\n"
		+ commandUsage[7] + " - delete a class with title <name>" + commandUsage[8]
		+ " - delete field <field name> in class titled <class name>" + commandUsage[9]
		+ " - delete method <method name> in class titled <class name>" + commandUsage[10]
		+ " - delete a relationship with type <type> (Aggregation, Realization, Composition, Inheritance) between <class name1> and <class name2>"
		+ commandUsage[11] + " - delete a parameter in <class name> for <method name> with  <parameter name>\n"
		+ commandUsage[12] + " - rename class <name> to <new name>" + commandUsage[13]
		+ " - rename field <field name> to <newname> in class titled <class name>" + commandUsage[14]
		+ " - rename method <method name> to <newname> in class titled <class name>" + commandUsage[15]
		+ " - rename parameter in <class name> for <method> titled <parameter name> to <parameter newname>\n"
		+ commandUsage[16] + " - set type of field <field name> in <class name> to <type>" + commandUsage[17]
		+ " - set type of method <method name> in <class name> to <type>" + commandUsage[18]
		+ " - set type of parameter <class name> in <method name> titled <parameter name> to <type>\n"
		+ commandUsage[19] + " - set visibility of field <field name> in <class name> to <visibility>"
		+ commandUsage[20] + " - set visibility of method <method name> in <class name> to <visibility>\n"
		+ commandUsage[21] + " - List all existing classes" + commandUsage[22]
		+ " - List all existing relationships" + commandUsage[23]
		+ " - List all existing classes and relationships\n" + commandUsage[24]
		+ " - Clear all classes and relationships\n" + "  quit - exits the program\n");
    }

}
