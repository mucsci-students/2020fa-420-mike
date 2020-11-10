package cli;

import mike.datastructures.Model;
import mike.datastructures.Entity.visibility;

public class SetvisCommand extends CommandObj {

    public SetvisCommand(Model m, String[] com, boolean p) {
	super(m, com, p);
    }

    public boolean execute() {
	if (commands.length != 5) {
	    System.out.println(errorMessage + commandUsage[19] + commandUsage[20] + "\n");
	    return prompt;
	}
	if (checkVis(commands[4]) == null) {
	    System.out.println(
		    "\nsetvis failed due to invalid visibility type. Valid visibility types are: public, private, and protected.\n");
	    return prompt;
	}
	if (commands[1].equals("field")) {
	    if (model.changeFieldVis(commands[2], commands[3], commands[4])) {
		return true;
	    } else {
		System.out.println(
			"\nsetvis field failed. Make sure the class and field both exist, and you passed a valid visibility.\n");
	    }
	} else if (commands[1].equals("method")) {
	    if (model.changeMethodVis(commands[2], commands[3], commands[4])) {
		return true;
	    } else {
		System.out.println(
			"\nsetvis method failed. Make sure the class and method both exist, and you passed a valid visibility.\n");
	    }
	} else {
	    System.out.println(errorMessage + commandUsage[19] + commandUsage[20] + "\n");
	}
	return prompt;
    }

    // Return enum type of visibility user requested, null if invalid
    private visibility checkVis(String visType) {
	visType = visType.toUpperCase();
	switch (visType) {
	case "PUBLIC":
	    return visibility.PUBLIC;
	case "PRIVATE":
	    return visibility.PRIVATE;
	case "PROTECTED":
	    return visibility.PROTECTED;
	default:
	    return null;
	}
    }
}
