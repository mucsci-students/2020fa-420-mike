package cli;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jline.reader.LineReader;
import org.jline.reader.MaskingCallback;

import mike.HelperMethods;
import mike.datastructures.Model;

public class LoadCommand extends CommandObj {

    private static LineReader savePromptReader;
    
    public LoadCommand(Model m, String[] com, boolean p, LineReader reader) {
	super(m, com, p);
	savePromptReader = reader;
    }

    public boolean execute() {
	if (commands.length == 2) {
	    try {
		if (prompt) {
		    System.out.println("\nYou have unsaved changes, are you sure you want to continue?");
		    System.out.println("Type 'yes' to continue loading, or 'no' to go back.");
		    prompt = savePrompt();
		}
		if (!prompt) {
		    File file = new File(commands[1]);
		    Path path;
		    if (file.isAbsolute()) {
			path = Paths.get(commands[1]);
		    } else {
			path = Paths.get(System.getProperty("user.dir") + "\\" + commands[1]);
		    }
		    HelperMethods.load(path, model, null, null);
		    return false;
		}
	    } catch (Exception e) {
		System.out.println("Failed to parse directory. Exiting.");
		return prompt;
	    }
	} else {
	    System.out.println(errorMessage + commandUsage[1] + "\n");
	    return prompt;
	}
	return prompt;
    }

    // Gets user input to set save prompt flag.
    // False if they wish to continue
    // True if they want to return
    private static boolean savePrompt() {
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

}
