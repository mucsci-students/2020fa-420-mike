package mike.cli;

import mike.datastructures.Model;
import mike.datastructures.Relationship.Type;
import mike.view.CLIView;

public class DeleteCommand extends CommandObj {

    public DeleteCommand(Model m, CLIView v, String[] com, boolean p) {
	super(m, v, com, p);
    }

    public boolean execute() {
	if (commands.length < 3) {
	    view.printError(errorMessage + commandUsage[7] + commandUsage[8] + commandUsage[9] + commandUsage[10]
		    + commandUsage[11] + "\n");
	    return prompt;
	}
	if (commands[1].equals("class")) {
	    if (commands.length != 3) {
		view.printError(errorMessage + commandUsage[7] + "\n");
		return prompt;
	    }
	    if (model.deleteClass(commands[2])) {
		return true;
	    } else {
		view.printError("Delete class failed. Make sure the class name exists.\n");
	    }
	} else if (commands[1].equals("field")) {
	    if (commands.length != 4) {
		view.printError(errorMessage + commandUsage[8] + "\n");
		return prompt;
	    }
	    if (model.deleteField(commands[2], commands[3])) {
		return true;
	    } else {
		view.printError("Delete field failed. Make sure the field and class name exist.\n");
	    }
	} else if (commands[1].equals("method")) {
	    if (commands.length != 4) {
		view.printError(errorMessage + commandUsage[9] + "\n");
		return prompt;
	    }
	    if (model.deleteMethod(commands[2], commands[3])) {
		return true;
	    } else {
		view.printError("Delete method failed. Make sure the method and class name exist.\n");
	    }
	} else if (commands[1].equals("relationship")) {
	    if (commands.length != 5) {
		view.printError(errorMessage + commandUsage[10] + "\n");
		return prompt;
	    }
	    if (model.deleteRelationship(checkEnum(commands[2].toUpperCase()), commands[3], commands[4])) {
		return true;
	    } else {
		view.printError("Delete relationship failed. Make sure the relationship exists.\n");
	    }
	} else if (commands[1].equals("parameter")) {
	    if (commands.length != 5) {
		view.printError(errorMessage + commandUsage[11] + "\n");
		return prompt;
	    }
	    if (model.deleteParameter(commands[2], commands[3], commands[4])) {
		return true;
	    } else {
		view.printError("Delete parameter failed. Make sure the class, method, and parameter all exist.\n");
	    }
	} else {
	    view.printError(errorMessage + commandUsage[7] + commandUsage[8] + commandUsage[9] + commandUsage[10]
		    + commandUsage[11] + "\n");
	}
	return prompt;
    }

    // Return enum type that user requested, null if invalid
    private static Type checkEnum(String command) {
	switch (command) {
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
