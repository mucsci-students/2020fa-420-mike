package mike.cli;

import java.io.File;
import java.io.IOException;

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
		HelperMethods.save(file, model);
		System.out.println("File saved at: " + file.getAbsolutePath());
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
