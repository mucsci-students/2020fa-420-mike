package mike.cli;

import mike.datastructures.Model;
import mike.view.CLIView;

public class SettypeCommand extends CommandObj {

    public SettypeCommand(Model m, CLIView v, String[] com, boolean p) {
	super(m, v, com, p);
    }

    public boolean execute() {
	if (commands.length == 5) {
	    if (commands[1].equals("field")) {
		if (model.changeFieldType(commands[2], commands[3], commands[4])) {
		    return true;
		} else {
		    view.printError("settype field failed. Make sure the class and field both exist.\n");
		}
	    } else if (commands[1].equals("method")) {
		if (model.changeMethodType(commands[2], commands[3], commands[4])) {
		    return true;
		} else {
		    view.printError("settype method failed. Make sure the class and method both exist.\n");
		}
	    } else {
		view.printError(errorMessage + commandUsage[16] + commandUsage[17] + "\n");
	    }
	} else if (commands.length == 6) {
	    if (commands[1].equals("parameter")) {
		if (model.changeParameterType(commands[2], commands[3], commands[4], commands[5])) {
		    return true;
		} else {
		    view.printError(
			    "settype parameter failed. Make sure the class, method, and parameter all exist.\n");
		}
	    } else {
		view.printError(errorMessage + commandUsage[18] + "\n");
	    }
	} else {
	    view.printError(errorMessage + commandUsage[16] + commandUsage[17] + commandUsage[18] + "\n");
	}
	return prompt;
    }

}
