package mike.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import mike.HelperMethods;
import mike.datastructures.Model;
import mike.view.CLIView;

public class SaveCommand extends CommandObj {
    
    public SaveCommand(Model m, CLIView v, String[] com, boolean p) {
	super(m, v, com, p);
    }

    public boolean execute() {
	if (commands.length == 2) {
	    try {
		File file = new File(commands[1]);
		Path path;
		if (file.isAbsolute()) {
		    path = Paths.get(commands[1]);
		} else {
		    path = Paths.get(System.getProperty("user.dir") + File.separator + commands[1]);
		}
		HelperMethods.save(path, model);
		System.out.println("File saved at: " + path.toString());
	    } catch (IOException e) {
		view.printError("Failed to parse directory. Exiting.");
		return prompt;
	    }
	} else if (commands.length == 3) {
	    try {
		File file = new File(commands[2] + File.separator + commands[1]);
		Path path;
		if (file.isAbsolute()) {
		    path = Paths.get(commands[2] + File.separator + commands[1]);
		} else {
		    path = Paths.get(System.getProperty("user.dir") + File.separator + commands[2] + File.separator + commands[1]);
		}
		HelperMethods.save(path, model);
		System.out.println("File saved at: " + path.toString());
	    } catch (IOException e) {
		view.printError("Failed to parse directory. Exiting.");
		return prompt;
	    }
	} else {
	    view.printError(errorMessage + commandUsage[0] + "\n");
	    return prompt;
	}
	return false;
    }

}
