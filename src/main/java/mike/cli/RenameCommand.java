package mike.cli;

import mike.datastructures.Model;
import mike.view.CLIView;

public class RenameCommand extends CommandObj {

    public RenameCommand(Model m, CLIView v, String[] com, boolean p) {
	super(m, v, com, p);
    }

    public boolean execute() {
	if (commands.length < 4) {
	    view.printError(
		    errorMessage + commandUsage[12] + commandUsage[13] + commandUsage[14] + commandUsage[15] + "\n");
	    return prompt;
	}
	if (commands[1].equals("class")) {
	    if (commands.length != 4) {
		view.printError(errorMessage + commandUsage[12] + "\n");
		return prompt;
	    }
	    if (model.renameClass(commands[2], commands[3])) {
		return true;
	    } else {
		view.printError(
			"Rename class failed. Make sure the class exists and the new class name doesn't exist.\n");
	    }
	} else if (commands[1].equals("field")) {
	    if (commands.length != 5) {
		view.printError(errorMessage + commandUsage[13] + "\n");
		return prompt;
	    }
	    if (model.renameField(commands[2], commands[3], commands[4])) {
		return true;
	    } else {
		view.printError(
			"Rename field failed. Make sure the class and field exist and the new field name doesn't exist.\n");
	    }
	} else if (commands[1].equals("method")) {
	    if (commands.length != 5) {
		view.printError(errorMessage + commandUsage[14] + "\n");
		return prompt;
	    }
	    if (model.renameMethod(commands[2], commands[3], commands[4])) {
		return true;
	    } else {
		view.printError(
			"Rename method failed. Make sure the class and method exist and the new method name doesn't exist.\n");
	    }
	} else if (commands[1].equals("parameter")) {
	    if (commands.length != 6) {
		view.printError(errorMessage + commandUsage[15] + "\n");
		return prompt;
	    }
	    if (model.renameParameter(commands[2], commands[3], commands[4], commands[5])) {
		return true;
	    } else {
		view.printError(
			"Rename parameter failed. Make sure the class, method, and parameter all exist and the new parameter name does not exist.");
	    }
	} else {
	    view.printError(
		    errorMessage + commandUsage[12] + commandUsage[13] + commandUsage[14] + commandUsage[15] + "\n");
	}
	return prompt;
    }
}
