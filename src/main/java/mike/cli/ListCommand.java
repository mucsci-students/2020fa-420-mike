package cli;

import mike.HelperMethods;
import mike.datastructures.Model;

public class ListCommand extends CommandObj {

    public ListCommand(Model m, String[] com, boolean p) {
	super(m, com, p);
    }

    public boolean execute() {
	if (commands.length < 2) {
	    System.out.println(errorMessage + commandUsage[21] + commandUsage[22] + commandUsage[23] + "\n");
	    return prompt;
	}
	if (commands[1].equals("classes")) {
	    if (commands.length != 2) {
		System.out.println(errorMessage + commandUsage[21] + "\n");
		return prompt;
	    } else {
		System.out.println();
		HelperMethods.listClasses(model);
		System.out.println();
		return true;
	    }
	} else if (commands[1].equals("relationships")) {
	    if (commands.length != 2) {
		System.out.println(errorMessage + commandUsage[22] + "\n");
		return prompt;
	    } else {
		System.out.println();
		HelperMethods.listRelationships(model);
		System.out.println();
		return true;
	    }
	} else if (commands[1].equals("all")) {
	    if (commands.length != 2) {
		System.out.println(errorMessage + commandUsage[23] + "\n");
		return prompt;
	    } else {
		System.out.println();
		HelperMethods.listClasses(model);
		System.out.println();
		HelperMethods.listRelationships(model);
		System.out.println();
		return true;
	    }
	} else {
	    System.out.println(errorMessage + commandUsage[21] + commandUsage[22] + commandUsage[23] + "\n");
	}
	return prompt;
    }
}
